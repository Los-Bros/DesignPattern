package com.domains.status;

import com.domains.Pedido;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnviadoStatus implements Status {

    private Pedido pedido;

    public EnviadoStatus(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void sucessoAoPagar() {
        try {
            throw new Exception("Operação não suportada - pedido enviado");
        } catch (Exception ex) {
            Logger.getLogger(CanceladoStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void cancelarPedido() {
        try {
            throw new Exception("Operação não suportada - pedido enviado");
        } catch (Exception ex) {
            Logger.getLogger(CanceladoStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void despacharPedido() {
        try {
            throw new Exception("Operação não suportada - pedido enviado");
        } catch (Exception ex) {
            Logger.getLogger(CanceladoStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
