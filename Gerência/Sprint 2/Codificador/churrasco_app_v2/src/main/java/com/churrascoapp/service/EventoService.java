package com.churrascoapp.service;

import com.churrascoapp.dao.EventoDAO;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.utils.UUIDUtil;

import java.util.List;

public class EventoService {
    private final EventoDAO dao;

    public EventoService(EventoDAO dao) {
        this.dao = dao;
    }

    public Churrasco criar(String titulo, String data,String hora, String local,String tipo,String pix, double preco, String descricao) {
        String id = UUIDUtil.randomId();
        Churrasco c = new Churrasco(id, titulo, data, hora, local,tipo,pix, preco, descricao);

        // >>> Ajuste aqui conforme seu EventoDAO
        boolean ok = dao.adicionar(c);   // use adicionar(...) como no CompraDAO
        if (!ok) throw new RuntimeException("Falha ao salvar evento.");

        return c;
    }

    public List<Churrasco> listar() {
        return dao.listar();
    }

    public Churrasco buscar(String id) {
        return dao.buscarPorId(id);
    }
}

