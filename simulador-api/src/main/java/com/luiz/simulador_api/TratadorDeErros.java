package com.luiz.simulador_api;

import conta.SaldoInsuficienteException;
import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ErroResponse> saldoInsuficiente(SaldoInsuficienteException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> regraInvalida(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErroResponse(e.getMessage()));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErroResponse> erroBanco(SQLException e) {
        // A mensagem técnica do banco não vai para o cliente
        return ResponseEntity.internalServerError()
                .body(new ErroResponse("Erro no banco de dados."));
    }
}
