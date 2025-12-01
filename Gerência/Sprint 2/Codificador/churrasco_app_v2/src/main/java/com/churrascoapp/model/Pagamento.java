
package com.churrascoapp.model;

public class Pagamento {
    private String id;
    private String compraId;
    private String metodo; // ex: CARTAO, PIX, DINHEIRO
    private String status; // PENDENTE, CONCLUIDO
    private String dataPagamento;

    public Pagamento(){}
    public Pagamento(String id, String compraId, String metodo, String status, String dataPagamento){
        this.id=id; this.compraId=compraId; this.metodo=metodo; this.status=status; this.dataPagamento=dataPagamento;
    }

    public String getId(){ return id; }
    public void setId(String id){ this.id = id; }
    public String getCompraId(){ return compraId; }
    public void setCompraId(String compraId){ this.compraId = compraId; }
    public String getMetodo(){ return metodo; }
    public void setMetodo(String metodo){ this.metodo = metodo; }
    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status = status; }
    public String getDataPagamento(){ return dataPagamento; }
    public void setDataPagamento(String dataPagamento){ this.dataPagamento = dataPagamento; }

    public String toCSV(){ return String.join(";", id, compraId, metodo, status, dataPagamento==null?"":dataPagamento); }
    public static Pagamento fromCSV(String line){ String[] p = line.split(";", -1); if (p.length<5) return null; return new Pagamento(p[0], p[1], p[2], p[3], p[4]); }
}
