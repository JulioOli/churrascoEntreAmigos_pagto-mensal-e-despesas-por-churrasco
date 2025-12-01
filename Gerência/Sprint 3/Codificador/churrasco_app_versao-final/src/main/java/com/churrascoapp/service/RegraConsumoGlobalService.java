package com.churrascoapp.service;

import com.churrascoapp.dao.RegraConsumoGlobalDAO;
import com.churrascoapp.model.RegraConsumoGlobal;
import com.churrascoapp.utils.UUIDUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class RegraConsumoGlobalService {

    private final RegraConsumoGlobalDAO dao;

    public RegraConsumoGlobalService(RegraConsumoGlobalDAO dao) {
        this.dao = dao;
    }

    public RegraConsumoGlobal criar(String nome, String descricao, 
                                   double valorLimitePadrao, double percentualAlerta, 
                                   double percentualCritico) {
        UUID id = UUIDUtil.randomId();
        String hoje = LocalDate.now().toString();
        
        RegraConsumoGlobal regra = new RegraConsumoGlobal(
                id, nome, descricao, valorLimitePadrao, 
                percentualAlerta, percentualCritico, true, 
                hoje, hoje
        );
        
        if (!dao.adicionar(regra)) {
            throw new RuntimeException("Falha ao salvar regra de consumo global.");
        }
        
        return regra;
    }

    public List<RegraConsumoGlobal> listar() {
        return dao.listar();
    }

    public RegraConsumoGlobal buscarPorId(String id) {
        return dao.buscarPorId(UUID.fromString(id));
    }

    public boolean atualizar(RegraConsumoGlobal regra) {
        regra.setDataAtualizacao(LocalDate.now().toString());
        return dao.atualizar(regra);
    }

    public boolean remover(String id) {
        return dao.remover(UUID.fromString(id));
    }

    public RegraConsumoGlobal buscarRegraAtiva() {
        return dao.buscarRegraAtiva();
    }

    public boolean ativarRegra(String id) {
        RegraConsumoGlobal regra = dao.buscarPorId(UUID.fromString(id));
        if (regra == null) return false;
        
        // Desativa todas as outras regras
        List<RegraConsumoGlobal> todas = dao.listar();
        for (RegraConsumoGlobal r : todas) {
            if (!r.getId().equals(regra.getId())) {
                r.setAtiva(false);
                dao.atualizar(r);
            }
        }
        
        // Ativa a regra selecionada
        regra.setAtiva(true);
        regra.setDataAtualizacao(LocalDate.now().toString());
        return dao.atualizar(regra);
    }
}

