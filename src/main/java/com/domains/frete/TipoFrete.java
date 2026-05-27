package com.domains.frete;

import java.text.Normalizer;
import java.util.Locale;

public enum TipoFrete {
    TERRESTRE {
        @Override
        public Frete criarFrete() {
            return new Terrestre();
        }
    },
    AEREO {
        @Override
        public Frete criarFrete() {
            return new Aereo();
        }
    };

    public abstract Frete criarFrete();

    public static TipoFrete fromFrete(Frete frete) {
        if (frete instanceof Terrestre) {
            return TERRESTRE;
        }

        if (frete instanceof Aereo) {
            return AEREO;
        }

        throw new IllegalArgumentException("Tipo de frete invalido");
    }

    public static TipoFrete fromDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Tipo de frete deve ser informado");
        }

        String normalizado = Normalizer.normalize(descricao.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace("-", "_")
                .replace(" ", "_")
                .toUpperCase(Locale.ROOT);

        return switch (normalizado) {
            case "TERRESTRE" -> TERRESTRE;
            case "AEREO" -> AEREO;
            default -> throw new IllegalArgumentException("Tipo de frete invalido: " + descricao);
        };
    }
}
