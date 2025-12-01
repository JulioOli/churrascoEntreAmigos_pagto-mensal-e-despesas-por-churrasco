package com.churrascoapp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.churrascoapp.dao.CompraDAO;
import com.churrascoapp.model.AlertaConsumo;
import com.churrascoapp.model.Carrinho;
import com.churrascoapp.model.Churrasco;
import com.churrascoapp.model.Compra;
import com.churrascoapp.model.ItemCarrinho;
import com.churrascoapp.model.Participacao;
import com.churrascoapp.service.AlertaConsumoService;
import com.churrascoapp.utils.UUIDUtil;

/**
 * Service para gerenciamento de carrinho de compras.
 * Conforme diagrama de classes - artefatos_astah_v3
 */
public class CarrinhoService {
    private final CompraDAO compraDAO;
    private final ChurrascoService churrascoService;
    private final AlertaConsumoService alertaConsumoService;
    private final Map<String, Carrinho> carrinhos; // carrinhoId -> Carrinho (em memória)

    public CarrinhoService(CompraDAO compraDAO, ChurrascoService churrascoService, AlertaConsumoService alertaConsumoService) {
        this.compraDAO = compraDAO;
        this.churrascoService = churrascoService;
        this.alertaConsumoService = alertaConsumoService;
        this.carrinhos = new HashMap<>();
    }

    /**
     * Cria novo carrinho para usuário
     * @param usuarioId ID do usuário
     * @param churrascoId ID do churrasco (opcional)
     * @return Carrinho criado
     */
    public Carrinho criarCarrinho(String usuarioId, String churrascoId) {
        UUID id = UUIDUtil.randomId();
        UUID churrascoUUID = churrascoId != null ? UUID.fromString(churrascoId) : null;
        Carrinho carrinho = new Carrinho(id, UUID.fromString(usuarioId), churrascoUUID);
        carrinhos.put(id.toString(), carrinho);
        return carrinho;
    }
    
    /**
     * Busca carrinho ativo do usuário para um churrasco específico
     * @param usuarioId ID do usuário
     * @param churrascoId ID do churrasco
     * @return Carrinho encontrado ou null
     */
    public Carrinho buscarCarrinhoPorUsuarioEChurrasco(String usuarioId, String churrascoId) {
        UUID usuarioUUID = UUID.fromString(usuarioId);
        UUID churrascoUUID = UUID.fromString(churrascoId);
        for (Carrinho c : carrinhos.values()) {
            if (c.getUsuarioId().equals(usuarioUUID) && 
                c.getChurrascoId() != null && 
                c.getChurrascoId().equals(churrascoUUID)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Cria novo carrinho para usuário (sem churrasco)
     * @param usuarioId ID do usuário
     * @return Carrinho criado
     */
    public Carrinho criarCarrinho(String usuarioId) {
        return criarCarrinho(usuarioId, null);
    }

    /**
     * Busca carrinho por ID
     * @param carrinhoId ID do carrinho
     * @return Carrinho ou null
     */
    public Carrinho buscarCarrinho(String carrinhoId) {
        return carrinhos.get(carrinhoId);
    }

    /**
     * Adiciona item ao carrinho
     * @param carrinhoId ID do carrinho
     * @param item Item a ser adicionado
     * @return true se foi adicionado
     */
    public boolean adicionarItem(String carrinhoId, ItemCarrinho item) {
        Carrinho carrinho = carrinhos.get(carrinhoId);
        if (carrinho == null) {
            return false;
        }
        carrinho.getItens().add(item);
        return true;
    }

    /**
     * Remove item do carrinho
     * @param carrinhoId ID do carrinho
     * @param itemId ID do item
     * @return true se foi removido
     */
    public boolean removerItem(String carrinhoId, String itemId) {
        Carrinho carrinho = carrinhos.get(carrinhoId);
        if (carrinho == null) {
            return false;
        }
        return carrinho.getItens().removeIf(item -> item.getId().equals(UUID.fromString(itemId)));
    }

    /**
     * Lista todos os itens do carrinho
     * @param carrinhoId ID do carrinho
     * @return Lista de itens
     */
    public List<ItemCarrinho> listarItens(String carrinhoId) {
        Carrinho carrinho = carrinhos.get(carrinhoId);
        if (carrinho == null) {
            return new ArrayList<>();
        }
        return carrinho.getItens();
    }

    /**
     * Calcula total do carrinho
     * @param carrinhoId ID do carrinho
     * @return Valor total
     */
    public double calcularTotal(String carrinhoId) {
        Carrinho carrinho = carrinhos.get(carrinhoId);
        if (carrinho == null) {
            return 0.0;
        }
        return carrinho.calcularTotal();
    }

    /**
     * Limpa carrinho
     * @param carrinhoId ID do carrinho
     * @return true se foi limpo
     */
    public boolean limparCarrinho(String carrinhoId) {
        Carrinho carrinho = carrinhos.get(carrinhoId);
        if (carrinho == null) {
            return false;
        }
        carrinho.getItens().clear();
        return true;
    }

    /**
     * Finaliza compra convertendo carrinho em compras
     * @param carrinhoId ID do carrinho
     * @param churrascoId ID do churrasco
     * @return ID da primeira compra criada ou null
     */
    public String finalizarCompra(String carrinhoId, String churrascoId) {
        Carrinho carrinho = carrinhos.get(carrinhoId);
        if (carrinho == null || carrinho.getItens().isEmpty()) {
            return null;
        }

        String primeiraCompraId = null;
        String dataAtual = LocalDate.now().toString();

        // Converte cada item do carrinho em uma compra
        for (ItemCarrinho item : carrinho.getItens()) {
            UUID compraId = UUIDUtil.randomId();
            if (primeiraCompraId == null) {
                primeiraCompraId = compraId.toString();
            }

            // Busca informações do churrasco para obter o título/nome
            Churrasco churrasco = churrascoService.buscar(churrascoId);
            String nomeItem = churrasco != null ? churrasco.getTitulo() : "Item " + churrascoId;

            Compra compra = new Compra();
            compra.setId(compraId);
            compra.setChurrascoId(UUID.fromString(churrascoId));
            compra.setItem(nomeItem);
            compra.setQuantidade(item.getQuantidade());
            compra.setValor(item.getPrecoUnitario() * item.getQuantidade());
            compra.setData(dataAtual);

            compraDAO.adicionar(compra);
        }

        // Limpa carrinho após finalizar
        carrinho.getItens().clear();
        carrinhos.remove(carrinhoId);

        return primeiraCompraId;
    }

    /**
     * Atualiza quantidade de item no carrinho
     * @param carrinhoId ID do carrinho
     * @param itemId ID do item
     * @param novaQuantidade Nova quantidade
     * @return true se foi atualizado
     */
    public boolean atualizarQuantidade(String carrinhoId, String itemId, int novaQuantidade) {
        Carrinho carrinho = carrinhos.get(carrinhoId);
        if (carrinho == null) {
            return false;
        }

        for (ItemCarrinho item : carrinho.getItens()) {
            if (item.getId().equals(UUID.fromString(itemId))) {
                item.setQuantidade(novaQuantidade);
                return true;
            }
        }
        return false;
    }

    /**
     * Avalia o carrinho e gera alertas de consumo se necessário
     * @param carrinhoId ID do carrinho
     * @param limite Limite de consumo permitido
     * @return Lista de alertas de consumo gerados
     */
    public List<AlertaConsumo> avaliarCarrinho(String carrinhoId, double limite) {
        Carrinho carrinho = carrinhos.get(carrinhoId);
        if (carrinho == null || carrinho.getChurrascoId() == null) {
            return new ArrayList<>();
        }

        // Calcula o total do carrinho
        double totalCarrinho = calcularTotal(carrinhoId);
        
        // Busca informações do churrasco
        Churrasco churrasco = churrascoService.buscar(carrinho.getChurrascoId().toString());
        if (churrasco == null) {
            return new ArrayList<>();
        }

        // Gera alertas usando o AlertaConsumoService
        // O método gerarAlertas já calcula o consumo total incluindo compras e participações
        return alertaConsumoService.gerarAlertas(
                carrinho.getChurrascoId().toString(), 
                limite
        );
    }
}
