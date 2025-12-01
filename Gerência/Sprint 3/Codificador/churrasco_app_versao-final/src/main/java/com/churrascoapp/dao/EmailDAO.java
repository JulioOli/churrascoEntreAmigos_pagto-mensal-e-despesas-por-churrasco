package com.churrascoapp.dao;

import com.churrascoapp.model.Email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmailDAO {

    private static final String PATH = "src/data/emails.csv";

    public EmailDAO() {
        ensureFile();
    }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    w.write("id;destinatario;assunto;corpo;dataEnvio;status;tipo");
                    w.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Email> listar() {
        List<Email> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String line = r.readLine(); // header
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Email e = Email.fromCSV(line);
                if (e != null) out.add(e);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    public boolean salvar(List<Email> lista) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            w.write("id;destinatario;assunto;corpo;dataEnvio;status;tipo");
            w.newLine();
            for (Email e : lista) {
                w.write(e.toCSV());
                w.newLine();
            }
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean adicionar(Email email) {
        List<Email> all = listar();
        all.add(email);
        return salvar(all);
    }

    public Email buscarPorId(UUID id) {
        if (id == null) return null;
        for (Email e : listar()) {
            if (id.equals(e.getId())) return e;
        }
        return null;
    }

    public boolean atualizar(Email email) {
        if (email == null || email.getId() == null) return false;
        List<Email> all = listar();
        for (int i = 0; i < all.size(); i++) {
            if (email.getId().equals(all.get(i).getId())) {
                all.set(i, email);
                return salvar(all);
            }
        }
        return false;
    }
}
