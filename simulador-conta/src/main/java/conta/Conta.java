package conta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Conta {
    private BigDecimal saldo;
    private List<Transacao> historico = new ArrayList<>();

    public Conta(BigDecimal saldoInicial) {
        if (saldoInicial == null || saldoInicial.signum() < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser nulo ou negativo.");
        }
        this.saldo = saldoInicial;
    }

    public void depositar(BigDecimal valor) {
        creditar(valor);
        historico.add(new Transacao(TipoTransacao.DEPOSITO, valor, this.saldo));
    }

    public void sacar(BigDecimal valor) {
        debitar(valor);
        historico.add(new Transacao(TipoTransacao.SAQUE, valor, this.saldo));
    }

    public void transferir(Conta destino, BigDecimal valor) {
        if (destino == null) {
            throw new IllegalArgumentException("Conta de destino não pode ser nula");
        }
        this.debitar(valor);
        destino.creditar(valor);

        this.historico.add(new Transacao(TipoTransacao.TRANSFERENCIA_ENVIADA, valor, this.saldo));
        destino.historico.add(new Transacao(TipoTransacao.TRANSFERENCIA_RECEBIDA, valor, destino.saldo));
    }

    private void creditar(BigDecimal valor) {
        if (valor == null || valor.signum() <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }
        this.saldo = this.saldo.add(valor);
    }

    private void debitar(BigDecimal valor) {
        if (valor == null || valor.signum() <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser positivo.");
        }
        if (this.saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar o saque. Saldo atual: R$ " + this.saldo);
        }
        this.saldo = this.saldo.subtract(valor);
    }

    public BigDecimal getSaldo() {
        return this.saldo;
    }

    public List<Transacao> getHistorico() {
        return List.copyOf(historico);
    }
}
