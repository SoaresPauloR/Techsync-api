package br.com.techsync.controller.chat;

import br.com.techsync.models.Empresa;
import br.com.techsync.models.Orcamento;
import br.com.techsync.models.Projeto;
import br.com.techsync.models.Servicos;
import br.com.techsync.models.Tarefa;
import br.com.techsync.models.cliente.Cliente;
import br.com.techsync.models.cliente.Responsavel;
import br.com.techsync.repository.EmpresaRepository;
import br.com.techsync.repository.OrcamentoRepository;
import br.com.techsync.repository.ProjetoRepository;
import br.com.techsync.repository.ServicosRepository;
import br.com.techsync.repository.TarefaRepository;
import br.com.techsync.repository.cliente.ClienteRepository;
import br.com.techsync.repository.cliente.ResponsavelRepository;
import br.com.techsync.service.EmpresaService;
import br.com.techsync.service.OrcamentoService;
import br.com.techsync.service.ProjetoService;
import br.com.techsync.service.ServicosService;
import br.com.techsync.service.TarefaService;
import br.com.techsync.service.cliente.ClienteService;
import br.com.techsync.service.cliente.ResponsavelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    private final ClienteService clienteService;
    private final EmpresaService empresaService;
    private final OrcamentoService orcamentoService;
    private final ProjetoService projetoService;
    private final ServicosService servicosService;
    private final TarefaService tarefaService;
    private final ResponsavelService responsavelService;


    public ChatController(ClienteRepository clienteRepository, EmpresaRepository empresaRepository, OrcamentoRepository orcamentoRepository, ProjetoRepository projetoRepository, ServicosRepository servicosRepository, TarefaRepository tarefaRepository, ResponsavelRepository responsavelRepository, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.clienteService = new ClienteService(clienteRepository);
        this.empresaService = new EmpresaService(empresaRepository);
        this.orcamentoService = new OrcamentoService(orcamentoRepository);
        this.projetoService = new ProjetoService(projetoRepository, clienteRepository);
        this.servicosService = new ServicosService(servicosRepository);
        this.tarefaService = new TarefaService(tarefaRepository, projetoRepository, null);
        this.responsavelService = new ResponsavelService(responsavelRepository);
    }


    @PostMapping
    public ResponseEntity<String> chat(@RequestBody Map<String, String> payload) throws JsonProcessingException {
        String userInput = payload.get("user_input");

        String geminiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-goog-api-key", "AIzaSyAV03IB_z_xADRd-jiRTfz3b_HuDSqZ7VM");

        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> contentItem = new HashMap<>();
        List<Map<String, String>> parts = new ArrayList<>();

        Map<String, String> instruction = new HashMap<>();
        instruction.put("text", "Você é um assistente inteligente. Responda de forma clara e objetiva usando os dados fornecidos.");
        parts.add(instruction);

        List<Map<String, Object>> clientesSimplificados = clienteService.findAll().stream().map(this::clienteToMap).collect(Collectors.toList());
        String clientesJson = objectMapper.writeValueAsString(clientesSimplificados);
        Map<String, String> apiDataClientes = new HashMap<>();
        apiDataClientes.put("text", "Dados de Clientes: " + clientesJson);
        parts.add(apiDataClientes);

        List<Map<String, Object>> empresasSimplificadas = empresaService.listarEmpresas().stream().map(this::empresaToMap).collect(Collectors.toList());
        String empresasJson = objectMapper.writeValueAsString(empresasSimplificadas);
        Map<String, String> apiDataEmpresas = new HashMap<>();
        apiDataEmpresas.put("text", "Dados de Empresas: " + empresasJson);
        parts.add(apiDataEmpresas);

        List<Map<String, Object>> orcamentosSimplificados = orcamentoService.listarTodos().stream().map(this::orcamentoToMap).collect(Collectors.toList());
        String orcamentosJson = objectMapper.writeValueAsString(orcamentosSimplificados);
        Map<String, String> apiDataOrcamentos = new HashMap<>();
        apiDataOrcamentos.put("text", "Dados de Orçamentos: " + orcamentosJson);
        parts.add(apiDataOrcamentos);

        List<Map<String, Object>> projetosSimplificados = projetoService.listarTodos().stream().map(this::projetoToMap).collect(Collectors.toList());
        String projetosJson = objectMapper.writeValueAsString(projetosSimplificados);
        Map<String, String> apiDataProjetos = new HashMap<>();
        apiDataProjetos.put("text", "Dados de Projetos: " + projetosJson);
        parts.add(apiDataProjetos);

        List<Map<String, Object>> servicosSimplificados = servicosService.listarTodos().stream().map(this::servicoToMap).collect(Collectors.toList());
        String servicosJson = objectMapper.writeValueAsString(servicosSimplificados);
        Map<String, String> apiDataServicos = new HashMap<>();
        apiDataServicos.put("text", "Dados de Serviços: " + servicosJson);
        parts.add(apiDataServicos);

        List<Map<String, Object>> tarefasSimplificadas = tarefaService.listarTodas().stream().map(this::tarefaToMap).collect(Collectors.toList());
        String tarefasJson = objectMapper.writeValueAsString(tarefasSimplificadas);
        Map<String, String> apiDataTarefas = new HashMap<>();
        apiDataTarefas.put("text", "Dados de Tarefas: " + tarefasJson);
        parts.add(apiDataTarefas);

        List<Map<String, Object>> responsaveisSimplificados = responsavelService.findAll().stream().map(this::responsavelToMap).collect(Collectors.toList());
        String responsaveisJson = objectMapper.writeValueAsString(responsaveisSimplificados);
        Map<String, String> apiDataResponsaveis = new HashMap<>();
        apiDataResponsaveis.put("text", "Dados de Responsáveis: " + responsaveisJson);
        parts.add(apiDataResponsaveis);

        Map<String, String> userPrompt = new HashMap<>();
        userPrompt.put("text", userInput);
        parts.add(userPrompt);

        contentItem.put("parts", parts);
        contents.add(contentItem);

        Map<String, Object> body = new HashMap<>();
        body.put("contents", contents);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(geminiUrl, request, String.class);

        return ResponseEntity.ok(response.getBody());
    }

    private Map<String, Object> clienteToMap(Cliente cliente) {
        Map<String, Object> map = new HashMap<>();
        map.put("nome", cliente.getNome());
        map.put("email", cliente.getEmail());
        map.put("telefone", cliente.getTelefone());
        return map;
    }

    private Map<String, Object> empresaToMap(Empresa empresa) {
        Map<String, Object> map = new HashMap<>();
        map.put("nome", empresa.getNome());
        return map;
    }

    private Map<String, Object> orcamentoToMap(Orcamento orcamento) {
        Map<String, Object> map = new HashMap<>();
        map.put("valor", orcamento.getValor());
        map.put("status", orcamento.getStatus());
        map.put("createdAt", orcamento.getCreatedAt());
        return map;
    }

    private Map<String, Object> projetoToMap(Projeto projeto) {
        Map<String, Object> map = new HashMap<>();
        map.put("nome", projeto.getNome());
        map.put("descricao", projeto.getDescricao());
        map.put("status", projeto.getStatus());
        map.put("dataInicio", projeto.getDataInicio());
        map.put("dataTermino", projeto.getDataTermino());
        map.put("statusPagamento", projeto.getStatusPagamento());
        return map;
    }

    private Map<String, Object> servicoToMap(Servicos servico) {
        Map<String, Object> map = new HashMap<>();
        map.put("descricao", servico.getDescricao());
        map.put("valor", servico.getValor());
        map.put("quantidade", servico.getQuantidade());
        return map;
    }

    private Map<String, Object> tarefaToMap(Tarefa tarefa) {
        Map<String, Object> map = new HashMap<>();
        map.put("nome", tarefa.getNome());
        map.put("descricao", tarefa.getDescricao());
        map.put("status", tarefa.getStatus());
        map.put("dataInicio", tarefa.getDataInicio());
        map.put("dataTermino", tarefa.getDataTermino());
        return map;
    }

    private Map<String, Object> responsavelToMap(Responsavel responsavel) {
        Map<String, Object> map = new HashMap<>();
        map.put("nome", responsavel.getNome());
        map.put("email", responsavel.getEmail());
        map.put("telefone", responsavel.getTelefone());
        map.put("cargo", responsavel.getFuncao());
        return map;
    }
}