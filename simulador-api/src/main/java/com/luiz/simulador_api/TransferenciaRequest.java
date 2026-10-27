package com.luiz.simulador_api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record TransferenciaRequest(@NotNull Integer destinoId, @NotNull @Positive BigDecimal valor) {
}
