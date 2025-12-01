package com.churrascoapp.model;

import java.util.UUID;

/**
 * Classe para representar relatórios gerados do sistema.
 */
public class Relatorio {
    private UUID id;
    private UUID churrascoId;
    private String tipoRelatorio; // FINANCEIRO, OPERACIONAL
    private String formato; // PDF, TXT, CSV
    private String conteudo; // Conteúdo do relatório
    private String dataGeracao;
    private String observacoes;

    public Relatorio() {}

    public Relatorio(UUID id, UUID churrascoId, String tipoRelatorio, String formato,
                    String conteudo, String dataGeracao, String observacoes) {
        this.id = id;
        this.churrascoId = churrascoId;
        this.tipoRelatorio = tipoRelatorio;
        this.formato = formato;
        this.conteudo = conteudo;
        this.dataGeracao = dataGeracao;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getChurrascoId() { return churrascoId; }
    public void setChurrascoId(UUID churrascoId) { this.churrascoId = churrascoId; }

    public String getTipoRelatorio() { return tipoRelatorio; }
    public void setTipoRelatorio(String tipoRelatorio) { this.tipoRelatorio = tipoRelatorio; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public String getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(String dataGeracao) { this.dataGeracao = dataGeracao; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    // CSV serialization
    public String toCSV() {
        return String.join(";",
                id.toString(),
                churrascoId.toString(),
                safe(tipoRelatorio),
                safe(formato),
                safe(conteudo),
                safe(dataGeracao),
                safe(observacoes)
        );
    }

    public static Relatorio fromCSV(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 7) return null;
        try {
            return new Relatorio(
                    UUID.fromString(p[0]),
                    UUID.fromString(p[1]),
                    p[2],
                    p[3],
                    p[4],
                    p[5],
                    p[6]
            );
        } catch (Exception e) {
            return null;
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace(";", ",").replace("\n", "\\n");
    }

    @Override
    public String toString() {
        return "Relatorio{id=" + id + ", tipo=" + tipoRelatorio + 
               ", formato=" + formato + ", churrasco=" + churrascoId + "}";
    }
}

