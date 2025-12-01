package com.churrascoapp.model;

import java.util.UUID;

/**
 * Classe para representar regras globais de consumo definidas pelo administrador.
 */
public class RegraConsumoGlobal {
    private UUID id;
    private String nome;
    private String descricao;
    private double valorLimitePadrao;
    private double percentualAlerta; // Ex: 80% do limite
    private double percentualCritico; // Ex: 120% do limite
    private boolean ativa;
    private String dataCriacao;
    private String dataAtualizacao;

    public RegraConsumoGlobal() {}

    public RegraConsumoGlobal(UUID id, String nome, String descricao, 
                              double valorLimitePadrao, double percentualAlerta, 
                              double percentualCritico, boolean ativa,
                              String dataCriacao, String dataAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.valorLimitePadrao = valorLimitePadrao;
        this.percentualAlerta = percentualAlerta;
        this.percentualCritico = percentualCritico;
        this.ativa = ativa;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getValorLimitePadrao() { return valorLimitePadrao; }
    public void setValorLimitePadrao(double valorLimitePadrao) { this.valorLimitePadrao = valorLimitePadrao; }

    public double getPercentualAlerta() { return percentualAlerta; }
    public void setPercentualAlerta(double percentualAlerta) { this.percentualAlerta = percentualAlerta; }

    public double getPercentualCritico() { return percentualCritico; }
    public void setPercentualCritico(double percentualCritico) { this.percentualCritico = percentualCritico; }

    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(String dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    // CSV serialization
    public String toCSV() {
        return String.join(";",
                id.toString(),
                safe(nome),
                safe(descricao),
                String.valueOf(valorLimitePadrao),
                String.valueOf(percentualAlerta),
                String.valueOf(percentualCritico),
                String.valueOf(ativa),
                safe(dataCriacao),
                safe(dataAtualizacao)
        );
    }

    public static RegraConsumoGlobal fromCSV(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 9) return null;
        try {
            return new RegraConsumoGlobal(
                    UUID.fromString(p[0]),
                    p[1],
                    p[2],
                    Double.parseDouble(p[3]),
                    Double.parseDouble(p[4]),
                    Double.parseDouble(p[5]),
                    Boolean.parseBoolean(p[6]),
                    p[7],
                    p[8]
            );
        } catch (Exception e) {
            return null;
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace(";", ",");
    }

    @Override
    public String toString() {
        return "RegraConsumoGlobal{id=" + id + ", nome=" + nome + 
               ", limite=" + valorLimitePadrao + ", ativa=" + ativa + "}";
    }
}

