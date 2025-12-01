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
import java.util.UUID;

import com.churrascoapp.model.Churrasco;

public class EventoDAO {
    private static final String PATH = "src/data/eventos.csv";
    private static final String SEP = ";";

    public EventoDAO() { ensureFile(); }

    private void ensureFile() {
        try {
            Path p = Paths.get(PATH);
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                    // Cabeçalho atualizado com todos os campos (incluindo criadorId)
                    w.write("id;criadorId;titulo;data;hora;local;tipo;pix;precoParticipante;descricao");
                    w.newLine();
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public List<Churrasco> listar() {
        List<Churrasco> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get(PATH), StandardCharsets.UTF_8)) {
            String line = r.readLine(); // cabeçalho
            if (line == null) {
                System.err.println("AVISO: Arquivo eventos.csv está vazio ou não existe.");
                return out;
            }
            
            int linhaNum = 1;
            int linhasIgnoradas = 0;
            while ((line = r.readLine()) != null) {
                linhaNum++;
                if (line.trim().isEmpty()) continue;
                
                Churrasco c = Churrasco.fromCSV(line);
                if (c != null) {
                    out.add(c);
                } else {
                    linhasIgnoradas++;
                    System.err.println("AVISO: Não foi possível parsear linha " + linhaNum + " do arquivo eventos.csv: " + line);
                }
            }
            
            if (linhasIgnoradas > 0) {
                System.err.println("ATENÇÃO: " + linhasIgnoradas + " linha(s) foram ignoradas ao carregar eventos.csv");
            }
        } catch (IOException ex) { 
            System.err.println("ERRO ao ler arquivo eventos.csv: " + ex.getMessage());
            ex.printStackTrace(); 
        }
        return out;
    }

    public Churrasco buscarPorId(String id) {
        for (Churrasco c : listar()) {
            if (c.getId().toString().equals(id)) return c;  // Converter UUID para String antes de comparar
        }
        return null;
    }

    public boolean adicionar(Churrasco c) {
        if (c == null || c.getId() == null) {
            System.err.println("ERRO: Tentativa de adicionar churrasco nulo ou sem ID");
            return false;
        }
        
        List<Churrasco> all = listar();
        
        // Verifica se já existe churrasco com mesmo ID
        boolean jaExiste = all.stream().anyMatch(existente -> 
            existente.getId() != null && existente.getId().equals(c.getId())
        );
        
        if (jaExiste) {
            System.err.println("AVISO: Churrasco com ID " + c.getId() + " já existe. Atualizando...");
            // Remove o existente e adiciona o novo (atualização)
            all.removeIf(existente -> existente.getId() != null && existente.getId().equals(c.getId()));
        }
        
        all.add(c);
        boolean sucesso = salvar(all);
        
        if (sucesso) {
            System.out.println("Churrasco adicionado/atualizado com sucesso. Total de churrascos: " + all.size());
        } else {
            System.err.println("ERRO: Falha ao salvar churrasco após adicionar");
        }
        
        return sucesso;
    }

    public boolean salvar(List<Churrasco> lista) {
        if (lista == null) {
            System.err.println("ERRO: Tentativa de salvar lista nula de churrascos");
            return false;
        }
        
        // Remove duplicados baseado no ID antes de salvar
        java.util.Map<UUID, Churrasco> unicos = new java.util.LinkedHashMap<>();
        for (Churrasco c : lista) {
            if (c != null && c.getId() != null) {
                unicos.put(c.getId(), c);
            }
        }
        
        int duplicadosRemovidos = lista.size() - unicos.size();
        if (duplicadosRemovidos > 0) {
            System.out.println("AVISO: " + duplicadosRemovidos + " churrasco(s) duplicado(s) removido(s) antes de salvar");
        }
        
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(PATH), StandardCharsets.UTF_8)) {
            // Cabeçalho atualizado (incluindo criadorId)
            w.write("id;criadorId;titulo;data;hora;local;tipo;pix;precoParticipante;descricao");
            w.newLine();

            int salvos = 0;
            for (Churrasco c : unicos.values()) {
                try {
                    String csvLine = c.toCSV();
                    if (csvLine != null && !csvLine.trim().isEmpty()) {
                        w.write(csvLine);
                        w.newLine();
                        salvos++;
                    } else {
                        System.err.println("AVISO: Churrasco com ID " + c.getId() + " gerou CSV vazio, pulando...");
                    }
                } catch (Exception e) {
                    System.err.println("ERRO ao escrever churrasco " + c.getId() + " no CSV: " + e.getMessage());
                }
            }
            
            System.out.println("Salvos " + salvos + " churrasco(s) no arquivo eventos.csv");
            return salvos > 0 || unicos.isEmpty(); // Retorna true se salvou algo ou se a lista estava vazia
        } catch (IOException ex) { 
            System.err.println("ERRO ao salvar arquivo eventos.csv: " + ex.getMessage());
            ex.printStackTrace(); 
            return false; 
        }
    }

    public List<Churrasco> listarPorCriador(java.util.UUID criadorId) {
        List<Churrasco> out = new ArrayList<>();
        if (criadorId == null) return out;
        for (Churrasco c : listar()) {
            if (criadorId.equals(c.getCriadorId())) {
                out.add(c);
            }
        }
        return out;
    }
}
