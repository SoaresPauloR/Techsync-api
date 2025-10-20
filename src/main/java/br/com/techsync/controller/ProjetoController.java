package br.com.techsync.controller;

import br.com.techsync.models.Projeto;
import br.com.techsync.service.ProjetoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @GetMapping
    public ResponseEntity<List<Projeto>> listarTodos() {
        return ResponseEntity.ok(projetoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projeto> buscarPorId(@PathVariable Integer id) {
        return projetoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> salvar(@RequestBody Projeto projeto,
                                         @RequestParam Integer clienteId) {
        projetoService.salvar(projeto, clienteId);
        return ResponseEntity.ok("Projeto criado com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Integer id,
                                            @RequestBody Projeto projetoAtualizado) {
        projetoService.atualizar(id, projetoAtualizado);
        return ResponseEntity.ok("Projeto atualizado com sucesso!");
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> mudarStatus(@PathVariable Integer id,
                                              @RequestParam String status) {
        boolean atualizado = projetoService.mudarStatus(id, status);
        if (atualizado) {
            return ResponseEntity.ok("Status do projeto atualizado com sucesso!");
        }
        return ResponseEntity.badRequest().body("Erro ao atualizar o status do projeto.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Integer id) {
        boolean deletado = projetoService.deletar(id);
        if (deletado) {
            return ResponseEntity.ok("Projeto deletado com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }
}
