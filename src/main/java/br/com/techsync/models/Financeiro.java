package br.com.techsync.models;

import br.com.techsync.enums.TipoTransacao;
import br.com.techsync.models.cliente.Cliente;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "T_TS_FINANCEIRO")
public class Financeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nomeTransacao;

    private Double valor;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo; // ENTRADA ou SAIDA

    private LocalDate dataTransacao;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private String numeroNotaFiscal;

    // ======== Construtores ========
    public Financeiro() {
        this.dataTransacao = LocalDate.now();
    }

    // ======== Métodos auxiliares ========
    public String gerarNotaFiscal() {
        this.numeroNotaFiscal = "NF-" + System.currentTimeMillis();
        return this.numeroNotaFiscal;
    }

    // ======== Getters e Setters ========
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNomeTransacao() { return nomeTransacao; }
    public void setNomeTransacao(String nomeTransacao) { this.nomeTransacao = nomeTransacao; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public TipoTransacao getTipo() { return tipo; }
    public void setTipo(TipoTransacao tipo) { this.tipo = tipo; }

    public LocalDate getDataTransacao() { return dataTransacao; }
    public void setDataTransacao(LocalDate dataTransacao) { this.dataTransacao = dataTransacao; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getNumeroNotaFiscal() { return numeroNotaFiscal; }
    public void setNumeroNotaFiscal(String numeroNotaFiscal) { this.numeroNotaFiscal = numeroNotaFiscal; }
}
