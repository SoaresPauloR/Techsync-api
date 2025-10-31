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

    // Lista todas as tarefas
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodas() {
        return ResponseEntity.ok(tarefaService.listarTodas());
    }

    // Lista tarefa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Integer id) {
        return tarefaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cria uma tarefa
    @PostMapping
    public Tarefa salvar(
            @RequestBody Tarefa tarefa,
            @RequestParam Integer projetoId,
            @RequestParam Integer usuarioId) {
        return tarefaService.salvar(tarefa, projetoId, usuarioId);
    }

    // Muda as informações referentes a tarefa
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarTarefa(
            @PathVariable Integer id,
            @RequestBody Tarefa tarefaAtualizada) {

        boolean atualizado = tarefaService.atualizar(id, tarefaAtualizada);

        if (atualizado) {
            return ResponseEntity.ok("Tarefa atualizada com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }

    // Muda o status da tarefa (EM ANDAMENTO, CONCLUIDA, etc)
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

    // Deleta uma tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Integer id) {
        tarefaService.deletar(id);
        return ResponseEntity.ok("Tarefa deletada com sucesso!");
    }
}