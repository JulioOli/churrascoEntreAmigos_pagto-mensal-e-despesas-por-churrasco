package com.churrascoapp.service;

import com.churrascoapp.dao.CompraDAO;
import com.churrascoapp.dao.PagamentoDAO;
import com.churrascoapp.model.Compra;
import com.churrascoapp.model.Pagamento;
import com.churrascoapp.utils.UUIDUtil;

import java.util.List;

public class CompraService {
    private final CompraDAO compraDAO;
    private final PagamentoDAO pagamentoDAO;

    public CompraService(CompraDAO compraDAO, PagamentoDAO pagamentoDAO) {
        this.compraDAO = compraDAO;
        this.pagamentoDAO = pagamentoDAO;
    }

    /** Registrar por campos (gera ID) e retorna o objeto persistido. */
    public Compra registrar(String churrascoId, String item, int quantidade,
                            double valor, String fornecedor, String data, String comprovantePath) {
        String id = UUIDUtil.randomId();
        Compra c = new Compra();
        c.setId(id);
        c.setChurrascoId(churrascoId);
        c.setItem(item);
        c.setQuantidade(quantidade);
        c.setValor(valor);
        c.setFornecedor(fornecedor);
        c.setData(data);
        c.setComprovantePath(comprovantePath);

        boolean ok = compraDAO.adicionar(c);
        if (!ok) throw new RuntimeException("Falha ao registrar compra.");
        return c;
    }

    /** Registrar recebendo o objeto pronto (compatível com a UI). */
    public boolean registrar(Compra c) {
        if (c.getId() == null || c.getId().isBlank()) {
            c.setId(UUIDUtil.randomId());
        }
        return compraDAO.adicionar(c);
    }

    public List<Compra> listar() {
        return compraDAO.listar();
    }

    public Pagamento registrarPagamento(String compraId, String metodo) {
        String id = UUIDUtil.randomId();
        Pagamento p = new Pagamento(id, compraId, metodo, "PENDENTE", null);
        try {
            // PagamentoDAO.salvar é void (sem retorno)
            pagamentoDAO.salvar(p);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao registrar pagamento.");
        }
        return p;
    }
}
