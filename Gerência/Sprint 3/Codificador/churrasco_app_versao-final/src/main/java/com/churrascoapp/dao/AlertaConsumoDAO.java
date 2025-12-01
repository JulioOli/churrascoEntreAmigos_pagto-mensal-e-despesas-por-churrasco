package com.churrascoapp.dao;

import com.churrascoapp.model.AlertaConsumo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlertaConsumoDAO {

    private static final String PATH = "src/data/alertas_consumo.csv";

    public AlertaConsumoDAO() {
        ensureFile();
    }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    w.write("id;churrascoId;usuarioId;mensagem;valorLimite;valorAtual;data;tipo");
                    w.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<AlertaConsumo> listar() {
        List<AlertaConsumo> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String line = r.readLine(); // header
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                AlertaConsumo a = AlertaConsumo.fromCSV(line);
                if (a != null) out.add(a);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public boolean salvar(List<AlertaConsumo> lista) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            w.write("id;churrascoId;usuarioId;mensagem;valorLimite;valorAtual;data;tipo");
            w.newLine();
            for (AlertaConsumo a : lista) {
                w.write(a.toCSV());
                w.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean adicionar(AlertaConsumo a) {
        List<AlertaConsumo> all = listar();
        all.add(a);
        return salvar(all);
    }

    public List<AlertaConsumo> listarPorChurrasco(UUID churrascoId) {
        List<AlertaConsumo> out = new ArrayList<>();
        if (churrascoId == null) return out;
        for (AlertaConsumo a : listar()) {
            if (churrascoId.equals(a.getChurrascoId())) {
                out.add(a);
            }
        }
        return out;
    }

    public List<AlertaConsumo> listarPorUsuario(UUID usuarioId) {
        List<AlertaConsumo> out = new ArrayList<>();
        if (usuarioId == null) return out;
        for (AlertaConsumo a : listar()) {
            if (usuarioId.equals(a.getUsuarioId())) {
                out.add(a);
            }
        }
        return out;
    }
}
