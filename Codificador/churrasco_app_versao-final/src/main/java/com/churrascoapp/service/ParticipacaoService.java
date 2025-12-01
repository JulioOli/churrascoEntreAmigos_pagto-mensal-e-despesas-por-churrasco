package com.churrascoapp.service;

import com.churrascoapp.dao.EventoDAO;
import com.churrascoapp.dao.ParticipacaoDAO;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.model.Participacao;
import com.churrascoapp.utils.UUIDUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ParticipacaoService {

    private final ParticipacaoDAO participacaoDAO;
    private final EventoDAO eventoDAO;

    public ParticipacaoService(ParticipacaoDAO participacaoDAO, EventoDAO eventoDAO) {
        this.participacaoDAO = participacaoDAO;
        this.eventoDAO = eventoDAO;
    }

    public Participacao registrarParticipacao(String churrascoId, String usuarioId) {
        Churrasco c = eventoDAO.buscarPorId(churrascoId);
        if (c == null) throw new IllegalArgumentException("Churrasco não encontrado.");

        UUID id = UUIDUtil.randomId();
        double valor = c.getPrecoParticipante();

        Participacao p = new Participacao(
                id,
                UUID.fromString(churrascoId),
                UUID.fromString(usuarioId),
                "PENDENTE",
                valor,
                false,
                LocalDate.now().toString(),
                ""
        );
        if (!participacaoDAO.adicionar(p)) {
            throw new RuntimeException("Falha ao salvar participação.");
        }
        return p;
    }

    public boolean confirmarPagamento(String participacaoId) {
        Participacao p = participacaoDAO.buscarPorId(UUID.fromString(participacaoId));
        if (p == null) return false;
        p.confirmarPagamento();
        return participacaoDAO.atualizar(p);
    }

    public boolean cancelarParticipacao(String participacaoId) {
        Participacao p = participacaoDAO.buscarPorId(UUID.fromString(participacaoId));
        if (p == null) return false;
        p.cancelar();
        return participacaoDAO.atualizar(p);
    }

    public boolean confirmarParticipacao(String participacaoId) {
        Participacao p = participacaoDAO.buscarPorId(UUID.fromString(participacaoId));
        if (p == null) return false;
        p.setStatus("CONFIRMADA");
        return participacaoDAO.atualizar(p);
    }

    public List<Participacao> listarPorChurrasco(String churrascoId) {
        return participacaoDAO.listarPorChurrasco(UUID.fromString(churrascoId));
    }

    public List<Participacao> listarPorUsuario(String usuarioId) {
        return participacaoDAO.listarPorUsuario(UUID.fromString(usuarioId));
    }

    public double calcularTotalArrecadado(String churrascoId) {
        return participacaoDAO.calcularTotalArrecadado(UUID.fromString(churrascoId));
    }
}
