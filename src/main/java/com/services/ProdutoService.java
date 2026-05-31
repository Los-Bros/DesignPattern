package com.services;

import com.domains.Produto;
import com.domains.dtos.ProdutoDTO;
import com.mappers.ProdutoMapper;
import com.repositories.ProdutoRepository;
import com.services.exceptions.ObjectNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProdutoService {

    private static final int MAX_PAGE_SIZE = 200;

    private final ProdutoRepository produtoRepo;

    public ProdutoService(ProdutoRepository produtoRepo) {
        this.produtoRepo = produtoRepo;
    }



    @Transactional(readOnly = true)
    public List<ProdutoDTO> findAll() {
        return ProdutoMapper.toDtoList(produtoRepo.findAll());
    }

    @Transactional(readOnly = true)
    public Page<ProdutoDTO> findAll(Pageable pageable) {
        final Pageable effective;
        if (pageable == null || pageable.isUnpaged()) {
            effective = Pageable.unpaged();
        } else {
            effective = PageRequest.of(
                    Math.max(0, pageable.getPageNumber()),
                    Math.min(pageable.getPageSize(), MAX_PAGE_SIZE),
                    pageable.getSort()
            );
        }

        Page<Produto> page = produtoRepo.findAll(effective);
        return ProdutoMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoDTO> findAllByDescricao(String descricao, Pageable pageable) {
        if (descricao == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Descrição é obrigatório");


        final Pageable effective =
                (pageable == null || pageable.isUnpaged())
                        ? Pageable.unpaged()
                        : PageRequest.of(
                        Math.max(0, pageable.getPageNumber()),
                        Math.min(pageable.getPageSize(), MAX_PAGE_SIZE),
                        pageable.getSort()
                );

        Page<Produto> page =
                produtoRepo.findByDescricao(descricao, effective);

        if(page.getTotalPages() == 0)
            throw new ObjectNotFoundException("Produto " + descricao + " não encontrado");


        return ProdutoMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> findAllByDescricao(String descricao) {
        return findAllByDescricao(descricao, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public ProdutoDTO findById(Long id) {
        if (id == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id é obrigatório");

        return produtoRepo.findById(id)
                .map(ProdutoMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Produto não encontrado: id=" + id));
    }

    @Transactional
    public ProdutoDTO create(ProdutoDTO dto) {
        if (dto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados são obrigatórios");

        dto.setId(null);
        Produto entidade = ProdutoMapper.toEntity(dto);

        return ProdutoMapper.toDto(produtoRepo.save(entidade));
    }

    @Transactional
    public ProdutoDTO update(Long id, ProdutoDTO dto) {
        if (dto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados são obrigatórios");

        produtoRepo.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado: id=" + id));

        dto.setId(id);
        Produto entidade = ProdutoMapper.toEntity(dto);

        return ProdutoMapper.toDto(produtoRepo.save(entidade));
    }

    @Transactional
    public void delete(Long id) {
        if (id == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");

        Produto mov = produtoRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Produto não encontrado: id=" + id));

        produtoRepo.delete(mov);
    }
    
}
