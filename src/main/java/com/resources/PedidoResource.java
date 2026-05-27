package com.resources;

import com.domains.dtos.PedidoDTO;
import com.services.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoResource {

    private final PedidoService service;

    public PedidoResource(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<PedidoDTO>> list(
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "criadoEm") Pageable pageable) {

        Page<PedidoDTO> page =
                (status != null)
                        ? service.findAllByStatus(status, pageable)
                        : service.findAll(pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PedidoDTO>> listAll(
            @RequestParam(required = false) String status) {

        List<PedidoDTO> body =
                (status != null)
                        ? service.findAllByStatus(status)
                        : service.findAll();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> create(
            @RequestBody @Validated(PedidoDTO.Create.class) PedidoDTO dto) {

        PedidoDTO created = service.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> update(
            @PathVariable Long id,
            @RequestBody @Validated(PedidoDTO.Update.class) PedidoDTO dto) {

        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<PedidoDTO> adicionarProduto(
            @PathVariable Long id,
            @RequestBody @Validated PedidoDTO.ItemPedidoDTO itemDto) {

        return ResponseEntity.ok(service.adicionarProduto(id, itemDto));
    }

    @PatchMapping("/{id}/frete/{tipoFrete}")
    public ResponseEntity<PedidoDTO> definirFrete(
            @PathVariable Long id,
            @PathVariable String tipoFrete) {

        return ResponseEntity.ok(service.definirFrete(id, tipoFrete));
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<PedidoDTO> atualizarStatus(
            @PathVariable Long id,
            @PathVariable String status) {

        return ResponseEntity.ok(service.atualizarStatus(id, status));
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<PedidoDTO> realizarPagamento(@PathVariable Long id) {
        return ResponseEntity.ok(service.realizarPagamento(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoDTO> cancelarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelarPedido(id));
    }

    @PatchMapping("/{id}/despachar")
    public ResponseEntity<PedidoDTO> despacharPedido(@PathVariable Long id) {
        return ResponseEntity.ok(service.despacharPedido(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
