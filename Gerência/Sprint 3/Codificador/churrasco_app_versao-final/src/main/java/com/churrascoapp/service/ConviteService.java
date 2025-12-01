package com.churrascoapp.service;

import com.churrascoapp.dao.ConviteDAO;
import com.churrascoapp.dao.EventoDAO;
import com.churrascoapp.dao.UsuarioDAO;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.model.Convite;
import com.churrascoapp.model.Participacao;
import com.churrascoapp.model.Usuario;
import com.churrascoapp.utils.UUIDUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ConviteService {

    private final ConviteDAO conviteDAO;
    private final ParticipacaoService participacaoService;
    private final EmailService emailService;
    private final UsuarioDAO usuarioDAO;
    private final EventoDAO eventoDAO;

    public ConviteService(ConviteDAO conviteDAO, ParticipacaoService participacaoService,
                         EmailService emailService, UsuarioDAO usuarioDAO, EventoDAO eventoDAO) {
        this.conviteDAO = conviteDAO;
        this.participacaoService = participacaoService;
        this.emailService = emailService;
        this.usuarioDAO = usuarioDAO;
        this.eventoDAO = eventoDAO;
    }

    public Convite enviarConvite(String churrascoId, String usuarioId, String mensagem) {
        UUID churrascoUUID = UUID.fromString(churrascoId);
        UUID usuarioUUID = UUID.fromString(usuarioId);
        
        // Verifica se já existe convite para este churrasco e usuário usando método específico do DAO
        Convite existente = conviteDAO.buscarPorChurrascoEUsuario(churrascoUUID, usuarioUUID);
        if (existente != null) {
            String status = existente.getStatus();
            if (status == null) status = "PENDENTE";
            
            // Não permite criar novo convite se já existe um (independente do status)
            throw new IllegalStateException(
                    "Já existe um convite para este usuário neste churrasco " +
                    "(Status: " + status + "). " +
                    "Não é possível criar convites duplicados."
            );
        }
        
        // Também verifica se o usuário já está participando do churrasco
        List<Participacao> participacoes = participacaoService.listarPorChurrasco(churrascoId);
        for (Participacao p : participacoes) {
            if (usuarioUUID.equals(p.getUsuarioId())) {
                String statusPart = p.getStatus();
                if (statusPart != null && 
                    ("CONFIRMADA".equals(statusPart) || "CONFIRMADO".equals(statusPart) || 
                     "PENDENTE".equals(statusPart))) {
                    throw new IllegalStateException(
                            "Este usuário já está participando ou tem participação pendente " +
                            "neste churrasco (Status: " + statusPart + "). " +
                            "Não é necessário enviar convite."
                    );
                }
            }
        }
        
        UUID id = UUIDUtil.randomId();
        Convite c = new Convite(
                id,
                churrascoUUID,
                usuarioUUID,
                "PENDENTE",
                LocalDate.now().toString(),
                null,
                mensagem
        );
        if (!conviteDAO.adicionar(c)) {
            throw new RuntimeException("Falha ao salvar convite.");
        }

        // Envia email de convite
        try {
            Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
            Churrasco churrasco = eventoDAO.buscarPorId(churrascoId);
            if (usuario != null && churrasco != null && usuario.getEmail() != null) {
                emailService.enviarEmailConvite(usuario, churrasco, mensagem);
            }
        } catch (Exception e) {
            // Não falha o convite se o email falhar
            System.err.println("Erro ao enviar email de convite: " + e.getMessage());
        }

        return c;
    }

    public List<Convite> listarConvitesRecebidos(String usuarioId) {
        return conviteDAO.listarPorUsuario(UUID.fromString(usuarioId));
    }

    public List<Convite> listarConvitesPorChurrasco(String churrascoId) {
        return conviteDAO.listarPorChurrasco(UUID.fromString(churrascoId));
    }

    public Participacao aceitarConvite(String conviteId) {
        Convite c = conviteDAO.buscarPorId(UUID.fromString(conviteId));
        if (c == null) throw new IllegalArgumentException("Convite não encontrado.");

        c.aceitar();
        conviteDAO.atualizar(c);

        // Ao aceitar, cria a participação
        Participacao participacao = participacaoService.registrarParticipacao(
                c.getChurrascoId().toString(),
                c.getUsuarioId().toString()
        );

        // Envia email de confirmação de participação
        try {
            Usuario usuario = usuarioDAO.buscarPorId(c.getUsuarioId().toString());
            Churrasco churrasco = eventoDAO.buscarPorId(c.getChurrascoId().toString());
            if (usuario != null && churrasco != null && usuario.getEmail() != null) {
                emailService.enviarEmailParticipacao(usuario, churrasco);
            }
        } catch (Exception e) {
            // Não falha a participação se o email falhar
            System.err.println("Erro ao enviar email de participação: " + e.getMessage());
        }

        return participacao;
    }

    public boolean recusarConvite(String conviteId) {
        Convite c = conviteDAO.buscarPorId(UUID.fromString(conviteId));
        if (c == null) return false;
        c.recusar();
        return conviteDAO.atualizar(c);
    }
}
