package br.com.techsync.controller;

import br.com.techsync.models.Tarefa;
import br.com.techsync.service.TarefaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodas() {
        return ResponseEntity.ok(tarefaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Integer id) {
        return tarefaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> salvar(
            @RequestBody Tarefa tarefa,
            @RequestParam Integer projetoId,
            @RequestParam Integer usuarioId) {
        tarefaService.salvar(tarefa, projetoId, usuarioId);
        return ResponseEntity.ok("Tarefa criada com sucesso!");
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> mudarStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        boolean atualizado = tarefaService.mudarStatus(id, status);
        if (atualizado) {
            return ResponseEntity.ok("Status da tarefa atualizado com sucesso!");
        }
        return ResponseEntity.badRequest().body("Não foi possível atualizar o status da tarefa.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Integer id) {
        tarefaService.deletar(id);
        return ResponseEntity.ok("Tarefa deletada com sucesso!");
    }
}