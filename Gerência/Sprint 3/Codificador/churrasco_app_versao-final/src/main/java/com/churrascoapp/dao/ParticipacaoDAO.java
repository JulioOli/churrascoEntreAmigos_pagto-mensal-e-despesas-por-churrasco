package com.churrascoapp.dao;

import com.churrascoapp.model.Participacao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ParticipacaoDAO {

    private static final String PATH = "src/data/participacoes.csv";

    public ParticipacaoDAO() {
        ensureFile();
    }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    w.write("id;churrascoId;usuarioId;status;valorPago;pagamentoConfirmado;dataInscricao;observacoes");
                    w.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Participacao> listar() {
        List<Participacao> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String line = r.readLine(); // header
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Participacao p = Participacao.fromCSV(line);
                if (p != null) out.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public boolean salvar(List<Participacao> lista) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            w.write("id;churrascoId;usuarioId;status;valorPago;pagamentoConfirmado;dataInscricao;observacoes");
            w.newLine();
            for (Participacao p : lista) {
                w.write(p.toCSV());
                w.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean adicionar(Participacao p) {
        List<Participacao> all = listar();
        all.add(p);
        return salvar(all);
    }

    public Participacao buscarPorId(UUID id) {
        if (id == null) return null;
        for (Participacao p : listar()) {
            if (id.equals(p.getId())) {
                return p;
            }
        }
        return null;
    }

    public boolean atualizar(Participacao p) {
        List<Participacao> all = listar();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            Participacao cur = all.get(i);
            if (cur.getId() != null && cur.getId().equals(p.getId())) {
                all.set(i, p);
                found = true;
                break;
            }
        }
        if (!found) all.add(p);
        return salvar(all);
    }

    public List<Participacao> listarPorChurrasco(UUID churrascoId) {
        List<Participacao> out = new ArrayList<>();
        if (churrascoId == null) return out;
        for (Participacao p : listar()) {
            if (churrascoId.equals(p.getChurrascoId())) {
                out.add(p);
            }
        }
        return out;
    }

    public List<Participacao> listarPorUsuario(UUID usuarioId) {
        List<Participacao> out = new ArrayList<>();
        if (usuarioId == null) return out;
        for (Participacao p : listar()) {
            if (usuarioId.equals(p.getUsuarioId())) {
                out.add(p);
            }
        }
        return out;
    }

    public double calcularTotalArrecadado(UUID churrascoId) {
        double total = 0.0;
        if (churrascoId == null) return total;
        for (Participacao p : listar()) {
            if (churrascoId.equals(p.getChurrascoId())
                    && p.isPagamentoConfirmado()) {
                total += p.getValorPago();
            }
        }
        return total;
    }
}
