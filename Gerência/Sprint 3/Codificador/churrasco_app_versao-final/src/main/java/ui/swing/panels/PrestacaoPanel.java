package ui.swing.panels;

import com.churrascoapp.app.AppContext;
import com.churrascoapp.controller.CompraController;
import com.churrascoapp.controller.EventoController;
import com.churrascoapp.controller.PrestacaoContaController;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.model.Compra;
import com.churrascoapp.model.PrestacaoConta;
import com.churrascoapp.model.Usuario;
import ui.swing.frames.CalcularAlertarFrame;
import ui.swing.frames.RegistrarCompraFrame;
import ui.swing.frames.VisualizarComprovanteFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

public class PrestacaoPanel extends JPanel {

    private final Usuario usuario;
    private final CompraController compraController;
    private final PrestacaoContaController prestacaoController;
    private final EventoController eventoController;

    private final JTable table;
    private final DefaultTableModel model;

    public PrestacaoPanel(Usuario usuario) {
        this.usuario = usuario;
        this.compraController = AppContext.get().compras();
        this.prestacaoController = AppContext.get().prestacoes();
        this.eventoController = AppContext.get().eventos();

        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton novaCompraBtn = new JButton("Registrar Compra");
        JButton atualizarBtn  = new JButton("Atualizar Compras");
        JButton visualizarComprovanteBtn = new JButton("Ver Comprovante");
        JButton prestacaoBtn  = new JButton("Gerar Prestação");
        JButton aprovarBtn    = new JButton("Aprovar Prestação");
        JButton rejeitarBtn   = new JButton("Rejeitar Prestação");
        JButton alertasBtn    = new JButton("Calcular / Alertar Consumo");

        top.add(novaCompraBtn);
        top.add(atualizarBtn);
        top.add(visualizarComprovanteBtn);
        top.add(prestacaoBtn);
        top.add(aprovarBtn);
        top.add(rejeitarBtn);
        top.add(alertasBtn);

        add(top, BorderLayout.NORTH);

        String[] cols = {
                "ID", "Churrasco ID", "Item", "Qtd",
                "Valor (R$)", "Fornecedor", "Data", "Comprovante"
        };
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        
        // Adiciona listener para duplo clique na coluna de comprovante
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    if (row >= 0 && col == 7) { // Coluna "Comprovante"
                        visualizarComprovante(row);
                    }
                }
            }
        });
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        novaCompraBtn.addActionListener(e -> abrirRegistrarCompra());
        atualizarBtn.addActionListener(e -> carregarCompras());
        visualizarComprovanteBtn.addActionListener(e -> visualizarComprovanteSelecionado());
        prestacaoBtn.addActionListener(e -> gerarPrestacao());
        aprovarBtn.addActionListener(e -> aprovarPrestacao());
        rejeitarBtn.addActionListener(e -> rejeitarPrestacao());
        alertasBtn.addActionListener(e -> abrirAlertas());

        carregarCompras();
    }

    private void carregarCompras() {
        model.setRowCount(0);
        try {
            List<Compra> lista = compraController.listar();
            for (Compra c : lista) {
                Object[] row = {
                        c.getId() != null ? c.getId().toString() : "",
                        c.getChurrascoId() != null ? c.getChurrascoId().toString() : "",
                        c.getItem(),
                        c.getQuantidade(),
                        c.getValor(),
                        c.getFornecedor(),
                        c.getData(),
                        c.getComprovantePath()
                };
                model.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar compras:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirRegistrarCompra() {
        RegistrarCompraFrame frame = new RegistrarCompraFrame();
        frame.setLocationRelativeTo(this);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                carregarCompras();
            }
        });
        frame.setVisible(true);
    }

    /**
     * Abre um diálogo para o usuário escolher o churrasco
     * a partir da lista de eventos, em vez de digitar o ID.
     */
    private String escolherChurrascoId() {
        try {
            List<Churrasco> eventos = eventoController.listar();
            if (eventos == null || eventos.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não há churrascos cadastrados.",
                        "Nenhum evento",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return null;
            }

            String[] opcoes = new String[eventos.size()];
            for (int i = 0; i < eventos.size(); i++) {
                Churrasco c = eventos.get(i);
                String titulo = c.getTitulo() != null ? c.getTitulo() : "(sem título)";
                String data   = c.getData()   != null ? c.getData()   : "";
                String id     = c.getId()     != null ? c.getId().toString() : "";

                opcoes[i] = titulo + " - " + data + " (ID: " + id + ")";
            }

            Object escolha = JOptionPane.showInputDialog(
                    this,
                    "Selecione o churrasco:",
                    "Selecionar Churrasco",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (escolha == null) {
                return null; // usuário cancelou
            }

            int idx = Arrays.asList(opcoes).indexOf(escolha.toString());
            if (idx < 0) return null;

            Churrasco selecionado = eventos.get(idx);
            if (selecionado.getId() == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "O churrasco selecionado não possui ID cadastrado.",
                        "Erro de cadastro",
                        JOptionPane.ERROR_MESSAGE
                );
                return null;
            }

            return selecionado.getId().toString();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao listar churrascos:\n" + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
    }

    private void gerarPrestacao() {
        String churrascoId = escolherChurrascoId();
        if (churrascoId == null) return;

        try {
            PrestacaoConta pc = prestacaoController.gerarPrestacao(churrascoId);
            if (pc == null) {
                JOptionPane.showMessageDialog(this,
                        "Não foi possível gerar prestação de contas.",
                        "Prestação de Contas", JOptionPane.WARNING_MESSAGE);
                return;
            }

            mostrarPrestacao(pc);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao gerar prestação:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarPrestacao(PrestacaoConta pc) {
        StringBuilder sb = new StringBuilder();
        sb.append("Prestação de Contas\n\n");
        sb.append("Churrasco ID: ").append(pc.getChurrascoId()).append("\n");
        sb.append("Total arrecadado: R$ ").append(String.format("%.2f", pc.getTotalArrecadado())).append("\n");
        sb.append("Total gasto:      R$ ").append(String.format("%.2f", pc.getTotalGasto())).append("\n");
        sb.append("Saldo:           R$ ").append(String.format("%.2f", pc.getSaldo())).append("\n");
        sb.append("Status: ").append(pc.getStatus()).append("\n");
        sb.append("Data:   ").append(pc.getDataPrestacao()).append("\n");
        if (pc.getObservacoes() != null && !pc.getObservacoes().isEmpty()) {
            sb.append("\nObservações:\n").append(pc.getObservacoes());
        }

        JOptionPane.showMessageDialog(this,
                sb.toString(),
                "Prestação de Contas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void aprovarPrestacao() {
        String churrascoId = escolherChurrascoId();
        if (churrascoId == null) return;

        PrestacaoConta pc = prestacaoController.buscarPorChurrasco(churrascoId);
        if (pc == null) {
            JOptionPane.showMessageDialog(this,
                    "Não há prestação de contas gerada para este churrasco.\n" +
                            "Gere a prestação primeiro.",
                    "Prestação não encontrada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("APROVADA".equals(pc.getStatus())) {
            JOptionPane.showMessageDialog(this,
                    "Esta prestação já foi aprovada.",
                    "Atenção", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JTextArea obsArea = new JTextArea(4, 30);
        obsArea.setLineWrap(true);
        obsArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(obsArea);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Observações (opcional):"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        int op = JOptionPane.showConfirmDialog(this, panel,
                "Aprovar Prestação de Contas",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (op != JOptionPane.OK_OPTION) return;

        try {
            boolean sucesso = prestacaoController.aprovarPrestacao(churrascoId, obsArea.getText().trim());
            if (sucesso) {
                JOptionPane.showMessageDialog(this,
                        "Prestação de contas aprovada com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                mostrarPrestacao(prestacaoController.buscarPorChurrasco(churrascoId));
            } else {
                JOptionPane.showMessageDialog(this,
                        "Não foi possível aprovar a prestação.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao aprovar prestação:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rejeitarPrestacao() {
        String churrascoId = escolherChurrascoId();
        if (churrascoId == null) return;

        PrestacaoConta pc = prestacaoController.buscarPorChurrasco(churrascoId);
        if (pc == null) {
            JOptionPane.showMessageDialog(this,
                    "Não há prestação de contas gerada para este churrasco.\n" +
                            "Gere a prestação primeiro.",
                    "Prestação não encontrada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("REJEITADA".equals(pc.getStatus())) {
            JOptionPane.showMessageDialog(this,
                    "Esta prestação já foi rejeitada.",
                    "Atenção", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JTextArea obsArea = new JTextArea(4, 30);
        obsArea.setLineWrap(true);
        obsArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(obsArea);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Motivo da rejeição (obrigatório):"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        int op = JOptionPane.showConfirmDialog(this, panel,
                "Rejeitar Prestação de Contas",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (op != JOptionPane.OK_OPTION) return;

        String motivo = obsArea.getText().trim();
        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "É necessário informar o motivo da rejeição.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean sucesso = prestacaoController.rejeitarPrestacao(churrascoId, motivo);
            if (sucesso) {
                JOptionPane.showMessageDialog(this,
                        "Prestação de contas rejeitada.",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                mostrarPrestacao(prestacaoController.buscarPorChurrasco(churrascoId));
            } else {
                JOptionPane.showMessageDialog(this,
                        "Não foi possível rejeitar a prestação.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao rejeitar prestação:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void visualizarComprovanteSelecionado() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma compra para visualizar o comprovante.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        visualizarComprovante(row);
    }

    private void visualizarComprovante(int row) {
        try {
            String comprovantePath = (String) model.getValueAt(row, 7); // Coluna "Comprovante"
            
            if (comprovantePath == null || comprovantePath.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Esta compra não possui comprovante cadastrado.",
                        "Atenção", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            VisualizarComprovanteFrame frame = new VisualizarComprovanteFrame(comprovantePath);
            frame.setLocationRelativeTo(this);
            frame.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao visualizar comprovante:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirAlertas() {
        String churrascoId = escolherChurrascoId();
        if (churrascoId == null) return;

        CalcularAlertarFrame frame = new CalcularAlertarFrame(churrascoId);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }
}
