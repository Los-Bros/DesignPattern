package com.domains.status;

import com.domains.Pedido;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AguardandoPagamentoStatus implements Status {

    private Pedido pedido;

    public AguardandoPagamentoStatus(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void sucessoAoPagar() {
        this.pedido.setEstadoAtual(new PagoStatus(pedido));
    }

    @Override
    public void cancelarPedido() {
        this.pedido.setEstadoAtual(new CanceladoStatus(pedido));
    }

    @Override
    public void despacharPedido() {
        try {
            throw new Exception("Operação não suportada - pedido ainda não foi pago");
        } catch (Exception ex) {
            Logger.getLogger(AguardandoPagamentoStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
