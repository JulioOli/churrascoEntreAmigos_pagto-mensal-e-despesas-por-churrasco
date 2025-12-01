package ui.swing.frames;

import com.churrascoapp.utils.ValidadorCampos;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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

    private com.churrascoapp.model.Churrasco churrascoExistente;
    private com.churrascoapp.model.Usuario usuarioCriador;

    public NovoChurrascoFrame(com.churrascoapp.model.Usuario usuario) {
        super("Novo Churrasco");
        this.churrascoExistente = null;
        this.usuarioCriador = usuario;
        init();
    }

    public NovoChurrascoFrame(com.churrascoapp.model.Churrasco churrasco, com.churrascoapp.model.Usuario usuario) {
        super("Editar Churrasco");
        this.churrascoExistente = churrasco;
        this.usuarioCriador = usuario;
        init();
        preencherCampos();
    }

    private void preencherCampos() {
        if (churrascoExistente != null) {
            tituloField.setText(churrascoExistente.getTitulo() != null ? churrascoExistente.getTitulo() : "");
            dataField.setText(churrascoExistente.getData() != null ? churrascoExistente.getData() : "");
            horaField.setText(churrascoExistente.getHora() != null ? churrascoExistente.getHora() : "");
            localField.setText(churrascoExistente.getLocal() != null ? churrascoExistente.getLocal() : "");
            pixField.setText(churrascoExistente.getPix() != null ? churrascoExistente.getPix() : "");
            precoField.setText(String.valueOf(churrascoExistente.getPrecoParticipante()));
            descricaoArea.setText(churrascoExistente.getDescricao() != null ? churrascoExistente.getDescricao() : "");
            
            // Seleciona o tipo no combo
            String tipo = churrascoExistente.getTipo();
            if (tipo != null) {
                for (int i = 0; i < tipoCombo.getItemCount(); i++) {
                    if (tipoCombo.getItemAt(i).equals(tipo)) {
                        tipoCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
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

        // Validações
        if (!ValidadorCampos.validarCampoObrigatorio(titulo)) {
            JOptionPane.showMessageDialog(this,
                    "Título é obrigatório.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            tituloField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarTamanho(titulo, 3, 100)) {
            JOptionPane.showMessageDialog(this,
                    "Título deve ter entre 3 e 100 caracteres.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            tituloField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarCampoObrigatorio(dataStr)) {
            JOptionPane.showMessageDialog(this,
                    "Data é obrigatória.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            dataField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarData(dataStr)) {
            JOptionPane.showMessageDialog(this,
                    "Data inválida. Use o formato AAAA-MM-DD (ex: 2025-12-25).",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            dataField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarDataFutura(dataStr)) {
            JOptionPane.showMessageDialog(this,
                    "A data do evento não pode ser no passado.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            dataField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarCampoObrigatorio(horaStr)) {
            JOptionPane.showMessageDialog(this,
                    "Hora é obrigatória.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            horaField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarHora(horaStr)) {
            JOptionPane.showMessageDialog(this,
                    "Hora inválida. Use o formato HH:MM (ex: 14:30).",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            horaField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarCampoObrigatorio(local)) {
            JOptionPane.showMessageDialog(this,
                    "Local é obrigatório.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            localField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarTamanho(local, 3, 200)) {
            JOptionPane.showMessageDialog(this,
                    "Local deve ter entre 3 e 200 caracteres.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            localField.requestFocus();
            return;
        }

        double preco = 0.0;
        try {
            String precoStr = precoField.getText().trim().replace(",", ".");
            if (precoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Preço é obrigatório.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                precoField.requestFocus();
                return;
            }
            preco = Double.parseDouble(precoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Preço inválido. Use apenas números (ex: 50.00 ou 50,00).",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            precoField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarValorPositivo(preco)) {
            JOptionPane.showMessageDialog(this,
                    "Preço deve ser maior que zero.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            precoField.requestFocus();
            return;
        }

        try {
            if (churrascoExistente != null) {
                // Edição
                churrascoExistente.setTitulo(titulo);
                churrascoExistente.setData(dataStr);
                churrascoExistente.setHora(horaStr);
                churrascoExistente.setLocal(local);
                churrascoExistente.setTipo(tipo);
                churrascoExistente.setPix(pix);
                churrascoExistente.setPrecoParticipante(preco);
                churrascoExistente.setDescricao(descricao);

                boolean sucesso = com.churrascoapp.app.AppContext.get()
                        .churrascos()
                        .atualizar(churrascoExistente);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                            "Churrasco atualizado com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao atualizar churrasco.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                // Criação
                java.util.UUID criadorId = usuarioCriador != null && usuarioCriador.getId() != null 
                        ? usuarioCriador.getId() 
                        : null;
                var novoChurrasco = com.churrascoapp.app.AppContext.get()
                        .churrascos()
                        .criar(
                                criadorId,
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
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
