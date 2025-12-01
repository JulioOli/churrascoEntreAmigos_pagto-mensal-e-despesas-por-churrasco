package com.churrascoapp.service;

import com.churrascoapp.dao.AlertaConsumoDAO;
import com.churrascoapp.dao.CompraDAO;
import com.churrascoapp.dao.ParticipacaoDAO;
import com.churrascoapp.model.AlertaConsumo;
import com.churrascoapp.model.Compra;
import com.churrascoapp.model.Participacao;
import com.churrascoapp.utils.UUIDUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlertaConsumoService {

    private final AlertaConsumoDAO alertaConsumoDAO;
    private final ParticipacaoDAO participacaoDAO;
    private final CompraDAO compraDAO;

    public AlertaConsumoService(AlertaConsumoDAO alertaConsumoDAO,
                                ParticipacaoDAO participacaoDAO,
                                CompraDAO compraDAO) {
        this.alertaConsumoDAO = alertaConsumoDAO;
        this.participacaoDAO = participacaoDAO;
        this.compraDAO = compraDAO;
    }

    public List<AlertaConsumo> gerarAlertas(String churrascoId, double limite) {
        UUID churrascoUUID = UUID.fromString(churrascoId);
        List<Participacao> participacoes =
                participacaoDAO.listarPorChurrasco(churrascoUUID);

        // Calcula total de gastos do churrasco
        double totalGastos = 0.0;
        List<Compra> compras = compraDAO.listar();
        for (Compra c : compras) {
            if (c.getChurrascoId() != null && churrascoUUID.equals(c.getChurrascoId())) {
                totalGastos += c.getValor();
            }
        }

        // Calcula consumo por pessoa (valor da participação + parte proporcional dos gastos)
        int numParticipantes = participacoes.size();
        double gastoPorPessoa = numParticipantes > 0 ? totalGastos / numParticipantes : 0.0;

        List<AlertaConsumo> gerados = new ArrayList<>();
        String hoje = LocalDate.now().toString();

        for (Participacao p : participacoes) {
            // Consumo total = valor pago na participação + parte proporcional dos gastos
            double consumoTotal = p.getValorPago() + gastoPorPessoa;
            
            if (consumoTotal <= limite) continue;

            String tipo;
            if (consumoTotal >= limite * 1.5) {
                tipo = "CRÍTICO";
            } else if (consumoTotal >= limite * 1.2) {
                tipo = "ALERTA";
            } else {
                tipo = "AVISO";
            }

            StringBuilder msg = new StringBuilder();
            msg.append("Consumo estimado de R$ ").append(String.format("%.2f", consumoTotal));
            msg.append(" excede o limite de R$ ").append(String.format("%.2f", limite));
            msg.append(" (").append(tipo).append(")\n\n");
            msg.append("Detalhamento:\n");
            msg.append("- Participação: R$ ").append(String.format("%.2f", p.getValorPago())).append("\n");
            msg.append("- Gasto proporcional: R$ ").append(String.format("%.2f", gastoPorPessoa));
            if (numParticipantes > 0) {
                msg.append(" (total de R$ ").append(String.format("%.2f", totalGastos));
                msg.append(" dividido por ").append(numParticipantes).append(" participantes)");
            }

            AlertaConsumo a = new AlertaConsumo(
                    UUIDUtil.randomId(),
                    churrascoUUID,
                    p.getUsuarioId(),
                    msg.toString(),
                    limite,
                    consumoTotal,
                    hoje,
                    tipo
            );
            alertaConsumoDAO.adicionar(a);
            gerados.add(a);
        }

        return gerados;
    }

    public List<AlertaConsumo> listarPorChurrasco(String churrascoId) {
        return alertaConsumoDAO.listarPorChurrasco(UUID.fromString(churrascoId));
    }
}
