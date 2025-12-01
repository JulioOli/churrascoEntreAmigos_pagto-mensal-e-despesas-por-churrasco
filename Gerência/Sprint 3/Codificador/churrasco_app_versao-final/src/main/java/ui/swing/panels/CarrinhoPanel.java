package ui.swing.panels;

import com.churrascoapp.app.AppContext;
import com.churrascoapp.model.*;
import com.churrascoapp.utils.ValidadorCampos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.UUID;

public class CarrinhoPanel extends JPanel {

    private final Usuario usuario;
    private DefaultTableModel modelItens;
    private DefaultTableModel modelAlertas;
    private JTable tableItens;
    private JTable tableAlertas;
    private JComboBox<String> churrascoCombo;
    private JComboBox<String> itemCatalogoCombo;
    private JTextField quantidadeField;
    private JTextField precoField;
    private Carrinho carrinhoAtual;

    public CarrinhoPanel(Usuario usuario) {
        this.usuario = usuario;
        init();
    }

    private void init() {
        setLayout(new BorderLayout(8, 8));

        // TOPO - Seleção de churrasco e criação de carrinho
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Churrasco:"));
        churrascoCombo = new JComboBox<>();
        top.add(churrascoCombo);
        
        JButton criarCarrinhoBtn = new JButton("Criar Carrinho");
        JButton avaliarAlertasBtn = new JButton("Avaliar Alertas de Consumo");
        top.add(criarCarrinhoBtn);
        top.add(avaliarAlertasBtn);

        add(top, BorderLayout.NORTH);

        // CENTRO - Split pane com itens do carrinho e alertas
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        // Painel superior - Itens do carrinho
        JPanel painelItens = new JPanel(new BorderLayout());
        painelItens.setBorder(BorderFactory.createTitledBorder("Itens do Carrinho"));

        String[] colsItens = {"ID", "Item", "Quantidade", "Preço Unit.", "Subtotal"};
        modelItens = new DefaultTableModel(colsItens, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableItens = new JTable(modelItens);
        painelItens.add(new JScrollPane(tableItens), BorderLayout.CENTER);

        // Painel de adicionar item
        JPanel painelAdicionar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelAdicionar.add(new JLabel("Item do Catálogo:"));
        itemCatalogoCombo = new JComboBox<>();
        painelAdicionar.add(itemCatalogoCombo);
        
        painelAdicionar.add(new JLabel("Quantidade:"));
        quantidadeField = new JTextField(5);
        painelAdicionar.add(quantidadeField);
        
        painelAdicionar.add(new JLabel("Preço Unitário (R$):"));
        precoField = new JTextField(8);
        painelAdicionar.add(precoField);
        
        JButton adicionarItemBtn = new JButton("Adicionar Item");
        JButton removerItemBtn = new JButton("Remover Item");
        JButton limparCarrinhoBtn = new JButton("Limpar Carrinho");
        JButton finalizarCompraBtn = new JButton("Finalizar Compra");
        
        painelAdicionar.add(adicionarItemBtn);
        painelAdicionar.add(removerItemBtn);
        painelAdicionar.add(limparCarrinhoBtn);
        painelAdicionar.add(finalizarCompraBtn);
        
        painelItens.add(painelAdicionar, BorderLayout.SOUTH);

        // Painel inferior - Alertas de consumo
        JPanel painelAlertas = new JPanel(new BorderLayout());
        painelAlertas.setBorder(BorderFactory.createTitledBorder("Alertas de Consumo"));

        String[] colsAlertas = {"Tipo", "Mensagem", "Limite", "Valor Atual"};
        modelAlertas = new DefaultTableModel(colsAlertas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableAlertas = new JTable(modelAlertas);
        painelAlertas.add(new JScrollPane(tableAlertas), BorderLayout.CENTER);

        splitPane.setTopComponent(painelItens);
        splitPane.setBottomComponent(painelAlertas);
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);

        // Eventos
        criarCarrinhoBtn.addActionListener(e -> criarCarrinho());
        adicionarItemBtn.addActionListener(e -> adicionarItem());
        removerItemBtn.addActionListener(e -> removerItem());
        limparCarrinhoBtn.addActionListener(e -> limparCarrinho());
        finalizarCompraBtn.addActionListener(e -> finalizarCompra());
        avaliarAlertasBtn.addActionListener(e -> avaliarAlertas());
        churrascoCombo.addActionListener(e -> atualizarItensCatalogo());

        carregarChurrascos();
    }

    private void carregarChurrascos() {
        churrascoCombo.removeAllItems();
        try {
            List<Churrasco> churrascos = AppContext.get().churrascos().listar();
            for (Churrasco c : churrascos) {
                churrascoCombo.addItem(c.getTitulo() + " (ID: " + c.getId() + ")");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar churrascos: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarItensCatalogo() {
        itemCatalogoCombo.removeAllItems();
        try {
            List<ItemCatalogo> itens = AppContext.get().catalogo().listar();
            for (ItemCatalogo item : itens) {
                itemCatalogoCombo.addItem(item.getNome() + " (" + item.getCategoria() + ")");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar catálogo: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getSelectedChurrascoId() {
        if (churrascoCombo.getSelectedItem() == null) return null;
        String selected = (String) churrascoCombo.getSelectedItem();
        int start = selected.indexOf("(ID: ") + 5;
        int end = selected.indexOf(")");
        if (start > 4 && end > start) {
            return selected.substring(start, end);
        }
        return null;
    }

    private void criarCarrinho() {
        String churrascoId = getSelectedChurrascoId();
        if (churrascoId == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um churrasco para criar o carrinho.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (usuario.getId() == null) {
                JOptionPane.showMessageDialog(this,
                        "Usuário não possui ID válido.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verifica se já existe carrinho para este usuário e churrasco
            carrinhoAtual = AppContext.get().carrinho().buscarCarrinhoPorUsuarioEChurrasco(
                    usuario.getId().toString(), churrascoId);
            
            if (carrinhoAtual == null) {
                carrinhoAtual = AppContext.get().carrinho().criarCarrinho(
                        usuario.getId().toString(), churrascoId);
            }
            
            atualizarItensCatalogo();
            atualizarTabelaItens();
            
            JOptionPane.showMessageDialog(this,
                    "Carrinho criado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao criar carrinho: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarItem() {
        if (carrinhoAtual == null) {
            JOptionPane.showMessageDialog(this,
                    "Crie um carrinho primeiro.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (itemCatalogoCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um item do catálogo.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String quantidadeStr = quantidadeField.getText().trim();
        String precoStr = precoField.getText().trim().replace(",", ".");

        if (!ValidadorCampos.validarCampoObrigatorio(quantidadeStr)) {
            JOptionPane.showMessageDialog(this,
                    "Quantidade é obrigatória.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            quantidadeField.requestFocus();
            return;
        }

        if (!ValidadorCampos.validarCampoObrigatorio(precoStr)) {
            JOptionPane.showMessageDialog(this,
                    "Preço unitário é obrigatório.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            precoField.requestFocus();
            return;
        }

        try {
            int quantidade = Integer.parseInt(quantidadeStr);
            double preco = Double.parseDouble(precoStr);

            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Quantidade deve ser maior que zero.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                quantidadeField.requestFocus();
                return;
            }

            if (!ValidadorCampos.validarValorPositivo(preco)) {
                JOptionPane.showMessageDialog(this,
                        "Preço deve ser maior que zero.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                precoField.requestFocus();
                return;
            }

            // Busca o item do catálogo selecionado
            String itemSelecionado = (String) itemCatalogoCombo.getSelectedItem();
            List<ItemCatalogo> itens = AppContext.get().catalogo().listar();
            ItemCatalogo itemCatalogo = null;
            for (ItemCatalogo item : itens) {
                String nomeCompleto = item.getNome() + " (" + item.getCategoria() + ")";
                if (nomeCompleto.equals(itemSelecionado)) {
                    itemCatalogo = item;
                    break;
                }
            }

            ItemCarrinho item = new ItemCarrinho();
            item.setId(UUID.randomUUID());
            item.setItemId(itemCatalogo != null ? itemCatalogo.getId() : null);
            item.setNome(itemCatalogo != null ? itemCatalogo.getNome() : itemSelecionado);
            item.setQuantidade(quantidade);
            item.setPrecoUnitario(preco);

            boolean sucesso = AppContext.get().carrinho().adicionarItem(
                    carrinhoAtual.getId().toString(), item);

            if (sucesso) {
                atualizarTabelaItens();
                quantidadeField.setText("");
                precoField.setText("");
                JOptionPane.showMessageDialog(this,
                        "Item adicionado ao carrinho!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao adicionar item ao carrinho.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Valores numéricos inválidos.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao adicionar item: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerItem() {
        if (carrinhoAtual == null) {
            JOptionPane.showMessageDialog(this,
                    "Crie um carrinho primeiro.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int row = tableItens.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um item para remover.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String itemId = String.valueOf(modelItens.getValueAt(row, 0));
        boolean sucesso = AppContext.get().carrinho().removerItem(
                carrinhoAtual.getId().toString(), itemId);

        if (sucesso) {
            atualizarTabelaItens();
            JOptionPane.showMessageDialog(this,
                    "Item removido do carrinho!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao remover item do carrinho.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCarrinho() {
        if (carrinhoAtual == null) {
            JOptionPane.showMessageDialog(this,
                    "Crie um carrinho primeiro.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja limpar o carrinho?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            boolean sucesso = AppContext.get().carrinho().limparCarrinho(
                    carrinhoAtual.getId().toString());
            if (sucesso) {
                atualizarTabelaItens();
                modelAlertas.setRowCount(0);
                JOptionPane.showMessageDialog(this,
                        "Carrinho limpo!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void finalizarCompra() {
        if (carrinhoAtual == null || carrinhoAtual.getChurrascoId() == null) {
            JOptionPane.showMessageDialog(this,
                    "Crie um carrinho com um churrasco selecionado primeiro.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<ItemCarrinho> itens = AppContext.get().carrinho().listarItens(
                carrinhoAtual.getId().toString());
        
        if (itens == null || itens.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "O carrinho está vazio.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = AppContext.get().carrinho().calcularTotal(
                carrinhoAtual.getId().toString());

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Finalizar compra?\n\nTotal: R$ " + String.format("%.2f", total),
                "Confirmar Compra", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                String compraId = AppContext.get().carrinho().finalizarCompra(
                        carrinhoAtual.getId().toString(),
                        carrinhoAtual.getChurrascoId().toString());

                if (compraId != null) {
                    carrinhoAtual = null;
                    atualizarTabelaItens();
                    modelAlertas.setRowCount(0);
                    JOptionPane.showMessageDialog(this,
                            "Compra finalizada com sucesso!",
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao finalizar compra.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao finalizar compra: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void avaliarAlertas() {
        if (carrinhoAtual == null || carrinhoAtual.getChurrascoId() == null) {
            JOptionPane.showMessageDialog(this,
                    "Crie um carrinho com um churrasco selecionado primeiro.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String limiteStr = JOptionPane.showInputDialog(this,
                "Informe o limite de consumo (R$):",
                "Avaliar Alertas de Consumo",
                JOptionPane.QUESTION_MESSAGE);

        if (limiteStr == null || limiteStr.trim().isEmpty()) {
            return;
        }

        try {
            double limite = Double.parseDouble(limiteStr.trim().replace(",", "."));
            
            if (!ValidadorCampos.validarValorPositivo(limite)) {
                JOptionPane.showMessageDialog(this,
                        "Limite deve ser maior que zero.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<AlertaConsumo> alertas = AppContext.get().carrinho().avaliarCarrinho(
                    carrinhoAtual.getId().toString(), limite);

            atualizarTabelaAlertas(alertas);

            if (alertas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nenhum alerta de consumo gerado. Todos os participantes estão dentro do limite.",
                        "Avaliação", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Foram gerados " + alertas.size() + " alerta(s) de consumo.",
                        "Avaliação", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Limite inválido.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao avaliar alertas: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabelaItens() {
        modelItens.setRowCount(0);
        if (carrinhoAtual == null) return;

        try {
            List<ItemCarrinho> itens = AppContext.get().carrinho().listarItens(
                    carrinhoAtual.getId().toString());

            for (ItemCarrinho item : itens) {
                double subtotal = item.getPrecoUnitario() * item.getQuantidade();
                modelItens.addRow(new Object[]{
                        item.getId(),
                        item.getNome(),
                        item.getQuantidade(),
                        String.format("%.2f", item.getPrecoUnitario()),
                        String.format("%.2f", subtotal)
                });
            }

            // Adiciona linha de total
            double total = AppContext.get().carrinho().calcularTotal(
                    carrinhoAtual.getId().toString());
            modelItens.addRow(new Object[]{
                    "", "TOTAL", "", "", String.format("%.2f", total)
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar itens: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabelaAlertas(List<AlertaConsumo> alertas) {
        modelAlertas.setRowCount(0);
        for (AlertaConsumo alerta : alertas) {
            modelAlertas.addRow(new Object[]{
                    alerta.getTipo(),
                    alerta.getMensagem(),
                    String.format("%.2f", alerta.getValorLimite()),
                    String.format("%.2f", alerta.getValorAtual())
            });
        }
    }
}

