package com.churrascoapp.controller;

import com.churrascoapp.model.Usuario;
import com.churrascoapp.service.AuthService;

/**
 * Controller para autenticação de usuários.
 * Conforme diagrama de classes - artefatos_astah_v3
 */
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Realiza login do usuário
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @return Usuario autenticado ou null se falhar
     */
    public Usuario login(String email, String senha) {
        return authService.autenticar(email, senha);
    }

    /**
     * Realiza logout do usuário
     * @param usuarioId ID do usuário
     * @return true se logout foi bem-sucedido
     */
    public boolean logout(String usuarioId) {
        return authService.logout(usuarioId);
    }

    /**
     * Registra novo usuário no sistema
     * @param usuario Objeto Usuario com dados do novo usuário
     * @return true se registro foi bem-sucedido
     */
    public boolean registrar(Usuario usuario) {
        return authService.registrar(usuario);
    }

    /**
     * Valida token de sessão do usuário
     * @param token Token de sessão
     * @return true se token é válido
     */
    public boolean validarToken(String token) {
        return authService.validarToken(token);
    }

    /**
     * Altera senha do usuário
     * @param usuarioId ID do usuário
     * @param senhaAtual Senha atual
     * @param novaSenha Nova senha
     * @return true se senha foi alterada
     */
    public boolean alterarSenha(String usuarioId, String senhaAtual, String novaSenha) {
        return authService.alterarSenha(usuarioId, senhaAtual, novaSenha);
    }
}
