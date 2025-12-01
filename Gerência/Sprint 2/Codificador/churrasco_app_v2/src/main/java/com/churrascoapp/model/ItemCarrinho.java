
package com.churrascoapp.model;

public class ItemCarrinho {
    private String id;
    private String churrascoId;
    private int quantidade;
    private double precoUnitario;

    public ItemCarrinho(){}
    public ItemCarrinho(String id, String churrascoId, int quantidade, double precoUnitario){
        this.id=id; this.churrascoId = churrascoId; this.quantidade = quantidade; this.precoUnitario = precoUnitario;
    }

    public String getId(){ return id; }
    public void setId(String id){ this.id = id; }
    public String getChurrascoId(){ return churrascoId; }
    public void setChurrascoId(String churrascoId){ this.churrascoId = churrascoId; }
    public int getQuantidade(){ return quantidade; }
    public void setQuantidade(int quantidade){ this.quantidade = quantidade; }
    public double getPrecoUnitario(){ return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario){ this.precoUnitario = precoUnitario; }

    public String toCSV(){ return String.join(";", id, churrascoId, String.valueOf(quantidade), String.valueOf(precoUnitario)); }
    public static ItemCarrinho fromCSV(String line){
        String[] p = line.split(";", -1);
        if (p.length < 4) return null;
        return new ItemCarrinho(p[0], p[1], Integer.parseInt(p[2]), Double.parseDouble(p[3]));
    }
}
