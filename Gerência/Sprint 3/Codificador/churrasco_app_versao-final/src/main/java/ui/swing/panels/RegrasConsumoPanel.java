package ui.swing.panels;

import com.churrascoapp.app.AppContext;
import com.churrascoapp.model.RegraConsumoGlobal;
import com.churrascoapp.model.Usuario;
import com.churrascoapp.utils.AdminUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegrasConsumoPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private final Usuario usuarioAdmin;

    public RegrasConsumoPanel(Usuario usuarioAdmin) {
        this.usuarioAdmin = usuarioAdmin;
        
        if (!AdminUtils.isAdmin(usuarioAdmin)) {
            setLayout(new BorderLayout());
            add(new JLabel("Acesso negado. Apenas administradores podem gerenciar regras globais de consumo.", 
                    SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        setLayout(new BorderLayout(8, 8));

        String[] cols = {"ID", "Nome", "Limite Padrão (R$)", "Alerta (%)", "Crítico (%)", "Ativa"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton atualizarBtn = new JButton("Atualizar");
        JButton novoBtn = new JButton("Nova Regra");
        JButton editarBtn = new JButton("Editar Regra");
        JButton ativarBtn = new JButton("Ativar Regra");
        JButton removerBtn = new JButton("Remover Regra");

        top.add(atualizarBtn);
        top.add(novoBtn);
        top.add(editarBtn);
        top.add(ativarBtn);
        top.add(removerBtn);

        add(top, BorderLayout.NORTH);

        atualizarBtn.addActionListener(e -> carregarRegras());
        novoBtn.addActionListener(e -> criarNovaRegra());
        editarBtn.addActionListener(e -> editarRegra());
        ativarBtn.addActionListener(e -> ativarRegra());
        removerBtn.addActionListener(e -> removerRegra());

        carregarRegras();
    }

    private void carregarRegras() {
        if (model == null) return;
        model.setRowCount(0);
        try {
            java.util.List<RegraConsumoGlobal> regras = AppContext.get().regrasConsumo().listar();
            for (RegraConsumoGlobal r : regras) {
                model.addRow(new Object[]{
                        r.getId() != null ? r.getId().toString() : "",
                        r.getNome(),
                        String.format("%.2f", r.getValorLimitePadrao()),
                        String.format("%.1f", r.getPercentualAlerta()),
                        String.format("%.1f", r.getPercentualCritico()),
                        r.isAtiva() ? "Sim" : "Não"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar regras:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getRegraIdSelecionada() {
        if (table == null) return null;
        int row = table.getSelectedRow();
        if (row < 0) return null;
        return String.valueOf(model.getValueAt(row, 0));
    }

    private void criarNovaRegra() {
        JTextField nomeField = new JTextField();
        JTextArea descricaoArea = new JTextArea(3, 20);
        JScrollPane descScroll = new JScrollPane(descricaoArea);
        JTextField limiteField = new JTextField("100.00");
        JTextField alertaField = new JTextField("80.0");
        JTextField criticoField = new JTextField("120.0");

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("Nome da Regra:"));
        panel.add(nomeField);
        panel.add(new JLabel("Descrição:"));
        panel.add(descScroll);
        panel.add(new JLabel("Limite Padrão (R$):"));
        panel.add(limiteField);
        panel.add(new JLabel("Percentual Alerta (%):"));
        panel.add(alertaField);
        panel.add(new JLabel("Percentual Crítico (%):"));
        panel.add(criticoField);

        int op = JOptionPane.showConfirmDialog(this, panel,
                "Nova Regra de Consumo Global", JOptionPane.OK_CANCEL_OPTION);

        if (op == JOptionPane.OK_OPTION) {
            try {
                String nome = nomeField.getText().trim();
                String descricao = descricaoArea.getText().trim();
                double limite = Double.parseDouble(limiteField.getText().trim().replace(",", "."));
                double alerta = Double.parseDouble(alertaField.getText().trim().replace(",", "."));
                double critico = Double.parseDouble(criticoField.getText().trim().replace(",", "."));

                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Nome da regra é obrigatório.",
                            "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (limite <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Limite padrão deve ser maior que zero.",
                            "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                AppContext.get().regrasConsumo().criar(nome, descricao, limite, alerta, critico);
                JOptionPane.showMessageDialog(this,
                        "Regra criada com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarRegras();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Valores numéricos inválidos.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao criar regra:\n" + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarRegra() {
        String id = getRegraIdSelecionada();
        if (id == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma regra para editar.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            RegraConsumoGlobal regra = AppContext.get().regrasConsumo().buscarPorId(id);
            if (regra == null) {
                JOptionPane.showMessageDialog(this,
                        "Regra não encontrada.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTextField nomeField = new JTextField(regra.getNome());
            JTextArea descricaoArea = new JTextArea(regra.getDescricao(), 3, 20);
            JScrollPane descScroll = new JScrollPane(descricaoArea);
            JTextField limiteField = new JTextField(String.valueOf(regra.getValorLimitePadrao()));
            JTextField alertaField = new JTextField(String.valueOf(regra.getPercentualAlerta()));
            JTextField criticoField = new JTextField(String.valueOf(regra.getPercentualCritico()));

            JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
            panel.add(new JLabel("Nome da Regra:"));
            panel.add(nomeField);
            panel.add(new JLabel("Descrição:"));
            panel.add(descScroll);
            panel.add(new JLabel("Limite Padrão (R$):"));
            panel.add(limiteField);
            panel.add(new JLabel("Percentual Alerta (%):"));
            panel.add(alertaField);
            panel.add(new JLabel("Percentual Crítico (%):"));
            panel.add(criticoField);

            int op = JOptionPane.showConfirmDialog(this, panel,
                    "Editar Regra de Consumo Global", JOptionPane.OK_CANCEL_OPTION);

            if (op == JOptionPane.OK_OPTION) {
                regra.setNome(nomeField.getText().trim());
                regra.setDescricao(descricaoArea.getText().trim());
                regra.setValorLimitePadrao(Double.parseDouble(limiteField.getText().trim().replace(",", ".")));
                regra.setPercentualAlerta(Double.parseDouble(alertaField.getText().trim().replace(",", ".")));
                regra.setPercentualCritico(Double.parseDouble(criticoField.getText().trim().replace(",", ".")));

                boolean sucesso = AppContext.get().regrasConsumo().atualizar(regra);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                            "Regra atualizada com sucesso!",
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarRegras();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao atualizar regra.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Valores numéricos inválidos.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao editar regra:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ativarRegra() {
        String id = getRegraIdSelecionada();
        if (id == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma regra para ativar.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean sucesso = AppContext.get().regrasConsumo().ativarRegra(id);
            if (sucesso) {
                JOptionPane.showMessageDialog(this,
                        "Regra ativada com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarRegras();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao ativar regra.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao ativar regra:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerRegra() {
        String id = getRegraIdSelecionada();
        if (id == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma regra para remover.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            RegraConsumoGlobal regra = AppContext.get().regrasConsumo().buscarPorId(id);
            if (regra == null) {
                JOptionPane.showMessageDialog(this,
                        "Regra não encontrada.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja remover a regra:\n" +
                            "Nome: " + regra.getNome() + "?",
                    "Confirmar Remoção",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = AppContext.get().regrasConsumo().remover(id);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                            "Regra removida com sucesso!",
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarRegras();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao remover regra.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao remover regra:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

