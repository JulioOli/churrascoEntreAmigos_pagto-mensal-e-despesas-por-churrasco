package com.churrascoapp.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.churrascoapp.model.Carrinho;

/**
 * Repository para persistência de carrinhos.
 * Conforme diagrama de classes - artefatos_astah_v3
 * NOTA: Diagrama usa nome "CarrinhoRepository"
 */
public class CarrinhoRepository {
    private static final String PATH = "src/data/carrinhos.csv";

    public CarrinhoRepository() {
        ensureFile();
    }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    w.write("id;usuarioId");
                    w.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lista todos os carrinhos
     * @return Lista de carrinhos
     */
    public List<Carrinho> listar() {
        List<Carrinho> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String line = r.readLine(); // header
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Carrinho c = Carrinho.fromCSV(line);
                if (c != null) out.add(c);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    /**
     * Busca carrinho por ID
     * @param id ID do carrinho
     * @return Carrinho ou null
     */
    public Carrinho buscarPorId(String id) {
        return listar().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adiciona novo carrinho
     * @param carrinho Carrinho a ser adicionado
     * @return true se foi adicionado
     */
    public boolean adicionar(Carrinho carrinho) {
        List<Carrinho> all = listar();
        all.add(carrinho);
        return salvar(all);
    }

    /**
     * Atualiza carrinho existente
     * @param carrinho Carrinho a ser atualizado
     * @return true se foi atualizado
     */
    public boolean atualizar(Carrinho carrinho) {
        List<Carrinho> all = listar();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(carrinho.getId())) {
                all.set(i, carrinho);
                return salvar(all);
            }
        }
        return false;
    }

    /**
     * Remove carrinho
     * @param id ID do carrinho
     * @return true se foi removido
     */
    public boolean remover(String id) {
        List<Carrinho> all = listar();
        boolean removed = all.removeIf(c -> c.getId().equals(id));
        if (removed) {
            return salvar(all);
        }
        return false;
    }

    /**
     * Salva lista completa de carrinhos
     * @param lista Lista de carrinhos
     * @return true se foi salvo
     */
    private boolean salvar(List<Carrinho> lista) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            w.write("id;usuarioId");
            w.newLine();
            for (Carrinho c : lista) {
                w.write(c.toCSV());
                w.newLine();
            }
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Busca carrinhos por usuário
     * @param usuarioId ID do usuário
     * @return Lista de carrinhos do usuário
     */
    public List<Carrinho> buscarPorUsuario(String usuarioId) {
        List<Carrinho> result = new ArrayList<>();
        for (Carrinho c : listar()) {
            if (c.getUsuarioId().equals(usuarioId)) {
                result.add(c);
            }
        }
        return result;
    }
}
