package com.churrascoapp.controller;

import com.churrascoapp.model.ItemCatalogo;
import com.churrascoapp.service.CatalogoService;

import java.util.List;

public class CatalogoController {
    private final CatalogoService service;

    public CatalogoController(CatalogoService service) {
        this.service = service;
    }

    public List<ItemCatalogo> listar() { return service.listar(); }
    public boolean salvar(List<ItemCatalogo> lista) { return service.salvar(lista); }
    public boolean adicionar(ItemCatalogo it) { return service.adicionar(it); }
    public boolean removerPorNome(String nome) { return service.removerPorNome(nome); }
}
