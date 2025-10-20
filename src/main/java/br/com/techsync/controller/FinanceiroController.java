package br.com.techsync.controller;

import br.com.techsync.models.Financeiro;
import br.com.techsync.service.FinanceiroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financeiro")
public class FinanceiroController {

    private final FinanceiroService financeiroService;

    public FinanceiroController(FinanceiroService financeiroService) {
        this.financeiroService = financeiroService;
    }

    @GetMapping
    public ResponseEntity<List<Financeiro>> listarTodos() {
        return ResponseEntity.ok(financeiroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Financeiro> buscarPorId(@PathVariable Integer id) {
        return financeiroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> salvar(@RequestBody Financeiro financeiro,
                                         @RequestParam Integer clienteId) {
        financeiroService.salvar(financeiro, clienteId);
        return ResponseEntity.ok("Transação registrada com sucesso e nota fiscal gerada!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Integer id) {
        boolean deletado = financeiroService.deletar(id);
        if (deletado) {
            return ResponseEntity.ok("Transação removida com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }
}
