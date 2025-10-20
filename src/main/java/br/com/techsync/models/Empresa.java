package br.com.techsync.models;

import jakarta.persistence.*;

@Entity
@Table(name = "T_TS_EMPRESA")
public class Empresa {

    @Id
    @Column(name = "id")
    // É uma boa prática usar @GeneratedValue para que o banco de dados cuide do ID,
    // mas mantive como está para minimizar as alterações.
    private int id;

    @Column(name = "name", nullable = false)
    private String nome;

    @Column(name = "cnpj", nullable = false, unique = true, length = 15)
    private long cnpj;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "currency", nullable = false, length = 3 )
    private String currency;

    @Column(name = "timezone", nullable = false, length = 5)
    private String timezone;

    // --- MUDANÇA ADICIONADA AQUI ---
    @OneToOne
    @JoinColumn(name = "usuario_id") // Garanta que o nome da coluna no seu banco seja "usuario_id"
    private Usuario usuario;
    // ---------------------------------

    // Construtores
    public Empresa() {}

    // Getters e Setters

    // Getter e Setter para o novo campo Usuario
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getCnpj() {
        return cnpj;
    }

    public void setCnpj(long cnpj) {
        this.cnpj = cnpj;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}