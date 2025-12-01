package com.churrascoapp.service;

import com.churrascoapp.dao.EventoDAO;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.utils.UUIDUtil;

import java.util.List;

public class ChurrascoService {
    private final EventoDAO dao;

    public ChurrascoService(EventoDAO dao) {
        this.dao = dao;
    }

    public Churrasco criar(String titulo, String data, String hora,
                           String local, String tipo, String pix,
                           double precoParticipante, String descricao) {
        String id = UUIDUtil.randomId(); // ou UUID.randomUUID().toString()
        Churrasco c = new Churrasco();
        c.setId(id);
        c.setTitulo(titulo);
        c.setData(data);
        c.setHora(hora);
        c.setLocal(local);
        c.setTipo(tipo);
        c.setPix(pix);
        c.setPrecoParticipante(precoParticipante);
        c.setDescricao(descricao);

        boolean ok = dao.adicionar(c); // ajuste: se seu DAO usa "adicionar", troque aqui
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
