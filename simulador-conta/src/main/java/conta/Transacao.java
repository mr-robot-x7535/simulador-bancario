package conta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transacao(TipoTransacao tipo, BigDecimal valor, BigDecimal saldoApos, LocalDateTime dataHora) {
    public Transacao(TipoTransacao tipo, BigDecimal valor, BigDecimal saldoApos) {
        this(tipo, valor, saldoApos, LocalDateTime.now());
    }
}
