package com.churrascoapp.model;

import java.util.UUID;

public class Compra {
    private UUID id;
    private UUID churrascoId;       // vínculo ao evento/churrasco
    private String item;
    private int quantidade;
    private double valor;
    private String fornecedor;        // opcional
    private String data;              // "AAAA-MM-DD"
    private String comprovantePath;   // opcional (caminho do arquivo)

    public Compra() {}

    public Compra(UUID id, UUID churrascoId, String item, int quantidade, double valor,
                  String fornecedor, String data, String comprovantePath) {
        this.id = id;
        this.churrascoId = churrascoId;
        this.item = item;
        this.quantidade = quantidade;
        this.valor = valor;
        this.fornecedor = fornecedor;
        this.data = data;
        this.comprovantePath = comprovantePath;
    }

    // Getters/Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getChurrascoId() { return churrascoId; }
    public void setChurrascoId(UUID churrascoId) { this.churrascoId = churrascoId; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getFornecedor() { return fornecedor; }
    public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getComprovantePath() { return comprovantePath; }
    public void setComprovantePath(String comprovantePath) { this.comprovantePath = comprovantePath; }

    // CSV (ordem alinhada ao CompraDAO)
    public String toCSV() {
        return String.join(";",
                id.toString(),
                churrascoId.toString(),
                safe(item),
                String.valueOf(quantidade),
                String.valueOf(valor),
                safe(fornecedor),
                safe(data),
                safe(comprovantePath)
        );
    }

    public static Compra fromCSV(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 8) return null;
        int qtd = 0;
        double val = 0.0;
        try { qtd = Integer.parseInt(p[3]); } catch (Exception ignored) {}
        try { val = Double.parseDouble(p[4]); } catch (Exception ignored) {}
        return new Compra(
                UUID.fromString(p[0]), UUID.fromString(p[1]), p[2], qtd, val,
                p[5], p[6], p[7]
        );
    }

    private static String safe(String s) { return s == null ? "" : s.replace(";", ","); }
}

