package br.com.techsync.service;

import br.com.techsync.models.Usuario;
import br.com.techsync.repository.UsuarioRepository;
import br.com.techsync.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioService {

    // Interface para operações de banco de dados
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Utilizado para criptografar e validar senhas
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Utilitário para geração de tokens JWT
    @Autowired
    private JwtUtil jwtUtil;

    // Busca um usuário pelo email
    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    // Cria um novo usuário, garantindo email único e senha criptografada
    public Usuario criarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado. Por favor, utilize outro.");
        }
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioRepository.save(usuario);
    }

    // Edita dados de um usuário existente
    public Usuario editarUsuario(int id, Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuario u = usuarioExistente.get();
            u.setNome(usuario.getNome());
            u.setEmail(usuario.getEmail());
            u.setTelefone(usuario.getTelefone());
            u.setCpf(usuario.getCpf());

            // Atualiza a senha apenas se foi fornecida
            if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
                String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
                u.setSenha(senhaCriptografada);
            }

            return usuarioRepository.save(u);
        }
        return null;
    }

    // Retorna a lista de todos os usuários
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Exclui um usuário pelo ID
    public boolean excluirUsuario(int id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Busca um usuário pelo ID
    public Usuario buscarUsuarioId(int id){
        return usuarioRepository.findById(id).orElse(null);
    }

    // Realiza login e retorna token JWT se credenciais estiverem corretas
    public String loginComJwt(String email, String senha) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            boolean senhaCorreta = passwordEncoder.matches(senha, usuario.getSenha());

            if (senhaCorreta) {
                return jwtUtil.gerarToken(email);
            }
        }
        return null;
    }

    // Reseta a senha do usuário
    public boolean resetSenha(String email, String novaSenha){
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()){
            Usuario usuario = usuarioOptional.get();
            String senhaCriptografada = passwordEncoder.encode(novaSenha);
            usuario.setSenha(senhaCriptografada);
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }

    // Gera código 2FA para autenticação em duas etapas
    public boolean gerarCodigo2FA(String email){
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()){
            Usuario usuario = usuarioOptional.get();

            String codigo2FA = String.format("%06d", new Random().nextInt(999999));
            usuario.setCodigo2FA(codigo2FA);

            usuarioRepository.save(usuario);

            System.out.println("Codigo gerado = " + codigo2FA);

            return true;
        }
        return false;
    }

    // Valida login com autenticação em duas etapas
    public boolean autenticar2FA(String email, String senha, String codigo2FA){
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()){
            Usuario usuario = usuarioOptional.get();

            boolean senhaValida = passwordEncoder.matches(senha, usuario.getSenha());
            boolean codigoValido = codigo2FA.equals(usuario.getCodigo2FA());

            return senhaValida && codigoValido;
        }
        return false;
    }
}
