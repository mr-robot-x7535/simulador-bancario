package conta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    private static final String URL =
        "jdbc:mysql://localhost:3306/simulador_banco?serverTimezone=America/Sao_Paulo";
    private static final String USUARIO = "root";

    // A senha vem da variável de ambiente DB_SENHA. Sem ela, usa o texto de exemplo.
    private static final String SENHA = System.getenv().getOrDefault("DB_SENHA", "SUA_SENHA_AQUI");

    public static Connection obter() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
