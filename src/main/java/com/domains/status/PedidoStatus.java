package com.domains.status;

import com.domains.Pedido;

import java.text.Normalizer;
import java.util.Locale;

public enum PedidoStatus {
    AGUARDANDO_PAGAMENTO {
        @Override
        public Status criarStatus(Pedido pedido) {
            return new AguardandoPagamentoStatus(pedido);
        }
    },
    PAGO {
        @Override
        public Status criarStatus(Pedido pedido) {
            return new PagoStatus(pedido);
        }
    },
    ENVIADO {
        @Override
        public Status criarStatus(Pedido pedido) {
            return new EnviadoStatus(pedido);
        }
    },
    CANCELADO {
        @Override
        public Status criarStatus(Pedido pedido) {
            return new CanceladoStatus(pedido);
        }
    };

    public abstract Status criarStatus(Pedido pedido);

    public static PedidoStatus fromStatus(Status status) {
        if (status instanceof AguardandoPagamentoStatus) {
            return AGUARDANDO_PAGAMENTO;
        }

        if (status instanceof PagoStatus) {
            return PAGO;
        }

        if (status instanceof EnviadoStatus) {
            return ENVIADO;
        }

        if (status instanceof CanceladoStatus) {
            return CANCELADO;
        }

        throw new IllegalArgumentException("Status do pedido invalido");
    }

    public static PedidoStatus fromDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Status do pedido deve ser informado");
        }

        String normalizado = Normalizer.normalize(descricao.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace("-", "_")
                .replace(" ", "_")
                .toUpperCase(Locale.ROOT);

        return switch (normalizado) {
            case "AGUARDANDO", "AGUARDANDO_PAGAMENTO" -> AGUARDANDO_PAGAMENTO;
            case "PAGO" -> PAGO;
            case "ENVIADO" -> ENVIADO;
            case "CANCELADO" -> CANCELADO;
            default -> throw new IllegalArgumentException("Status do pedido invalido: " + descricao);
        };
    }
}
