
package com.churrascoapp.model;

import java.util.*;

public class Carrinho {
    private String id;
    private String usuarioId;
    private List<ItemCarrinho> itens = new ArrayList<>();

    public Carrinho(){}
    public Carrinho(String id, String usuarioId){ this.id = id; this.usuarioId = usuarioId; }

    public String getId(){ return id; }
    public void setId(String id){ this.id = id; }
    public String getUsuarioId(){ return usuarioId; }
    public void setUsuarioId(String usuarioId){ this.usuarioId = usuarioId; }

    public List<ItemCarrinho> getItens(){ return itens; }
    public void setItens(List<ItemCarrinho> itens){ this.itens = itens; }

    public double calcularTotal(){
        return itens.stream().mapToDouble(i -> i.getPrecoUnitario() * i.getQuantidade()).sum();
    }

    // CSV minimal: id;usuarioId
    public String toCSV(){ return String.join(";", id, usuarioId); }
    public static Carrinho fromCSV(String line){ String[] p = line.split(";", -1); if (p.length<2) return null; return new Carrinho(p[0], p[1]); }
}
