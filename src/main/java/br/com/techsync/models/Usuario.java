package br.com.techsync.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "T_TS_USUARIOS")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Identificador único do usuário

    @Column(nullable = false)
    private String nome; // Nome completo do usuário

    @Column(nullable = false, unique = true)
    private String email; // E-mail usado para login

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String senha; // Senha do usuário (oculta nas respostas JSON)

    @Column
    private String codigo2FA; // Código usado para autenticação em duas etapas

    @Column(length = 15)
    private String telefone; // Telefone de contato

    @Column(length = 14, unique = true)
    private String cpf; // CPF do usuário, valor único no sistema

    // Construtores
    public Usuario() {} // Construtor padrão exigido pelo JPA

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCodigo2FA() { return codigo2FA; }
    public void setCodigo2FA(String codigo2FA) { this.codigo2FA = codigo2FA; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    // Retorna informações básicas do usuário
    public String getDados() {
        return "Id: " + id + ", Nome: " + nome + ", Email: " + email;
    }
}

