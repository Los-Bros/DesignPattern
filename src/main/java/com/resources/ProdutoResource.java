package com.resources;

import com.domains.dtos.ProdutoDTO;
import com.services.ProdutoService;
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
@RequestMapping("/api/produtos")
public class ProdutoResource {

    private final ProdutoService service;

    public ProdutoResource(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> list(
            @RequestParam(required = false) String descricao,
            @PageableDefault(size = 20, sort = "descricao") Pageable pageable) {

        Page<ProdutoDTO> page =
                (descricao != null)
                        ? service.findAllByDescricao(descricao, pageable)
                        : service.findAll(pageable);

        return ResponseEntity.ok(page);
    }


    @GetMapping("/all")
    public ResponseEntity<List<ProdutoDTO>> listAll(
            @RequestParam(required = false) String descricao) {

        List<ProdutoDTO> body =
                (descricao != null)
                        ? service.findAllByDescricao(descricao)
                        : service.findAll();

        return ResponseEntity.ok(body);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> findById(@PathVariable Long id) {
        ProdutoDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    public ResponseEntity<ProdutoDTO> create(
            @RequestBody @Validated(ProdutoDTO.Create.class) ProdutoDTO dto
    ) {
        ProdutoDTO created = service.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> update(
            @PathVariable Long id,
            @RequestBody @Validated(ProdutoDTO.Update.class) ProdutoDTO dto
    ) {
        dto.setId(id);
        ProdutoDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
