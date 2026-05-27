package com.mappers;

import com.domains.ItemPedido;
import com.domains.Pedido;
import com.domains.Produto;
import com.domains.dtos.PedidoDTO;
import com.domains.frete.TipoFrete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PedidoMapper {

    private PedidoMapper() {
    }

    public static PedidoDTO toDto(Pedido e) {
        if (e == null) return null;

        return new PedidoDTO(
                e.getId(),
                e.getValor(),
                e.getCriadoEm(),
                e.getValorFinal(),
                e.getTipoFreteAtual() == null ? null : e.getTipoFreteAtual().name(),
                e.getStatusAtual() == null ? null : e.getStatusAtual().name(),
                toItemDtoList(e.getItens())
        );
    }

    public static Pedido toEntity(PedidoDTO dto, Function<Long, Produto> produtoResolver) {
        if (dto == null) return null;

        Pedido e = new Pedido();
        e.setId(dto.getId());
        copyToEntity(dto, e, produtoResolver);
        return e;
    }

    public static void copyToEntity(PedidoDTO dto, Pedido target, Function<Long, Produto> produtoResolver) {
        if (dto == null || target == null) return;

        if (dto.getCriadoEm() != null) {
            target.setCriadoEm(dto.getCriadoEm());
        }

        if (dto.getItens() != null) {
            target.getItens().clear();
            target.setValor(BigDecimal.ZERO);

            dto.getItens().stream()
                    .filter(Objects::nonNull)
                    .forEach(item -> adicionarItem(target, item, produtoResolver));
        } else if (dto.getValor() != null) {
            target.setValor(dto.getValor());
        }

        if (dto.getTipoFrete() != null && !dto.getTipoFrete().isBlank()) {
            target.setTipoFreteAtual(TipoFrete.fromDescricao(dto.getTipoFrete()));
        }
    }

    public static void adicionarItem(Pedido pedido, PedidoDTO.ItemPedidoDTO itemDto,
                                     Function<Long, Produto> produtoResolver) {
        if (pedido == null || itemDto == null) return;

        Produto produto = produtoResolver.apply(itemDto.getProdutoId());
        pedido.adicionarItem(produto, itemDto.getQuantidade());
    }

    public static List<PedidoDTO> toDtoList(Collection<Pedido> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(PedidoMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Page<PedidoDTO> toDtoPage(Page<Pedido> page) {
        List<PedidoDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    private static List<PedidoDTO.ItemPedidoDTO> toItemDtoList(Collection<ItemPedido> itens) {
        if (itens == null) return List.of();
        return itens.stream()
                .filter(Objects::nonNull)
                .map(PedidoMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private static PedidoDTO.ItemPedidoDTO toItemDto(ItemPedido item) {
        Produto produto = item.getProduto();
        return new PedidoDTO.ItemPedidoDTO(
                item.getId(),
                produto == null ? null : produto.getId(),
                produto == null ? null : produto.getDescricao(),
                produto == null ? null : produto.getValor(),
                item.getQuantidade(),
                item.getValor()
        );
    }
}
