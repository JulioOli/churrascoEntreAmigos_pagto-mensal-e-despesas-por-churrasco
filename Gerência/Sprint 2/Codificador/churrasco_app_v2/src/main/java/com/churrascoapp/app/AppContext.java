package com.churrascoapp.app;

import com.churrascoapp.controller.*;
import com.churrascoapp.dao.*;
import com.churrascoapp.service.*;

public class AppContext {
    private static AppContext INSTANCE;

    // ==== DAOs (CSV em src/data/) ====
    private final UsuarioDAO   usuarioDAO   = new UsuarioDAO();
    private final EventoDAO    eventoDAO    = new EventoDAO();
    private final CatalogoDAO  catalogoDAO  = new CatalogoDAO();
    private final CompraDAO    compraDAO    = new CompraDAO();
    private final PagamentoDAO pagamentoDAO = new PagamentoDAO();

    // ==== Services (injeção de DAOs) ====
    private final UsuarioService  usuarioService  = new UsuarioService(usuarioDAO);
    private final EventoService   eventoService   = new EventoService(eventoDAO);
    private final CatalogoService catalogoService = new CatalogoService(catalogoDAO);
    private final CompraService   compraService   = new CompraService(compraDAO, pagamentoDAO);

    // ==== Controllers (injeção de Services) ====
    private final UsuarioController  usuarioController  = new UsuarioController(usuarioService);
    private final EventoController   eventoController   = new EventoController(eventoService);
    private final CatalogoController catalogoController = new CatalogoController(catalogoService);
    private final CompraController   compraController   = new CompraController(compraService);

    private AppContext() {}

    public static synchronized AppContext get() {
        if (INSTANCE == null) INSTANCE = new AppContext();
        return INSTANCE;
    }

    // ==== Getters de Controllers ====
    public UsuarioController usuarios()   { return usuarioController; }
    public EventoController  eventos()    { return eventoController; }
    public CatalogoController catalogo()  { return catalogoController; }
    public CompraController  compras()    { return compraController; }

    // ==== Getters de Services (útil em CLIs/testes) ====
    public UsuarioService usuarioService() { return usuarioService; }
    public EventoService  eventoService()  { return eventoService; }
    public CatalogoService catalogoService() { return catalogoService; }
    public CompraService  compraService()  { return compraService; }

    // ==== Getters de DAOs (se necessário) ====
    public UsuarioDAO   usuarioDAO()   { return usuarioDAO; }
    public EventoDAO    eventoDAO()    { return eventoDAO; }
    public CatalogoDAO  catalogoDAO()  { return catalogoDAO; }
    public CompraDAO    compraDAO()    { return compraDAO; }
    public PagamentoDAO pagamentoDAO() { return pagamentoDAO; }
}
