package com.domains;

import com.domains.frete.Frete;
import com.domains.status.Status;
import com.domains.status.AguardandoPagamentoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
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

    private List<ItemPedido> itens = new ArrayList<>();

    //estado atual do pedido.
    private Status estadoAtual;

    //Frete
    private Frete frete;

    public Pedido() {
        System.out.println("Pedido aguardando pagamento");
        //define o estado atual
        this.estadoAtual = new AguardandoPagamentoStatus(this);
    }

    public Pedido(Long id, BigDecimal valor, LocalDate criadoEm,
                  BigDecimal valorFinal,List<ItemPedido> itens) {
        this.id = id;
        this.valor = valor;
        this.criadoEm = criadoEm;
        this.valorFinal = valorFinal;
        this.itens = itens;
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
        this.valorFinal = valorFinal;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public Frete getFrete() {
        return frete;
    }

    public void setFrete(Frete frete) {
        this.frete = frete;
    }

    public void adicionarItem(Produto produto, int quantidade) {
        // Criando a "ponte" entre o Pedido e o Produto
        ItemPedido novoItem = new ItemPedido(id, produto.getValor(), quantidade, produto);
        this.itens.add(novoItem);

        // Atualiza o valor total do pedido
        this.valor = this.valor.add(novoItem.getValor());
    }


    public void sucessoAoPagar(){
        try{
            System.out.println("Pedido Pago");
            this.estadoAtual.sucessoAoPagar();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void cancelarPedido(){
        try{
            System.out.println("Pedido Cancelar");
            this.estadoAtual.cancelarPedido();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void despacharPedido(){
        try{
            System.out.println("Pedido Enviado");
            this.estadoAtual.despacharPedido();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void setEstadoAtual(Status estadoAtual) {
        this.estadoAtual = estadoAtual;
    }

}
