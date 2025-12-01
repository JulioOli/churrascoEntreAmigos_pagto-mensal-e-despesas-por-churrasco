
package com.churrascoapp.dao;

import com.churrascoapp.model.Usuario;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class UsuarioDAO {
    private static final String PATH = "src/data/usuarios.csv";
    private static final String SEP = ";";

    public UsuarioDAO() { ensureFile(); }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    w.write("nome;email;senha;tipo"); w.newLine();
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public List<Usuario> listar() {
        List<Usuario> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String line = r.readLine(); // header
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] f = line.split(SEP, -1);
                String nome = f.length>0?f[0]:"";
                String email = f.length>1?f[1]:"";
                String senha = f.length>2?f[2]:"";
                String tipo = f.length>3?f[3]:"";
                Usuario u = new Usuario();
                try { u.setNome(nome); } catch (Exception ignored) {}
                try { u.setEmail(email); } catch (Exception ignored) {}
                try { u.setSenha(senha); } catch (Exception ignored) {}
                try { u.setPapel(tipo); } catch (Exception ignored) {}
                out.add(u);
            }
        } catch (IOException ex) { ex.printStackTrace(); }
        return out;
    }

    // email como “id”
    public Usuario buscarPorId(String email) {
        if (email == null) return null;
        for (Usuario u : listar()) {
            try { if (email.equalsIgnoreCase(u.getEmail())) return u; } catch (Exception ignored) {}
        }
        return null;
    }

    public boolean adicionar(Usuario u) {
        List<Usuario> all = listar();
        all.add(u);
        return salvar(all);
    }

    public boolean salvar(List<Usuario> lista) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            w.write("nome;email;senha;tipo"); w.newLine();
            for (Usuario u : lista) {
                String line = String.join(SEP,
                        safe(u.getNome()),
                        safe(u.getEmail()),
                        safe(u.getSenha()),
                        safe(u.getPapel())
                );
                w.write(line); w.newLine();
            }
            return true;
        } catch (IOException ex) { ex.printStackTrace(); return false; }
    }

    private String safe(String s) { return s == null ? "" : s.replace(";", ","); }
}