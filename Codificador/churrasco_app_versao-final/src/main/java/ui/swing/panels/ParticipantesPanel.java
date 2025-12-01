package ui.swing.panels;

import com.churrascoapp.app.AppContext;
import com.churrascoapp.controller.EventoController;
import com.churrascoapp.controller.ParticipacaoController;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.model.Participacao;
import com.churrascoapp.model.Usuario;
import com.churrascoapp.service.UsuarioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ParticipantesPanel extends JPanel {

    private final Usuario usuario;
    private final ParticipacaoController participacaoController;
    private final EventoController eventoController;
    private final UsuarioService usuarioService;

    private final JTable table;
    private final DefaultTableModel model;
    private final JComboBox<String> churrascoCombo;

    public ParticipantesPanel(Usuario usuario) {
        this.usuario = usuario;
        this.participacaoController = AppContext.get().participacoes();
        this.eventoController = AppContext.get().eventos();
        this.usuarioService = AppContext.get().usuarioService();

        setLayout(new BorderLayout(8, 8));

        // TOPO
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Churrasco:"));
        churrascoCombo = new JComboBox<>();
        churrascoCombo.setPreferredSize(new Dimension(300, 25));
        top.add(churrascoCombo);

        JButton atualizarBtn = new JButton("Atualizar Lista");
        JButton confirmarPagamentoBtn = new JButton("Confirmar Pagamento");
        top.add(atualizarBtn);
        top.add(confirmarPagamentoBtn);

        add(top, BorderLayout.NORTH);

        String[] cols = {"ID", "Usuário ID", "Status", "Valor Pago (R$)", "Pagamento Confirmado", "Data Inscrição"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        atualizarBtn.addActionListener(e -> carregarParticipantes());
        confirmarPagamentoBtn.addActionListener(e -> confirmarPagamento());
        churrascoCombo.addActionListener(e -> carregarParticipantes());

        carregarChurrascos();
    }

    private void carregarChurrascos() {
        churrascoCombo.removeAllItems();
        try {
            List<Churrasco> churrascos = eventoController.listar();
            for (Churrasco c : churrascos) {
                String titulo = c.getTitulo() != null ? c.getTitulo() : "(sem título)";
                String data = c.getData() != null ? c.getData() : "";
                String id = c.getId() != null ? c.getId().toString() : "";
                churrascoCombo.addItem(titulo + " - " + data + " (ID: " + id + ")");
            }
            if (churrascos.isEmpty()) {
                churrascoCombo.addItem("Nenhum churrasco cadastrado");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar churrascos:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getChurrascoIdSelecionado() {
        String selected = (String) churrascoCombo.getSelectedItem();
        if (selected == null || selected.contains("Nenhum")) {
            return null;
        }
        // Extrai o ID do formato "Título - Data (ID: uuid)"
        int startIdx = selected.lastIndexOf("(ID: ");
        int endIdx = selected.lastIndexOf(")");
        if (startIdx >= 0 && endIdx > startIdx) {
            return selected.substring(startIdx + 5, endIdx).trim();
        }
        return null;
    }

    private void carregarParticipantes() {
        model.setRowCount(0);
        String churrascoId = getChurrascoIdSelecionado();
        if (churrascoId == null || churrascoId.isEmpty()) {
            return;
        }

        try {
            List<Participacao> lista = participacaoController.listarPorChurrasco(churrascoId);
            for (Participacao p : lista) {
                Object[] row = {
                        p.getId() != null ? p.getId().toString() : "",
                        p.getUsuarioId() != null ? p.getUsuarioId().toString() : "",
                        p.getStatus(),
                        p.getValorPago(),
                        p.isPagamentoConfirmado() ? "Sim" : "Não",
                        p.getDataInscricao()
                };
                model.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar participantes:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getParticipacaoIdSelecionada() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return null;
        }
        return String.valueOf(model.getValueAt(row, 0));
    }

    private void confirmarPagamento() {
        String participacaoId = getParticipacaoIdSelecionada();
        if (participacaoId == null || participacaoId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma participação para confirmar o pagamento.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja confirmar o pagamento desta participação?",
                "Confirmar Pagamento",
                JOptionPane.YES_NO_OPTION);

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean sucesso = participacaoController.confirmarPagamento(participacaoId);
            if (sucesso) {
                JOptionPane.showMessageDialog(this,
                        "Pagamento confirmado com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarParticipantes();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Não foi possível confirmar o pagamento.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao confirmar pagamento:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

