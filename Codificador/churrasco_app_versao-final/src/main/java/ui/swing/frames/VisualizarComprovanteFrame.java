package ui.swing.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class VisualizarComprovanteFrame extends JFrame {

    public VisualizarComprovanteFrame(String caminhoComprovante) {
        super("Visualizar Comprovante");
        init(caminhoComprovante);
    }

    private void init(String caminhoComprovante) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        if (caminhoComprovante == null || caminhoComprovante.trim().isEmpty()) {
            JLabel mensagem = new JLabel("Nenhum comprovante disponível.", SwingConstants.CENTER);
            mensagem.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
            add(mensagem, BorderLayout.CENTER);
            return;
        }

        File arquivo = new File(caminhoComprovante);
        if (!arquivo.exists()) {
            JLabel mensagem = new JLabel(
                    "<html><center>Arquivo não encontrado:<br>" + caminhoComprovante + "</center></html>",
                    SwingConstants.CENTER
            );
            mensagem.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            add(mensagem, BorderLayout.CENTER);
            return;
        }

        try {
            // Tenta carregar como imagem
            BufferedImage imagem = ImageIO.read(arquivo);
            if (imagem != null) {
                // Redimensiona a imagem se for muito grande
                Image imagemRedimensionada = redimensionarImagem(imagem, 750, 550);
                JLabel labelImagem = new JLabel(new ImageIcon(imagemRedimensionada));
                labelImagem.setHorizontalAlignment(SwingConstants.CENTER);
                
                JScrollPane scrollPane = new JScrollPane(labelImagem);
                scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                add(scrollPane, BorderLayout.CENTER);

                // Informações do arquivo
                JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                infoPanel.add(new JLabel("Arquivo: " + arquivo.getName()));
                infoPanel.add(new JLabel(" | Tamanho: " + formatarTamanho(arquivo.length())));
                add(infoPanel, BorderLayout.SOUTH);
            } else {
                // Se não for imagem, mostra mensagem
                JLabel mensagem = new JLabel(
                        "<html><center>O arquivo não é uma imagem válida ou não pode ser exibido.<br>" +
                        "Caminho: " + caminhoComprovante + "</center></html>",
                        SwingConstants.CENTER
                );
                mensagem.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                add(mensagem, BorderLayout.CENTER);
            }
        } catch (IOException e) {
            JLabel mensagem = new JLabel(
                    "<html><center>Erro ao carregar imagem:<br>" + e.getMessage() + 
                    "<br><br>Caminho: " + caminhoComprovante + "</center></html>",
                    SwingConstants.CENTER
            );
            mensagem.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            add(mensagem, BorderLayout.CENTER);
        }
    }

    private Image redimensionarImagem(BufferedImage imagemOriginal, int larguraMax, int alturaMax) {
        int largura = imagemOriginal.getWidth();
        int altura = imagemOriginal.getHeight();

        // Calcula o fator de escala mantendo a proporção
        double escalaLargura = (double) larguraMax / largura;
        double escalaAltura = (double) alturaMax / altura;
        double escala = Math.min(escalaLargura, escalaAltura);

        // Se a imagem já é menor que o máximo, não redimensiona
        if (escala >= 1.0) {
            return imagemOriginal;
        }

        int novaLargura = (int) (largura * escala);
        int novaAltura = (int) (altura * escala);

        return imagemOriginal.getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);
    }

    private String formatarTamanho(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        }
    }
}

