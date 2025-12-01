package ui.swing.panels;

import com.churrascoapp.app.AppContext;
import com.churrascoapp.controller.ParticipacaoController;
import com.churrascoapp.model.Participacao;
import com.churrascoapp.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MinhasParticipacoesPanel extends JPanel {

    private final Usuario usuario;
    private final ParticipacaoController participacaoController;

    private JTable table;
    private DefaultTableModel model;

    public MinhasParticipacoesPanel(Usuario usuario) {
        this.usuario = usuario;
        this.participacaoController = AppContext.get().participacoes();

        setLayout(new BorderLayout(8,8));

        JButton atualizarBtn = new JButton("Atualizar");
        JButton confirmarBtn = new JButton("Confirmar Participação");
        JButton marcarPagoBtn = new JButton("Marcar como Pago");
        JButton cancelarBtn  = new JButton("Cancelar participação");

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(atualizarBtn);
        top.add(confirmarBtn);
        top.add(marcarPagoBtn);
        top.add(cancelarBtn);

        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Churrasco", "Status", "Valor Pago", "Pagou?"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        atualizarBtn.addActionListener(e -> carregar());
        confirmarBtn.addActionListener(e -> confirmarParticipacao());
        marcarPagoBtn.addActionListener(e -> marcarComoPago());
        cancelarBtn.addActionListener(e -> cancelar());

        carregar();
    }

    private void carregar() {
        model.setRowCount(0);

        // >>>>>>> TRATAMENTO DO ID NULO AQUI <<<<<<<<
        if (usuario.getId() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seu usuário não possui ID cadastrado no sistema.\n" +
                            "Por isso não é possível listar as participações automaticamente.\n\n" +
                            "Para usar essa funcionalidade, o usuário precisa ter um UUID salvo.",
                    "ID do usuário ausente",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        List<Participacao> lista =
                participacaoController.listarPorUsuario(usuario.getId().toString());

        for (Participacao p : lista) {
            model.addRow(new Object[]{
                    p.getId(), p.getChurrascoId(), p.getStatus(),
                    p.getValorPago(), p.isPagamentoConfirmado()
            });
        }
    }

    private String getSelecionado() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        return String.valueOf(model.getValueAt(row, 0));
    }

    private void confirmarParticipacao() {
        String id = getSelecionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma participação.", 
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Busca a participação para verificar status
            List<Participacao> todas = participacaoController.listarPorUsuario(usuario.getId().toString());
            Participacao p = null;
            for (Participacao part : todas) {
                if (part.getId().toString().equals(id)) {
                    p = part;
                    break;
                }
            }
            
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Participação não encontrada.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if ("CONFIRMADA".equals(p.getStatus()) || "CANCELADA".equals(p.getStatus())) {
                JOptionPane.showMessageDialog(this, 
                        "Esta participação já está " + p.getStatus().toLowerCase() + ".", 
                        "Atenção", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Deseja confirmar sua participação neste churrasco?",
                    "Confirmar Participação",
                    JOptionPane.YES_NO_OPTION);
            
            if (confirmacao != JOptionPane.YES_OPTION) {
                return;
            }
            
            boolean sucesso = participacaoController.confirmarParticipacao(id);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, 
                        "Participação confirmada! Entre em contato com o organizador para confirmar o pagamento.", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregar();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Não foi possível confirmar a participação.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void marcarComoPago() {
        String id = getSelecionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma participação.", 
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean sucesso = participacaoController.confirmarPagamento(id);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, 
                        "Pagamento marcado como confirmado!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregar();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Não foi possível marcar como pago.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {
        String id = getSelecionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma participação.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja cancelar esta participação?",
                "Confirmar Cancelamento",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            participacaoController.cancelar(id);
            JOptionPane.showMessageDialog(this, "Participação cancelada.");
            carregar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
