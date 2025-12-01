package com.churrascoapp.utils;

import com.churrascoapp.model.Usuario;

/**
 * Utilitários para verificação de permissões de administrador.
 */
public class AdminUtils {
    
    /**
     * Verifica se um usuário é administrador.
     * @param usuario Usuário a verificar
     * @return true se o usuário é administrador
     */
    public static boolean isAdmin(Usuario usuario) {
        if (usuario == null || usuario.getPapel() == null) {
            return false;
        }
        String papel = usuario.getPapel().trim();
        return "Administrador".equalsIgnoreCase(papel) || 
               "ADMIN".equalsIgnoreCase(papel) ||
               "ADMINISTRADOR".equalsIgnoreCase(papel);
    }
    
    /**
     * Verifica se um usuário é administrador pelo papel (String).
     * @param papel Papel do usuário
     * @return true se o papel é de administrador
     */
    public static boolean isAdmin(String papel) {
        if (papel == null) {
            return false;
        }
        String p = papel.trim();
        return "Administrador".equalsIgnoreCase(p) || 
               "ADMIN".equalsIgnoreCase(p) ||
               "ADMINISTRADOR".equalsIgnoreCase(p);
    }
}

