package com.churrascoapp.controller;

import com.churrascoapp.model.AlertaConsumo;
import com.churrascoapp.service.AlertaConsumoService;

import java.util.List;

public class AlertaConsumoController {

    private final AlertaConsumoService alertaConsumoService;

    public AlertaConsumoController(AlertaConsumoService alertaConsumoService) {
        this.alertaConsumoService = alertaConsumoService;
    }

    public List<AlertaConsumo> gerarAlertas(String churrascoId, double limite) {
        return alertaConsumoService.gerarAlertas(churrascoId, limite);
    }

    public List<AlertaConsumo> listarPorChurrasco(String churrascoId) {
        return alertaConsumoService.listarPorChurrasco(churrascoId);
    }
}
