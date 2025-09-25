package br.com.techsync.service;

import br.com.techsync.models.Projeto;
import br.com.techsync.models.Tarefa;
import br.com.techsync.models.Usuario;
import br.com.techsync.repository.ProjetoRepository;
import br.com.techsync.repository.TarefaRepository;
import br.com.techsync.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;

    public TarefaService(TarefaRepository tarefaRepository,
                         ProjetoRepository projetoRepository,
                         UsuarioRepository usuarioRepository) {
        this.tarefaRepository = tarefaRepository;
        this.projetoRepository = projetoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    public Optional<Tarefa> buscarPorId(Integer id) {
        return tarefaRepository.findById(id);
    }

    public Tarefa salvar(Tarefa tarefa, Integer projetoId, Integer usuarioId) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        tarefa.setProjeto(projeto);
        tarefa.setResponsavel(usuario);
        return tarefaRepository.save(tarefa);
    }

    public Optional<Tarefa> deletar(Integer id) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        tarefa.ifPresent(t -> tarefaRepository.delete(t));
        return tarefa;
    }

    public boolean atualizar(Integer id, Tarefa tarefaAtualizada, Integer responsavelId) {
        Optional<Tarefa> tarefaOpt = tarefaRepository.findById(id);
        if (tarefaOpt.isPresent()) {
            Tarefa tarefa = tarefaOpt.get();

            // Atualiza dados
            tarefa.setNome(tarefaAtualizada.getNome());
            tarefa.setDescricao(tarefaAtualizada.getDescricao());
            tarefa.setStatus(tarefaAtualizada.getStatus());
            tarefa.setDataInicio(tarefaAtualizada.getDataInicio());
            tarefa.setDataTermino(tarefaAtualizada.getDataTermino());

            // Atualiza responsável
            Usuario responsavel = usuarioRepository.findById(responsavelId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            tarefa.setResponsavel(responsavel);

            tarefaRepository.save(tarefa);
            return true;
        }
        return false;
    }


    public boolean mudarStatus(Integer id, String status) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        if (tarefa.isPresent()) {
            tarefa.get().mudarStatus(status);
            tarefaRepository.save(tarefa.get());
            return true;
        }
        return false;
    }
}
