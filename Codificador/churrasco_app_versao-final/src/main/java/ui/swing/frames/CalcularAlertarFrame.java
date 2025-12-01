package ui.swing.frames;

import com.churrascoapp.app.AppContext;
import com.churrascoapp.controller.AlertaConsumoController;
import com.churrascoapp.controller.PrestacaoContaController;
import com.churrascoapp.model.AlertaConsumo;
import com.churrascoapp.model.PrestacaoConta;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class CalcularAlertarFrame extends JFrame {

    private final String churrascoId;
    private final AlertaConsumoController alertaController;
    private final PrestacaoContaController prestacaoController;

    private JTextField limiteField;
    private JTextArea resultadoArea;

    public CalcularAlertarFrame(String churrascoId) {
        super("Calcular & Alertar Consumo");
        this.churrascoId = churrascoId;
        this.alertaController = AppContext.get().alertas();
        this.prestacaoController = AppContext.get().prestacoes();

        setSize(700, 500);
        setLayout(new BorderLayout(8, 8));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        construirUI();
    }

    private void construirUI() {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Parâmetros"));

        inputPanel.add(new JLabel("Limite por participante (R$):"));
        limiteField = new JTextField("50.00", 10);
        inputPanel.add(limiteField);

        JButton calcularBtn = new JButton("Gerar Alertas");
        inputPanel.add(calcularBtn);

        add(inputPanel, BorderLayout.NORTH);

        resultadoArea = new JTextArea(18, 60);
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(resultadoArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultado"));
        add(scroll, BorderLayout.CENTER);

        JButton fecharBtn = new JButton("Fechar");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(fecharBtn);
        add(bottom, BorderLayout.SOUTH);

        calcularBtn.addActionListener(e -> calcular());
        fecharBtn.addActionListener(e -> dispose());
    }

    private void calcular() {
        double limite;
        try {
            limite = Double.parseDouble(limiteField.getText().trim().replace(",", "."));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Limite inválido.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DecimalFormat df = new DecimalFormat("#0.00");
        StringBuilder sb = new StringBuilder();

        try {
            PrestacaoConta pc = prestacaoController.gerarPrestacao(churrascoId);

            sb.append("Churrasco: ").append(churrascoId).append("\n");
            if (pc != null) {
                sb.append("Total arrecadado: R$ ").append(df.format(pc.getTotalArrecadado())).append("\n");
                sb.append("Total gasto:      R$ ").append(df.format(pc.getTotalGasto())).append("\n");
                sb.append("Saldo:           R$ ").append(df.format(pc.getSaldo())).append("\n");
                sb.append("Status prestação: ").append(pc.getStatus()).append("\n\n");
            }

            sb.append("Limite por participante: R$ ").append(df.format(limite)).append("\n\n");

            List<AlertaConsumo> alertas = alertaController.gerarAlertas(churrascoId, limite);
            if (alertas == null || alertas.isEmpty()) {
                sb.append("Nenhum alerta gerado. Consumo estimado dentro do limite.\n");
            } else {
                sb.append("Alertas gerados:\n");
                sb.append("==============================================\n");
                for (AlertaConsumo a : alertas) {
                    sb.append("Usuário: ").append(a.getUsuarioId()).append("\n");
                    sb.append("Tipo:    ").append(a.getTipo()).append("\n");
                    sb.append("Valor atual: R$ ").append(df.format(a.getValorAtual())).append("\n");
                    sb.append("Limite:      R$ ").append(df.format(a.getValorLimite())).append("\n");
                    sb.append("Mensagem: ").append(a.getMensagem()).append("\n");
                    sb.append("Data: ").append(a.getData()).append("\n");
                    sb.append("----------------------------------------------\n");
                }
            }

            resultadoArea.setText(sb.toString());
            resultadoArea.setCaretPosition(0);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao calcular alertas:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}