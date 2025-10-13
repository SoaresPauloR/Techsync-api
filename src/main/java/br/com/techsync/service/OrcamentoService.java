package br.com.techsync.service;

import br.com.techsync.models.Orcamento;
import br.com.techsync.models.Servicos;
import br.com.techsync.repository.OrcamentoRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;

    public OrcamentoService(OrcamentoRepository orcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
    }

    public Orcamento criarOrcamento(Orcamento orcamento) {
        double valorTotal = 0.0;

        for (Servicos s : orcamento.getServicos()) {
            s.setOrcamento(orcamento);
            valorTotal += s.getValor() * s.getQuantidade();
        }

        orcamento.setValor(valorTotal);

        return orcamentoRepository.save(orcamento);
    }

    public List<Orcamento> listarTodos() {
        return orcamentoRepository.findAll();
    }

    public Optional<Orcamento> buscarPorId(int id) {
        return orcamentoRepository.findById(id);
    }

    public boolean deletar(int id) {
        if (orcamentoRepository.existsById(id)) {
            orcamentoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Orcamento salvar(Orcamento orcamento) {
        return orcamentoRepository.save(orcamento);
    }

    public Orcamento atualizarOrcamento(int id, Orcamento dadosAtualizados) {
        return orcamentoRepository.findById(id).map(orcamentoExistente -> {

            // Copia os dados básicos
            orcamentoExistente.setCliente(dadosAtualizados.getCliente());
            orcamentoExistente.setStatus(dadosAtualizados.getStatus());

            // Limpa os serviços antigos e adiciona os novos
            orcamentoExistente.getServicos().clear();
            double valorTotal = 0.0;

            for (Servicos s : dadosAtualizados.getServicos()) {
                s.setOrcamento(orcamentoExistente); // mantém o vínculo bidirecional
                orcamentoExistente.getServicos().add(s);
                valorTotal += s.getValor() * s.getQuantidade();
            }

            // Atualiza o valor total
            orcamentoExistente.setValor(valorTotal);

            // Agora salva tudo
            return orcamentoRepository.save(orcamentoExistente);
        }).orElse(null);
    }

}