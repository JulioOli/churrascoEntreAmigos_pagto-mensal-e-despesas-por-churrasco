package com.churrascoapp.service;

import com.churrascoapp.dao.CompraDAO;
import com.churrascoapp.dao.ParticipacaoDAO;
import com.churrascoapp.dao.RelatorioDAO;
import com.churrascoapp.model.*;
import com.churrascoapp.utils.UUIDUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class RelatorioService {

    private final RelatorioDAO relatorioDAO;
    private final ParticipacaoDAO participacaoDAO;
    private final CompraDAO compraDAO;
    private final PrestacaoContaService prestacaoContaService;

    public RelatorioService(RelatorioDAO relatorioDAO, ParticipacaoDAO participacaoDAO,
                           CompraDAO compraDAO, PrestacaoContaService prestacaoContaService) {
        this.relatorioDAO = relatorioDAO;
        this.participacaoDAO = participacaoDAO;
        this.compraDAO = compraDAO;
        this.prestacaoContaService = prestacaoContaService;
    }

    /**
     * Gera relatório financeiro de um churrasco
     * @param churrascoId ID do churrasco
     * @param formato Formato do relatório (TXT, CSV, PDF)
     * @return Relatório gerado
     */
    public Relatorio gerarFinanceiro(String churrascoId, String formato) {
        UUID id = UUIDUtil.randomId();
        UUID churrascoUUID = UUID.fromString(churrascoId);
        String hoje = LocalDate.now().toString();

        // Busca prestação de contas
        PrestacaoConta prestacao = prestacaoContaService.buscarPorChurrasco(churrascoId);
        
        StringBuilder conteudo = new StringBuilder();
        conteudo.append("═══════════════════════════════════════\n");
        conteudo.append("     RELATÓRIO FINANCEIRO\n");
        conteudo.append("═══════════════════════════════════════\n\n");
        conteudo.append("Data de Geração: ").append(hoje).append("\n");
        conteudo.append("Churrasco ID: ").append(churrascoId).append("\n\n");

        if (prestacao != null) {
            conteudo.append("RESUMO FINANCEIRO\n");
            conteudo.append("─────────────────────────────────────\n");
            conteudo.append("Total Arrecadado: R$ ").append(String.format("%.2f", prestacao.getTotalArrecadado())).append("\n");
            conteudo.append("Total Gasto:      R$ ").append(String.format("%.2f", prestacao.getTotalGasto())).append("\n");
            conteudo.append("Saldo:            R$ ").append(String.format("%.2f", prestacao.getSaldo())).append("\n");
            conteudo.append("Status:           ").append(prestacao.getStatus()).append("\n\n");
        }

        // Lista compras
        List<Compra> compras = compraDAO.listarPorChurrasco(churrascoUUID);
        conteudo.append("COMPRAS REALIZADAS\n");
        conteudo.append("─────────────────────────────────────\n");
        if (compras.isEmpty()) {
            conteudo.append("Nenhuma compra registrada.\n\n");
        } else {
            double totalCompras = 0.0;
            for (Compra c : compras) {
                conteudo.append(String.format("- %s: %d x R$ %.2f = R$ %.2f\n",
                        c.getItem(), c.getQuantidade(), c.getValor() / c.getQuantidade(), c.getValor()));
                totalCompras += c.getValor();
            }
            conteudo.append("\nTotal de Compras: R$ ").append(String.format("%.2f", totalCompras)).append("\n\n");
        }

        // Lista participações
        List<Participacao> participacoes = participacaoDAO.listarPorChurrasco(churrascoUUID);
        conteudo.append("PARTICIPAÇÕES\n");
        conteudo.append("─────────────────────────────────────\n");
        if (participacoes.isEmpty()) {
            conteudo.append("Nenhuma participação registrada.\n\n");
        } else {
            double totalArrecadado = 0.0;
            int confirmadas = 0;
            for (Participacao p : participacoes) {
                String statusPagamento = p.isPagamentoConfirmado() ? "Confirmado" : "Pendente";
                conteudo.append(String.format("- Participação ID: %s | Valor: R$ %.2f | Status: %s | Pagamento: %s\n",
                        p.getId(), p.getValorPago(), p.getStatus(), statusPagamento));
                totalArrecadado += p.getValorPago();
                if (p.isPagamentoConfirmado()) confirmadas++;
            }
            conteudo.append("\nTotal Arrecadado: R$ ").append(String.format("%.2f", totalArrecadado)).append("\n");
            conteudo.append("Participações Confirmadas: ").append(confirmadas).append(" / ").append(participacoes.size()).append("\n\n");
        }

        if (prestacao != null && prestacao.getObservacoes() != null && !prestacao.getObservacoes().isEmpty()) {
            conteudo.append("OBSERVAÇÕES\n");
            conteudo.append("─────────────────────────────────────\n");
            conteudo.append(prestacao.getObservacoes()).append("\n\n");
        }

        Relatorio relatorio = new Relatorio(id, churrascoUUID, "FINANCEIRO", formato,
                conteudo.toString(), hoje, "");
        
        relatorioDAO.adicionar(relatorio);
        return relatorio;
    }

    /**
     * Gera relatório operacional de um churrasco
     * @param churrascoId ID do churrasco
     * @param formato Formato do relatório (TXT, CSV, PDF)
     * @return Relatório gerado
     */
    public Relatorio gerarOperacional(String churrascoId, String formato) {
        UUID id = UUIDUtil.randomId();
        UUID churrascoUUID = UUID.fromString(churrascoId);
        String hoje = LocalDate.now().toString();

        StringBuilder conteudo = new StringBuilder();
        conteudo.append("═══════════════════════════════════════\n");
        conteudo.append("     RELATÓRIO OPERACIONAL\n");
        conteudo.append("═══════════════════════════════════════\n\n");
        conteudo.append("Data de Geração: ").append(hoje).append("\n");
        conteudo.append("Churrasco ID: ").append(churrascoId).append("\n\n");

        // Lista participações com status
        List<Participacao> participacoes = participacaoDAO.listarPorChurrasco(churrascoUUID);
        conteudo.append("PARTICIPAÇÕES E STATUS\n");
        conteudo.append("─────────────────────────────────────\n");
        if (participacoes.isEmpty()) {
            conteudo.append("Nenhuma participação registrada.\n\n");
        } else {
            int pendentes = 0, confirmadas = 0, canceladas = 0;
            for (Participacao p : participacoes) {
                conteudo.append(String.format("- ID: %s | Status: %s | Pagamento: %s | Valor: R$ %.2f\n",
                        p.getId(), p.getStatus(), 
                        p.isPagamentoConfirmado() ? "Confirmado" : "Pendente",
                        p.getValorPago()));
                
                if ("CONFIRMADO".equals(p.getStatus()) || "CONFIRMADA".equals(p.getStatus())) {
                    confirmadas++;
                } else if ("CANCELADO".equals(p.getStatus()) || "CANCELADA".equals(p.getStatus())) {
                    canceladas++;
                } else {
                    pendentes++;
                }
            }
            conteudo.append("\nResumo:\n");
            conteudo.append("- Confirmadas: ").append(confirmadas).append("\n");
            conteudo.append("- Pendentes: ").append(pendentes).append("\n");
            conteudo.append("- Canceladas: ").append(canceladas).append("\n\n");
        }

        // Lista compras
        List<Compra> compras = compraDAO.listarPorChurrasco(churrascoUUID);
        conteudo.append("COMPRAS REGISTRADAS\n");
        conteudo.append("─────────────────────────────────────\n");
        if (compras.isEmpty()) {
            conteudo.append("Nenhuma compra registrada.\n\n");
        } else {
            for (Compra c : compras) {
                conteudo.append(String.format("- %s | Quantidade: %d | Valor: R$ %.2f | Data: %s\n",
                        c.getItem(), c.getQuantidade(), c.getValor(),
                        c.getData() != null ? c.getData() : "-"));
            }
        }

        Relatorio relatorio = new Relatorio(id, churrascoUUID, "OPERACIONAL", formato,
                conteudo.toString(), hoje, "");
        
        relatorioDAO.adicionar(relatorio);
        return relatorio;
    }

    public List<Relatorio> listarPorChurrasco(String churrascoId) {
        return relatorioDAO.listarPorChurrasco(UUID.fromString(churrascoId));
    }

    public Relatorio buscarPorId(String id) {
        return relatorioDAO.buscarPorId(UUID.fromString(id));
    }
}

