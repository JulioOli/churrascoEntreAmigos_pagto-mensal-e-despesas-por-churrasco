package ui.swing.frames;

import javax.swing.*;
import java.awt.*;

import com.churrascoapp.app.AppContext;
import com.churrascoapp.model.Usuario;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginBtn;

    public LoginFrame() {
        super("Churrasco App - Login");
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,220);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(3,2,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        p.add(new JLabel("E-mail:"));
        emailField = new JTextField();
        p.add(emailField);

        p.add(new JLabel("Senha:"));
        passwordField = new JPasswordField();
        p.add(passwordField);

        loginBtn = new JButton("Entrar");
        JButton registerBtn = new JButton("Cadastrar");

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.add(registerBtn);
        bp.add(loginBtn);

        add(p, BorderLayout.CENTER);
        add(bp, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> doLogin());
        registerBtn.addActionListener(e -> doRegister());
    }

    private void doLogin() {
        String email = emailField.getText().trim();
        String senha = new String(passwordField.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Informe e-mail e senha.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuario = AppContext.get()
                .usuarios()
                .authenticate(email, senha);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this,
                    "Credenciais inválidas.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        MainFrame mf = new MainFrame(usuario);
        mf.setVisible(true);
        this.dispose();
    }

    private void doRegister() {
        RegisterFrame rf = new RegisterFrame();
        rf.setVisible(true);
    }
}
