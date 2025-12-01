package com.churrascoapp.controller;

import com.churrascoapp.model.PrestacaoConta;
import com.churrascoapp.service.PrestacaoContaService;

public class PrestacaoContaController {

    private final PrestacaoContaService prestacaoContaService;

    public PrestacaoContaController(PrestacaoContaService prestacaoContaService) {
        this.prestacaoContaService = prestacaoContaService;
    }

    public PrestacaoConta gerarPrestacao(String churrascoId) {
        return prestacaoContaService.gerarPrestacao(churrascoId);
    }

    public PrestacaoConta buscarPorChurrasco(String churrascoId) {
        return prestacaoContaService.buscarPorChurrasco(churrascoId);
    }

    public boolean aprovarPrestacao(String churrascoId, String observacoes) {
        return prestacaoContaService.aprovarPrestacao(churrascoId, observacoes);
    }

    public boolean rejeitarPrestacao(String churrascoId, String observacoes) {
        return prestacaoContaService.rejeitarPrestacao(churrascoId, observacoes);
    }
}
