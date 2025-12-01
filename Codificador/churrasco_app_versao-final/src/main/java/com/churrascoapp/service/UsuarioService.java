package com.churrascoapp.service;

import com.churrascoapp.dao.UsuarioDAO;
import com.churrascoapp.model.Usuario;

import java.util.List;
import java.util.UUID;

public class UsuarioService {

    private final UsuarioDAO dao;

    public UsuarioService(UsuarioDAO dao) {
        this.dao = dao;
    }

    /**
     * Registra um novo usuário, garantindo:
     *  - nome, email, senha e tipo não vazios
     *  - e-mail único
     *  - UUID gerado
     */
    public Usuario registrar(String nome, String email, String senha, String tipo) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Tipo/Papel é obrigatório.");
        }

        // Verifica se já existe usuário com mesmo e-mail
        Usuario existente = dao.buscarPorEmail(email);
        if (existente != null) {
            throw new IllegalStateException("E-mail já cadastrado.");
        }

        Usuario u = new Usuario(
                UUID.randomUUID(),
                nome,
                email,
                senha,
                tipo
        );

        boolean ok = dao.adicionar(u);
        if (!ok) {
            throw new RuntimeException("Falha ao cadastrar usuário.");
        }

        return u;
    }

    /**
     * Autentica usuário por e-mail + senha.
     * Retorna o objeto completo (com id preenchido) ou null se inválido.
     */
    public Usuario autenticar(String email, String senha) {
        if (email == null || email.isBlank()) return null;
        if (senha == null) senha = "";

        Usuario u = dao.buscarPorEmail(email);
        if (u == null) return null;

        String s = u.getSenha() == null ? "" : u.getSenha();
        return s.equals(senha) ? u : null;
    }

    /**
     * Busca usuário por nome OU e-mail (exato, case-insensitive).
     * Usado para convites via nome.
     */
    public Usuario buscarPorNomeOuEmail(String termo) {
        if (termo == null || termo.isBlank()) return null;
        return dao.buscarPorNomeOuEmail(termo.trim());
    }

    /**
     * Lista todos os usuários (para administradores).
     */
    public List<Usuario> listarTodos() {
        return dao.listar();
    }

    /**
     * Atualiza um usuário existente.
     */
    public boolean atualizar(Usuario usuario) {
        if (usuario == null) return false;
        return dao.atualizar(usuario);
    }

    /**
     * Remove um usuário (apenas para administradores).
     */
    public boolean remover(String usuarioId) {
        if (usuarioId == null) return false;
        List<Usuario> todos = dao.listar();
        boolean removed = false;
        try {
            java.util.UUID id = java.util.UUID.fromString(usuarioId);
            removed = todos.removeIf(u -> id.equals(u.getId()));
        } catch (IllegalArgumentException e) {
            // Se não for UUID, tenta por email
            removed = todos.removeIf(u -> usuarioId.equalsIgnoreCase(u.getEmail()));
        }
        if (removed) {
            return dao.salvar(todos);
        }
        return false;
    }
}
