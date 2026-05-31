package com.services;

import com.domains.*;
import com.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class DBService {

    @Autowired
    private ProdutoRepository produtoRepo;

    public void initDB() {

        try {
            // Criar produtos
            Produto produto1 = new Produto(null, "Televisão", new BigDecimal("1500.00"));
            Produto produto2 = new Produto(null, "Armário", new BigDecimal("800.00"));
            produtoRepo.save(produto1);
            produtoRepo.save(produto2);


        } catch (Exception e) {
            // Logar o erro ou lançar uma exceção customizada
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
