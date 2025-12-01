package com.churrascoapp.controller;

import com.churrascoapp.model.Compra;
import com.churrascoapp.model.Pagamento;
import com.churrascoapp.service.CompraService;

import java.util.List;

public class CompraController {
    private final CompraService service;

    // Injeção por construtor (usado pelo AppContext)
    public CompraController(CompraService service) {
        this.service = service;
    }

    // ==== API compatível com a UI ====
    /** Usado pelo RegistrarCompraFrame: recebe Compra e devolve boolean. */
    public boolean registrar(Compra c) {
        return service.registrar(c);
    }

    public List<Compra> listar() {
        return service.listar();
    }

    // ==== API extra (compat com eventuais CLIs) ====
    public Compra criarCompra(String churrascoId, String item, int quantidade,
                              double valor, String fornecedor, String data, String comprovantePath) {
        return service.registrar(churrascoId, item, quantidade, valor, fornecedor, data, comprovantePath);
    }

    public Pagamento registrarPagamento(String compraId, String metodo) {
        return service.registrarPagamento(compraId, metodo);
    }
}
