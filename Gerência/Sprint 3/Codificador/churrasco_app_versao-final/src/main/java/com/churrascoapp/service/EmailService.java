package com.churrascoapp.service;

import com.churrascoapp.dao.EmailDAO;
import com.churrascoapp.model.Email;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.model.Usuario;
import com.churrascoapp.utils.UUIDUtil;

import java.time.LocalDateTime;

public class EmailService {

    private final EmailDAO emailDAO;

    public EmailService(EmailDAO emailDAO) {
        this.emailDAO = emailDAO;
    }

    public Email registrarEmail(String destinatario, String assunto, String corpo, String tipo) {
        Email e = new Email(
                UUIDUtil.randomId(),
                destinatario,
                assunto,
                corpo,
                LocalDateTime.now().toString(),
                "PENDENTE",
                tipo
        );
        emailDAO.adicionar(e);
        // Simula envio (em produção, aqui seria feita a chamada ao servidor SMTP)
        e.enviar();
        emailDAO.atualizar(e);
        return e;
    }

    /**
     * Cria e envia email de convite para churrasco
     */
    public Email enviarEmailConvite(Usuario destinatario, Churrasco churrasco, String mensagemPersonalizada) {
        String assunto = "Convite para Churrasco: " + churrasco.getTitulo();
        
        StringBuilder corpo = new StringBuilder();
        corpo.append("Olá ").append(destinatario.getNome()).append("!\n\n");
        corpo.append("Você foi convidado para participar do churrasco:\n\n");
        corpo.append("Título: ").append(churrasco.getTitulo()).append("\n");
        corpo.append("Data: ").append(churrasco.getData()).append("\n");
        corpo.append("Hora: ").append(churrasco.getHora()).append("\n");
        corpo.append("Local: ").append(churrasco.getLocal()).append("\n");
        corpo.append("Preço por participante: R$ ").append(churrasco.getPrecoParticipante()).append("\n");
        
        if (mensagemPersonalizada != null && !mensagemPersonalizada.trim().isEmpty()) {
            corpo.append("\nMensagem do organizador:\n");
            corpo.append(mensagemPersonalizada).append("\n");
        }
        
        corpo.append("\nAcesse o sistema para aceitar ou recusar o convite.\n\n");
        corpo.append("Atenciosamente,\nSistema Churrasco Entre Amigos");

        return registrarEmail(destinatario.getEmail(), assunto, corpo.toString(), "CONVITE");
    }

    /**
     * Cria e envia email de participação confirmada
     */
    public Email enviarEmailParticipacao(Usuario destinatario, Churrasco churrasco) {
        String assunto = "Participação confirmada: " + churrasco.getTitulo();
        
        StringBuilder corpo = new StringBuilder();
        corpo.append("Olá ").append(destinatario.getNome()).append("!\n\n");
        corpo.append("Sua participação no churrasco foi confirmada:\n\n");
        corpo.append("Título: ").append(churrasco.getTitulo()).append("\n");
        corpo.append("Data: ").append(churrasco.getData()).append("\n");
        corpo.append("Hora: ").append(churrasco.getHora()).append("\n");
        corpo.append("Local: ").append(churrasco.getLocal()).append("\n");
        corpo.append("Valor: R$ ").append(churrasco.getPrecoParticipante()).append("\n");
        corpo.append("\nChave PIX para pagamento: ").append(churrasco.getPix()).append("\n");
        corpo.append("\nAtenciosamente,\nSistema Churrasco Entre Amigos");

        return registrarEmail(destinatario.getEmail(), assunto, corpo.toString(), "PARTICIPACAO");
    }

    /**
     * Cria e envia email de alerta de consumo
     */
    public Email enviarEmailAlerta(Usuario destinatario, Churrasco churrasco, String mensagemAlerta) {
        String assunto = "Alerta de Consumo: " + churrasco.getTitulo();
        
        StringBuilder corpo = new StringBuilder();
        corpo.append("Olá ").append(destinatario.getNome()).append("!\n\n");
        corpo.append("Alerta de consumo para o churrasco:\n\n");
        corpo.append("Título: ").append(churrasco.getTitulo()).append("\n");
        corpo.append("Data: ").append(churrasco.getData()).append("\n");
        corpo.append("\n").append(mensagemAlerta).append("\n");
        corpo.append("\nAtenciosamente,\nSistema Churrasco Entre Amigos");

        return registrarEmail(destinatario.getEmail(), assunto, corpo.toString(), "ALERTA");
    }
}
