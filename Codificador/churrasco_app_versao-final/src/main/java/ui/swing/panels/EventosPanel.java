package ui.swing.panels;

import com.churrascoapp.app.AppContext;
import com.churrascoapp.controller.ConviteController;
import com.churrascoapp.controller.EventoController;
import com.churrascoapp.controller.ParticipacaoController;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.model.Convite;
import com.churrascoapp.model.Usuario;
import com.churrascoapp.service.UsuarioService;
import ui.swing.frames.NovoChurrascoFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class EventosPanel extends JPanel {

    private final Usuario usuario;
    private final EventoController eventoController;
    private final ConviteController conviteController;
    private final ParticipacaoController participacaoController;
    private final UsuarioService usuarioService;

    private final JTable table;
    private final DefaultTableModel model;

    public EventosPanel(Usuario usuario) {
        this.usuario = usuario;
        this.eventoController = AppContext.get().eventos();
        this.conviteController = AppContext.get().convites();
        this.participacaoController = AppContext.get().participacoes();
        this.usuarioService = AppContext.get().usuarioService();

        setLayout(new BorderLayout(8, 8));

        // TOPO
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton novoBtn      = new JButton("Novo Churrasco");
        JButton atualizarBtn = new JButton("Atualizar Lista");
        JButton meusChurrascosBtn = new JButton("Meus Churrascos");
        JButton todosChurrascosBtn = new JButton("Todos os Churrascos");
        JButton detalhesBtn  = new JButton("Detalhes");
        JButton editarBtn    = new JButton("Editar");
        JButton excluirBtn   = new JButton("Excluir");
        JButton inscreverBtn = new JButton("Inscrever-se");
        JButton convidarBtn  = new JButton("Convidar Participante");
        JButton verConvitesBtn = new JButton("Convites do Churrasco");

        top.add(novoBtn);
        top.add(atualizarBtn);
        top.add(meusChurrascosBtn);
        top.add(todosChurrascosBtn);
        top.add(detalhesBtn);
        top.add(editarBtn);
        top.add(excluirBtn);
        top.add(inscreverBtn);
        top.add(convidarBtn);
        top.add(verConvitesBtn);

        add(top, BorderLayout.NORTH);

        String[] cols = {"ID", "Título", "Data", "Hora", "Local", "Tipo", "Preço (R$)"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        atualizarBtn.addActionListener(e -> carregarEventos());
        novoBtn.addActionListener(e -> abrirNovoChurrasco());
        meusChurrascosBtn.addActionListener(e -> carregarMeusChurrascos());
        todosChurrascosBtn.addActionListener(e -> carregarEventos());
        detalhesBtn.addActionListener(e -> mostrarDetalhesSelecionado());
        editarBtn.addActionListener(e -> editarChurrasco());
        excluirBtn.addActionListener(e -> excluirChurrasco());
        inscreverBtn.addActionListener(e -> inscreverSe());
        convidarBtn.addActionListener(e -> convidarParticipante());
        verConvitesBtn.addActionListener(e -> listarConvitesDoChurrasco());

        carregarEventos();
    }

    private void carregarEventos() {
        model.setRowCount(0);
        try {
            List<Churrasco> lista = eventoController.listar();
            for (Churrasco c : lista) {
                Object[] row = {
                        c.getId() != null ? c.getId().toString() : "",
                        c.getTitulo(),
                        c.getData(),
                        c.getHora(),
                        c.getLocal(),
                        c.getTipo(),
                        c.getPrecoParticipante()
                };
                model.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar eventos:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarMeusChurrascos() {
        model.setRowCount(0);
        
        if (usuario.getId() == null) {
            JOptionPane.showMessageDialog(this,
                    "Seu usuário não possui ID cadastrado.\n" +
                            "Não é possível filtrar seus churrascos.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<Churrasco> meusChurrascos = com.churrascoapp.app.AppContext.get()
                    .churrascos()
                    .listarPorCriador(usuario.getId());
            
            if (meusChurrascos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Você ainda não criou nenhum churrasco.",
                        "Meus Churrascos", JOptionPane.INFORMATION_MESSAGE);
            }
            
            for (Churrasco c : meusChurrascos) {
                Object[] row = {
                        c.getId() != null ? c.getId().toString() : "",
                        c.getTitulo(),
                        c.getData(),
                        c.getHora(),
                        c.getLocal(),
                        c.getTipo(),
                        c.getPrecoParticipante()
                };
                model.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar meus churrascos:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getChurrascoIdSelecionado() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um churrasco.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (String) model.getValueAt(row, 0);
    }

    private void abrirNovoChurrasco() {
        NovoChurrascoFrame frame = new NovoChurrascoFrame(usuario);
        frame.setLocationRelativeTo(this);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                carregarEventos();
            }
        });
        frame.setVisible(true);
    }

    private void mostrarDetalhesSelecionado() {
        String id = getChurrascoIdSelecionado();
        if (id == null) return;

        try {
            Churrasco evento = eventoController.buscar(id);
            if (evento == null) {
                JOptionPane.showMessageDialog(this, "Evento não encontrado.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder detalhes = new StringBuilder();
            detalhes.append("🎉 ").append(evento.getTitulo()).append("\n\n");
            detalhes.append("📅 Data: ").append(
                    evento.getData() != null ? evento.getData() : "-").append("\n");
            detalhes.append("⏰ Hora: ").append(
                    evento.getHora() != null ? evento.getHora() : "-").append("\n");
            detalhes.append("📍 Local: ").append(
                    evento.getLocal() != null ? evento.getLocal() : "-").append("\n");
            detalhes.append("📌 Tipo: ").append(
                    evento.getTipo() != null ? evento.getTipo() : "-").append("\n");
            detalhes.append("💰 Preço p/ participante: R$ ").append(evento.getPrecoParticipante()).append("\n");
            if (evento.getDescricao() != null && !evento.getDescricao().isEmpty()) {
                detalhes.append("\n📝 Descrição:\n").append(evento.getDescricao()).append("\n");
            }
            detalhes.append("\n🆔 ID: ").append(evento.getId());

            JOptionPane.showMessageDialog(this, detalhes.toString(),
                    "Detalhes do Churrasco", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao buscar detalhes:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void convidarParticipante() {
        String churrascoId = getChurrascoIdSelecionado();
        if (churrascoId == null) return;

        JTextField nomeOuEmailField = new JTextField();
        JTextArea mensagemArea = new JTextArea(4, 20);
        JScrollPane scroll = new JScrollPane(mensagemArea);

        JPanel panel = new JPanel(new BorderLayout(5,5));

        JPanel linha1 = new JPanel(new BorderLayout(5,5));
        linha1.add(new JLabel("Nome ou e-mail do convidado:"), BorderLayout.NORTH);
        linha1.add(nomeOuEmailField, BorderLayout.CENTER);

        panel.add(linha1, BorderLayout.NORTH);
        panel.add(new JLabel("Mensagem (opcional):"), BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);

        int op = JOptionPane.showConfirmDialog(this, panel,
                "Enviar Convite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (op != JOptionPane.OK_OPTION) return;

        String termo = nomeOuEmailField.getText().trim();
        String msg = mensagemArea.getText().trim();

        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Informe o nome ou e-mail do convidado.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Procura usuário por nome OU e-mail
            Usuario convidado = usuarioService.buscarPorNomeOuEmail(termo);
            if (convidado == null) {
                JOptionPane.showMessageDialog(this,
                        "Nenhum usuário encontrado com esse nome/e-mail.",
                        "Não encontrado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (convidado.getId() == null) {
                JOptionPane.showMessageDialog(this,
                        "O usuário encontrado não possui ID cadastrado.\n" +
                                "Verifique o cadastro desse usuário.",
                        "Erro de cadastro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Só pra confirmar para o organizador
            int conf = JOptionPane.showConfirmDialog(this,
                    "Convidar este usuário?\n\n" +
                            "Nome:  " + convidado.getNome() + "\n" +
                            "E-mail:" + convidado.getEmail(),
                    "Confirmar convite",
                    JOptionPane.OK_CANCEL_OPTION);
            if (conf != JOptionPane.OK_OPTION) return;

            String usuarioId = convidado.getId().toString();
            conviteController.enviarConvite(churrascoId, usuarioId, msg);

            JOptionPane.showMessageDialog(this,
                    "Convite enviado para " + convidado.getNome() + "!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao enviar convite:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarChurrasco() {
        String id = getChurrascoIdSelecionado();
        if (id == null) return;

        try {
            Churrasco churrasco = eventoController.buscar(id);
            if (churrasco == null) {
                JOptionPane.showMessageDialog(this,
                        "Churrasco não encontrado.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ui.swing.frames.NovoChurrascoFrame frame = new ui.swing.frames.NovoChurrascoFrame(churrasco, usuario);
            frame.setLocationRelativeTo(this);
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    carregarEventos();
                }
            });
            frame.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao editar churrasco:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirChurrasco() {
        String id = getChurrascoIdSelecionado();
        if (id == null) return;

        try {
            Churrasco churrasco = eventoController.buscar(id);
            if (churrasco == null) {
                JOptionPane.showMessageDialog(this,
                        "Churrasco não encontrado.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o churrasco?\n\n" +
                            "Título: " + churrasco.getTitulo() + "\n" +
                            "Data: " + churrasco.getData() + "\n\n" +
                            "Esta ação não pode ser desfeita!",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacao != JOptionPane.YES_OPTION) {
                return;
            }

            boolean sucesso = com.churrascoapp.app.AppContext.get()
                    .churrascos()
                    .remover(id);

            if (sucesso) {
                JOptionPane.showMessageDialog(this,
                        "Churrasco excluído com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarEventos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Não foi possível excluir o churrasco.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao excluir churrasco:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inscreverSe() {
        String churrascoId = getChurrascoIdSelecionado();
        if (churrascoId == null) return;

        if (usuario.getId() == null) {
            JOptionPane.showMessageDialog(this,
                    "Seu usuário não possui ID cadastrado.\n" +
                            "Não é possível inscrever-se sem ID.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Churrasco churrasco = eventoController.buscar(churrascoId);
            if (churrasco == null) {
                JOptionPane.showMessageDialog(this,
                        "Churrasco não encontrado.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Deseja inscrever-se no churrasco?\n\n" +
                            "Título: " + churrasco.getTitulo() + "\n" +
                            "Data: " + churrasco.getData() + "\n" +
                            "Preço: R$ " + churrasco.getPrecoParticipante(),
                    "Confirmar Inscrição",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacao != JOptionPane.YES_OPTION) {
                return;
            }

            participacaoController.inscrever(churrascoId, usuario.getId().toString());

            JOptionPane.showMessageDialog(this,
                    "Inscrição realizada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao inscrever-se:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarConvitesDoChurrasco() {
        String churrascoId = getChurrascoIdSelecionado();
        if (churrascoId == null) return;

        try {
            List<Convite> lista = conviteController.listarConvitesPorChurrasco(churrascoId);
            if (lista == null || lista.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nenhum convite para este churrasco.",
                        "Convites", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("Convites deste churrasco:\n\n");
            for (Convite c : lista) {
                sb.append("ID: ").append(c.getId()).append("\n");
                sb.append("Usuário: ").append(c.getUsuarioId()).append("\n");
                sb.append("Status: ").append(c.getStatus()).append("\n");
                sb.append("Enviado em: ").append(c.getDataEnvio()).append("\n");
                if (c.getDataResposta() != null) {
                    sb.append("Resposta em: ").append(c.getDataResposta()).append("\n");
                }
                if (c.getMensagem() != null && !c.getMensagem().isEmpty()) {
                    sb.append("Mensagem: ").append(c.getMensagem()).append("\n");
                }
                sb.append("----------------------------------------\n");
            }

            JTextArea area = new JTextArea(sb.toString(), 20, 60);
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);

            JOptionPane.showMessageDialog(this, scroll,
                    "Convites do Churrasco", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao listar convites:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
