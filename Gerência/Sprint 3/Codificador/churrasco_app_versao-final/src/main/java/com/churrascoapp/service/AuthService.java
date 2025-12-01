package com.churrascoapp.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.churrascoapp.dao.UsuarioDAO;
import com.churrascoapp.model.Usuario;
import com.churrascoapp.utils.ValidadorSenha;

/**
 * Service para autenticação de usuários.
 * Conforme diagrama de classes - artefatos_astah_v3
 */
public class AuthService {
    private final UsuarioDAO usuarioDAO;
    private final Map<String, String> sessoes; // token -> usuarioId

    public AuthService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
        this.sessoes = new HashMap<>();
    }

    /**
     * Autentica usuário com email e senha
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @return Usuario autenticado ou null se falhar
     */
    public Usuario autenticar(String email, String senha) {
        Usuario usuario = usuarioDAO.buscarPorEmail(email);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            return usuario;
        }
        return null;
    }

    /**
     * Registra novo usuário
     * @param usuario Dados do novo usuário
     * @return true se registro foi bem-sucedido
     */
    public boolean registrar(Usuario usuario) {
        // Valida senha
        if (!ValidadorSenha.validar(usuario.getSenha())) {
            return false;
        }

        // Verifica se email já existe
        if (usuarioDAO.buscarPorEmail(usuario.getEmail()) != null) {
            return false;
        }

        // Salva usuário
        return usuarioDAO.adicionar(usuario);
    }

    /**
     * Realiza logout do usuário
     * @param usuarioId ID do usuário
     * @return true se logout foi bem-sucedido
     */
    public boolean logout(String usuarioId) {
        // Remove todas as sessões do usuário
        sessoes.entrySet().removeIf(entry -> entry.getValue().equals(usuarioId));
        return true;
    }

    /**
     * Valida token de sessão
     * @param token Token a ser validado
     * @return true se token é válido
     */
    public boolean validarToken(String token) {
        return sessoes.containsKey(token);
    }

    /**
     * Cria novo token de sessão para usuário
     * @param usuarioId ID do usuário
     * @return Token criado
     */
    public String criarToken(String usuarioId) {
        String token = UUID.randomUUID().toString();
        sessoes.put(token, usuarioId);
        return token;
    }

    /**
     * Altera senha do usuário
     * @param usuarioId ID do usuário
     * @param senhaAtual Senha atual
     * @param novaSenha Nova senha
     * @return true se senha foi alterada
     */
    public boolean alterarSenha(String usuarioId, String senhaAtual, String novaSenha) {
        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
        if (usuario == null || !usuario.getSenha().equals(senhaAtual)) {
            return false;
        }

        if (!ValidadorSenha.validar(novaSenha)) {
            return false;
        }

        usuario.setSenha(novaSenha);
        return usuarioDAO.atualizar(usuario);
    }

    /**
     * Busca usuário por token de sessão
     * @param token Token de sessão
     * @return Usuario ou null
     */
    public Usuario buscarPorToken(String token) {
        String usuarioId = sessoes.get(token);
        if (usuarioId != null) {
            return usuarioDAO.buscarPorId(usuarioId);
        }
        return null;
    }
}
