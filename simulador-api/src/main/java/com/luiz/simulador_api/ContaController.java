package com.luiz.simulador_api;

import conta.ContaDAO;
import jakarta.validation.Valid;
import java.sql.SQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaRepository repository;
    private final ContaDAO contaDAO = new ContaDAO();

    public ContaController(ContaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResumo> buscar(@PathVariable Integer id) {
        return repository.findById(id)
                .map(c -> ResponseEntity.ok(new ContaResumo(c.getId(), c.getTitular(), c.getSaldo())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/depositos")
    public OperacaoResponse depositar(@PathVariable Integer id, @Valid @RequestBody ValorRequest pedido)
            throws SQLException {
        contaDAO.depositar(id, pedido.valor());
        return new OperacaoResponse(id, contaDAO.consultarSaldo(id));
    }

    @PostMapping("/{id}/saques")
    public OperacaoResponse sacar(@PathVariable Integer id, @Valid @RequestBody ValorRequest pedido)
            throws SQLException {
        contaDAO.sacar(id, pedido.valor());
        return new OperacaoResponse(id, contaDAO.consultarSaldo(id));
    }

    @PostMapping("/{id}/transferencias")
    public OperacaoResponse transferir(@PathVariable Integer id, @Valid @RequestBody TransferenciaRequest pedido)
            throws SQLException {
        contaDAO.transferir(id, pedido.destinoId(), pedido.valor());
        return new OperacaoResponse(id, contaDAO.consultarSaldo(id));
    }
}
