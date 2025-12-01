package com.churrascoapp.model;

import java.util.UUID;

public class Churrasco {
    private UUID id;
    private UUID criadorId; // ID do usuário que criou o churrasco
    private String titulo;
    private String data; // exemplo: "2025-05-22"
    private String hora; // exemplo: "14:30"
    private String local;
    private String tipo; // exemplo: "Apenas Churrasco"
    private String pix;  // chave pix opcional
    private double precoParticipante;
    private String descricao;

    public Churrasco() {}

    public Churrasco(UUID id, String titulo, String data, String hora, String local,
                     String tipo, String pix, double precoParticipante, String descricao) {
        this.id = id;
        this.titulo = titulo;
        this.data = data;
        this.hora = hora;
        this.local = local;
        this.tipo = tipo;
        this.pix = pix;
        this.precoParticipante = precoParticipante;
        this.descricao = descricao;
    }

    public Churrasco(UUID id, UUID criadorId, String titulo, String data, String hora, String local,
                     String tipo, String pix, double precoParticipante, String descricao) {
        this.id = id;
        this.criadorId = criadorId;
        this.titulo = titulo;
        this.data = data;
        this.hora = hora;
        this.local = local;
        this.tipo = tipo;
        this.pix = pix;
        this.precoParticipante = precoParticipante;
        this.descricao = descricao;
    }

    // Getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCriadorId() { return criadorId; }
    public void setCriadorId(UUID criadorId) { this.criadorId = criadorId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getPix() { return pix; }
    public void setPix(String pix) { this.pix = pix; }

    public double getPrecoParticipante() { return precoParticipante; }
    public void setPrecoParticipante(double precoParticipante) { this.precoParticipante = precoParticipante; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    // CSV
    public String toCSV() {
        return String.join(";",
                id.toString(), 
                criadorId != null ? criadorId.toString() : "",
                titulo, data, hora, local, tipo, pix,
                String.valueOf(precoParticipante),
                descricao == null ? "" : descricao
        );
    }

    public static Churrasco fromCSV(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        
        String[] p = line.split(";", -1); // -1 mantém campos vazios
        
        // Formato novo: id;criadorId;titulo;data;hora;local;tipo;pix;precoParticipante;descricao (10 campos)
        if (p.length >= 10) {
            try {
                UUID id = UUID.fromString(p[0].trim());
                UUID criadorId = null;
                if (p[1] != null && !p[1].trim().isEmpty()) {
                    try {
                        criadorId = UUID.fromString(p[1].trim());
                    } catch (Exception ignored) {
                        // criadorId inválido, mantém null
                    }
                }
                
                return new Churrasco(
                        id,
                        criadorId,
                        p[2] != null ? p[2] : "",
                        p[3] != null ? p[3] : "",
                        p[4] != null ? p[4] : "",
                        p[5] != null ? p[5] : "",
                        p[6] != null ? p[6] : "",
                        p[7] != null ? p[7] : "",
                        p[8] != null && !p[8].trim().isEmpty() ? Double.parseDouble(p[8].trim()) : 0.0,
                        p.length > 9 && p[9] != null ? p[9] : ""
                );
            } catch (Exception e) {
                System.err.println("Erro ao parsear churrasco (formato novo): " + line + " - " + e.getMessage());
                return null;
            }
        }
        
        // Formato antigo: id;titulo;data;hora;local;tipo;pix;precoParticipante;descricao (9 campos)
        if (p.length >= 9) {
            try {
                UUID id = UUID.fromString(p[0].trim());
                
                return new Churrasco(
                        id,
                        null, // criadorId
                        p[1] != null ? p[1] : "",
                        p[2] != null ? p[2] : "",
                        p[3] != null ? p[3] : "",
                        p[4] != null ? p[4] : "",
                        p[5] != null ? p[5] : "",
                        p[6] != null ? p[6] : "",
                        p[7] != null && !p[7].trim().isEmpty() ? Double.parseDouble(p[7].trim()) : 0.0,
                        p.length > 8 && p[8] != null ? p[8] : ""
                );
            } catch (Exception e) {
                System.err.println("Erro ao parsear churrasco (formato antigo): " + line + " - " + e.getMessage());
                return null;
            }
        }
        
        // Formato muito antigo: id;titulo;data;hora;local;tipo;pix;precoParticipante (8 campos)
        if (p.length >= 8) {
            try {
                UUID id = UUID.fromString(p[0].trim());
                
                return new Churrasco(
                        id,
                        null, // criadorId
                        p[1] != null ? p[1] : "",
                        p[2] != null ? p[2] : "",
                        p[3] != null ? p[3] : "",
                        p[4] != null ? p[4] : "",
                        p[5] != null ? p[5] : "",
                        p[6] != null ? p[6] : "",
                        p[7] != null && !p[7].trim().isEmpty() ? Double.parseDouble(p[7].trim()) : 0.0,
                        "" // descricao vazia
                );
            } catch (Exception e) {
                System.err.println("Erro ao parsear churrasco (formato muito antigo): " + line + " - " + e.getMessage());
                return null;
            }
        }
        
        System.err.println("Linha CSV inválida (menos de 8 campos): " + line);
        return null;
    }
}

