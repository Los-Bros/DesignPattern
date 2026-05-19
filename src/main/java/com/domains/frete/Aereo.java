package com.domains.frete;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Aereo implements Frete {

    private static final BigDecimal PERCENTUAL_FRETE = new BigDecimal("0.10");

    @Override
    public BigDecimal calcula(BigDecimal valorPedido) {
        validarValorPedido(valorPedido);
        return valorPedido.multiply(PERCENTUAL_FRETE).setScale(3, RoundingMode.HALF_UP);
    }

    private void validarValorPedido(BigDecimal valorPedido) {
        if (valorPedido == null || valorPedido.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor do pedido deve ser informado e nao pode ser negativo");
        }
    }

}
