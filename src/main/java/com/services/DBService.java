package com.services;

import com.domains.Pedido;
import com.domains.Produto;
import com.domains.frete.Aereo;
import com.domains.frete.Terrestre;
import com.repositories.PedidoRepository;
import com.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class DBService {

    @Autowired
    private ProdutoRepository produtoRepo;

    @Autowired
    private PedidoRepository pedidoRepo;

    public void initDB() {

        try {
            Produto produto1 = new Produto(null, "Televisao", new BigDecimal("1500.000"));
            Produto produto2 = new Produto(null, "Armario", new BigDecimal("800.000"));
            produtoRepo.saveAll(List.of(produto1, produto2));

            Pedido pedido1 = new Pedido();
            pedido1.setTipoFrete(new Terrestre());
            pedido1.adicionarItem(produto1, 1);
            pedido1.adicionarItem(produto2, 1);
            pedido1.realizarPagamento();

            Pedido pedido2 = new Pedido();
            pedido2.setTipoFrete(new Aereo());
            pedido2.adicionarItem(produto2, 2);

            pedidoRepo.saveAll(List.of(pedido1, pedido2));
        } catch (Exception e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
