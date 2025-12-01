package com.churrascoapp.controller;

import com.churrascoapp.model.Participacao;
import com.churrascoapp.service.ParticipacaoService;

import java.util.List;

public class ParticipacaoController {

    private final ParticipacaoService participacaoService;

    public ParticipacaoController(ParticipacaoService participacaoService) {
        this.participacaoService = participacaoService;
    }

    public Participacao inscrever(String churrascoId, String usuarioId) {
        return participacaoService.registrarParticipacao(churrascoId, usuarioId);
    }

    public boolean confirmarPagamento(String participacaoId) {
        return participacaoService.confirmarPagamento(participacaoId);
    }

    public boolean cancelar(String participacaoId) {
        return participacaoService.cancelarParticipacao(participacaoId);
    }

    public boolean confirmarParticipacao(String participacaoId) {
        return participacaoService.confirmarParticipacao(participacaoId);
    }

    public List<Participacao> listarPorChurrasco(String churrascoId) {
        return participacaoService.listarPorChurrasco(churrascoId);
    }

    public List<Participacao> listarPorUsuario(String usuarioId) {
        return participacaoService.listarPorUsuario(usuarioId);
    }

    public double totalArrecadado(String churrascoId) {
        return participacaoService.calcularTotalArrecadado(churrascoId);
    }
}
