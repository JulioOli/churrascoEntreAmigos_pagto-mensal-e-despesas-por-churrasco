package com.churrascoapp.dao;

import com.churrascoapp.model.RegraConsumoGlobal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegraConsumoGlobalDAO {

    private static final String PATH = "src/data/regras_consumo_global.csv";

    public RegraConsumoGlobalDAO() {
        ensureFile();
    }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    w.write("id;nome;descricao;valorLimitePadrao;percentualAlerta;percentualCritico;ativa;dataCriacao;dataAtualizacao");
                    w.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<RegraConsumoGlobal> listar() {
        List<RegraConsumoGlobal> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String line = r.readLine(); // header
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                RegraConsumoGlobal regra = RegraConsumoGlobal.fromCSV(line);
                if (regra != null) out.add(regra);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public boolean salvar(List<RegraConsumoGlobal> lista) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            w.write("id;nome;descricao;valorLimitePadrao;percentualAlerta;percentualCritico;ativa;dataCriacao;dataAtualizacao");
            w.newLine();
            for (RegraConsumoGlobal regra : lista) {
                w.write(regra.toCSV());
                w.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean adicionar(RegraConsumoGlobal regra) {
        List<RegraConsumoGlobal> all = listar();
        all.add(regra);
        return salvar(all);
    }

    public RegraConsumoGlobal buscarPorId(UUID id) {
        if (id == null) return null;
        for (RegraConsumoGlobal regra : listar()) {
            if (id.equals(regra.getId())) {
                return regra;
            }
        }
        return null;
    }

    public boolean atualizar(RegraConsumoGlobal regra) {
        List<RegraConsumoGlobal> all = listar();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            RegraConsumoGlobal r = all.get(i);
            if (r.getId() != null && r.getId().equals(regra.getId())) {
                all.set(i, regra);
                found = true;
                break;
            }
        }
        if (!found) {
            all.add(regra);
        }
        return salvar(all);
    }

    public boolean remover(UUID id) {
        List<RegraConsumoGlobal> all = listar();
        boolean removed = all.removeIf(r -> id.equals(r.getId()));
        if (removed) {
            return salvar(all);
        }
        return false;
    }

    /**
     * Busca a regra ativa padrão (primeira regra ativa encontrada).
     */
    public RegraConsumoGlobal buscarRegraAtiva() {
        for (RegraConsumoGlobal regra : listar()) {
            if (regra.isAtiva()) {
                return regra;
            }
        }
        return null;
    }
}

