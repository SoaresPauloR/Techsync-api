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

    // Listar todos os projetos
    public List<Projeto> listarTodos() {
        return projetoRepository.findAll();
    }

    // Buscar projeto por ID
    public Optional<Projeto> buscarPorId(Integer id) {
        return projetoRepository.findById(id);
    }

    // Salvar projeto (inicializa statusPagamento como PENDENTE)
    public Projeto salvar(Projeto projeto, Integer clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        projeto.setCliente(cliente);

        // Inicializa financeiro simplificado
        if (projeto.getStatusPagamento() == null) {
            projeto.setStatusPagamento("PENDENTE");
        }

        return projetoRepository.save(projeto);
    }

    // Deletar projeto
    public Optional<Projeto> deletar(Integer id) {
        Optional<Projeto> projeto = projetoRepository.findById(id);
        projeto.ifPresent(p -> projetoRepository.delete(p));
        return projeto;
    }

    public boolean atualizar(Integer id, Projeto projetoAtualizado) {
        Optional<Projeto> projetoOpt = projetoRepository.findById(id);
        if (projetoOpt.isPresent()) {
            Projeto projeto = projetoOpt.get();

            // Atualiza dados
            projeto.setNome(projetoAtualizado.getNome());
            projeto.setDescricao(projetoAtualizado.getDescricao());
            projeto.setStatus(projetoAtualizado.getStatus());
            projeto.setDataInicio(projetoAtualizado.getDataInicio());
            projeto.setDataTermino(projetoAtualizado.getDataTermino());

            // Atualiza cliente, se necessário
            Cliente cliente = clienteRepository.findById(projetoAtualizado.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            projeto.setCliente(cliente);

            projetoRepository.save(projeto);
            return true;
        }
        return false;
    }


    // Mudar status do projeto (EM ANDAMENTO, CONCLUIDO, etc)
    public boolean mudarStatus(Integer id, String status) {
        Optional<Projeto> projeto = projetoRepository.findById(id);
        if (projeto.isPresent()) {
            projeto.get().mudarStatus(status);
            projetoRepository.save(projeto.get());
            return true;
        }
        return false;
    }

    // Registrar pagamento do projeto (PENDENTE -> PAGO)
    public boolean pagarProjeto(Integer id) {
        Optional<Projeto> projetoOpt = projetoRepository.findById(id);
        if (projetoOpt.isPresent()) {
            Projeto projeto = projetoOpt.get();
            if (!"PAGO".equals(projeto.getStatusPagamento())) {
                projeto.setStatusPagamento("PAGO");
                projetoRepository.save(projeto);
                return true;
            }
        }
        return false;
    }
}
