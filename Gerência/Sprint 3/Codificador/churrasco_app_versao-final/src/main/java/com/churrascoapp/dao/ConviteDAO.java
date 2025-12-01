package com.churrascoapp.dao;

import com.churrascoapp.model.Convite;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConviteDAO {

    private static final String PATH = "src/data/convites.csv";

    public ConviteDAO() {
        ensureFile();
    }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    w.write("id;churrascoId;usuarioId;status;dataEnvio;dataResposta;mensagem");
                    w.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Convite> listar() {
        List<Convite> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String line = r.readLine(); // cabeçalho
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Convite c = Convite.fromCSV(line);
                if (c != null) out.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public boolean salvar(List<Convite> lista) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            w.write("id;churrascoId;usuarioId;status;dataEnvio;dataResposta;mensagem");
            w.newLine();
            for (Convite c : lista) {
                w.write(c.toCSV());
                w.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean adicionar(Convite convite) {
        // Verifica duplicatas antes de adicionar
        List<Convite> all = listar();
        
        // Verifica se já existe convite com mesmo churrascoId e usuarioId
        for (Convite existente : all) {
            if (convite.getChurrascoId() != null && convite.getUsuarioId() != null &&
                convite.getChurrascoId().equals(existente.getChurrascoId()) &&
                convite.getUsuarioId().equals(existente.getUsuarioId())) {
                // Se já existe um convite para este churrasco e usuário, não adiciona
                return false;
            }
        }
        
        all.add(convite);
        return salvar(all);
    }
    
    public Convite buscarPorChurrascoEUsuario(UUID churrascoId, UUID usuarioId) {
        if (churrascoId == null || usuarioId == null) return null;
        for (Convite c : listar()) {
            if (churrascoId.equals(c.getChurrascoId()) && usuarioId.equals(c.getUsuarioId())) {
                return c;
            }
        }
        return null;
    }

    public Convite buscarPorId(UUID id) {
        if (id == null) return null;
        for (Convite c : listar()) {
            if (id.equals(c.getId())) {
                return c;
            }
        }
        return null;
    }

    public boolean atualizar(Convite convite) {
        List<Convite> all = listar();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            Convite c = all.get(i);
            if (c.getId() != null && c.getId().equals(convite.getId())) {
                all.set(i, convite);
                found = true;
                break;
            }
        }
        if (!found) {
            all.add(convite);
        }
        return salvar(all);
    }

    public List<Convite> listarPorChurrasco(UUID churrascoId) {
        List<Convite> out = new ArrayList<>();
        if (churrascoId == null) return out;
        for (Convite c : listar()) {
            if (churrascoId.equals(c.getChurrascoId())) {
                out.add(c);
            }
        }
        return out;
    }

    public List<Convite> listarPorUsuario(UUID usuarioId) {
        List<Convite> out = new ArrayList<>();
        if (usuarioId == null) return out;
        for (Convite c : listar()) {
            if (usuarioId.equals(c.getUsuarioId())) {
                out.add(c);
            }
        }
        return out;
    }
}
