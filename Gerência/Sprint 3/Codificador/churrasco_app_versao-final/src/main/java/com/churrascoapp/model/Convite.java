package com.churrascoapp.model;

import java.util.UUID;

/**
 * Classe para representar convites para churrascos.
 * Sprint 2
 */
public class Convite {
    private UUID id;
    private UUID churrascoId;
    private UUID usuarioId;
    private String status; // PENDENTE, ACEITO, RECUSADO
    private String dataEnvio;
    private String dataResposta;
    private String mensagem;

    public Convite() {}

    public Convite(UUID id, UUID churrascoId, UUID usuarioId, String status,
                  String dataEnvio, String dataResposta, String mensagem) {
        this.id = id;
        this.churrascoId = churrascoId;
        this.usuarioId = usuarioId;
        this.status = status;
        this.dataEnvio = dataEnvio;
        this.dataResposta = dataResposta;
        this.mensagem = mensagem;
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

    public String getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(String dataEnvio) { this.dataEnvio = dataEnvio; }

    public String getDataResposta() { return dataResposta; }
    public void setDataResposta(String dataResposta) { this.dataResposta = dataResposta; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    // Métodos de negócio
    public void aceitar() {
        this.status = "ACEITO";
        this.dataResposta = java.time.LocalDate.now().toString();
    }

    public void recusar() {
        this.status = "RECUSADO";
        this.dataResposta = java.time.LocalDate.now().toString();
    }

    // CSV serialization
    public String toCSV() {
        return String.join(";",
                id.toString(),
                churrascoId.toString(),
                usuarioId.toString(),
                safe(status),
                safe(dataEnvio),
                safe(dataResposta),
                safe(mensagem)
        );
    }

    public static Convite fromCSV(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 7) return null;
        return new Convite(UUID.fromString(p[0]), UUID.fromString(p[1]), UUID.fromString(p[2]), p[3], p[4], p[5], p[6]);
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace(";", ",");
    }

    @Override
    public String toString() {
        return "Convite{id=" + id + ", churrasco=" + churrascoId +
                ", usuario=" + usuarioId + ", status=" + status + "}";
    }
}
