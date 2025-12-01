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

    // Novos DAOs
    private final ConviteDAO        conviteDAO        = new ConviteDAO();
    private final ParticipacaoDAO   participacaoDAO   = new ParticipacaoDAO();
    private final PrestacaoContaDAO prestacaoContaDAO = new PrestacaoContaDAO();
    private final AlertaConsumoDAO  alertaConsumoDAO  = new AlertaConsumoDAO();
    private final EmailDAO          emailDAO          = new EmailDAO();
    private final RegraConsumoGlobalDAO regraConsumoGlobalDAO = new RegraConsumoGlobalDAO();
    private final RelatorioDAO      relatorioDAO      = new RelatorioDAO();

    // ==== Services ====
    private final UsuarioService  usuarioService  = new UsuarioService(usuarioDAO);
    private final EventoService   eventoService   = new EventoService(eventoDAO);
    private final ChurrascoService churrascoService = new ChurrascoService(eventoDAO);
    private final CatalogoService catalogoService = new CatalogoService(catalogoDAO);
    private final CompraService   compraService   = new CompraService(compraDAO, pagamentoDAO);

    private final ParticipacaoService participacaoService =
            new ParticipacaoService(participacaoDAO, eventoDAO);

    private final PrestacaoContaService prestacaoContaService =
            new PrestacaoContaService(prestacaoContaDAO, participacaoDAO, compraDAO);

    private final AlertaConsumoService alertaConsumoService =
            new AlertaConsumoService(alertaConsumoDAO, participacaoDAO, compraDAO);

    private final EmailService emailService =
            new EmailService(emailDAO);

    private final ConviteService conviteService =
            new ConviteService(conviteDAO, participacaoService, emailService, usuarioDAO, eventoDAO);

    private final CarrinhoService carrinhoService =
            new CarrinhoService(compraDAO, churrascoService, alertaConsumoService);

    private final RegraConsumoGlobalService regraConsumoGlobalService =
            new RegraConsumoGlobalService(regraConsumoGlobalDAO);

    private final RelatorioService relatorioService =
            new RelatorioService(relatorioDAO, participacaoDAO, compraDAO, prestacaoContaService);

    // ==== Controllers ====
    private final UsuarioController  usuarioController  = new UsuarioController(usuarioService);
    private final EventoController   eventoController   = new EventoController(eventoService);
    private final ChurrascoController churrascoController = new ChurrascoController(churrascoService);
    private final CatalogoController catalogoController = new CatalogoController(catalogoService);
    private final CompraController   compraController   = new CompraController(compraService);

    private final ParticipacaoController participacaoController =
            new ParticipacaoController(participacaoService);

    private final PrestacaoContaController prestacaoContaController =
            new PrestacaoContaController(prestacaoContaService);

    private final AlertaConsumoController alertaConsumoController =
            new AlertaConsumoController(alertaConsumoService);

    private final ConviteController conviteController =
            new ConviteController(conviteService);

    private final CarrinhoController carrinhoController =
            new CarrinhoController(carrinhoService);

    private final RegraConsumoGlobalController regraConsumoGlobalController =
            new RegraConsumoGlobalController(regraConsumoGlobalService);

    private final RelatorioController relatorioController =
            new RelatorioController(relatorioService);

    private AppContext() {}

    public static synchronized AppContext get() {
        if (INSTANCE == null) INSTANCE = new AppContext();
        return INSTANCE;
    }

    // ==== Getters de Controllers ====
    public UsuarioController usuarios()   { return usuarioController; }
    public EventoController  eventos()    { return eventoController; }
    public ChurrascoController churrascos() { return churrascoController; }
    public CatalogoController catalogo()  { return catalogoController; }
    public CompraController  compras()    { return compraController; }

    public ParticipacaoController participacoes() { return participacaoController; }
    public PrestacaoContaController prestacoes()  { return prestacaoContaController; }
    public AlertaConsumoController alertas()      { return alertaConsumoController; }
    public ConviteController convites()           { return conviteController; }
    public CarrinhoController carrinho()          { return carrinhoController; }
    public RegraConsumoGlobalController regrasConsumo() { return regraConsumoGlobalController; }
    public RelatorioController relatorios()             { return relatorioController; }

    // ==== Getters de Services ====
    public UsuarioService usuarioService()      { return usuarioService; }
    public EventoService  eventoService()       { return eventoService; }
    public ChurrascoService churrascoService()  { return churrascoService; }
    public CatalogoService catalogoService()    { return catalogoService; }
    public CompraService  compraService()       { return compraService; }
    public ParticipacaoService participacaoService() { return participacaoService; }
    public PrestacaoContaService prestacaoContaService() { return prestacaoContaService; }
    public AlertaConsumoService alertaConsumoService()   { return alertaConsumoService; }
    public EmailService emailService()          { return emailService; }
    public ConviteService conviteService()      { return conviteService; }
    public CarrinhoService carrinhoService()    { return carrinhoService; }

    // ==== Getters de DAOs (se necessário) ====
    public UsuarioDAO   usuarioDAO()   { return usuarioDAO; }
    public EventoDAO    eventoDAO()    { return eventoDAO; }
    public CatalogoDAO  catalogoDAO()  { return catalogoDAO; }
    public CompraDAO    compraDAO()    { return compraDAO; }
    public PagamentoDAO pagamentoDAO() { return pagamentoDAO; }
    public ConviteDAO        conviteDAO()        { return conviteDAO; }
    public ParticipacaoDAO   participacaoDAO()   { return participacaoDAO; }
    public PrestacaoContaDAO prestacaoContaDAO() { return prestacaoContaDAO; }
    public AlertaConsumoDAO  alertaConsumoDAO()  { return alertaConsumoDAO; }
    public EmailDAO          emailDAO()          { return emailDAO; }
    public RegraConsumoGlobalDAO regraConsumoGlobalDAO() { return regraConsumoGlobalDAO; }
}
