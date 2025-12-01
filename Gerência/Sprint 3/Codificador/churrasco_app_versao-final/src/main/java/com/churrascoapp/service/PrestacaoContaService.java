package com.churrascoapp.service;

import com.churrascoapp.dao.CompraDAO;
import com.churrascoapp.dao.ParticipacaoDAO;
import com.churrascoapp.dao.PrestacaoContaDAO;
import com.churrascoapp.model.Compra;
import com.churrascoapp.model.PrestacaoConta;
import com.churrascoapp.utils.UUIDUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PrestacaoContaService {

    private final PrestacaoContaDAO prestacaoContaDAO;
    private final ParticipacaoDAO participacaoDAO;
    private final CompraDAO compraDAO;

    public PrestacaoContaService(PrestacaoContaDAO prestacaoContaDAO,
                                 ParticipacaoDAO participacaoDAO,
                                 CompraDAO compraDAO) {
        this.prestacaoContaDAO = prestacaoContaDAO;
        this.participacaoDAO = participacaoDAO;
        this.compraDAO = compraDAO;
    }

    public PrestacaoConta gerarPrestacao(String churrascoId) {
        UUID churrascoUUID = UUID.fromString(churrascoId);

        double totalArrecadado = participacaoDAO.calcularTotalArrecadado(churrascoUUID);

        double totalGasto = 0.0;
        List<Compra> compras = compraDAO.listar();
        for (Compra c : compras) {
            if (c.getChurrascoId() != null && churrascoUUID.equals(c.getChurrascoId())) {
                totalGasto += c.getValor();
            }
        }

        PrestacaoConta existente = prestacaoContaDAO.buscarPorChurrasco(churrascoUUID);
        String hoje = LocalDate.now().toString();

        if (existente == null) {
            PrestacaoConta pc = new PrestacaoConta(
                    UUIDUtil.randomId(),
                    churrascoUUID,
                    totalArrecadado,
                    totalGasto,
                    totalArrecadado - totalGasto,
                    hoje,
                    "PENDENTE",
                    ""
            );
            prestacaoContaDAO.adicionar(pc);
            return pc;
        } else {
            existente.setTotalArrecadado(totalArrecadado);
            existente.setTotalGasto(totalGasto);
            existente.calcularSaldo();
            existente.setDataPrestacao(hoje);
            prestacaoContaDAO.atualizar(existente);
            return existente;
        }
    }

    public PrestacaoConta buscarPorChurrasco(String churrascoId) {
        return prestacaoContaDAO.buscarPorChurrasco(UUID.fromString(churrascoId));
    }

    public boolean aprovarPrestacao(String churrascoId, String observacoes) {
        PrestacaoConta pc = prestacaoContaDAO.buscarPorChurrasco(UUID.fromString(churrascoId));
        if (pc == null) {
            return false;
        }
        pc.setStatus("APROVADA");
        if (observacoes != null && !observacoes.trim().isEmpty()) {
            pc.setObservacoes(observacoes);
        }
        return prestacaoContaDAO.atualizar(pc);
    }

    public boolean rejeitarPrestacao(String churrascoId, String observacoes) {
        PrestacaoConta pc = prestacaoContaDAO.buscarPorChurrasco(UUID.fromString(churrascoId));
        if (pc == null) {
            return false;
        }
        pc.setStatus("REJEITADA");
        if (observacoes != null && !observacoes.trim().isEmpty()) {
            pc.setObservacoes(observacoes);
        }
        return prestacaoContaDAO.atualizar(pc);
    }
}
