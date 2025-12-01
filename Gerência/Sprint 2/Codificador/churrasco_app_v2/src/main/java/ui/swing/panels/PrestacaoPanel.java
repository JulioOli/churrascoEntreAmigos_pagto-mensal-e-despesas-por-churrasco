package ui.swing.panels;

import ui.swing.frames.RegistrarCompraFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PrestacaoPanel extends JPanel {
    public PrestacaoPanel() {
        setLayout(new BorderLayout(8,8));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton novo = new JButton("Registrar Compra");
        top.add(novo);
        add(top, BorderLayout.NORTH);

        String[] cols = {"ID","Item","Qtd","Valor (R$)","Fornecedor","Comprovante"};
        Object[][] data = {};
        JTable table = new JTable(data, cols);
        add(new JScrollPane(table), BorderLayout.CENTER);

        novo.addActionListener(e -> {
            RegistrarCompraFrame frame = new RegistrarCompraFrame();
            frame.setVisible(true);
        });
    }
}