package com.services;

import com.domains.Pedido;
import com.domains.Produto;
import com.domains.dtos.PedidoDTO;
import com.domains.frete.TipoFrete;
import com.domains.status.PedidoStatus;
import com.mappers.PedidoMapper;
import com.repositories.PedidoRepository;
import com.repositories.ProdutoRepository;
import com.services.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PedidoService {

    private static final int MAX_PAGE_SIZE = 200;

    private final PedidoRepository pedidoRepo;
    private final ProdutoRepository produtoRepo;

    public PedidoService(PedidoRepository pedidoRepo, ProdutoRepository produtoRepo) {
        this.pedidoRepo = pedidoRepo;
        this.produtoRepo = produtoRepo;
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> findAll() {
        return PedidoMapper.toDtoList(pedidoRepo.findAll());
    }

    @Transactional(readOnly = true)
    public Page<PedidoDTO> findAll(Pageable pageable) {
        Page<Pedido> page = pedidoRepo.findAll(paginaEfetiva(pageable));
        return PedidoMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public Page<PedidoDTO> findAllByStatus(String status, Pageable pageable) {
        PedidoStatus statusAtual = PedidoStatus.fromDescricao(status);
        Page<Pedido> page = pedidoRepo.findByStatusAtual(statusAtual, paginaEfetiva(pageable));
        return PedidoMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> findAllByStatus(String status) {
        return findAllByStatus(status, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public PedidoDTO findById(Long id) {
        return PedidoMapper.toDto(buscarPedido(id));
    }

    @Transactional
    public PedidoDTO create(PedidoDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados sao obrigatorios");
        }

        dto.setId(null);
        Pedido pedido = PedidoMapper.toEntity(dto, this::buscarProduto);

        return PedidoMapper.toDto(pedidoRepo.save(pedido));
    }

    @Transactional
    public PedidoDTO update(Long id, PedidoDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados sao obrigatorios");
        }

        Pedido pedido = buscarPedido(id);
        PedidoMapper.copyToEntity(dto, pedido, this::buscarProduto);

        return PedidoMapper.toDto(pedidoRepo.save(pedido));
    }

    @Transactional
    public void delete(Long id) {
        Pedido pedido = buscarPedido(id);
        pedidoRepo.delete(pedido);
    }

    @Transactional
    public PedidoDTO adicionarProduto(Long pedidoId, PedidoDTO.ItemPedidoDTO itemDto) {
        if (itemDto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item do pedido e obrigatorio");
        }

        Pedido pedido = buscarPedido(pedidoId);
        PedidoMapper.adicionarItem(pedido, itemDto, this::buscarProduto);

        return PedidoMapper.toDto(pedidoRepo.save(pedido));
    }

    @Transactional
    public PedidoDTO definirFrete(Long pedidoId, String tipoFrete) {
        Pedido pedido = buscarPedido(pedidoId);
        pedido.setTipoFreteAtual(TipoFrete.fromDescricao(tipoFrete));

        return PedidoMapper.toDto(pedidoRepo.save(pedido));
    }

    @Transactional
    public PedidoDTO realizarPagamento(Long pedidoId) {
        Pedido pedido = buscarPedido(pedidoId);
        pedido.realizarPagamento();

        return PedidoMapper.toDto(pedidoRepo.save(pedido));
    }

    @Transactional
    public PedidoDTO cancelarPedido(Long pedidoId) {
        Pedido pedido = buscarPedido(pedidoId);
        pedido.cancelarPedido();

        return PedidoMapper.toDto(pedidoRepo.save(pedido));
    }

    @Transactional
    public PedidoDTO despacharPedido(Long pedidoId) {
        Pedido pedido = buscarPedido(pedidoId);
        pedido.despacharPedido();

        return PedidoMapper.toDto(pedidoRepo.save(pedido));
    }

    @Transactional
    public PedidoDTO atualizarStatus(Long pedidoId, String status) {
        PedidoStatus novoStatus = PedidoStatus.fromDescricao(status);

        return switch (novoStatus) {
            case AGUARDANDO_PAGAMENTO -> {
                Pedido pedido = buscarPedido(pedidoId);
                pedido.setStatusAtual(PedidoStatus.AGUARDANDO_PAGAMENTO);
                yield PedidoMapper.toDto(pedidoRepo.save(pedido));
            }
            case PAGO -> realizarPagamento(pedidoId);
            case ENVIADO -> despacharPedido(pedidoId);
            case CANCELADO -> cancelarPedido(pedidoId);
        };
    }

    private Pedido buscarPedido(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do pedido e obrigatorio");
        }

        return pedidoRepo.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pedido nao encontrado: id=" + id));
    }

    private Produto buscarProduto(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do produto e obrigatorio");
        }

        return produtoRepo.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Produto nao encontrado: id=" + id));
    }

    private Pageable paginaEfetiva(Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            return Pageable.unpaged();
        }

        return PageRequest.of(
                Math.max(0, pageable.getPageNumber()),
                Math.min(pageable.getPageSize(), MAX_PAGE_SIZE),
                pageable.getSort()
        );
    }
}
