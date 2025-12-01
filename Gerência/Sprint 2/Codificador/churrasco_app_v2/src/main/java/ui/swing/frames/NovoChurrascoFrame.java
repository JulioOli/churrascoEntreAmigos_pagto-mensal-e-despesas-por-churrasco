package ui.swing.frames;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class NovoChurrascoFrame extends JFrame {

    private JTextField tituloField;
    private JTextField dataField;
    private JTextField horaField;
    private JTextField localField;
    private JComboBox<String> tipoCombo;
    private JTextField pixField;
    private JTextField precoField;
    private JTextArea descricaoArea;

    private JButton salvarBtn;
    private JButton cancelarBtn;

    public NovoChurrascoFrame() {
        super("Novo Churrasco");
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(8, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        form.add(new JLabel("Título:"));
        tituloField = new JTextField();
        form.add(tituloField);

        form.add(new JLabel("Data (AAAA-MM-DD):"));
        dataField = new JTextField();
        form.add(dataField);

        form.add(new JLabel("Hora (HH:MM):"));
        horaField = new JTextField();
        form.add(horaField);

        form.add(new JLabel("Local:"));
        localField = new JTextField();
        form.add(localField);

        form.add(new JLabel("Tipo do evento:"));
        tipoCombo = new JComboBox<>(new String[]{"Apenas Churrasco", "Refeição Completa"});
        form.add(tipoCombo);

        form.add(new JLabel("Chave PIX:"));
        pixField = new JTextField();
        form.add(pixField);

        form.add(new JLabel("Preço por participante (R$):"));
        precoField = new JTextField();
        form.add(precoField);

        add(form, BorderLayout.CENTER);

        descricaoArea = new JTextArea(4, 30);
        descricaoArea.setBorder(BorderFactory.createTitledBorder("Descrição"));
        add(new JScrollPane(descricaoArea), BorderLayout.SOUTH);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        salvarBtn = new JButton("Salvar");
        cancelarBtn = new JButton("Cancelar");
        botoes.add(cancelarBtn);
        botoes.add(salvarBtn);
        add(botoes, BorderLayout.NORTH);

        salvarBtn.addActionListener(e -> salvarChurrasco());
        cancelarBtn.addActionListener(e -> dispose());
    }

    private void salvarChurrasco() {
        String titulo = tituloField.getText().trim();
        String dataStr = dataField.getText().trim();
        String horaStr = horaField.getText().trim();
        String local = localField.getText().trim();
        String tipo = (String) tipoCombo.getSelectedItem();
        String pix = pixField.getText().trim();
        String descricao = descricaoArea.getText().trim();

        double preco = 0.0;
        try {
            preco = Double.parseDouble(precoField.getText().trim().replace(",", "."));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Preço inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (titulo.isEmpty() || dataStr.isEmpty() || horaStr.isEmpty() || local.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha os campos obrigatórios.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        var e = new com.churrascoapp.model.Churrasco();
        e.setId(java.util.UUID.randomUUID().toString());
        e.setTitulo(titulo);
        e.setData(dataStr);
        e.setHora(horaStr);
        e.setTipo(tipo);
        e.setLocal(local);
        e.setPix(pix);
        e.setPrecoParticipante(preco);
        e.setDescricao(descricao);

        try {
            var novoChurrasco = com.churrascoapp.app.AppContext.get()
                    .eventos()
                    .criar(
                            titulo,
                            dataStr,
                            horaStr,
                            local,
                            tipo,
                            pix,
                            preco,
                            descricao
                    );

            JOptionPane.showMessageDialog(this,
                    "Churrasco salvo com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
