package ui.swing.panels;

import com.churrascoapp.app.AppContext;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.model.Relatorio;
import com.churrascoapp.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RelatoriosPanel extends JPanel {

    private final Usuario usuario;
    private DefaultTableModel model;
    private JTable table;
    private JComboBox<String> churrascoCombo;
    private JComboBox<String> tipoCombo;
    private JComboBox<String> formatoCombo;

    public RelatoriosPanel(Usuario usuario) {
        this.usuario = usuario;
        init();
    }

    private void init() {
        setLayout(new BorderLayout(8, 8));

        // TOPO - Seleção e geração
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Churrasco:"));
        churrascoCombo = new JComboBox<>();
        top.add(churrascoCombo);
        
        top.add(new JLabel("Tipo:"));
        tipoCombo = new JComboBox<>(new String[]{"FINANCEIRO", "OPERACIONAL"});
        top.add(tipoCombo);
        
        top.add(new JLabel("Formato:"));
        formatoCombo = new JComboBox<>(new String[]{"TXT", "CSV", "PDF"});
        top.add(formatoCombo);
        
        JButton gerarBtn = new JButton("Gerar Relatório");
        JButton visualizarBtn = new JButton("Visualizar Relatório");
        JButton atualizarBtn = new JButton("Atualizar Lista");
        
        top.add(gerarBtn);
        top.add(visualizarBtn);
        top.add(atualizarBtn);

        add(top, BorderLayout.NORTH);

        // CENTRO - Lista de relatórios
        String[] cols = {"ID", "Churrasco", "Tipo", "Formato", "Data Geração"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Eventos
        gerarBtn.addActionListener(e -> gerarRelatorio());
        visualizarBtn.addActionListener(e -> visualizarRelatorio());
        atualizarBtn.addActionListener(e -> atualizarLista());
        churrascoCombo.addActionListener(e -> atualizarLista());

        carregarChurrascos();
        atualizarLista();
    }

    private void carregarChurrascos() {
        churrascoCombo.removeAllItems();
        churrascoCombo.addItem("Todos");
        try {
            List<Churrasco> churrascos = AppContext.get().churrascos().listar();
            for (Churrasco c : churrascos) {
                churrascoCombo.addItem(c.getTitulo() + " (ID: " + c.getId() + ")");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar churrascos: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getSelectedChurrascoId() {
        if (churrascoCombo.getSelectedItem() == null) return null;
        String selected = (String) churrascoCombo.getSelectedItem();
        if ("Todos".equals(selected)) return null;
        int start = selected.indexOf("(ID: ") + 5;
        int end = selected.indexOf(")");
        if (start > 4 && end > start) {
            return selected.substring(start, end);
        }
        return null;
    }

    private void gerarRelatorio() {
        String churrascoId = getSelectedChurrascoId();
        if (churrascoId == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um churrasco para gerar o relatório.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipo = (String) tipoCombo.getSelectedItem();
        String formato = (String) formatoCombo.getSelectedItem();

        try {
            Relatorio relatorio;
            if ("FINANCEIRO".equals(tipo)) {
                relatorio = AppContext.get().relatorios().gerarFinanceiro(churrascoId, formato);
            } else {
                relatorio = AppContext.get().relatorios().gerarOperacional(churrascoId, formato);
            }

            if (relatorio != null) {
                JOptionPane.showMessageDialog(this,
                        "Relatório gerado com sucesso!\n\nID: " + relatorio.getId() +
                        "\nTipo: " + relatorio.getTipoRelatorio() +
                        "\nFormato: " + relatorio.getFormato(),
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarLista();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao gerar relatório.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao gerar relatório: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void visualizarRelatorio() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um relatório para visualizar.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String relatorioId = String.valueOf(model.getValueAt(row, 0));
        try {
            Relatorio relatorio = AppContext.get().relatorios().buscarPorId(relatorioId);
            if (relatorio == null) {
                JOptionPane.showMessageDialog(this,
                        "Relatório não encontrado.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTextArea area = new JTextArea(relatorio.getConteudo(), 30, 80);
            area.setEditable(false);
            area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(900, 600));

            JOptionPane.showMessageDialog(this, scroll,
                    "Relatório " + relatorio.getTipoRelatorio() + " - " + relatorio.getFormato(),
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao visualizar relatório: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarLista() {
        model.setRowCount(0);
        try {
            String churrascoId = getSelectedChurrascoId();
            List<Relatorio> relatorios;
            
            if (churrascoId != null) {
                relatorios = AppContext.get().relatorios().listarPorChurrasco(churrascoId);
            } else {
                // Lista todos os relatórios - busca por cada churrasco
                relatorios = new java.util.ArrayList<>();
                List<Churrasco> churrascos = AppContext.get().churrascos().listar();
                for (Churrasco c : churrascos) {
                    relatorios.addAll(AppContext.get().relatorios().listarPorChurrasco(c.getId().toString()));
                }
            }

            // Busca nome do churrasco para cada relatório
            for (Relatorio r : relatorios) {
                String nomeChurrasco = "ID: " + r.getChurrascoId();
                try {
                    Churrasco c = AppContext.get().churrascos().buscar(r.getChurrascoId().toString());
                    if (c != null) {
                        nomeChurrasco = c.getTitulo();
                    }
                } catch (Exception ignored) {}

                model.addRow(new Object[]{
                        r.getId(),
                        nomeChurrasco,
                        r.getTipoRelatorio(),
                        r.getFormato(),
                        r.getDataGeracao()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar relatórios: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

