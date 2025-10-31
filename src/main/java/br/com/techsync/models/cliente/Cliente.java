package br.com.techsync.models.cliente;

import br.com.techsync.enums.Status;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "T_TS_CLIENTE")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Identificador único do cliente

    @Column(nullable = false)
    private String nome; // Nome do cliente

    @Column(nullable = false, unique = true)
    private String email; // Email do cliente

    @Column(unique = true)
    private String cnpj_cpf; // CNPJ ou CPF do cliente

    @Column(nullable = false)
    private String telefone; // Número de telefone do cliente

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; // Status do cliente (ativo, inativo etc.)

    @Column(length = 1000)
    private String endereco; // Endereço do cliente

    @Lob
    private byte[] anexo; // Anexo (arquivo) associado ao cliente

    @Column(length = 1000)
    private String observacao; // Observações adicionais sobre o cliente

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Responsavel> responsaveis = new ArrayList<>(); // Lista de responsáveis vinculados ao cliente

    // Construtor padrão
    public Cliente() {}

    // ================== Getters e Setters ==================
    // Métodos para acessar e modificar os atributos do cliente

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCnpj_cpf() { return cnpj_cpf; }
    public void setCnpj_cpf(String cnpj_cpf) { this.cnpj_cpf = cnpj_cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public byte[] getAnexo() { return anexo; }
    public void setAnexo(byte[] anexo) { this.anexo = anexo; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public List<Responsavel> getResponsaveis() { return responsaveis; }
    public void setResponsaveis(List<Responsavel> responsaveis) { this.responsaveis = responsaveis; }
}
