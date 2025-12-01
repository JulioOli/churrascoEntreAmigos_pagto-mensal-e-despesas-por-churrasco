package com.churrascoapp.controller;

import java.util.List;

import com.churrascoapp.model.AlertaConsumo;
import com.churrascoapp.model.Carrinho;
import com.churrascoapp.model.ItemCarrinho;
import com.churrascoapp.service.CarrinhoService;

/**
 * Controller para gerenciamento de carrinho de compras.
 * Conforme diagrama de classes - artefatos_astah_v3
 */
public class CarrinhoController {
    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    /**
     * Cria um novo carrinho para o usuário
     * @param usuarioId ID do usuário
     * @return Carrinho criado
     */
    public Carrinho criarCarrinho(String usuarioId) {
        return carrinhoService.criarCarrinho(usuarioId);
    }

    /**
     * Cria um novo carrinho para o usuário com churrasco
     * @param usuarioId ID do usuário
     * @param churrascoId ID do churrasco
     * @return Carrinho criado
     */
    public Carrinho criarCarrinho(String usuarioId, String churrascoId) {
        return carrinhoService.criarCarrinho(usuarioId, churrascoId);
    }

    /**
     * Busca carrinho ativo do usuário para um churrasco específico
     * @param usuarioId ID do usuário
     * @param churrascoId ID do churrasco
     * @return Carrinho encontrado ou null
     */
    public Carrinho buscarCarrinhoPorUsuarioEChurrasco(String usuarioId, String churrascoId) {
        return carrinhoService.buscarCarrinhoPorUsuarioEChurrasco(usuarioId, churrascoId);
    }

    /**
     * Busca carrinho pelo ID
     * @param carrinhoId ID do carrinho
     * @return Carrinho encontrado ou null
     */
    public Carrinho buscarCarrinho(String carrinhoId) {
        return carrinhoService.buscarCarrinho(carrinhoId);
    }

    /**
     * Adiciona item ao carrinho
     * @param carrinhoId ID do carrinho
     * @param item Item a ser adicionado
     * @return true se item foi adicionado
     */
    public boolean adicionarItem(String carrinhoId, ItemCarrinho item) {
        return carrinhoService.adicionarItem(carrinhoId, item);
    }

    /**
     * Remove item do carrinho
     * @param carrinhoId ID do carrinho
     * @param itemId ID do item a ser removido
     * @return true se item foi removido
     */
    public boolean removerItem(String carrinhoId, String itemId) {
        return carrinhoService.removerItem(carrinhoId, itemId);
    }

    /**
     * Lista todos os itens do carrinho
     * @param carrinhoId ID do carrinho
     * @return Lista de itens do carrinho
     */
    public List<ItemCarrinho> listarItens(String carrinhoId) {
        return carrinhoService.listarItens(carrinhoId);
    }

    /**
     * Calcula o total do carrinho
     * @param carrinhoId ID do carrinho
     * @return Valor total do carrinho
     */
    public double calcularTotal(String carrinhoId) {
        return carrinhoService.calcularTotal(carrinhoId);
    }

    /**
     * Limpa todos os itens do carrinho
     * @param carrinhoId ID do carrinho
     * @return true se carrinho foi limpo
     */
    public boolean limparCarrinho(String carrinhoId) {
        return carrinhoService.limparCarrinho(carrinhoId);
    }

    /**
     * Finaliza compra do carrinho
     * @param carrinhoId ID do carrinho
     * @param churrascoId ID do churrasco relacionado
     * @return ID da compra criada ou null se falhar
     */
    public String finalizarCompra(String carrinhoId, String churrascoId) {
        return carrinhoService.finalizarCompra(carrinhoId, churrascoId);
    }

    /**
     * Avalia o carrinho e gera alertas de consumo
     * @param carrinhoId ID do carrinho
     * @param limite Limite de consumo permitido
     * @return Lista de alertas de consumo gerados
     */
    public List<AlertaConsumo> avaliarCarrinho(String carrinhoId, double limite) {
        return carrinhoService.avaliarCarrinho(carrinhoId, limite);
    }
}
