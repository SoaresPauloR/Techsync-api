package br.com.techsync.models.cliente;

import br.com.techsync.enums.Status;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um cliente.
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "T_TS_CLIENTE")
public class Cliente {

    /**
     * Identificador único do cliente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * Nome do cliente.
     */
    @Column(name = "name", nullable = false)
    private String nome;

    /**
     * E-mail do cliente.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * CNPJ/CPF do cliente.
     */
    @Column(name = "cnpj_cpf", unique = true)
    private String cnpj_cpf;

    /**
     * Número de telefone do cliente.
     */
    @Column(name = "phone_number", nullable = false)
    private String telefone;

    /**
     * Status do cliente.
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * Endereço do cliente.
     */
    @Column(name = "address", length = 1000)
    private String endereco;

    /**
     * Anexo (arquivo) associado ao cliente.
     */
    @Lob
    @Column(name = "anexo")
    private byte[] anexo;

    /**
     * Observação sobre o cliente.
     */
    @Column(name = "obs", length = 1000)
    private String observacao;

    /**
     * Responsáveis do cliente.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Responsavel> responsaveis = new ArrayList<>();

    // Construtor
    public Cliente() {}

    // Getter e Setters

    /**
     * Obtém o identificador do cliente.
     *
     * @return id do cliente
     */
    public Integer getId() {
        return id;
    }

    /**
     * Define o identificador do cliente.
     *
     * @param id novo identificador do cliente
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtém o nome do cliente.
     *
     * @return nome do cliente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do cliente.
     *
     * @param nome novo nome do cliente
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o e-mail do cliente.
     *
     * @return e-mail do cliente
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o e-mail do cliente.
     *
     * @param email novo e-mail do cliente
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtém o CNPJ/CPF do cliente.
     *
     * @return CNPJ/CPF do cliente
     */
    public String getCnpj_cpf() {
        return cnpj_cpf;
    }

    /**
     * Define o CNPJ/CPF do cliente.
     *
     * @param cnpj_cpf novo CNPJ/CPF do cliente
     */
    public void setCnpj_cpf(String cnpj_cpf) {
        this.cnpj_cpf = cnpj_cpf;
    }

    /**
     * Obtém o número de telefone do cliente.
     *
     * @return número de telefone do cliente
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * Define o número de telefone do cliente.
     *
     * @param telefone novo número de telefone do cliente
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Obtém o status do cliente.
     *
     * @return status do cliente
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Define o status do cliente.
     *
     * @param status novo status do cliente
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Obtém o endereço do cliente.
     *
     * @return endereço do cliente
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Define o endereço do cliente.
     *
     * @param endereco novo endereço do cliente
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * Obtém o anexo (arquivo) associado ao cliente.
     *
     * @return anexo (arquivo) associado ao cliente
     */
    public byte[] getAnexo() {
        return anexo;
    }

    /**
     * Define o anexo (arquivo) associado ao cliente.
     *
     * @param anexo novo anexo (arquivo) associado ao cliente
     */
    public void setAnexo(byte[] anexo) {
        this.anexo = anexo;
    }

    /**
     * Obtém a observação sobre o cliente.
     *
     * @return observação sobre o cliente
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * Define a observação sobre o cliente.
     *
     * @param observacao nova observação sobre o cliente
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * Obtém os responsáveis do cliente.
     *
     * @return responsáveis do cliente
     */
    public List<Responsavel> getResponsaveis() {
        return responsaveis;
    }

    /**
     * Define os responsáveis do cliente.
     *
     * @param responsaveis novos responsáveis do cliente
     */
    public void setResponsaveis(List<Responsavel> responsaveis) {
        this.responsaveis = responsaveis;
    }
}
