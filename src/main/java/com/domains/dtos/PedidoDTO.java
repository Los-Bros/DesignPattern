package com.domains.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PedidoDTO {

    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = PedidoDTO.Create.class, message = "Id deve ser omitido na criacao")
    @NotNull(groups = PedidoDTO.Update.class, message = "Id e obrigatorio na atualizacao")
    private Long id;

    @Digits(integer = 15, fraction = 3, message = "Valor deve ter no maximo 15 inteiros e 3 decimais")
    private BigDecimal valor;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate criadoEm;

    @Digits(integer = 15, fraction = 3, message = "Valor final deve ter no maximo 15 inteiros e 3 decimais")
    private BigDecimal valorFinal;

    private String tipoFrete;

    private String statusAtual;

    @Valid
    private List<ItemPedidoDTO> itens;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, BigDecimal valor, LocalDate criadoEm, BigDecimal valorFinal,
                     String tipoFrete, String statusAtual, List<ItemPedidoDTO> itens) {
        this.id = id;
        this.valor = valor;
        this.criadoEm = criadoEm;
        this.valorFinal = valorFinal;
        this.tipoFrete = tipoFrete;
        this.statusAtual = statusAtual;
        this.itens = itens != null ? itens : new ArrayList<>();
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
        this.valor = valor;
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

    public String getTipoFrete() {
        return tipoFrete;
    }

    public void setTipoFrete(String tipoFrete) {
        this.tipoFrete = tipoFrete;
    }

    public String getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(String statusAtual) {
        this.statusAtual = statusAtual;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PedidoDTO pedidoDTO = (PedidoDTO) o;
        return Objects.equals(id, pedidoDTO.id)
                && Objects.equals(valor, pedidoDTO.valor)
                && Objects.equals(criadoEm, pedidoDTO.criadoEm)
                && Objects.equals(valorFinal, pedidoDTO.valorFinal)
                && Objects.equals(tipoFrete, pedidoDTO.tipoFrete)
                && Objects.equals(statusAtual, pedidoDTO.statusAtual)
                && Objects.equals(itens, pedidoDTO.itens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valor, criadoEm, valorFinal, tipoFrete, statusAtual, itens);
    }

    public static class ItemPedidoDTO {

        private Long id;

        @NotNull(message = "Produto e obrigatorio")
        private Long produtoId;

        private String produtoDescricao;

        @Digits(integer = 15, fraction = 3, message = "Valor do produto deve ter no maximo 15 inteiros e 3 decimais")
        private BigDecimal valorProduto;

        @NotNull(message = "Quantidade e obrigatoria")
        @Positive(message = "Quantidade deve ser maior que zero")
        private Integer quantidade;

        @Digits(integer = 15, fraction = 3, message = "Valor do item deve ter no maximo 15 inteiros e 3 decimais")
        private BigDecimal valor;

        public ItemPedidoDTO() {
        }

        public ItemPedidoDTO(Long id, Long produtoId, String produtoDescricao,
                             BigDecimal valorProduto, Integer quantidade, BigDecimal valor) {
            this.id = id;
            this.produtoId = produtoId;
            this.produtoDescricao = produtoDescricao;
            this.valorProduto = valorProduto;
            this.quantidade = quantidade;
            this.valor = valor;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getProdutoId() {
            return produtoId;
        }

        public void setProdutoId(Long produtoId) {
            this.produtoId = produtoId;
        }

        public String getProdutoDescricao() {
            return produtoDescricao;
        }

        public void setProdutoDescricao(String produtoDescricao) {
            this.produtoDescricao = produtoDescricao;
        }

        public BigDecimal getValorProduto() {
            return valorProduto;
        }

        public void setValorProduto(BigDecimal valorProduto) {
            this.valorProduto = valorProduto;
        }

        public Integer getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
        }

        public BigDecimal getValor() {
            return valor;
        }

        public void setValor(BigDecimal valor) {
            this.valor = valor;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            ItemPedidoDTO that = (ItemPedidoDTO) o;
            return Objects.equals(id, that.id)
                    && Objects.equals(produtoId, that.produtoId)
                    && Objects.equals(produtoDescricao, that.produtoDescricao)
                    && Objects.equals(valorProduto, that.valorProduto)
                    && Objects.equals(quantidade, that.quantidade)
                    && Objects.equals(valor, that.valor);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, produtoId, produtoDescricao, valorProduto, quantidade, valor);
        }
    }
}
