package com.churrascoapp.service;

import com.churrascoapp.dao.CatalogoDAO;
import com.churrascoapp.model.ItemCatalogo;

import java.util.List;

public class CatalogoService {
    private final CatalogoDAO dao;

    public CatalogoService(CatalogoDAO dao) {
        this.dao = dao;
    }

    public List<ItemCatalogo> listar() {
        return dao.listar();
    }

    public boolean salvar(List<ItemCatalogo> lista) {
        return dao.salvar(lista);
    }

    public boolean adicionar(ItemCatalogo it) {
        return dao.adicionar(it);
    }

    public boolean removerPorNome(String nome) {
        List<ItemCatalogo> all = dao.listar();
        all.removeIf(x -> x.getNome()!=null && x.getNome().equalsIgnoreCase(nome));
        return dao.salvar(all);
    }
}

