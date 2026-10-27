package conta;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static int lerInteiro(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Digite apenas um número inteiro: ");
            }
        }
    }

    private static BigDecimal lerValor(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine().trim().replace(",", ".");
            try {
                return new BigDecimal(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Valor inválido. Digite um número (ex: 50,00): ");
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        ContaDAO dao = new ContaDAO();

        System.out.print("Digite o ID da sua conta (1 = Luiz, 2 = Anna): ");
        int contaId = lerInteiro(scanner);

        int opc;
        do {
            System.out.println("\n====== CONTA BANCÁRIA ======");
            System.out.println("1 - Depositar");
            System.out.println("2 - Sacar");
            System.out.println("3 - Transferir");
            System.out.println("4 - Ver histórico");
            System.out.println("5 - Ver saldo");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            opc = lerInteiro(scanner);

            try {
                switch (opc) {
                    case 1:
                        System.out.print("Digite o valor do depósito: ");
                        dao.depositar(contaId, lerValor(scanner));
                        System.out.println("Depósito realizado.");
                        break;

                    case 2:
                        System.out.print("Digite o valor do saque: ");
                        dao.sacar(contaId, lerValor(scanner));
                        System.out.println("Saque realizado.");
                        break;

                    case 3:
                        System.out.print("ID da conta destino: ");
                        int destinoId = lerInteiro(scanner);
                        System.out.print("Digite o valor da transferência: ");
                        dao.transferir(contaId, destinoId, lerValor(scanner));
                        System.out.println("Transferência realizada.");
                        break;

                    case 4:
                        System.out.println("\n--- HISTÓRICO ---");
                        dao.listarHistorico(contaId);
                        break;

                    case 5:
                        System.out.println("Saldo atual: R$ " + dao.consultarSaldo(contaId));
                        break;

                    case 0:
                        System.out.println("Saindo...");
                        break;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            } catch (IllegalArgumentException | SaldoInsuficienteException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            }
        } while (opc != 0);

        scanner.close();
    }
}
