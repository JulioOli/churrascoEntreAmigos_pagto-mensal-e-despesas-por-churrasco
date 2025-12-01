package com.churrascoapp.model;

public class ItemCatalogo {

    private String categoria;
    private String nome;      // funciona como "id"
    private String unidade;
    private double quantidade;
    private String observacao;

    public ItemCatalogo() {}

    public ItemCatalogo(String categoria, String nome, String unidade, double quantidade, String observacao) {
        this.categoria = categoria;
        this.nome = nome;
        this.unidade = unidade;
        this.quantidade = quantidade;
        this.observacao = observacao;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public double getQuantidade() { return quantidade; }
    public void setQuantidade(double quantidade) { this.quantidade = quantidade; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    // Converte para CSV (usado em CatalogoDAO)
    public String toCSV() {
        return String.join(";",
                safe(categoria),
                safe(nome),
                safe(unidade),
                String.valueOf(quantidade),
                safe(observacao)
        );
    }

    public static ItemCatalogo fromCSV(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 5) return null;
        double qtd = 0.0;
        try { qtd = Double.parseDouble(p[3]); } catch (Exception ignored) {}
        return new ItemCatalogo(
                p[0], p[1], p[2], qtd, p[4]
        );
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace(";", ",");
    }
}

