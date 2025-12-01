package com.churrascoapp.dao;

import com.churrascoapp.model.ItemCatalogo;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

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
                    w.write("categoria;item;unidade;quantidade;observacao"); w.newLine();
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public List<ItemCatalogo> listar() {
        List<ItemCatalogo> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String line = r.readLine(); // header
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] f = line.split(SEP, -1);
                ItemCatalogo it = new ItemCatalogo();
                try { if (f.length>0) it.setCategoria(f[0]); } catch (Exception ignored) {}
                try { if (f.length>1) it.setNome(f[1]); } catch (Exception ignored) {}
                try { if (f.length>2) it.setUnidade(f[2]); } catch (Exception ignored) {}
                try { if (f.length>3) it.setQuantidade(Double.parseDouble(f[3])); } catch (Exception ignored) {}
                try { if (f.length>4) it.setObservacao(f[4]); } catch (Exception ignored) {}
                out.add(it);
            }
        } catch (IOException ex) { ex.printStackTrace(); }
        return out;
    }

    // usa o nome do item como “id” simples
    public ItemCatalogo buscarPorId(String nome) {
        if (nome == null) return null;
        for (ItemCatalogo it : listar()) {
            try { if (nome.equalsIgnoreCase(it.getNome())) return it; } catch (Exception ignored) {}
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
            w.write("categoria;item;unidade;quantidade;observacao"); w.newLine();
            for (ItemCatalogo it : lista) {
                String line = String.join(SEP,
                        safe(it.getCategoria()),
                        safe(it.getNome()),
                        safe(it.getUnidade()),
                        String.valueOf(it.getQuantidade()),
                        safe(it.getObservacao())
                );
                w.write(line); w.newLine();
            }
            return true;
        } catch (IOException ex) { ex.printStackTrace(); return false; }
    }

    private String safe(String s) { return s == null ? "" : s.replace(";", ","); }
}
