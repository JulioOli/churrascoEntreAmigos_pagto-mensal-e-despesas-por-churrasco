package com.churrascoapp.controller;

import com.churrascoapp.model.Convite;
import com.churrascoapp.model.Participacao;
import com.churrascoapp.service.ConviteService;

import java.util.List;

public class ConviteController {

    private final ConviteService conviteService;

    public ConviteController(ConviteService conviteService) {
        this.conviteService = conviteService;
    }

    public Convite enviarConvite(String churrascoId, String usuarioId, String mensagem) {
        return conviteService.enviarConvite(churrascoId, usuarioId, mensagem);
    }

    public List<Convite> listarConvitesRecebidos(String usuarioId) {
        return conviteService.listarConvitesRecebidos(usuarioId);
    }

    public List<Convite> listarConvitesPorChurrasco(String churrascoId) {
        return conviteService.listarConvitesPorChurrasco(churrascoId);
    }

    public Participacao aceitarConvite(String conviteId) {
        return conviteService.aceitarConvite(conviteId);
    }

    public boolean recusarConvite(String conviteId) {
        return conviteService.recusarConvite(conviteId);
    }
}
