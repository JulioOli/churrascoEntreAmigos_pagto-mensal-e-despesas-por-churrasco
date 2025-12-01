package com.churrascoapp.model;

import java.util.UUID;

public class ItemCatalogo {

    private UUID id;
    private String categoria;
    private String nome;
    private double quantidade;
    private String observacao;

    public ItemCatalogo() {
        this.id = UUID.randomUUID();
    }

    public ItemCatalogo(UUID id, String categoria, String nome, double quantidade, String observacao) {
        this.id = id != null ? id : UUID.randomUUID();
        this.categoria = categoria;
        this.nome = nome;
        this.quantidade = quantidade;
        this.observacao = observacao;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id != null ? id : UUID.randomUUID(); }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getQuantidade() { return quantidade; }
    public void setQuantidade(double quantidade) { this.quantidade = quantidade; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    // Converte para CSV (usado em CatalogoDAO)
    public String toCSV() {
        return String.join(";",
                id != null ? id.toString() : UUID.randomUUID().toString(),
                safe(categoria),
                safe(nome),
                String.valueOf(quantidade),
                safe(observacao)
        );
    }

    public static ItemCatalogo fromCSV(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 3) return null;
        
        UUID id = null;
        double qtd = 0.0;
        
        // Detecta formato pelo primeiro campo (se é UUID ou não)
        boolean formatoNovo = false;
        try {
            UUID.fromString(p[0]);
            formatoNovo = true;
        } catch (Exception ignored) {
            formatoNovo = false;
        }
        
        if (formatoNovo && p.length >= 4) {
            // Formato novo: id;categoria;nome;quantidade;observacao
            try {
                id = UUID.fromString(p[0]);
            } catch (Exception ignored) {
                id = UUID.randomUUID();
            }
            try {
                qtd = Double.parseDouble(p[3]);
            } catch (Exception ignored) {}
            return new ItemCatalogo(id, p[1], p[2], qtd, p.length > 4 ? p[4] : "");
        } else if (p.length >= 4) {
            // Formato antigo: categoria;nome;unidade;quantidade;observacao (sem ID)
            id = UUID.randomUUID();
            try {
                qtd = Double.parseDouble(p[3]);
            } catch (Exception ignored) {}
            return new ItemCatalogo(id, p[0], p[1], qtd, p.length > 4 ? p[4] : "");
        }
        
        return null;
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace(";", ",");
    }
}

