package com.luiz.simulador_api;

import java.math.BigDecimal;

public record OperacaoResponse(Integer contaId, BigDecimal saldo) {
}
