package com.churrascoapp.model;

import java.util.UUID;

/**
 * Classe para representar a participação de usuários em churrascos.
 * Relaciona Usuario com Churrasco (associação N:M)
 */
public class Participacao {
    private UUID id;
    private UUID churrascoId;
    private UUID usuarioId;
    private String status; // CONFIRMADO, PENDENTE, CANCELADO
    private double valorPago;
    private boolean pagamentoConfirmado;
    private String dataInscricao;
    private String observacoes;

    public Participacao() {}

    public Participacao(UUID id, UUID churrascoId, UUID usuarioId, String status,
                       double valorPago, boolean pagamentoConfirmado,
                       String dataInscricao, String observacoes) {
        this.id = id;
        this.churrascoId = churrascoId;
        this.usuarioId = usuarioId;
        this.status = status;
        this.valorPago = valorPago;
        this.pagamentoConfirmado = pagamentoConfirmado;
        this.dataInscricao = dataInscricao;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getChurrascoId() { return churrascoId; }
    public void setChurrascoId(UUID churrascoId) { this.churrascoId = churrascoId; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getValorPago() { return valorPago; }
    public void setValorPago(double valorPago) { this.valorPago = valorPago; }

    public boolean isPagamentoConfirmado() { return pagamentoConfirmado; }
    public void setPagamentoConfirmado(boolean pagamentoConfirmado) {
        this.pagamentoConfirmado = pagamentoConfirmado;
    }

    public String getDataInscricao() { return dataInscricao; }
    public void setDataInscricao(String dataInscricao) { this.dataInscricao = dataInscricao; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    // Métodos de negócio
    public void confirmarPagamento() {
        this.pagamentoConfirmado = true;
        this.status = "CONFIRMADO";
    }

    public void cancelar() {
        this.status = "CANCELADO";
    }

    // CSV serialization
    public String toCSV() {
        return String.join(";",
                id.toString(),
                churrascoId.toString(),
                usuarioId.toString(),
                safe(status),
                String.valueOf(valorPago),
                String.valueOf(pagamentoConfirmado),
                safe(dataInscricao),
                safe(observacoes)
        );
    }

    public static Participacao fromCSV(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 8) return null;
        double valor = 0.0;
        boolean pago = false;
        try {
            valor = Double.parseDouble(p[4]);
            pago = Boolean.parseBoolean(p[5]);
        } catch (Exception ignored) {}
        return new Participacao(UUID.fromString(p[0]), UUID.fromString(p[1]), UUID.fromString(p[2]), p[3], valor, pago, p[6], p[7]);
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace(";", ",");
    }

    @Override
    public String toString() {
        return "Participacao{id=" + id + ", churrasco=" + churrascoId +
                ", usuario=" + usuarioId + ", status=" + status +
                ", pago=" + pagamentoConfirmado + "}";
    }
}
