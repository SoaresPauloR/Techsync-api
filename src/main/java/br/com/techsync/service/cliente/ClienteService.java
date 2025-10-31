package br.com.techsync.service.cliente;

import br.com.techsync.models.cliente.Cliente;
import br.com.techsync.models.cliente.Responsavel;
import br.com.techsync.repository.cliente.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Retorna todos os clientes cadastrados
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    // Busca um cliente pelo ID, retorna null se não encontrado
    public Cliente findById(Integer id) {
        if (id == null) return null;
        return clienteRepository.findById(id).orElse(null);
    }

    // Salva um novo cliente, atualizando a referência nos responsáveis
    public Cliente save(Cliente cliente) {
        if (cliente == null) return null;

        for (Responsavel r : cliente.getResponsaveis()) {
            r.setCliente(cliente);
        }

        return clienteRepository.save(cliente);
    }

    // Atualiza um cliente existente, também atualizando a referência nos responsáveis
    public Cliente update(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) return null;

        for (Responsavel r : cliente.getResponsaveis()) {
            r.setCliente(cliente);
        }

        return clienteRepository.save(cliente);
    }

    // Exclui um cliente pelo ID, retorna true se excluído com sucesso
    public boolean delete(Integer id) {
        if (id == null) return false;

        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isEmpty()) return false;

        clienteRepository.deleteById(id);
        return true;
    }
}


