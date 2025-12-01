package com.churrascoapp.dao;

import com.churrascoapp.model.PrestacaoConta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PrestacaoContaDAO {

    private static final String PATH = "src/data/prestacoes.csv";

    public PrestacaoContaDAO() {
        ensureFile();
    }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    w.write("id;churrascoId;totalArrecadado;totalGasto;saldo;dataPrestacao;status;observacoes");
                    w.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<PrestacaoConta> listar() {
        List<PrestacaoConta> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String line = r.readLine(); // header
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                PrestacaoConta pc = PrestacaoConta.fromCSV(line);
                if (pc != null) out.add(pc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public boolean salvar(List<PrestacaoConta> lista) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            w.write("id;churrascoId;totalArrecadado;totalGasto;saldo;dataPrestacao;status;observacoes");
            w.newLine();
            for (PrestacaoConta pc : lista) {
                w.write(pc.toCSV());
                w.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean adicionar(PrestacaoConta pc) {
        List<PrestacaoConta> all = listar();
        all.add(pc);
        return salvar(all);
    }

    public PrestacaoConta buscarPorId(UUID id) {
        if (id == null) return null;
        for (PrestacaoConta pc : listar()) {
            if (id.equals(pc.getId())) return pc;
        }
        return null;
    }

    public PrestacaoConta buscarPorChurrasco(UUID churrascoId) {
        PrestacaoConta last = null;
        if (churrascoId == null) return null;
        for (PrestacaoConta pc : listar()) {
            if (churrascoId.equals(pc.getChurrascoId())) {
                last = pc;
            }
        }
        return last;
    }

    public boolean atualizar(PrestacaoConta pc) {
        List<PrestacaoConta> all = listar();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            PrestacaoConta cur = all.get(i);
            if (cur.getId() != null && cur.getId().equals(pc.getId())) {
                all.set(i, pc);
                found = true;
                break;
            }
        }
        if (!found) all.add(pc);
        return salvar(all);
    }
}
