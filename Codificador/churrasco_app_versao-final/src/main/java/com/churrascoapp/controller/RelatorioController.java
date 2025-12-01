package com.churrascoapp.controller;

import com.churrascoapp.model.Relatorio;
import com.churrascoapp.service.RelatorioService;

import java.util.List;

public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    public Relatorio gerarFinanceiro(String churrascoId, String formato) {
        return relatorioService.gerarFinanceiro(churrascoId, formato);
    }

    public Relatorio gerarOperacional(String churrascoId, String formato) {
        return relatorioService.gerarOperacional(churrascoId, formato);
    }

    public List<Relatorio> listarPorChurrasco(String churrascoId) {
        return relatorioService.listarPorChurrasco(churrascoId);
    }

    public Relatorio buscarPorId(String id) {
        return relatorioService.buscarPorId(id);
    }
}

