package br.com.techsync.dto;

public class OrcamentoStatusResumo {
    private String mes;
    private String status;
    private Long quantidade;

    public OrcamentoStatusResumo(String mes, String status, Long quantidade) {
        this.mes = mes;
        this.status = status;
        this.quantidade = quantidade;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }
}
