package com.churrascoapp.dao;

import com.churrascoapp.model.Usuario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsuarioDAO {

    private static final String PATH = "src/data/usuarios.csv";
    private static final String SEP  = ";";

    public UsuarioDAO() {
        ensureFile();
    }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    // Formato novo: id;nome;email;senha;papel
                    w.write("id;nome;email;senha;papel");
                    w.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lê todos os usuários.
     * Suporta:
     *  - Formato novo:  id;nome;email;senha;papel
     *  - Formato antigo: nome;email;senha;tipo
     * Se detectar formato antigo, MIGRA automaticamente para o formato novo com UUID.
     */
    public List<Usuario> listar() {
        List<Usuario> out = new ArrayList<>();
        Path p = Paths.get(PATH);
        if (!Files.exists(p)) {
            return out;
        }

        boolean legacySemId = false;

        try (BufferedReader r = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            String header = r.readLine(); // primeira linha

            if (header == null) {
                return out;
            }

            String hLower = header.toLowerCase();
            // Se não começar com "id;", consideramos legado
            legacySemId = !hLower.startsWith("id;");

            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                Usuario u = null;
                if (legacySemId) {
                    // Formato antigo: nome;email;senha;tipo
                    String[] f = line.split(SEP, -1);
                    String nome  = f.length > 0 ? f[0] : "";
                    String email = f.length > 1 ? f[1] : "";
                    String senha = f.length > 2 ? f[2] : "";
                    String tipo  = f.length > 3 ? f[3] : "";

                    // Gera um UUID novo na MIGRAÇÃO
                    u = new Usuario(UUID.randomUUID(), nome, email, senha, tipo);
                } else {
                    // Formato novo: usa o fromCSV do próprio model
                    u = Usuario.fromCSV(line);
                }

                if (u != null) {
                    out.add(u);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Se era formato antigo, migramos automaticamente para o novo formato
        if (legacySemId && !out.isEmpty()) {
            salvar(out);
        }

        return out;
    }

    /**
     * Salva a lista inteira no formato: id;nome;email;senha;papel
     */
    public boolean salvar(List<Usuario> lista) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            w.write("id;nome;email;senha;papel");
            w.newLine();

            for (Usuario u : lista) {
                // Garante que sempre tem um UUID
                if (u.getId() == null) {
                    u.setId(UUID.randomUUID());
                }
                w.write(u.toCSV());
                w.newLine();
            }
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Adiciona um usuário na lista.
     */
    public boolean adicionar(Usuario u) {
        if (u == null) return false;
        List<Usuario> lista = listar();

        // Se não tiver id ainda, gera
        if (u.getId() == null) {
            u.setId(UUID.randomUUID());
        }

        lista.add(u);
        return salvar(lista);
    }

    /**
     * Busca usuário por e-mail (case-insensitive).
     */
    public Usuario buscarPorEmail(String email) {
        if (email == null) return null;
        for (Usuario u : listar()) {
            try {
                if (u.getEmail() != null && email.equalsIgnoreCase(u.getEmail())) {
                    return u;
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    /**
     * Busca usuário por NOME exato (case-insensitive).
     * Retorna o primeiro que bater.
     */
    public Usuario buscarPorNome(String nome) {
        if (nome == null) return null;
        for (Usuario u : listar()) {
            try {
                if (u.getNome() != null && nome.equalsIgnoreCase(u.getNome())) {
                    return u;
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    /**
     * Busca por nome OU e-mail (exato, case-insensitive).
     * Primeiro tenta e-mail, depois nome.
     */
    public Usuario buscarPorNomeOuEmail(String termo) {
        if (termo == null) return null;
        // tenta como email
        Usuario u = buscarPorEmail(termo);
        if (u != null) return u;

        // tenta como nome
        return buscarPorNome(termo);
    }

    /**
     * Busca usuário por ID (UUID em String) ou, como compatibilidade,
     * se não for UUID válido, tenta tratar como e-mail.
     */
    public Usuario buscarPorId(String idOrEmail) {
        if (idOrEmail == null) return null;

        // Tenta interpretar como UUID
        try {
            UUID id = UUID.fromString(idOrEmail);
            for (Usuario u : listar()) {
                if (u.getId() != null && id.equals(u.getId())) {
                    return u;
                }
            }
            return null;
        } catch (IllegalArgumentException ex) {
            // Não é UUID -> tratamos como e-mail (compatibilidade)
            return buscarPorEmail(idOrEmail);
        }
    }

    /**
     * Atualiza um usuário com base no e-mail (chave lógica)
     * ou, se tiver ID, tenta bater pelo ID também.
     */
    public boolean atualizar(Usuario usuario) {
        if (usuario == null) return false;

        List<Usuario> lista = listar();
        boolean alterou = false;

        for (int i = 0; i < lista.size(); i++) {
            Usuario atual = lista.get(i);

            boolean mesmoEmail = false;
            boolean mesmoId    = false;

            try {
                if (atual.getEmail() != null && usuario.getEmail() != null) {
                    mesmoEmail = atual.getEmail().equalsIgnoreCase(usuario.getEmail());
                }
            } catch (Exception ignored) {}

            try {
                if (atual.getId() != null && usuario.getId() != null) {
                    mesmoId = atual.getId().equals(usuario.getId());
                }
            } catch (Exception ignored) {}

            if (mesmoEmail || mesmoId) {
                lista.set(i, usuario);
                alterou = true;
                break;
            }
        }

        if (alterou) {
            return salvar(lista);
        }
        return false;
    }
}
