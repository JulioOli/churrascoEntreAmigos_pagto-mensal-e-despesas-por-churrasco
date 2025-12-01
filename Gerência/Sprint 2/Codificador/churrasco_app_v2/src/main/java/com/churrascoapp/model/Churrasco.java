package com.churrascoapp.model;

public class Churrasco {
    private String id;
    private String titulo;
    private String data; // exemplo: "2025-05-22"
    private String hora; // exemplo: "14:30"
    private String local;
    private String tipo; // exemplo: "Apenas Churrasco"
    private String pix;  // chave pix opcional
    private double precoParticipante;
    private String descricao;

    public Churrasco() {}

    public Churrasco(String id, String titulo, String data, String hora, String local,
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

    // Getters e setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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
                id, titulo, data, hora, local, tipo, pix,
                String.valueOf(precoParticipante),
                descricao == null ? "" : descricao
        );
    }

    public static Churrasco fromCSV(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 9) return null; // garantir estrutura correta
        return new Churrasco(
                p[0], p[1], p[2], p[3], p[4], p[5], p[6],
                Double.parseDouble(p[7]), p[8]
        );
    }
}

