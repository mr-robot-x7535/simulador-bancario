CREATE DATABASE IF NOT EXISTS simulador_banco;
USE simulador_banco;

CREATE TABLE IF NOT EXISTS conta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titular VARCHAR(100) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL DEFAULT 0.00
);

CREATE TABLE IF NOT EXISTS movimentacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    conta_id INT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conta_id) REFERENCES conta(id)
);

-- Dados de exemplo (rode só em banco novo)
-- INSERT INTO conta (titular, saldo) VALUES ('Luiz', 300.00), ('Anna', 100.00);
