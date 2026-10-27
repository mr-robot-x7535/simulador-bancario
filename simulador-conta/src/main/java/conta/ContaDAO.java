package conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContaDAO {

    public BigDecimal consultarSaldo(int contaId) throws SQLException {
        String sql = "SELECT saldo FROM conta WHERE id = ?";

        try (Connection conexao = ConexaoBanco.obter();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, contaId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("Conta " + contaId + " não encontrada.");
                }
                return rs.getBigDecimal("saldo");
            }
        }
    }

    public void depositar(int contaId, BigDecimal valor) throws SQLException {
        operarConta(contaId, valor, TipoTransacao.DEPOSITO);
    }

    public void sacar(int contaId, BigDecimal valor) throws SQLException {
        operarConta(contaId, valor, TipoTransacao.SAQUE);
    }

    public void transferir(int origemId, int destinoId, BigDecimal valor) throws SQLException {
        if (origemId == destinoId) {
            throw new IllegalArgumentException("Conta de origem e destino não podem ser iguais.");
        }

        Connection conexao = ConexaoBanco.obter();
        try {
            conexao.setAutoCommit(false);

            // Trava as contas sempre na mesma ordem (menor id primeiro), evitando deadlock
            Conta origem;
            Conta destino;
            if (origemId < destinoId) {
                origem = buscarConta(conexao, origemId);
                destino = buscarConta(conexao, destinoId);
            } else {
                destino = buscarConta(conexao, destinoId);
                origem = buscarConta(conexao, origemId);
            }

            // As regras (valor positivo, saldo suficiente) ficam na classe Conta
            origem.transferir(destino, valor);

            atualizarSaldo(conexao, origemId, origem.getSaldo());
            atualizarSaldo(conexao, destinoId, destino.getSaldo());
            inserirMovimentacao(conexao, origemId, TipoTransacao.TRANSFERENCIA_ENVIADA, valor);
            inserirMovimentacao(conexao, destinoId, TipoTransacao.TRANSFERENCIA_RECEBIDA, valor);

            conexao.commit();
        } catch (SQLException | RuntimeException e) {
            conexao.rollback();
            throw e;
        } finally {
            conexao.close();
        }
    }

    public void listarHistorico(int contaId) throws SQLException {
        String sql = """
            SELECT c.titular, m.tipo, m.valor, m.data_hora
            FROM movimentacao m
            INNER JOIN conta c ON m.conta_id = c.id
            WHERE c.id = ?
            ORDER BY m.id
            """;

        try (Connection conexao = ConexaoBanco.obter();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, contaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getTimestamp("data_hora").toLocalDateTime() + " | "
                            + rs.getString("titular") + " | "
                            + rs.getString("tipo") + " | R$ "
                            + rs.getBigDecimal("valor"));
                }
            }
        }
    }

    private void operarConta(int contaId, BigDecimal valor, TipoTransacao tipo) throws SQLException {
        Connection conexao = ConexaoBanco.obter();
        try {
            conexao.setAutoCommit(false);

            Conta conta = buscarConta(conexao, contaId);
            if (tipo == TipoTransacao.DEPOSITO) {
                conta.depositar(valor);
            } else {
                conta.sacar(valor);
            }

            atualizarSaldo(conexao, contaId, conta.getSaldo());
            inserirMovimentacao(conexao, contaId, tipo, valor);

            conexao.commit();
        } catch (SQLException | RuntimeException e) {
            conexao.rollback();
            throw e;
        } finally {
            conexao.close();
        }
    }

    private Conta buscarConta(Connection conexao, int contaId) throws SQLException {
        String sql = "SELECT saldo FROM conta WHERE id = ? FOR UPDATE";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, contaId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("Conta " + contaId + " não encontrada.");
                }
                return new Conta(rs.getBigDecimal("saldo"));
            }
        }
    }

    private void atualizarSaldo(Connection conexao, int contaId, BigDecimal novoSaldo) throws SQLException {
        String sql = "UPDATE conta SET saldo = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setBigDecimal(1, novoSaldo);
            stmt.setInt(2, contaId);
            stmt.executeUpdate();
        }
    }

    private void inserirMovimentacao(Connection conexao, int contaId, TipoTransacao tipo, BigDecimal valor) throws SQLException {
        String sql = "INSERT INTO movimentacao (conta_id, tipo, valor) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, contaId);
            stmt.setString(2, tipo.name());
            stmt.setBigDecimal(3, valor);
            stmt.executeUpdate();
        }
    }
}
