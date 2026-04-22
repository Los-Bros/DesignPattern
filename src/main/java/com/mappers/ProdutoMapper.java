package com.mappers;

import com.domains.Produto;
import com.domains.dtos.ProdutoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper manual (sem frameworks) para Servidor.
 * - Entity -> DTO: enum Provimento vira int (0/1) e Departamento vira departamentoId.
 * - DTO -> Entity: int (0/1) vira enum Provimento; departamentoId vira Departamento (via resolver).
 * - NÃO seta valorEstoque na Entity (é calculado no domínio).
 */

public class ProdutoMapper {

    private ProdutoMapper() {}

    /** Converte uma Entity em DTO. */
    public static ProdutoDTO toDto(Produto e) {
        if (e == null) return null;
        return new ProdutoDTO(
                e.getId(),
                e.getDescricao(),
                e.getValor()
        );
    }

    /** Cria uma nova Entity a partir do DTO (respeita id do DTO se presente). */
    public static Produto toEntity(ProdutoDTO dto) {
        if (dto == null) return null;
        Produto e = new Produto();
        e.setId(dto.getId()); // se null, JPA gera; se não, usado no update
        e.setDescricao(dto.getDescricao() == null ? null : dto.getDescricao().trim());
        e.setValor(dto.getValor() == null ? null : dto.getValor());
        return e;
    }

    /**
     * Copia dados do DTO para uma Entity existente (PUT “completo”).
     * Não altera o id da entidade alvo.
     */
    public static void copyToEntity(ProdutoDTO dto, Produto target) {
        if (dto == null || target == null) return;
        target.setDescricao(dto.getDescricao() == null ? null : dto.getDescricao().trim());
        target.setValor(dto.getValor() == null ? null : dto.getValor());
    }

    /** Converte uma coleção de Entities em lista de DTOs. */
    public static List<ProdutoDTO> toDtoList(Collection<Produto> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(ProdutoMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Converte uma coleção de DTOs em lista de Entities. */
    public static List<Produto> toEntityList(Collection<ProdutoDTO> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .filter(Objects::nonNull)
                .map(ProdutoMapper::toEntity)
                .collect(Collectors.toList());
    }

    /** Converte Page<Entity> em Page<DTO> (preserva paginação). */
    public static Page<ProdutoDTO> toDtoPage(Page<Produto> page) {
        List<ProdutoDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }
    
}
