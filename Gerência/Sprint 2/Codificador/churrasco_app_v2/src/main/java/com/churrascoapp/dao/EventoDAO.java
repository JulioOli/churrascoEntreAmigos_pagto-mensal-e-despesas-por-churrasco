package com.churrascoapp.dao;

import com.churrascoapp.model.Churrasco;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class EventoDAO {
    private static final String PATH = "src/data/eventos.csv";
    private static final String SEP = ";";

    public EventoDAO() { ensureFile(); }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    // Cabeçalho atualizado com todos os campos
                    w.write("id;titulo;data;hora;local;tipo;pix;precoParticipante;descricao");
                    w.newLine();
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public List<Churrasco> listar() {
        List<Churrasco> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String line = r.readLine(); // cabeçalho
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Churrasco c = Churrasco.fromCSV(line);
                if (c != null) out.add(c);
            }
        } catch (IOException ex) { ex.printStackTrace(); }
        return out;
    }

    public Churrasco buscarPorId(String id) {
        for (Churrasco c : listar()) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public boolean adicionar(Churrasco c) {
        List<Churrasco> all = listar();
        all.add(c);
        return salvar(all);
    }

    public boolean salvar(List<Churrasco> lista) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            // Cabeçalho atualizado
            w.write("id;titulo;data;hora;local;tipo;pix;precoParticipante;descricao");
            w.newLine();

            for (Churrasco c : lista) {
                w.write(c.toCSV());
                w.newLine();
            }
            return true;
        } catch (IOException ex) { ex.printStackTrace(); return false; }
    }
}
