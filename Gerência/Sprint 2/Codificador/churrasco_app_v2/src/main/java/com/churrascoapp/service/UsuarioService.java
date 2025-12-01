package com.churrascoapp.service;

import com.churrascoapp.dao.UsuarioDAO;
import com.churrascoapp.model.Usuario;

public class UsuarioService {
    private final UsuarioDAO dao;

    public UsuarioService(UsuarioDAO dao) {
        this.dao = dao;
    }

    public Usuario registrar(String nome, String email, String senha, String tipo) {
        if (nome == null || nome.isBlank())   throw new IllegalArgumentException("Nome é obrigatório.");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("E-mail é obrigatório.");
        if (senha == null || senha.isBlank()) throw new IllegalArgumentException("Senha é obrigatória.");
        if (tipo == null || tipo.isBlank())   tipo = "Usuário";

        if (dao.buscarPorId(email) != null)
            throw new IllegalStateException("E-mail já cadastrado.");

        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setSenha(senha);
        u.setPapel(tipo);

        boolean ok = dao.adicionar(u);
        if (!ok) throw new RuntimeException("Falha ao cadastrar usuário.");

        return u;
    }

    public Usuario autenticar(String email, String senha) {
        Usuario u = dao.buscarPorId(email);
        if (u == null) return null;
        String s = u.getSenha() == null ? "" : u.getSenha();
        return s.equals(senha) ? u : null;
    }
}

