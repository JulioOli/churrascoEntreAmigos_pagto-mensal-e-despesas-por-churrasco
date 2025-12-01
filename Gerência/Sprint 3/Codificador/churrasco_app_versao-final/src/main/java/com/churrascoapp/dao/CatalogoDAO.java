package com.churrascoapp.dao;

import com.churrascoapp.model.ItemCatalogo;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.UUID;

public class CatalogoDAO {
    private static final String PATH = "src/data/catalogo.csv";
    private static final String SEP = ";";

    public CatalogoDAO() { ensureFile(); }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    w.write("id;categoria;item;quantidade;observacao"); w.newLine();
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public List<ItemCatalogo> listar() {
        List<ItemCatalogo> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String header = r.readLine(); // header
            boolean formatoAntigo = header != null && header.contains("unidade");
            
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                ItemCatalogo it = ItemCatalogo.fromCSV(line);
                if (it != null) {
                    out.add(it);
                }
            }
            
            // Migração automática se detectar formato antigo
            if (formatoAntigo && !out.isEmpty()) {
                salvar(out);
            }
        } catch (IOException ex) { ex.printStackTrace(); }
        return out;
    }

    public ItemCatalogo buscarPorId(UUID id) {
        if (id == null) return null;
        for (ItemCatalogo it : listar()) {
            if (id.equals(it.getId())) return it;
        }
        return null;
    }

    public ItemCatalogo buscarPorNome(String nome) {
        if (nome == null) return null;
        for (ItemCatalogo it : listar()) {
            if (nome != null && nome.equalsIgnoreCase(it.getNome())) return it;
        }
        return null;
    }

    public boolean adicionar(ItemCatalogo it) {
        List<ItemCatalogo> all = listar();
        all.add(it);
        return salvar(all);
    }

    public boolean salvar(List<ItemCatalogo> lista) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            w.write("id;categoria;item;quantidade;observacao"); w.newLine();
            for (ItemCatalogo it : lista) {
                if (it.getId() == null) {
                    it.setId(UUID.randomUUID());
                }
                w.write(it.toCSV()); w.newLine();
            }
            return true;
        } catch (IOException ex) { ex.printStackTrace(); return false; }
    }

    private String safe(String s) { return s == null ? "" : s.replace(";", ","); }
}
