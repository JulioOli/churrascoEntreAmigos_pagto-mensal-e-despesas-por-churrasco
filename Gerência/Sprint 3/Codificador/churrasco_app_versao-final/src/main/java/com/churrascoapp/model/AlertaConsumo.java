package com.churrascoapp.model;

import java.util.UUID;

/**
 * Classe para representar alertas de consumo excessivo em churrascos.
 * Sprint 2 - Funcionalidades Financeiras
 */
public class AlertaConsumo {
    private UUID id;
    private UUID churrascoId;
    private UUID usuarioId;
    private String mensagem;
    private double valorLimite;
    private double valorAtual;
    private String data;
    private String tipo; // ALERTA, AVISO, CRÍTICO

    public AlertaConsumo() {}

    public AlertaConsumo(UUID id, UUID churrascoId, UUID usuarioId, String mensagem,
                        double valorLimite, double valorAtual, String data, String tipo) {
        this.id = id;
        this.churrascoId = churrascoId;
        this.usuarioId = usuarioId;
        this.mensagem = mensagem;
        this.valorLimite = valorLimite;
        this.valorAtual = valorAtual;
        this.data = data;
        this.tipo = tipo;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getChurrascoId() { return churrascoId; }
    public void setChurrascoId(UUID churrascoId) { this.churrascoId = churrascoId; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public double getValorLimite() { return valorLimite; }
    public void setValorLimite(double valorLimite) { this.valorLimite = valorLimite; }

    public double getValorAtual() { return valorAtual; }
    public void setValorAtual(double valorAtual) { this.valorAtual = valorAtual; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // CSV serialization
    public String toCSV() {
        return String.join(";",
                id.toString(),
                churrascoId.toString(),
                usuarioId.toString(),
                safe(mensagem),
                String.valueOf(valorLimite),
                String.valueOf(valorAtual),
                safe(data),
                safe(tipo)
        );
    }

    public static AlertaConsumo fromCSV(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 8) return null;
        double vLimite = 0.0;
        double vAtual = 0.0;
        try {
            vLimite = Double.parseDouble(p[4]);
            vAtual = Double.parseDouble(p[5]);
        } catch (Exception ignored) {}
        return new AlertaConsumo(UUID.fromString(p[0]), UUID.fromString(p[1]), 
                                UUID.fromString(p[2]), p[3], vLimite, vAtual, p[6], p[7]);
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace(";", ",");
    }

    @Override
    public String toString() {
        return "AlertaConsumo{id=" + id + ", churrasco=" + churrascoId +
                ", usuario=" + usuarioId + ", tipo=" + tipo +
                ", limite=" + valorLimite + ", atual=" + valorAtual + "}";
    }
}
