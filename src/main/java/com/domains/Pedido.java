package com.domains;

import com.domains.frete.Frete;
import com.domains.frete.TipoFrete;
import com.domains.status.Status;
import com.domains.status.PedidoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Pedido")
@SequenceGenerator(
        name = "seq_Pedido",
        sequenceName = "seq_Pedido",
        allocationSize = 1
)
public class Pedido {

    private static final BigDecimal VALOR_INICIAL = BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_Pedido")
    private Long id;

    @NotNull
    @Digits(integer = 15, fraction = 3)
    @Column(precision = 18, scale = 3, nullable = false)
    private BigDecimal valor;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate criadoEm;

    @NotNull
    @Digits(integer = 15, fraction = 3)
    @Column(precision = 18, scale = 3, nullable = false)
    private BigDecimal valorFinal;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pedido_id")
    private List<ItemPedido> itens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PedidoStatus statusAtual = PedidoStatus.AGUARDANDO_PAGAMENTO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TipoFrete tipoFreteAtual;

    // Estado atual do pedido usado pelo State.
    @Transient
    private Status estadoAtual;

    // Tipo de frete usado pelo Strategy.
    @Transient
    private Frete tipoFrete;

    public Pedido() {
        this.valor = VALOR_INICIAL;
        this.valorFinal = VALOR_INICIAL;
        this.criadoEm = LocalDate.now();
        sincronizarObjetosDeDominio();
    }

    public Pedido(Long id, BigDecimal valor, LocalDate criadoEm,
                  BigDecimal valorFinal,List<ItemPedido> itens) {
        this.id = id;
        this.valor = valor != null ? valor : VALOR_INICIAL;
        this.criadoEm = criadoEm != null ? criadoEm : LocalDate.now();
        this.valorFinal = valorFinal != null ? valorFinal : this.valor;
        this.itens = itens != null ? itens : new ArrayList<>();
        sincronizarObjetosDeDominio();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor do pedido deve ser informado e nao pode ser negativo");
        }

        this.valor = valor.setScale(3, RoundingMode.HALF_UP);
        atualizarValorFinal();
    }

    public LocalDate getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDate criadoEm) {
        this.criadoEm = criadoEm;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal == null
                ? VALOR_INICIAL
                : valorFinal.setScale(3, RoundingMode.HALF_UP);
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens != null ? itens : new ArrayList<>();
        recalcularValorItens();
    }

    public Frete getFrete() {
        return tipoFrete;
    }

    public void setFrete(Frete frete) {
        setTipoFrete(frete);
    }

    public Frete getTipoFrete() {
        return tipoFrete;
    }

    public void setTipoFrete(Frete tipoFrete) {
        if (tipoFrete == null) {
            throw new IllegalArgumentException("Tipo de frete deve ser informado");
        }

        this.tipoFrete = tipoFrete;
        this.tipoFreteAtual = TipoFrete.fromFrete(tipoFrete);
        atualizarValorFinal();
    }

    public TipoFrete getTipoFreteAtual() {
        return tipoFreteAtual;
    }

    public void setTipoFreteAtual(TipoFrete tipoFreteAtual) {
        this.tipoFreteAtual = tipoFreteAtual;
        this.tipoFrete = tipoFreteAtual == null ? null : tipoFreteAtual.criarFrete();
        atualizarValorFinal();
    }

    public PedidoStatus getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(PedidoStatus statusAtual) {
        if (statusAtual == null) {
            throw new IllegalArgumentException("Status do pedido deve ser informado");
        }

        this.statusAtual = statusAtual;
        this.estadoAtual = statusAtual.criarStatus(this);
    }

    public void adicionarItem(Produto produto, int quantidade) {
        if (produto == null || produto.getValor() == null) {
            throw new IllegalArgumentException("Produto deve ser informado");
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        BigDecimal valorItem = produto.getValor()
                .multiply(BigDecimal.valueOf(quantidade))
                .setScale(3, RoundingMode.HALF_UP);

        ItemPedido novoItem = new ItemPedido(null, valorItem, quantidade, produto);
        this.itens.add(novoItem);

        this.valor = this.valor.add(valorItem).setScale(3, RoundingMode.HALF_UP);
        atualizarValorFinal();
    }

    public void realizarPagamento(){
        if (this.tipoFrete == null) {
            throw new IllegalStateException("Tipo de frete deve ser informado antes do pagamento");
        }

        atualizarValorFinal();
        sucessoAoPagar();
    }

    private void atualizarValorFinal() {
        BigDecimal valorPedido = this.valor != null ? this.valor : VALOR_INICIAL;

        if (this.tipoFrete == null) {
            this.valorFinal = valorPedido.setScale(3, RoundingMode.HALF_UP);
            return;
        }

        BigDecimal valorFrete = this.tipoFrete.calcula(valorPedido);
        this.valorFinal = valorPedido.add(valorFrete).setScale(3, RoundingMode.HALF_UP);
    }

    private void recalcularValorItens() {
        this.valor = this.itens.stream()
                .map(ItemPedido::getValor)
                .filter(v -> v != null)
                .reduce(VALOR_INICIAL, BigDecimal::add)
                .setScale(3, RoundingMode.HALF_UP);
        atualizarValorFinal();
    }

    public void sucessoAoPagar(){
        garantirEstadoAtual();
        this.estadoAtual.sucessoAoPagar();
    }
    public void cancelarPedido(){
        garantirEstadoAtual();
        this.estadoAtual.cancelarPedido();
    }
    public void despacharPedido(){
        garantirEstadoAtual();
        this.estadoAtual.despacharPedido();
    }
    public void setEstadoAtual(Status estadoAtual) {
        if (estadoAtual == null) {
            throw new IllegalArgumentException("Status do pedido deve ser informado");
        }

        this.estadoAtual = estadoAtual;
        this.statusAtual = PedidoStatus.fromStatus(estadoAtual);
    }

    @PrePersist
    @PreUpdate
    private void antesDeSalvar() {
        if (this.criadoEm == null) {
            this.criadoEm = LocalDate.now();
        }

        if (this.statusAtual == null) {
            this.statusAtual = PedidoStatus.AGUARDANDO_PAGAMENTO;
        }

        if (this.valor == null) {
            this.valor = VALOR_INICIAL;
        }

        if (this.itens == null) {
            this.itens = new ArrayList<>();
        }

        sincronizarObjetosDeDominio();
        atualizarValorFinal();
    }

    @PostLoad
    private void sincronizarObjetosDeDominio() {
        if (this.statusAtual == null) {
            this.statusAtual = PedidoStatus.AGUARDANDO_PAGAMENTO;
        }

        this.estadoAtual = this.statusAtual.criarStatus(this);
        this.tipoFrete = this.tipoFreteAtual == null ? null : this.tipoFreteAtual.criarFrete();
    }

    private void garantirEstadoAtual() {
        if (this.estadoAtual == null) {
            sincronizarObjetosDeDominio();
        }
    }

}
