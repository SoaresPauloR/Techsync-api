package br.com.techsync.enums;

public enum StatusOrcamento {
    PENDENTE,       // Orçamento criado, aguardando aprovação
    APROVADO,       // Orçamento aprovado pelo cliente
    REJEITADO,      // Orçamento rejeitado pelo cliente
    EM_ANALISE,     // Orçamento em análise interna
    CANCELADO       // Orçamento cancelado pelo sistema ou cliente
}