package com.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name="ItemPedido")
@SequenceGenerator(
        name = "seq_ItemPedido",
        sequenceName = "seq_ItemPedido",
        allocationSize = 1
)
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ItemPedido")
    private Long id;

    @NotNull
    @Digits(integer = 15, fraction = 3)
    @Column(precision = 18, scale = 3, nullable = false)
    private BigDecimal valor;

    @NotNull
    @Digits(integer = 15, fraction = 0)
    @Column(precision = 18, scale = 3, nullable = false)
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "seq_Produto")
    private Produto produto;

    public ItemPedido() {
    }

    public ItemPedido(Long id, BigDecimal valor, Integer quantidade, Produto produto) {
        this.id = id;
        this.valor = valor;
        this.quantidade = quantidade;
        this.produto = produto;
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
        this.valor = produto.getValor().multiply(BigDecimal.valueOf(quantidade));
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

}
