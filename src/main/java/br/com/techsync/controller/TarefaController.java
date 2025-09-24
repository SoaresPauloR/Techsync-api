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
        Optional<Tarefa> tarefa = tarefaService.buscarPorId(id);
        return tarefa.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tarefa> salvar(
            @RequestBody Tarefa tarefa,
            @RequestParam(required = false) Integer projetoId,
            @RequestParam(required = false) Integer usuarioId) {
        return ResponseEntity.ok(tarefaService.salvar(tarefa, projetoId, usuarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Optional<Tarefa>> deletar(@PathVariable Integer id) {
        return ResponseEntity.ok(tarefaService.deletar(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> mudarStatus(@PathVariable Integer id, @RequestParam String status) {
        boolean atualizado = tarefaService.mudarStatus(id, status);
        if (atualizado) {
            return ResponseEntity.ok("Status atualizado com sucesso!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
