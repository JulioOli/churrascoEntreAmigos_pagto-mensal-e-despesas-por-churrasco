package com.churrascoapp.controller;

import com.churrascoapp.model.RegraConsumoGlobal;
import com.churrascoapp.service.RegraConsumoGlobalService;

import java.util.List;

public class RegraConsumoGlobalController {

    private final RegraConsumoGlobalService service;

    public RegraConsumoGlobalController(RegraConsumoGlobalService service) {
        this.service = service;
    }

    public RegraConsumoGlobal criar(String nome, String descricao, 
                                   double valorLimitePadrao, double percentualAlerta, 
                                   double percentualCritico) {
        return service.criar(nome, descricao, valorLimitePadrao, percentualAlerta, percentualCritico);
    }

    public List<RegraConsumoGlobal> listar() {
        return service.listar();
    }

    public RegraConsumoGlobal buscarPorId(String id) {
        return service.buscarPorId(id);
    }

    public boolean atualizar(RegraConsumoGlobal regra) {
        return service.atualizar(regra);
    }

    public boolean remover(String id) {
        return service.remover(id);
    }

    public RegraConsumoGlobal buscarRegraAtiva() {
        return service.buscarRegraAtiva();
    }

    public boolean ativarRegra(String id) {
        return service.ativarRegra(id);
    }
}

