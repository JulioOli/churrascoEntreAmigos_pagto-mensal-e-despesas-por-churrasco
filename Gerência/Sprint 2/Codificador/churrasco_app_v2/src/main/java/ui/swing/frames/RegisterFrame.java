package ui.swing.frames;

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

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Atenção", JOptionPane.WARNING_MESSAGE);
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