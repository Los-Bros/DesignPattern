package com.domains.status;

import com.domains.Pedido;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PagoStatus implements Status {

    private Pedido pedido;

    public PagoStatus(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void sucessoAoPagar() {
        try {
            throw new Exception("Operação não suportada - pedido já foi pago");
        } catch (Exception ex) {
            Logger.getLogger(CanceladoStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void cancelarPedido() {
        this.pedido.setEstadoAtual(new CanceladoStatus(pedido));
    }

    @Override
    public void despacharPedido() {
        this.pedido.setEstadoAtual(new EnviadoStatus(pedido));
    }

}
