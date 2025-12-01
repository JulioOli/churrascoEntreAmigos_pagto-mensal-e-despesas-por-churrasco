package ui.swing.panels;

import com.churrascoapp.app.AppContext;
import com.churrascoapp.controller.ConviteController;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.model.Convite;
import com.churrascoapp.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MeusConvitesPanel extends JPanel {

    private final Usuario usuario;
    private final ConviteController conviteController;

    private JTable table;
    private DefaultTableModel model;

    public MeusConvitesPanel(Usuario usuario) {
        this.usuario = usuario;
        this.conviteController = AppContext.get().convites();

        setLayout(new BorderLayout(8,8));

        JButton atualizarBtn = new JButton("Atualizar");
        JButton verDetalhesBtn = new JButton("Ver Detalhes / Instruções");
        JButton aceitarBtn   = new JButton("Aceitar Convite");
        JButton recusarBtn   = new JButton("Recusar Convite");

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(atualizarBtn);
        top.add(verDetalhesBtn);
        top.add(aceitarBtn);
        top.add(recusarBtn);

        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Churrasco", "Status", "Mensagem"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        atualizarBtn.addActionListener(e -> carregar());
        verDetalhesBtn.addActionListener(e -> verDetalhes());
        aceitarBtn.addActionListener(e -> aceitar());
        recusarBtn.addActionListener(e -> recusar());

        carregar();
    }

    private void carregar() {
        model.setRowCount(0);

        // >>>>>>> TRATAMENTO DO ID NULO AQUI <<<<<<<<
        if (usuario.getId() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seu usuário não possui ID cadastrado no sistema.\n" +
                            "Por isso não é possível localizar convites automaticamente.\n\n" +
                            "Se isso for para TCC, você pode:\n" +
                            "- Ajustar o cadastro de usuários para salvar um UUID;\n" +
                            "- Ou alimentar o CSV de usuários com um ID para cada linha.",
                    "ID do usuário ausente",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        List<Convite> lista =
                conviteController.listarConvitesRecebidos(usuario.getId().toString());

        for (Convite c : lista) {
            model.addRow(new Object[]{
                    c.getId(), c.getChurrascoId(), c.getStatus(), c.getMensagem()
            });
        }
    }

    private String getSelecionado() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        return String.valueOf(model.getValueAt(row, 0));
    }

    private void aceitar() {
        String id = getSelecionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Selecione um convite.");
            return;
        }

        try {
            conviteController.aceitarConvite(id);
            JOptionPane.showMessageDialog(this, "Convite aceito!");
            carregar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recusar() {
        String id = getSelecionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Selecione um convite.");
            return;
        }

        try {
            conviteController.recusarConvite(id);
            JOptionPane.showMessageDialog(this, "Convite recusado.");
            carregar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDetalhes() {
        String id = getSelecionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Selecione um convite para ver os detalhes.", 
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Convite> todos = conviteController.listarConvitesRecebidos(usuario.getId().toString());
            Convite convite = null;
            for (Convite c : todos) {
                if (c.getId().toString().equals(id)) {
                    convite = c;
                    break;
                }
            }

            if (convite == null) {
                JOptionPane.showMessageDialog(this, "Convite não encontrado.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Busca informações do churrasco
            Churrasco churrasco = AppContext.get().churrascos().buscar(convite.getChurrascoId().toString());
            
            StringBuilder detalhes = new StringBuilder();
            detalhes.append("═══════════════════════════════════════\n");
            detalhes.append("        DETALHES DO CONVITE\n");
            detalhes.append("═══════════════════════════════════════\n\n");
            
            detalhes.append("📋 STATUS: ").append(convite.getStatus()).append("\n\n");
            
            if (churrasco != null) {
                detalhes.append("🎉 CHURRASCO: ").append(churrasco.getTitulo()).append("\n");
                detalhes.append("📅 Data: ").append(churrasco.getData() != null ? churrasco.getData() : "-").append("\n");
                detalhes.append("⏰ Hora: ").append(churrasco.getHora() != null ? churrasco.getHora() : "-").append("\n");
                detalhes.append("📍 Local: ").append(churrasco.getLocal() != null ? churrasco.getLocal() : "-").append("\n");
                detalhes.append("🏷️  Tipo: ").append(churrasco.getTipo() != null ? churrasco.getTipo() : "-").append("\n\n");
                
                detalhes.append("═══════════════════════════════════════\n");
                detalhes.append("     INSTRUÇÕES DE PAGAMENTO\n");
                detalhes.append("═══════════════════════════════════════\n\n");
                detalhes.append("💰 Valor por participante: R$ ").append(String.format("%.2f", churrasco.getPrecoParticipante())).append("\n");
                
                if (churrasco.getPix() != null && !churrasco.getPix().trim().isEmpty()) {
                    detalhes.append("🔑 Chave PIX: ").append(churrasco.getPix()).append("\n");
                    detalhes.append("\n📱 Para pagar via PIX:\n");
                    detalhes.append("   1. Abra o app do seu banco\n");
                    detalhes.append("   2. Escaneie o QR Code ou digite a chave PIX\n");
                    detalhes.append("   3. Confirme o valor de R$ ").append(String.format("%.2f", churrasco.getPrecoParticipante())).append("\n");
                    detalhes.append("   4. Envie o comprovante ao organizador\n");
                } else {
                    detalhes.append("⚠️  Chave PIX não informada. Entre em contato com o organizador.\n");
                }
                
                if (churrasco.getDescricao() != null && !churrasco.getDescricao().trim().isEmpty()) {
                    detalhes.append("\n📝 Observações:\n").append(churrasco.getDescricao()).append("\n");
                }
            } else {
                detalhes.append("⚠️  Informações do churrasco não encontradas.\n");
            }
            
            detalhes.append("\n═══════════════════════════════════════\n");
            detalhes.append("        INFORMAÇÕES DO CONVITE\n");
            detalhes.append("═══════════════════════════════════════\n\n");
            detalhes.append("📨 Enviado em: ").append(convite.getDataEnvio() != null ? convite.getDataEnvio() : "-").append("\n");
            if (convite.getDataResposta() != null && !convite.getDataResposta().isEmpty()) {
                detalhes.append("📬 Respondido em: ").append(convite.getDataResposta()).append("\n");
            }
            if (convite.getMensagem() != null && !convite.getMensagem().trim().isEmpty()) {
                detalhes.append("\n💬 Mensagem do organizador:\n");
                detalhes.append(convite.getMensagem()).append("\n");
            }
            
            detalhes.append("\n🆔 ID do Convite: ").append(convite.getId()).append("\n");
            detalhes.append("🆔 ID do Churrasco: ").append(convite.getChurrascoId()).append("\n");

            JTextArea area = new JTextArea(detalhes.toString(), 25, 60);
            area.setEditable(false);
            area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(700, 500));

            JOptionPane.showMessageDialog(this, scroll,
                    "Detalhes do Convite e Instruções de Pagamento",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar detalhes:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
