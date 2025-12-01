package ui.swing.frames;

import javax.swing.*;
import java.awt.*;

import com.churrascoapp.model.Usuario;
import com.churrascoapp.utils.AdminUtils;
import ui.swing.panels.CatalogoPanel;
import ui.swing.panels.EventosPanel;
import ui.swing.panels.PrestacaoPanel;
import ui.swing.panels.MeusConvitesPanel;
import ui.swing.panels.MinhasParticipacoesPanel;
import ui.swing.panels.ParticipantesPanel;
import ui.swing.panels.GerenciarUsuariosPanel;
import ui.swing.panels.RegrasConsumoPanel;
import ui.swing.panels.CarrinhoPanel;
import ui.swing.panels.RelatoriosPanel;

public class MainFrame extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);

    private final Usuario usuarioLogado;

    public MainFrame(Usuario usuario) {
        super("Churrasco Entre Amigos");
        this.usuarioLogado = usuario;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // MENU LATERAL
        JPanel left = new JPanel(new GridLayout(0, 1, 4, 4));
        left.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton dashBtn  = new JButton("Dashboard");
        JButton catBtn   = new JButton("Catálogo");
        JButton evtBtn   = new JButton("Eventos");
        JButton convBtn  = new JButton("Meus Convites");
        JButton partBtn  = new JButton("Minhas Participações");
        JButton participantesBtn = new JButton("Participantes");
        JButton compBtn  = new JButton("Compras / Prestação");
        JButton carrinhoBtn = new JButton("Carrinho");
        JButton relatoriosBtn = new JButton("Relatórios");
        
        // Botões apenas para administradores
        JButton gerenciarUsuariosBtn = null;
        JButton regrasConsumoBtn = null;
        
        if (AdminUtils.isAdmin(usuarioLogado)) {
            gerenciarUsuariosBtn = new JButton("Gerenciar Usuários");
            regrasConsumoBtn = new JButton("Regras de Consumo");
        }
        
        JButton sairBtn  = new JButton("Sair");

        left.add(dashBtn);
        left.add(catBtn);
        left.add(evtBtn);
        left.add(convBtn);
        left.add(partBtn);
        left.add(participantesBtn);
        left.add(compBtn);
        left.add(carrinhoBtn);
        left.add(relatoriosBtn);
        
        // Adiciona botões de admin se for administrador
        if (AdminUtils.isAdmin(usuarioLogado)) {
            left.add(Box.createVerticalStrut(10)); // Separador visual
            left.add(new JLabel("Administração:"));
            left.add(gerenciarUsuariosBtn);
            left.add(regrasConsumoBtn);
        }
        
        left.add(Box.createVerticalGlue());
        left.add(sairBtn);

        // ÁREA PRINCIPAL
        JPanel dash = new JPanel(new BorderLayout());
        dash.add(new JLabel(
                "Bem-vindo, " + usuario.getNome() + " (" + usuario.getEmail() + ")",
                SwingConstants.CENTER), BorderLayout.CENTER);

        content.add(dash, "DASH");
        content.add(new CatalogoPanel(), "CAT");
        content.add(new EventosPanel(usuarioLogado), "EVT");
        content.add(new MeusConvitesPanel(usuarioLogado), "CONV");
        content.add(new MinhasParticipacoesPanel(usuarioLogado), "PART");
        content.add(new ParticipantesPanel(usuarioLogado), "PARTICIPANTES");
        content.add(new PrestacaoPanel(usuarioLogado), "COMP");
        content.add(new CarrinhoPanel(usuarioLogado), "CARRINHO");
        content.add(new RelatoriosPanel(usuarioLogado), "RELATORIOS");
        
        // Adiciona painéis de admin se for administrador
        if (AdminUtils.isAdmin(usuarioLogado)) {
            content.add(new GerenciarUsuariosPanel(usuarioLogado), "GERENCIAR_USUARIOS");
            content.add(new RegrasConsumoPanel(usuarioLogado), "REGRAS_CONSUMO");
        }

        add(left, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        // EVENTOS DE CLIQUE
        dashBtn.addActionListener(e -> cards.show(content, "DASH"));
        catBtn.addActionListener(e -> cards.show(content, "CAT"));
        evtBtn.addActionListener(e -> cards.show(content, "EVT"));
        convBtn.addActionListener(e -> cards.show(content, "CONV"));
        partBtn.addActionListener(e -> cards.show(content, "PART"));
        participantesBtn.addActionListener(e -> cards.show(content, "PARTICIPANTES"));
        compBtn.addActionListener(e -> cards.show(content, "COMP"));
        carrinhoBtn.addActionListener(e -> cards.show(content, "CARRINHO"));
        relatoriosBtn.addActionListener(e -> cards.show(content, "RELATORIOS"));
        
        // Eventos de clique para botões de admin
        if (AdminUtils.isAdmin(usuarioLogado)) {
            gerenciarUsuariosBtn.addActionListener(e -> cards.show(content, "GERENCIAR_USUARIOS"));
            regrasConsumoBtn.addActionListener(e -> cards.show(content, "REGRAS_CONSUMO"));
        }

        sairBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        cards.show(content, "DASH");
    }
}
