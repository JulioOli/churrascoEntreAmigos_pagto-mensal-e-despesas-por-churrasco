package com.churrascoapp.dao;

import com.churrascoapp.model.Compra;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class CompraDAO {
    private static final String PATH = "src/data/compras.csv";

    public CompraDAO() { ensureFile(); }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    w.write("id;churrascoId;item;quantidade;valor;fornecedor;data;comprovantePath");
                    w.newLine();
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public List<Compra> listar() {
        List<Compra> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String line = r.readLine(); // header
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Compra c = Compra.fromCSV(line);
                if (c != null) out.add(c);
            }
        } catch (IOException ex) { ex.printStackTrace(); }
        return out;
    }

    public boolean adicionar(Compra c) {
        List<Compra> all = listar();
        all.add(c);
        return salvar(all);
    }

    public boolean salvar(List<Compra> lista) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            w.write("id;churrascoId;item;quantidade;valor;fornecedor;data;comprovantePath");
            w.newLine();
            for (Compra c : lista) {
                w.write(c.toCSV());
                w.newLine();
            }
            return true;
        } catch (IOException ex) { ex.printStackTrace(); return false; }
    }

    public List<Compra> listarPorChurrasco(UUID churrascoId) {
        List<Compra> out = new ArrayList<>();
        if (churrascoId == null) return out;
        for (Compra c : listar()) {
            if (c.getChurrascoId() != null && churrascoId.equals(c.getChurrascoId())) {
                out.add(c);
            }
        }
        return out;
    }
}
