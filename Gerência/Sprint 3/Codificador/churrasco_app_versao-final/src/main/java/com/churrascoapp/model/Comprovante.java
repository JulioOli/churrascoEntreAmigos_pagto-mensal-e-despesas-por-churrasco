package com.churrascoapp.model;

import java.util.UUID;

/**
 * Classe para representar comprovantes de compras.
 * Sprint 2 - Funcionalidades Financeiras
 */
public class Comprovante {
    private UUID id;
    private UUID compraId;
    private String caminho; // caminho do arquivo do comprovante
    private String tipo; // PDF, IMAGEM, etc.
    private String dataUpload;
    private String descricao;

    public Comprovante() {}

    public Comprovante(UUID id, UUID compraId, String caminho,
                      String tipo, String dataUpload, String descricao) {
        this.id = id;
        this.compraId = compraId;
        this.caminho = caminho;
        this.tipo = tipo;
        this.dataUpload = dataUpload;
        this.descricao = descricao;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCompraId() { return compraId; }
    public void setCompraId(UUID compraId) { this.compraId = compraId; }

    public String getCaminho() { return caminho; }
    public void setCaminho(String caminho) { this.caminho = caminho; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDataUpload() { return dataUpload; }
    public void setDataUpload(String dataUpload) { this.dataUpload = dataUpload; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    // CSV serialization
    public String toCSV() {
        return String.join(";",
                id.toString(),
                compraId.toString(),
                safe(caminho),
                safe(tipo),
                safe(dataUpload),
                safe(descricao)
        );
    }

    public static Comprovante fromCSV(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 6) return null;
        return new Comprovante(UUID.fromString(p[0]), UUID.fromString(p[1]), p[2], p[3], p[4], p[5]);
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace(";", ",");
    }

    @Override
    public String toString() {
        return "Comprovante{id=" + id + ", compra=" + compraId +
                ", tipo=" + tipo + ", data=" + dataUpload + "}";
    }
}
