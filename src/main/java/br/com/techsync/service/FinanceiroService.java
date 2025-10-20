package br.com.techsync.service;

import br.com.techsync.models.cliente.Cliente;
import br.com.techsync.models.Financeiro;
import br.com.techsync.repository.cliente.ClienteRepository;
import br.com.techsync.repository.FinanceiroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FinanceiroService {

    private final FinanceiroRepository financeiroRepository;
    private final ClienteRepository clienteRepository;

    public FinanceiroService(FinanceiroRepository financeiroRepository,
                             ClienteRepository clienteRepository) {
        this.financeiroRepository = financeiroRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Financeiro> listarTodos() {
        return financeiroRepository.findAll();
    }

    public Optional<Financeiro> buscarPorId(Integer id) {
        return financeiroRepository.findById(id);
    }

    public Financeiro salvar(Financeiro financeiro, Integer clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        financeiro.setCliente(cliente);
        financeiro.gerarNotaFiscal();

        return financeiroRepository.save(financeiro);
    }

    public boolean deletar(Integer id) {
        Optional<Financeiro> financeiro = financeiroRepository.findById(id);
        if (financeiro.isPresent()) {
            financeiroRepository.delete(financeiro.get());
            return true;
        }
        return false;
    }
}
