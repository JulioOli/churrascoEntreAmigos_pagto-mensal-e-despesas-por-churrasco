package com.churrascoapp.controller;

import com.churrascoapp.model.Churrasco;
import com.churrascoapp.service.EventoService;

import java.util.List;

public class EventoController {
    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    public Churrasco criar(String titulo, String data, String hora,
                           String local, String tipo, String pix,
                           double precoParticipante, String descricao) {
        return service.criar(titulo, data, hora, local, tipo, pix, precoParticipante, descricao);
    }

    public List<Churrasco> listar() {
        return service.listar();
    }

    public Churrasco buscar(String id) {
        return service.buscar(id);
    }

    // Compatibilidade com UI
    public boolean criar(Churrasco c) {
        if (c == null) return false;
        Churrasco novo = service.criar(
                c.getTitulo(), c.getData(), c.getHora(), c.getLocal(),
                c.getTipo(), c.getPix(), c.getPrecoParticipante(), c.getDescricao()
        );
        return novo != null;
    }
}



