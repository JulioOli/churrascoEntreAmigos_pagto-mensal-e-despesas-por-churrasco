package ui.swing.panels;

import ui.swing.frames.NovoChurrascoFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EventosPanel extends JPanel {
    public EventosPanel() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton novo = new JButton("Novo Churrasco");
        top.add(novo);
        add(top, BorderLayout.NORTH);

        // placeholder table
        String[] cols = {"ID","Título","Data","Local","Tipo","Preço p/ pessoa","Ações"};
        Object[][] data = {};
        JTable table = new JTable(data, cols);
        add(new JScrollPane(table), BorderLayout.CENTER);

        novo.addActionListener(e -> {
            NovoChurrascoFrame frame = new NovoChurrascoFrame();
            frame.setVisible(true);
        });

    }
}