package com.churrascoapp.controller;

import com.churrascoapp.model.Churrasco;
import com.churrascoapp.service.ChurrascoService;

import java.util.List;

/**
 * Controller para gerenciamento de churrascos.
 * Conforme diagrama de classes - artefatos_astah_v3
 * NOTA: Renomeado de EventoController para ChurrascoController conforme diagrama
 */
public class ChurrascoController {
    private final ChurrascoService churrascoService;

    public ChurrascoController(ChurrascoService churrascoService) {
        this.churrascoService = churrascoService;
    }

    /**
     * Cria um novo churrasco
     * @param titulo Título do churrasco
     * @param data Data do churrasco
     * @param hora Hora do churrasco
     * @param local Local do churrasco
     * @param tipo Tipo do churrasco
     * @param pix Chave PIX para pagamento
     * @param precoParticipante Preço por participante
     * @param descricao Descrição do churrasco
     * @return Churrasco criado
     */
    public Churrasco criar(String titulo, String data, String hora, String local,
                          String tipo, String pix, double precoParticipante, String descricao) {
        return churrascoService.criar(titulo, data, hora, local, tipo, pix, precoParticipante, descricao);
    }

    /**
     * Cria um novo churrasco com criador
     * @param criadorId ID do usuário criador
     * @param titulo Título do churrasco
     * @param data Data do churrasco
     * @param hora Hora do churrasco
     * @param local Local do churrasco
     * @param tipo Tipo do churrasco
     * @param pix Chave PIX para pagamento
     * @param precoParticipante Preço por participante
     * @param descricao Descrição do churrasco
     * @return Churrasco criado
     */
    public Churrasco criar(java.util.UUID criadorId, String titulo, String data, String hora, String local,
                          String tipo, String pix, double precoParticipante, String descricao) {
        return churrascoService.criar(criadorId, titulo, data, hora, local, tipo, pix, precoParticipante, descricao);
    }

    /**
     * Lista todos os churrascos
     * @return Lista de churrascos
     */
    public List<Churrasco> listar() {
        return churrascoService.listar();
    }

    /**
     * Busca churrasco por ID
     * @param id ID do churrasco
     * @return Churrasco encontrado ou null
     */
    public Churrasco buscar(String id) {
        return churrascoService.buscar(id);
    }

    /**
     * Atualiza dados de um churrasco
     * @param churrasco Churrasco com dados atualizados
     * @return true se foi atualizado com sucesso
     */
    public boolean atualizar(Churrasco churrasco) {
        return churrascoService.atualizar(churrasco);
    }

    /**
     * Remove um churrasco
     * @param id ID do churrasco
     * @return true se foi removido com sucesso
     */
    public boolean remover(String id) {
        return churrascoService.remover(id);
    }

    /**
     * Lista churrascos por tipo
     * @param tipo Tipo do churrasco
     * @return Lista de churrascos do tipo especificado
     */
    public List<Churrasco> listarPorTipo(String tipo) {
        return churrascoService.listarPorTipo(tipo);
    }

    /**
     * Lista churrascos por data
     * @param data Data do churrasco
     * @return Lista de churrascos na data especificada
     */
    public List<Churrasco> listarPorData(String data) {
        return churrascoService.listarPorData(data);
    }

    /**
     * Lista churrascos criados por um usuário
     * @param criadorId ID do criador
     * @return Lista de churrascos criados pelo usuário
     */
    public List<Churrasco> listarPorCriador(java.util.UUID criadorId) {
        return churrascoService.listarPorCriador(criadorId);
    }
}
