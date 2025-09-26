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

    // Listar todos os projetos
    @GetMapping
    public ResponseEntity<List<Projeto>> listarTodos() {
        return ResponseEntity.ok(projetoService.listarTodos());
    }

    // Buscar projeto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Projeto> buscarPorId(@PathVariable Integer id) {
        return projetoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Criar projeto
    @PostMapping
    public ResponseEntity<String> salvar(
            @RequestBody Projeto projeto,
            @RequestParam Integer clienteId) {
        projetoService.salvar(projeto, clienteId);
        return ResponseEntity.ok("Projeto criado com sucesso!");
    }

    // Editar informações referentes ao projeto
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarProjeto(
            @PathVariable Integer id,
            @RequestBody Projeto projetoAtualizado) {

        boolean atualizado = projetoService.atualizar(id, projetoAtualizado);

        if (atualizado) {
            return ResponseEntity.ok("Projeto atualizado com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }


    // Mudar status do projeto (EM ANDAMENTO, CONCLUIDO, etc)
    @PutMapping("/{id}/status")
    public ResponseEntity<String> mudarStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        boolean atualizado = projetoService.mudarStatus(id, status);
        if (atualizado) {
            return ResponseEntity.ok("Status do projeto atualizado com sucesso!");
        }
        return ResponseEntity.badRequest().body("Não foi possível atualizar o status do projeto.");
    }

    // Deletar projeto
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Integer id) {
        projetoService.deletar(id);
        return ResponseEntity.ok("Projeto deletado com sucesso!");
    }

    // Registrar pagamento do projeto (PENDENTE -> PAGO)
    @PutMapping("/{id}/pagar")
    public ResponseEntity<String> pagarProjeto(@PathVariable Integer id) {
        boolean pago = projetoService.pagarProjeto(id);
        if (pago) {
            return ResponseEntity.ok("Projeto pago com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Projeto já está pago ou não existe.");
        }
    }
}

