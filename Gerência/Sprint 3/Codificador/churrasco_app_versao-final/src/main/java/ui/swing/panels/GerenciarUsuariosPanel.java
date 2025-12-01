package ui.swing.panels;

import com.churrascoapp.app.AppContext;
import com.churrascoapp.model.Usuario;
import com.churrascoapp.utils.AdminUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GerenciarUsuariosPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private final Usuario usuarioAdmin;

    public GerenciarUsuariosPanel(Usuario usuarioAdmin) {
        this.usuarioAdmin = usuarioAdmin;
        
        if (!AdminUtils.isAdmin(usuarioAdmin)) {
            setLayout(new BorderLayout());
            add(new JLabel("Acesso negado. Apenas administradores podem gerenciar usuários.", 
                    SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        setLayout(new BorderLayout(8, 8));

        String[] cols = {"ID", "Nome", "E-mail", "Papel"};
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
        JButton editarBtn = new JButton("Editar Usuário");
        JButton removerBtn = new JButton("Remover Usuário");
        JButton novoBtn = new JButton("Novo Usuário");

        top.add(atualizarBtn);
        top.add(novoBtn);
        top.add(editarBtn);
        top.add(removerBtn);

        add(top, BorderLayout.NORTH);

        atualizarBtn.addActionListener(e -> carregarUsuarios());
        novoBtn.addActionListener(e -> criarNovoUsuario());
        editarBtn.addActionListener(e -> editarUsuario());
        removerBtn.addActionListener(e -> removerUsuario());

        carregarUsuarios();
    }

    private void carregarUsuarios() {
        if (model == null) return;
        model.setRowCount(0);
        try {
            java.util.List<Usuario> usuarios = AppContext.get().usuarios().listarTodos();
            for (Usuario u : usuarios) {
                model.addRow(new Object[]{
                        u.getId() != null ? u.getId().toString() : "",
                        u.getNome(),
                        u.getEmail(),
                        u.getPapel()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar usuários:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getUsuarioIdSelecionado() {
        if (table == null) return null;
        int row = table.getSelectedRow();
        if (row < 0) return null;
        return String.valueOf(model.getValueAt(row, 0));
    }

    private void criarNovoUsuario() {
        ui.swing.frames.RegisterFrame frame = new ui.swing.frames.RegisterFrame();
        frame.setLocationRelativeTo(this);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                carregarUsuarios();
            }
        });
        frame.setVisible(true);
    }

    private void editarUsuario() {
        String id = getUsuarioIdSelecionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usuário para editar.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            java.util.List<Usuario> todos = AppContext.get().usuarios().listarTodos();
            Usuario usuario = null;
            for (Usuario u : todos) {
                if (u.getId() != null && u.getId().toString().equals(id)) {
                    usuario = u;
                    break;
                }
            }

            if (usuario == null) {
                JOptionPane.showMessageDialog(this,
                        "Usuário não encontrado.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Diálogo de edição
            JTextField nomeField = new JTextField(usuario.getNome());
            JTextField emailField = new JTextField(usuario.getEmail());
            JPasswordField senhaField = new JPasswordField();
            JComboBox<String> papelCombo = new JComboBox<>(
                    new String[]{"Usuário", "Administrador"}
            );
            papelCombo.setSelectedItem(usuario.getPapel());

            JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
            panel.add(new JLabel("Nome:"));
            panel.add(nomeField);
            panel.add(new JLabel("E-mail:"));
            panel.add(emailField);
            panel.add(new JLabel("Nova Senha (deixe em branco para manter):"));
            panel.add(senhaField);
            panel.add(new JLabel("Papel:"));
            panel.add(papelCombo);

            int op = JOptionPane.showConfirmDialog(this, panel,
                    "Editar Usuário", JOptionPane.OK_CANCEL_OPTION);

            if (op == JOptionPane.OK_OPTION) {
                usuario.setNome(nomeField.getText().trim());
                usuario.setEmail(emailField.getText().trim());
                String novaSenha = new String(senhaField.getPassword()).trim();
                if (!novaSenha.isEmpty()) {
                    usuario.setSenha(novaSenha);
                }
                usuario.setPapel((String) papelCombo.getSelectedItem());

                boolean sucesso = AppContext.get().usuarios().atualizar(usuario);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                            "Usuário atualizado com sucesso!",
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao atualizar usuário.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao editar usuário:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerUsuario() {
        String id = getUsuarioIdSelecionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usuário para remover.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            java.util.List<Usuario> todos = AppContext.get().usuarios().listarTodos();
            Usuario usuario = null;
            for (Usuario u : todos) {
                if (u.getId() != null && u.getId().toString().equals(id)) {
                    usuario = u;
                    break;
                }
            }

            if (usuario == null) {
                JOptionPane.showMessageDialog(this,
                        "Usuário não encontrado.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Não permite remover a si mesmo
            if (usuario.getId().equals(usuarioAdmin.getId())) {
                JOptionPane.showMessageDialog(this,
                        "Você não pode remover seu próprio usuário.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja remover o usuário:\n" +
                            "Nome: " + usuario.getNome() + "\n" +
                            "E-mail: " + usuario.getEmail() + "?",
                    "Confirmar Remoção",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = AppContext.get().usuarios().remover(id);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                            "Usuário removido com sucesso!",
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao remover usuário.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao remover usuário:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

