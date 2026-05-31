package com.domains;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Produto")
@SequenceGenerator(
        name = "seq_Produto",
        sequenceName = "seq_Produto",
        allocationSize = 1
)
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_Produto")
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String descricao;

    @NotNull
    @Digits(integer = 15, fraction = 3)
    @Column(precision = 18, scale = 3, nullable = false)
    private BigDecimal valor;

    public Produto() {
    }

    public Produto(Long id, String descricao, BigDecimal valor) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id) && Objects.equals(descricao, produto.descricao) && Objects.equals(valor, produto.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, valor);
    }
}
