package ui.swing.frames;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.churrascoapp.app.AppContext;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.model.Compra;

public class RegistrarCompraFrame extends JFrame {

    private JComboBox<ComboChurrasco> churrascoCombo;
    private JTextField itemField;
    private JTextField quantidadeField;
    private JTextField valorField;
    private JTextField fornecedorField;
    private JTextField dataField;
    private JTextField comprovanteField;

    private JButton selecionarArquivoBtn;
    private JButton salvarBtn;
    private JButton cancelarBtn;

    private File arquivoSelecionado;

    public RegistrarCompraFrame() {
        super("Registrar Compra");
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 460);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Churrasco (carregado do CSV)
        form.add(new JLabel("Churrasco:"));
        churrascoCombo = new JComboBox<>();
        carregarChurrascos();
        form.add(churrascoCombo);

        form.add(new JLabel("Item comprado:"));
        itemField = new JTextField();
        form.add(itemField);

        form.add(new JLabel("Quantidade:"));
        quantidadeField = new JTextField();
        form.add(quantidadeField);

        form.add(new JLabel("Valor total (R$):"));
        valorField = new JTextField();
        form.add(valorField);

        form.add(new JLabel("Fornecedor (opcional):"));
        fornecedorField = new JTextField();
        form.add(fornecedorField);

        form.add(new JLabel("Data da compra (AAAA-MM-DD):"));
        dataField = new JTextField(LocalDate.now().toString());
        form.add(dataField);

        form.add(new JLabel("Comprovante:"));
        JPanel arquivoPanel = new JPanel(new BorderLayout());
        comprovanteField = new JTextField();
        comprovanteField.setEditable(false);
        selecionarArquivoBtn = new JButton("Selecionar...");
        selecionarArquivoBtn.addActionListener(e -> selecionarArquivo());
        arquivoPanel.add(comprovanteField, BorderLayout.CENTER);
        arquivoPanel.add(selecionarArquivoBtn, BorderLayout.EAST);
        form.add(arquivoPanel);

        add(form, BorderLayout.CENTER);

        // Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cancelarBtn = new JButton("Cancelar");
        salvarBtn = new JButton("Salvar");
        botoes.add(cancelarBtn);
        botoes.add(salvarBtn);
        add(botoes, BorderLayout.SOUTH);

        cancelarBtn.addActionListener(e -> dispose());
        salvarBtn.addActionListener(e -> salvarCompra());
    }

    private void carregarChurrascos() {
        DefaultComboBoxModel<ComboChurrasco> model = new DefaultComboBoxModel<>();
        for (Churrasco c : AppContext.get().eventos().listar()) {
            // rótulo agradável no combo (ex.: "Título — 2025-10-12 18:00")
            String rotulo = c.getTitulo();
            if (c.getData() != null && !c.getData().isEmpty()) {
                rotulo += " — " + c.getData();
                if (c.getHora() != null && !c.getHora().isEmpty()) rotulo += " " + c.getHora();
            }
            model.addElement(new ComboChurrasco(c.getId().toString(), rotulo));
        }
        churrascoCombo.setModel(model);
    }

    private void selecionarArquivo() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            arquivoSelecionado = chooser.getSelectedFile();
            comprovanteField.setText(arquivoSelecionado.getAbsolutePath());
        }
    }

    private void salvarCompra() {
        ComboChurrasco sel = (ComboChurrasco) churrascoCombo.getSelectedItem();
        String churrascoId = (sel != null ? sel.id : null);

        String item = itemField.getText().trim();
        String quantidadeStr = quantidadeField.getText().trim();
        String valorStr = valorField.getText().trim();
        String fornecedor = fornecedorField.getText().trim();
        String dataStr = dataField.getText().trim();
        String comprovante = (arquivoSelecionado != null ? arquivoSelecionado.getAbsolutePath() : "");

        if (churrascoId == null || churrascoId.isEmpty() || item.isEmpty() ||
                quantidadeStr.isEmpty() || valorStr.isEmpty() || dataStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha os campos obrigatórios.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int quantidade;
        double valor;
        try {
            quantidade = Integer.parseInt(quantidadeStr);
            valor = Double.parseDouble(valorStr.replace(",", "."));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Quantidade ou valor inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // dentro do salvarCompra():
        Compra c = new Compra();
        c.setId(java.util.UUID.randomUUID());
        c.setChurrascoId(java.util.UUID.fromString(churrascoId));
        c.setItem(item);
        c.setQuantidade(quantidade);
        c.setValor(valor);
        c.setFornecedor(fornecedor);
        c.setData(dataStr);
        c.setComprovantePath(comprovante);

        boolean ok = AppContext.get().compras().registrar(c);


        JOptionPane.showMessageDialog(this,
                ok ? "Compra registrada com sucesso!" : "Erro ao registrar.",
                ok ? "Sucesso" : "Erro",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
        );

        if (ok) dispose();
    }

    // item de combo (id real + texto amigável)
    private static class ComboChurrasco {
        final String id;
        final String label;
        ComboChurrasco(String id, String label) { this.id = id; this.label = label; }
        public String toString() { return label; }
    }
}
