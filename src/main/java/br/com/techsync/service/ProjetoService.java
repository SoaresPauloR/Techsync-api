package br.com.techsync.service;

import br.com.techsync.models.Projeto;
import br.com.techsync.models.cliente.Cliente;
import br.com.techsync.repository.ProjetoRepository;
import br.com.techsync.repository.cliente.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final ClienteRepository clienteRepository;

    public ProjetoService(ProjetoRepository projetoRepository,
                          ClienteRepository clienteRepository) {
        this.projetoRepository = projetoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Projeto> listarTodos() {
        return projetoRepository.findAll();
    }

    public Optional<Projeto> buscarPorId(Integer id) {
        return projetoRepository.findById(id);
    }

    public Projeto salvar(Projeto projeto, Integer clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        projeto.setCliente(cliente);
        return projetoRepository.save(projeto);
    }

    public Optional<Projeto> deletar(Integer id) {
        Optional<Projeto> projeto = projetoRepository.findById(id);
        projeto.ifPresent(p -> projetoRepository.delete(p));
        return projeto;
    }

    public boolean mudarStatus(Integer id, String status) {
        Optional<Projeto> projeto = projetoRepository.findById(id);
        if (projeto.isPresent()) {
            projeto.get().mudarStatus(status);
            projetoRepository.save(projeto.get());
            return true;
        }
        return false;
    }
}
