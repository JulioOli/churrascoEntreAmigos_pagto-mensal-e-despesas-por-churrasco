package com.churrascoapp.controller;

import com.churrascoapp.model.Usuario;
import com.churrascoapp.service.UsuarioService;

import java.util.List;

public class UsuarioController {
    private final UsuarioService service;

    // Preferencial: DI por construtor (AppContext)
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // === API principal ===
    public Usuario cadastrar(String nome, String email, String senha, String tipo) {
        return service.registrar(nome, email, senha, tipo);
    }

    public Usuario authenticate(String email, String senha) {
        return service.autenticar(email, senha);
    }

    // Compatibilidade: receber objeto pronto
    public Usuario cadastrar(Usuario u) {
        if (u == null) return null;
        return service.registrar(u.getNome(), u.getEmail(), u.getSenha(), u.getPapel());
    }

    // === Métodos para administradores ===
    public List<Usuario> listarTodos() {
        return service.listarTodos();
    }

    public boolean atualizar(Usuario usuario) {
        return service.atualizar(usuario);
    }

    public boolean remover(String usuarioId) {
        return service.remover(usuarioId);
    }
}
