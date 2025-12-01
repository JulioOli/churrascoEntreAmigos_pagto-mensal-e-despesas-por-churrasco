package ui.swing.frames;

import javax.swing.*;
import java.awt.*;
import ui.swing.panels.CatalogoPanel;
import ui.swing.panels.EventosPanel;
import ui.swing.panels.PrestacaoPanel;

public class MainFrame extends JFrame {
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final String usuarioEmail;

    public MainFrame(String usuarioEmail) {
        super("Churrasco App");
        this.usuarioEmail = usuarioEmail;
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Lateral
        JPanel left = new JPanel();
        left.setLayout(new GridLayout(0,1,8,8));
        left.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JButton dashBtn    = new JButton("Dashboard");
        JButton catBtn     = new JButton("Catálogo");
        JButton evtBtn     = new JButton("Eventos");
        JButton compBtn    = new JButton("Compras / Prestação");
        JButton sairBtn    = new JButton("Sair");

        left.add(dashBtn);
        left.add(catBtn);
        left.add(evtBtn);
        left.add(compBtn);
        left.add(Box.createVerticalGlue());
        left.add(sairBtn);

        // Conteúdo
        JPanel dash = new JPanel(new BorderLayout());
        dash.add(new JLabel("Bem-vindo, " + usuarioEmail, SwingConstants.CENTER), BorderLayout.CENTER);

        CatalogoPanel catalogoPanel = new CatalogoPanel(); // atualizado abaixo
        EventosPanel eventosPanel   = new EventosPanel();  // já existente no seu projeto
        PrestacaoPanel prestacao    = new PrestacaoPanel(); // já existente no seu projeto

        content.add(dash, "DASH");
        content.add(catalogoPanel, "CAT");
        content.add(eventosPanel, "EVT");
        content.add(prestacao, "COMP");

        add(left, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        dashBtn.addActionListener(e -> cards.show(content, "DASH"));
        catBtn.addActionListener(e -> cards.show(content, "CAT"));
        evtBtn.addActionListener(e -> cards.show(content, "EVT"));
        compBtn.addActionListener(e -> cards.show(content, "COMP"));
        sairBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        cards.show(content, "DASH");
    }
}
