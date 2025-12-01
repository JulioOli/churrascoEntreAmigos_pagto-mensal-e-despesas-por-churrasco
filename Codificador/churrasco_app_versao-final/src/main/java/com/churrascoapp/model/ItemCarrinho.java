
package com.churrascoapp.model;

import java.util.UUID;

public class ItemCarrinho {
    private UUID id;
    private UUID churrascoId;
    private UUID itemId; // ID do item do catálogo
    private String nome; // Nome do item
    private int quantidade;
    private double precoUnitario;

    public ItemCarrinho(){}
    public ItemCarrinho(UUID id, UUID churrascoId, int quantidade, double precoUnitario){
        this.id=id; this.churrascoId = churrascoId; this.quantidade = quantidade; this.precoUnitario = precoUnitario;
    }

    public UUID getId(){ return id; }
    public void setId(UUID id){ this.id = id; }
    public UUID getChurrascoId(){ return churrascoId; }
    public void setChurrascoId(UUID churrascoId){ this.churrascoId = churrascoId; }
    public UUID getItemId(){ return itemId; }
    public void setItemId(UUID itemId){ this.itemId = itemId; }
    public String getNome(){ return nome; }
    public void setNome(String nome){ this.nome = nome; }
    public int getQuantidade(){ return quantidade; }
    public void setQuantidade(int quantidade){ this.quantidade = quantidade; }
    public double getPrecoUnitario(){ return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario){ this.precoUnitario = precoUnitario; }

    public String toCSV(){ return String.join(";", id.toString(), churrascoId.toString(), String.valueOf(quantidade), String.valueOf(precoUnitario)); }
    public static ItemCarrinho fromCSV(String line){
        String[] p = line.split(";", -1);
        if (p.length < 4) return null;
        return new ItemCarrinho(UUID.fromString(p[0]), UUID.fromString(p[1]), Integer.parseInt(p[2]), Double.parseDouble(p[3]));
    }
}
