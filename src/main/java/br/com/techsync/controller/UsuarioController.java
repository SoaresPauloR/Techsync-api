package br.com.techsync.controller;

import br.com.techsync.dto.LoginResponse;
import br.com.techsync.dto.LoginRequest;
import br.com.techsync.dto.UsuarioRequest;
import br.com.techsync.dto.UsuarioResponse;
import br.com.techsync.models.Usuario;
import br.com.techsync.service.LogService;
import br.com.techsync.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LogService logService;

    // Criar um novo usuário
    @PostMapping
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        try {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setNome(usuarioRequest.getNome());
            novoUsuario.setEmail(usuarioRequest.getEmail());
            novoUsuario.setSenha(usuarioRequest.getSenha()); // A senha será criptografada no serviço
            novoUsuario.setTelefone(usuarioRequest.getTelefone());
            novoUsuario.setCpf(usuarioRequest.getCpf());

            Usuario usuarioSalvo = usuarioService.criarUsuario(novoUsuario);
            UsuarioResponse usuarioResponse = new UsuarioResponse(usuarioSalvo);

            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponse);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = Map.of("error", "Ocorreu um erro interno ao criar o usuário.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Editar um usuário
    @PutMapping("/{id}")
    public ResponseEntity<?> editarUsuario(@PathVariable int id, @Valid @RequestBody UsuarioRequest usuarioRequest) {
        try {
            Usuario usuarioParaAtualizar = new Usuario();
            usuarioParaAtualizar.setNome(usuarioRequest.getNome());
            usuarioParaAtualizar.setEmail(usuarioRequest.getEmail());
            // A senha é opcional na edição, então só a atualizamos se ela for enviada.
            // Para maior segurança, um endpoint separado de "mudar senha" é uma boa prática.
            if (usuarioRequest.getSenha() != null && !usuarioRequest.getSenha().isBlank()) {
                usuarioParaAtualizar.setSenha(usuarioRequest.getSenha());
            }
            usuarioParaAtualizar.setTelefone(usuarioRequest.getTelefone());
            usuarioParaAtualizar.setCpf(usuarioRequest.getCpf());

            Usuario usuarioEditado = usuarioService.editarUsuario(id, usuarioParaAtualizar);

            if (usuarioEditado != null) {
                UsuarioResponse usuarioResponse = new UsuarioResponse(usuarioEditado);
                return ResponseEntity.ok(usuarioResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuário não encontrado."));
            }
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = Map.of("error", "Ocorreu um erro interno ao atualizar o usuário.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Listar todos os usuários
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        List<UsuarioResponse> usuarioResponses = usuarios.stream()
                .map(UsuarioResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarioResponses);
    }

    // Listar um único usuário por ID
    @GetMapping ("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioID(@PathVariable int id){
        Usuario usuario = usuarioService.buscarUsuarioId(id);
        if (usuario != null){
            return ResponseEntity.ok(new UsuarioResponse(usuario));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Excluir um usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirUsuario(@PathVariable int id) {
        return usuarioService.excluirUsuario(id)
                ? ResponseEntity.ok("Usuario excluido com sucesso!")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
    }

    // Fazer login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) { // <-- Tipo de retorno alterado para LoginResponse
        String token = usuarioService.loginComJwt(loginRequest.getEmail(), loginRequest.getSenha());
        String ip = request.getRemoteAddr();

        if (token != null) {
            // Buscando o usuário para retornar o objeto completo, não apenas o token
            Usuario usuarioAutenticado = usuarioService.buscarUsuarioPorEmail(loginRequest.getEmail());
            logService.registrarLogin(loginRequest.getEmail(), ip, true, null);

            // Cria o objeto LoginResponse
            LoginResponse responseBody = new LoginResponse(token, usuarioAutenticado);
            return ResponseEntity.ok().body(responseBody); // Spring serializará para JSON automaticamente
        } else {
            logService.registrarLogin(loginRequest.getEmail(), ip, false, "Email ou senha inválidos.");
            // Para erros, você pode retornar um ResponseEntity<Map<String, String>> ou um DTO de erro,
            // mas aqui manteremos a consistência com o que você já tinha para erros, retornando um JSON string
            // Mas, para ser consistente com o ResponseEntity<LoginResponse>, o ideal seria ter um ResponseEntity<ErrorResponse>
            // Por simplicidade, vamos retornar um erro HTTP BAD_REQUEST ou UNAUTHORIZED sem um corpo LoginResponse
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Retorna null no corpo se o tipo for LoginResponse, ou mude para ResponseEntity<String>
        }
    }


    // Resetar Senha do usuário
    @PostMapping("/resetSenha")
    public ResponseEntity<String> resetSenha(@RequestParam String email, @RequestParam String novaSenha){
        boolean sucesso = usuarioService.resetSenha(email,novaSenha);

        if (sucesso){
            return ResponseEntity.ok("Senha alterada com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
    }

    //Gerar codigo de autentificação de dois fatores (2FA)
    @PostMapping("/gerar2fa")
    public ResponseEntity<String> gerarCodigo2FA(@RequestParam String email){
        boolean gerado = usuarioService.gerarCodigo2FA(email);

        if(gerado){
            return ResponseEntity.ok("Codigo 2FA gerado com sucesso.");
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
    }

    //Realizando a autenticação de dois fatores (2FA)
    @PostMapping("/autenticar2fa")
    public ResponseEntity<String> autenticar2FA(
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam String codigo2FA){
        boolean autenticado = usuarioService.autenticar2FA(email, senha, codigo2FA);

        if (autenticado){
            return ResponseEntity.ok("Autenticação 2FA bem-sucedida");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação 2FA.");
        }
    }
}