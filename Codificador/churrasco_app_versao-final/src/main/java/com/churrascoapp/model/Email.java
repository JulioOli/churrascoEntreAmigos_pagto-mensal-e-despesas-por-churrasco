package com.churrascoapp.model;

import java.util.UUID;

/**
 * Classe para representar emails de notificação do sistema.
 * NOTA: Nome em minúscula "email" conforme diagrama UML
 */
public class Email {
    private UUID id;
    private String destinatario;
    private String assunto;
    private String corpo;
    private String dataEnvio;
    private String status; // PENDENTE, ENVIADO, ERRO
    private String tipo; // CONVITE, LEMBRETE, PRESTACAO_CONTAS, etc.

    public Email() {}

    public Email(UUID id, String destinatario, String assunto, String corpo,
                String dataEnvio, String status, String tipo) {
        this.id = id;
        this.destinatario = destinatario;
        this.assunto = assunto;
        this.corpo = corpo;
        this.dataEnvio = dataEnvio;
        this.status = status;
        this.tipo = tipo;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }

    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }

    public String getCorpo() { return corpo; }
    public void setCorpo(String corpo) { this.corpo = corpo; }

    public String getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(String dataEnvio) { this.dataEnvio = dataEnvio; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // Métodos de negócio
    public void enviar() {
        // Lógica de envio seria implementada aqui
        this.status = "ENVIADO";
        this.dataEnvio = java.time.LocalDateTime.now().toString();
    }

    public void marcarErro() {
        this.status = "ERRO";
    }

    // CSV serialization
    public String toCSV() {
        return String.join(";",
                id.toString(),
                safe(destinatario),
                safe(assunto),
                safe(corpo),
                safe(dataEnvio),
                safe(status),
                safe(tipo)
        );
    }

    public static Email fromCSV(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 7) return null;
        return new Email(UUID.fromString(p[0]), p[1], p[2], p[3], p[4], p[5], p[6]);
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace(";", ",");
    }

    @Override
    public String toString() {
        return "Email{id=" + id + ", para=" + destinatario +
                ", assunto=" + assunto + ", status=" + status + "}";
    }
}
