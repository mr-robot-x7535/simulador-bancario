# Simulador Bancário

Projeto de estudo em Java para praticar backend bancário: regras de negócio testadas, persistência em MySQL com JDBC e JPA, transações e uma API REST com Spring Boot.

## Estrutura

```
simulador-bancario/
├── sql/
│   └── simulador_banco.sql      # script de criação do banco e das tabelas
├── simulador-conta/             # regras de negócio, JDBC e menu no terminal
└── simulador-api/               # API REST com Spring Boot (usa o simulador-conta)
```

## Tecnologias

- Java 17
- Maven
- JUnit 5
- MySQL 8 (JDBC, `PreparedStatement`, transações)
- Spring Boot 3.3.5 (Web, Data JPA, Validation)

## Configuração do banco

1. No MySQL Workbench, execute o script `sql/simulador_banco.sql`.
2. Defina a senha do banco na variável de ambiente `DB_SENHA`. No PowerShell, na mesma janela em que for rodar os comandos:

```powershell
$env:DB_SENHA = "sua_senha_aqui"
```

Se a variável não estiver definida, o projeto usa o texto `SUA_SENHA_AQUI`, que vai falhar na conexão. Isso é de propósito: a senha real nunca fica no código.

## Como rodar

### 1. Simulador de conta (terminal)

```powershell
cd simulador-conta
mvn test
mvn exec:java "-Dexec.mainClass=conta.Main"
```

### 2. API REST

Antes, instale o `simulador-conta` no repositório local do Maven. Isso só precisa ser refeito quando ele mudar:

```powershell
cd simulador-conta
mvn install -DskipTests
cd ..\simulador-api
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.

## Endpoints

| Método | Caminho                          | Corpo (JSON)                          | Descrição              |
|--------|----------------------------------|---------------------------------------|------------------------|
| GET    | `/ola`                           | -                                     | Teste de funcionamento |
| GET    | `/contas/{id}`                   | -                                     | Consulta a conta       |
| POST   | `/contas/{id}/depositos`         | `{"valor": 10.00}`                    | Depósito               |
| POST   | `/contas/{id}/saques`            | `{"valor": 10.00}`                    | Saque                  |
| POST   | `/contas/{id}/transferencias`    | `{"destinoId": 2, "valor": 10.00}`    | Transferência          |

Erros de regra de negócio (saldo insuficiente) respondem 422. Dados inválidos respondem 400.

## Roadmap

- [ ] Transformar o `ContaDAO` em um `@Service` e injetá-lo no controller
- [ ] Autenticação com JWT
- [ ] Docker, com MySQL e API em um `docker-compose`
- [ ] Testes de integração da API
- [ ] Usar uma única forma de acesso aos dados (JPA ou JDBC)
