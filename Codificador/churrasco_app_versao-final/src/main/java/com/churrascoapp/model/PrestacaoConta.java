package com.churrascoapp.model;

import java.util.UUID;

/**
 * Classe para representar prestação de contas de churrascos.
 * Sprint 2 - Funcionalidades Financeiras
 */
public class PrestacaoConta {
    private UUID id;
    private UUID churrascoId;
    private double totalArrecadado;
    private double totalGasto;
    private double saldo;
    private String dataPrestacao;
    private String status; // PENDENTE, APROVADA, REJEITADA
    private String observacoes;

    public PrestacaoConta() {}

    public PrestacaoConta(UUID id, UUID churrascoId, double totalArrecadado,
                         double totalGasto, double saldo, String dataPrestacao,
                         String status, String observacoes) {
        this.id = id;
        this.churrascoId = churrascoId;
        this.totalArrecadado = totalArrecadado;
        this.totalGasto = totalGasto;
        this.saldo = saldo;
        this.dataPrestacao = dataPrestacao;
        this.status = status;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getChurrascoId() { return churrascoId; }
    public void setChurrascoId(UUID churrascoId) { this.churrascoId = churrascoId; }

    public double getTotalArrecadado() { return totalArrecadado; }
    public void setTotalArrecadado(double totalArrecadado) { this.totalArrecadado = totalArrecadado; }

    public double getTotalGasto() { return totalGasto; }
    public void setTotalGasto(double totalGasto) { this.totalGasto = totalGasto; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public String getDataPrestacao() { return dataPrestacao; }
    public void setDataPrestacao(String dataPrestacao) { this.dataPrestacao = dataPrestacao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    // Método para calcular saldo automaticamente
    public void calcularSaldo() {
        this.saldo = this.totalArrecadado - this.totalGasto;
    }

    // CSV serialization
    public String toCSV() {
        return String.join(";",
                id.toString(),
                churrascoId.toString(),
                String.valueOf(totalArrecadado),
                String.valueOf(totalGasto),
                String.valueOf(saldo),
                safe(dataPrestacao),
                safe(status),
                safe(observacoes)
        );
    }

    public static PrestacaoConta fromCSV(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 8) return null;
        double arrecadado = 0.0;
        double gasto = 0.0;
        double saldo = 0.0;
        try {
            arrecadado = Double.parseDouble(p[2]);
            gasto = Double.parseDouble(p[3]);
            saldo = Double.parseDouble(p[4]);
        } catch (Exception ignored) {}
        return new PrestacaoConta(UUID.fromString(p[0]), UUID.fromString(p[1]), arrecadado, gasto, saldo, p[5], p[6], p[7]);
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace(";", ",");
    }

    @Override
    public String toString() {
        return "PrestacaoConta{id=" + id + ", churrasco=" + churrascoId +
                ", arrecadado=" + totalArrecadado + ", gasto=" + totalGasto +
                ", saldo=" + saldo + ", status=" + status + "}";
    }
}
