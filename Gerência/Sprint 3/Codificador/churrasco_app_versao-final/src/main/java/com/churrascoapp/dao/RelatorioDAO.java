package com.churrascoapp.dao;

import com.churrascoapp.model.Relatorio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RelatorioDAO {

    private static final String PATH = "src/data/relatorios.csv";

    public RelatorioDAO() {
        ensureFile();
    }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    w.write("id;churrascoId;tipoRelatorio;formato;conteudo;dataGeracao;observacoes");
                    w.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Relatorio> listar() {
        List<Relatorio> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String line = r.readLine(); // header
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Relatorio rel = Relatorio.fromCSV(line);
                if (rel != null) out.add(rel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public boolean salvar(List<Relatorio> lista) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            w.write("id;churrascoId;tipoRelatorio;formato;conteudo;dataGeracao;observacoes");
            w.newLine();
            for (Relatorio rel : lista) {
                w.write(rel.toCSV());
                w.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean adicionar(Relatorio relatorio) {
        List<Relatorio> all = listar();
        all.add(relatorio);
        return salvar(all);
    }

    public Relatorio buscarPorId(UUID id) {
        if (id == null) return null;
        for (Relatorio rel : listar()) {
            if (id.equals(rel.getId())) {
                return rel;
            }
        }
        return null;
    }

    public List<Relatorio> listarPorChurrasco(UUID churrascoId) {
        List<Relatorio> out = new ArrayList<>();
        if (churrascoId == null) return out;
        for (Relatorio rel : listar()) {
            if (churrascoId.equals(rel.getChurrascoId())) {
                out.add(rel);
            }
        }
        return out;
    }
}

