package com.churrascoapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.churrascoapp.dao.EventoDAO;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.utils.UUIDUtil;

public class ChurrascoService {
    private final EventoDAO dao;

    public ChurrascoService(EventoDAO dao) {
        this.dao = dao;
    }

    public Churrasco criar(String titulo, String data, String hora,
                           String local, String tipo, String pix,
                           double precoParticipante, String descricao) {
        return criar(null, titulo, data, hora, local, tipo, pix, precoParticipante, descricao);
    }

    public Churrasco criar(UUID criadorId, String titulo, String data, String hora,
                           String local, String tipo, String pix,
                           double precoParticipante, String descricao) {
        UUID id = UUIDUtil.randomId();
        Churrasco c = new Churrasco();
        c.setId(id);
        c.setCriadorId(criadorId);
        c.setTitulo(titulo);
        c.setData(data);
        c.setHora(hora);
        c.setLocal(local);
        c.setTipo(tipo);
        c.setPix(pix);
        c.setPrecoParticipante(precoParticipante);
        c.setDescricao(descricao);

        boolean ok = dao.adicionar(c);
        if (!ok) throw new RuntimeException("Falha ao salvar evento.");
        return c;
    }

    public List<Churrasco> listar() {
        return dao.listar();
    }

    public Churrasco buscar(String id) {
        return dao.buscarPorId(id);
    }

    public boolean atualizar(Churrasco churrasco) {
        List<Churrasco> lista = dao.listar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(churrasco.getId())) {
                lista.set(i, churrasco);
                return dao.salvar(lista);
            }
        }
        return false;
    }

    public boolean remover(String id) {
        List<Churrasco> lista = dao.listar();
        UUID uuidId;
        try {
            uuidId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return false;
        }
        boolean removed = lista.removeIf(c -> c.getId() != null && c.getId().equals(uuidId));
        if (removed) {
            return dao.salvar(lista);
        }
        return false;
    }

    public List<Churrasco> listarPorTipo(String tipo) {
        List<Churrasco> todos = dao.listar();
        List<Churrasco> filtrados = new ArrayList<>();
        for (Churrasco c : todos) {
            if (tipo.equalsIgnoreCase(c.getTipo())) {
                filtrados.add(c);
            }
        }
        return filtrados;
    }

    public List<Churrasco> listarPorData(String data) {
        List<Churrasco> todos = dao.listar();
        List<Churrasco> filtrados = new ArrayList<>();
        for (Churrasco c : todos) {
            if (data.equals(c.getData())) {
                filtrados.add(c);
            }
        }
        return filtrados;
    }

    public List<Churrasco> listarPorCriador(UUID criadorId) {
        if (criadorId == null) return new ArrayList<>();
        return dao.listarPorCriador(criadorId);
    }
}
