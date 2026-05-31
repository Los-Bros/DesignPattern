package com.domains.dtos;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

public class ProdutoDTO {

    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = ProdutoDTO.Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = ProdutoDTO.Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Descrição do produto é obrigatório")
    @Size(max = 150, message = "Descrição do produto deve ter no máximo 150 caracteres")
    private String descricao;

    @Digits(integer = 12, fraction = 3, message = "Valor deve ter no máximo 12 inteiros e 3 decimais")
    @PositiveOrZero(message = "valor não pode ser negativo")
    private BigDecimal valor;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Long id, String descricao, BigDecimal valor) {
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
        ProdutoDTO that = (ProdutoDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(descricao, that.descricao) && Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, valor);
    }
}
