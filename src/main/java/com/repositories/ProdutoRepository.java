package com.repositories;

import com.domains.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Page<Produto> findById(Long id, Pageable pageable);
    Page<Produto> findByDescricao(String descricao, Pageable pageable);
}
