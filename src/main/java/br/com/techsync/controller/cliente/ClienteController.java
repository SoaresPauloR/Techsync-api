package br.com.techsync.controller.cliente;

import br.com.techsync.models.cliente.Cliente;
import br.com.techsync.service.cliente.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    // Construtor do controller recebendo a instância do serviço de cliente
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // ================== Endpoints ==================

    // Cria um novo cliente
    @PostMapping
    public ResponseEntity<?> criarCliente(@RequestBody Cliente cliente) {
        try {
            // Verifica se o objeto cliente é válido
            if (cliente == null) {
                throw new NullPointerException("Objeto cliente não pode ser nulo.");
            }
            // Salva o cliente usando o serviço e retorna OK
            return ResponseEntity.ok(clienteService.save(cliente));
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ocorreu um erro interno");
        }
    }

    // Atualiza um cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<?> editarCliente(@PathVariable Integer id, @RequestBody Cliente cliente) {
        try {
            // Valida ID e objeto cliente
            if (id == null || id <= 0) {
                throw new NullPointerException("ID do cliente deve ser um número inteiro.");
            }
            if (cliente == null) {
                throw new NullPointerException("Objeto cliente não pode ser nulo.");
            }

            // Define o ID do cliente antes de atualizar
            cliente.setId(id);
            // Atualiza o cliente via serviço
            return ResponseEntity.ok(clienteService.update(cliente));
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ocorreu um erro interno");
        }
    }

    // Busca um cliente pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> listarCliente(@PathVariable Integer id) {
        try {
            // Valida o ID
            if (id == null || id <= 0) {
                throw new NullPointerException("ID do cliente deve ser um número inteiro.");
            }
            // Retorna o cliente encontrado
            return ResponseEntity.ok(clienteService.findById(id));
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ocorreu um erro interno");
        }
    }

    // Lista todos os clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    // Exclui um cliente pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Integer id) {
        try {
            // Valida o ID
            if (id == null || id <= 0) {
                throw new NullPointerException("ID do cliente deve ser um número inteiro.");
            }
            // Exclui o cliente via serviço e retorna status adequado
            return clienteService.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ocorreu um erro interno");
        }
    }
}