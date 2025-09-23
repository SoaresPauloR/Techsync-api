package br.com.techsync.controller.chat;

import br.com.techsync.models.cliente.Cliente;
import br.com.techsync.repository.cliente.ClienteRepository;
import br.com.techsync.service.cliente.ClienteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ClienteRepository cliente;
    private final ClienteService clienteService;

    public ChatController(ClienteRepository cliente) {
        this.cliente = cliente;
        this.clienteService = new ClienteService(cliente);
    }


    @PostMapping
    public ResponseEntity<String> chat(@RequestBody Map<String, String> payload) throws JsonProcessingException {
        String userInput = payload.get("user_input");

        String geminiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-goog-api-key", "AIzaSyAV03IB_z_xADRd-jiRTfz3b_HuDSqZ7VM");

        // Montando os 3 "parts"
        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> contentItem = new HashMap<>();
        List<Map<String, String>> parts = new ArrayList<>();

        // 1️⃣ Instrução para a IA
        Map<String, String> instruction = new HashMap<>();
        instruction.put("text", "Você é um assistente inteligente. Responda de forma clara e objetiva usando os dados fornecidos.");
        parts.add(instruction);

        // 2️⃣ Dados da sua API
        List<Cliente> apiDataJson = clienteService.findAll();

        // Converte a lista de clientes para JSON
        ObjectMapper mapper = new ObjectMapper();
        String clientesJson = mapper.writeValueAsString(apiDataJson);

        Map<String, String> apiData = new HashMap<>();
        apiData.put("text", "Aqui estão os dados da minha API: " + clientesJson);
        parts.add(apiData);

        // 3️⃣ Prompt do usuário
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
}