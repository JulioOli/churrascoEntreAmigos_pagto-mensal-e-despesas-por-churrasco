package ui.swing.frames;

import com.churrascoapp.utils.ValidadorCampos;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterFrame extends JFrame {

    private JTextField nomeField;
    private JTextField emailField;
    private JPasswordField senhaField;
    private JComboBox<String> tipoCombo;
    private JButton salvarBtn;
    private JButton cancelarBtn;

    public RegisterFrame() {
        super("Cadastro de Usuário");
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        form.add(new JLabel("Nome completo:"));
        nomeField = new JTextField();
        form.add(nomeField);

        form.add(new JLabel("E-mail:"));
        emailField = new JTextField();
        form.add(emailField);

        form.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        form.add(senhaField);

        form.add(new JLabel("Tipo de usuário:"));
        tipoCombo = new JComboBox<>(new String[]{"Usuário", "Administrador"});
        form.add(tipoCombo);

        salvarBtn = new JButton("Salvar");
        cancelarBtn = new JButton("Cancelar");

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(cancelarBtn);
        botoes.add(salvarBtn);

        add(form, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        salvarBtn.addActionListener(e -> salvarUsuario());
        cancelarBtn.addActionListener(e -> dispose());
    }

    private void salvarUsuario() {
        String nome = nomeField.getText().trim();
        String email = emailField.getText().trim();
        String senha = new String(senhaField.getPassword());
        String tipo = (String) tipoCombo.getSelectedItem();

        // Validações
        if (!ValidadorCampos.validarCampoObrigatorio(nome)) {
            JOptionPane.showMessageDialog(this,
                    "Nome é obrigatório.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            nomeField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarTamanho(nome, 3, 100)) {
            JOptionPane.showMessageDialog(this,
                    "Nome deve ter entre 3 e 100 caracteres.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            nomeField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarCampoObrigatorio(email)) {
            JOptionPane.showMessageDialog(this,
                    "E-mail é obrigatório.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarEmail(email)) {
            JOptionPane.showMessageDialog(this,
                    "E-mail inválido. Use o formato: exemplo@dominio.com",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarCampoObrigatorio(senha)) {
            JOptionPane.showMessageDialog(this,
                    "Senha é obrigatória.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            senhaField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarTamanho(senha, 6, 50)) {
            JOptionPane.showMessageDialog(this,
                    "Senha deve ter entre 6 e 50 caracteres.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            senhaField.requestFocus();
            return;
        }

        try {
            // CHAMADA CORRETA (agora por PARÂMETROS, não objeto)
            var novoUsuario = com.churrascoapp.app.AppContext.get()
                    .usuarios()
                    .cadastrar(nome, email, senha, tipo);

            JOptionPane.showMessageDialog(this,
                    "Usuário cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


}