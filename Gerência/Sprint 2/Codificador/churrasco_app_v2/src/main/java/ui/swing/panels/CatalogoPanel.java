package ui.swing.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.churrascoapp.app.AppContext;
import com.churrascoapp.model.ItemCatalogo;

public class CatalogoPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;

    public CatalogoPanel() {
        setLayout(new BorderLayout(8,8));
        add(new JLabel("Catálogo de Itens"), BorderLayout.NORTH);

        String[] cols = {"Categoria","Item","Unidade","Quantidade","Observações"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return c >= 0; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(0,1,6,6));
        right.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        JTextField catField = new JTextField();
        JTextField nomeField = new JTextField();
        JTextField unField = new JTextField();
        JTextField qntField = new JTextField();
        JTextField obsField = new JTextField();

        right.add(new JLabel("Categoria:"));  right.add(catField);
        right.add(new JLabel("Item:"));       right.add(nomeField);
        right.add(new JLabel("Unidade:"));    right.add(unField);
        right.add(new JLabel("Quantidade:")); right.add(qntField);
        right.add(new JLabel("Observações:"));right.add(obsField);

        JButton addBtn = new JButton("Adicionar");
        JButton delBtn = new JButton("Excluir Selecionado");
        JButton saveBtn = new JButton("Salvar CSV");
        right.add(addBtn);
        right.add(delBtn);
        right.add(saveBtn);

        add(right, BorderLayout.EAST);

        // Carregar do CSV via Controller
        reloadFromController();

        addBtn.addActionListener(e -> {
            String cat = catField.getText().trim();
            String nome = nomeField.getText().trim();
            String un = unField.getText().trim();
            String qnt = qntField.getText().trim().replace(",", ".");
            String obs = obsField.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o nome do item.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double qtd = 0.0;
            try { qtd = Double.parseDouble(qnt.isEmpty() ? "0" : qnt); } catch (Exception ex) {}
            model.addRow(new Object[]{cat, nome, un, qtd, obs});
        });

        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Selecione uma linha.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String nome = String.valueOf(model.getValueAt(row, 1));
            AppContext.get().catalogo().removerPorNome(nome);
            model.removeRow(row);
        });

        saveBtn.addActionListener(e -> {
            List<ItemCatalogo> all = new ArrayList<>();
            for (int i=0;i<model.getRowCount();i++) {
                ItemCatalogo it = new ItemCatalogo();
                it.setCategoria(String.valueOf(model.getValueAt(i,0)));
                it.setNome(String.valueOf(model.getValueAt(i,1)));
                it.setUnidade(String.valueOf(model.getValueAt(i,2)));
                try { it.setQuantidade(Double.parseDouble(String.valueOf(model.getValueAt(i,3)).replace(",", "."))); }
                catch (Exception ex) { it.setQuantidade(0.0); }
                it.setObservacao(String.valueOf(model.getValueAt(i,4)));
                all.add(it);
            }
            boolean ok = AppContext.get().catalogo().salvar(all);
            JOptionPane.showMessageDialog(this, ok ? "Salvo com sucesso." : "Falha ao salvar.", ok? "OK":"Erro",
                    ok? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        });
    }

    private void reloadFromController() {
        model.setRowCount(0);
        for (ItemCatalogo it : AppContext.get().catalogo().listar()) {
            model.addRow(new Object[]{it.getCategoria(), it.getNome(), it.getUnidade(), it.getQuantidade(), it.getObservacao()});
        }
    }
}



