
package com.churrascoapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Carrinho {
    private UUID id;
    private UUID usuarioId;
    private UUID churrascoId; // ID do churrasco relacionado ao carrinho
    private List<ItemCarrinho> itens = new ArrayList<>();

    public Carrinho(){}
    public Carrinho(UUID id, UUID usuarioId){ this.id = id; this.usuarioId = usuarioId; }
    public Carrinho(UUID id, UUID usuarioId, UUID churrascoId){ 
        this.id = id; 
        this.usuarioId = usuarioId; 
        this.churrascoId = churrascoId;
    }

    public UUID getId(){ return id; }
    public void setId(UUID id){ this.id = id; }
    public UUID getUsuarioId(){ return usuarioId; }
    public void setUsuarioId(UUID usuarioId){ this.usuarioId = usuarioId; }
    public UUID getChurrascoId(){ return churrascoId; }
    public void setChurrascoId(UUID churrascoId){ this.churrascoId = churrascoId; }

    public List<ItemCarrinho> getItens(){ return itens; }
    public void setItens(List<ItemCarrinho> itens){ this.itens = itens; }

    public double calcularTotal(){
        return itens.stream().mapToDouble(i -> i.getPrecoUnitario() * i.getQuantidade()).sum();
    }

    // CSV minimal: id;usuarioId
    public String toCSV(){ return String.join(";", id.toString(), usuarioId.toString()); }
    public static Carrinho fromCSV(String line){ String[] p = line.split(";", -1); if (p.length<2) return null; return new Carrinho(UUID.fromString(p[0]), UUID.fromString(p[1])); }
}
