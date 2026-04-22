package com.domains;

import com.domains.status.Status;
import com.domains.status.AguardandoPagamentoStatus;

public class Pedido {

    //estado atual do pedido.
    private Status estadoAtual;

    public Pedido() {
        System.out.println("Pedido aguardando pagamento");
        //define o estado atual
        this.estadoAtual = new AguardandoPagamentoStatus(this);
    }
    public void sucessoAoPagar(){
        try{
            System.out.println("Pedido Pago");
            this.estadoAtual.sucessoAoPagar();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void cancelarPedido(){
        try{
            System.out.println("Pedido Cancelar");
            this.estadoAtual.cancelarPedido();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void despacharPedido(){
        try{
            System.out.println("Pedido Enviado");
            this.estadoAtual.despacharPedido();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void setEstadoAtual(Status estadoAtual) {
        this.estadoAtual = estadoAtual;
    }

}
