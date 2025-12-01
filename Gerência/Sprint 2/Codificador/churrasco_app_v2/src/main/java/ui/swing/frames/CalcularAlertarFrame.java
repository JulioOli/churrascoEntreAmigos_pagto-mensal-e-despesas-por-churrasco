package ui.swing.frames;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class CalcularAlertarFrame extends JFrame {

    private final double carneAtual;
    private final double bebidaAtual;
    private final double carvaoAtual;
    private final double geloAtual;

    private JTextField participantesField;
    private JTextArea resultadoArea;

    public CalcularAlertarFrame(double carne, double bebida, double carvao, double gelo) {
        super("Cálculo e Alertas - Churrasco App");
        this.carneAtual = carne;
        this.bebidaAtual = bebida;
        this.carvaoAtual = carvao;
        this.geloAtual = gelo;
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(520, 480);
        setLocationRelativeTo(null);

        JLabel titulo = new JLabel("Cálculo e Comparação de Itens", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.add(new JLabel("Número de participantes:"));
        participantesField = new JTextField(8);
        inputPanel.add(participantesField);
        JButton calcularBtn = new JButton("Calcular e Alertar");
        inputPanel.add(calcularBtn);
        add(inputPanel, BorderLayout.PAGE_START);

        resultadoArea = new JTextArea(14, 50);
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultadoArea.setBorder(BorderFactory.createTitledBorder("Resultado"));
        JScrollPane scroll = new JScrollPane(resultadoArea);
        add(scroll, BorderLayout.CENTER);

        JButton fecharBtn = new JButton("Fechar");
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(fecharBtn);
        add(botoes, BorderLayout.SOUTH);

        calcularBtn.addActionListener(e -> calcular());
        fecharBtn.addActionListener(e -> dispose());
    }

    private void calcular() {
        String input = participantesField.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o número de participantes.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int pessoas;
        try {
            pessoas = Integer.parseInt(input);
            if (pessoas <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número inválido de participantes.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DecimalFormat df = new DecimalFormat("#.##");

        // quantidades ideais
        double carneIdeal = Math.ceil((pessoas * 0.4) * 10) / 10.0;
        double bebidaIdeal = Math.ceil((pessoas * 0.6) / 2.0) * 2.0;
        double carvaoIdeal = Math.ceil(pessoas / 5.0);
        double geloIdeal = Math.ceil((pessoas / 10.0) * 5.0);

        // diferença
        double carneFalta = Math.max(0, carneIdeal - carneAtual);
        double bebidaFalta = Math.max(0, bebidaIdeal - bebidaAtual);
        double carvaoFalta = Math.max(0, carvaoIdeal - carvaoAtual);
        double geloFalta = Math.max(0, geloIdeal - geloAtual);

        StringBuilder sb = new StringBuilder();
        sb.append("=== Cálculo e Comparativo ===\n");
        sb.append("Data: ").append(LocalDate.now()).append("\n");
        sb.append("Participantes: ").append(pessoas).append("\n\n");

        sb.append(String.format("%-10s %-12s %-12s %-12s\n", "Item", "Ideal", "Atual", "Falta"));
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("%-10s %-12s %-12s %-12s\n", "Carne", df.format(carneIdeal) + " kg", df.format(carneAtual) + " kg", df.format(carneFalta) + " kg"));
        sb.append(String.format("%-10s %-12s %-12s %-12s\n", "Bebida", df.format(bebidaIdeal) + " L", df.format(bebidaAtual) + " L", df.format(bebidaFalta) + " L"));
        sb.append(String.format("%-10s %-12s %-12s %-12s\n", "Carvão", df.format(carvaoIdeal) + " kg", df.format(carvaoAtual) + " kg", df.format(carvaoFalta) + " kg"));
        sb.append(String.format("%-10s %-12s %-12s %-12s\n\n", "Gelo", df.format(geloIdeal) + " kg", df.format(geloAtual) + " kg", df.format(geloFalta) + " kg"));

        sb.append("=== Alertas ===\n");
        boolean alert = false;

        if (carneFalta > 0) { sb.append("⚠️ Faltam ").append(df.format(carneFalta)).append(" kg de carne.\n"); alert = true; }
        if (bebidaFalta > 0) { sb.append("⚠️ Faltam ").append(df.format(bebidaFalta)).append(" L de bebida.\n"); alert = true; }
        if (carvaoFalta > 0) { sb.append("⚠️ Faltam ").append(df.format(carvaoFalta)).append(" kg de carvão.\n"); alert = true; }
        if (geloFalta > 0) { sb.append("⚠️ Faltam ").append(df.format(geloFalta)).append(" kg de gelo.\n"); alert = true; }

        if (!alert) sb.append("✅ Todos os itens estão na quantidade ideal!\n");

        resultadoArea.setText(sb.toString());
        resultadoArea.setCaretPosition(0);
    }
}


