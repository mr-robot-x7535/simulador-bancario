package conta;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class ContaTest {

    @Test
    void depositoAumentaSaldo() {
        Conta conta = new Conta(new BigDecimal("100.00"));
        conta.depositar(new BigDecimal("50.00"));
        assertEquals(new BigDecimal("150.00"), conta.getSaldo());
    }

    @Test
    void transferenciaComSucesso() {
        Conta origem = new Conta(new BigDecimal("300.00"));
        Conta destino = new Conta(new BigDecimal("100.00"));

        origem.transferir(destino, new BigDecimal("120.00"));

        assertEquals(new BigDecimal("180.00"), origem.getSaldo());
        assertEquals(new BigDecimal("220.00"), destino.getSaldo());
    }

    @Test
    void transferenciaSaldoInsuficienteLancaExcecao() {
        Conta origem = new Conta(new BigDecimal("50.00"));
        Conta destino = new Conta(new BigDecimal("100.00"));

        assertThrows(SaldoInsuficienteException.class, () -> {
            origem.transferir(destino, new BigDecimal("100.00"));
        });

        assertEquals(new BigDecimal("50.00"), origem.getSaldo());
        assertEquals(new BigDecimal("100.00"), destino.getSaldo());
    }

    @Test
    void transferenciaContaNulaLancaExcecao() {
        Conta origem = new Conta(new BigDecimal("200.00"));

        assertThrows(IllegalArgumentException.class, () -> {
            origem.transferir(null, new BigDecimal("50.00"));
        });
    }

    @Test
    void historicoRegistraOperacoesNaOrdem() {
        Conta conta = new Conta(new BigDecimal("100.00"));
        conta.depositar(new BigDecimal("50.00"));
        conta.sacar(new BigDecimal("30.00"));

        List<Transacao> historico = conta.getHistorico();

        assertEquals(2, historico.size());
        assertEquals(TipoTransacao.DEPOSITO, historico.get(0).tipo());
        assertEquals(TipoTransacao.SAQUE, historico.get(1).tipo());
    }

    @Test
    void historicoNaoPodeSerAlteradoDeFora() {
        Conta conta = new Conta(new BigDecimal("100.00"));

        assertThrows(UnsupportedOperationException.class, () -> {
            conta.getHistorico().add(new Transacao(TipoTransacao.SAQUE, BigDecimal.ONE, BigDecimal.ONE));
        });
    }
}
