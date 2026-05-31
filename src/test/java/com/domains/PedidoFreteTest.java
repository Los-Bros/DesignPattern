package com.domains;

import com.domains.frete.Aereo;
import com.domains.frete.Terrestre;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PedidoFreteTest {

    @Test
    void deveCalcularValorFinalComFreteTerrestre() {
        Pedido pedido = new Pedido();

        pedido.setValor(new BigDecimal("200.000"));
        pedido.setTipoFrete(new Terrestre());

        assertEquals(new BigDecimal("210.000"), pedido.getValorFinal());
    }

    @Test
    void deveCalcularValorFinalComFreteAereo() {
        Pedido pedido = new Pedido();

        pedido.setValor(new BigDecimal("200.000"));
        pedido.setTipoFrete(new Aereo());

        assertEquals(new BigDecimal("220.000"), pedido.getValorFinal());
    }

    @Test
    void deveAtualizarValorFinalAoAdicionarItem() {
        Pedido pedido = new Pedido();
        Produto produto = new Produto(null, "Produto teste", new BigDecimal("50.000"));

        pedido.setTipoFrete(new Terrestre());
        pedido.adicionarItem(produto, 2);

        assertEquals(new BigDecimal("100.000"), pedido.getValor());
        assertEquals(new BigDecimal("105.000"), pedido.getValorFinal());
    }

    @Test
    void deveExigirTipoFreteParaRealizarPagamento() {
        Pedido pedido = new Pedido();

        assertThrows(IllegalStateException.class, pedido::realizarPagamento);
    }

}
