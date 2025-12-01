package com.churrascoapp.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class ValidadorCampos {

    // Padrão para validação de email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // Padrão para validação de hora (HH:MM)
    private static final Pattern HORA_PATTERN = Pattern.compile(
            "^([01]?[0-9]|2[0-3]):[0-5][0-9]$"
    );

    /**
     * Valida formato de email
     */
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Valida formato de data (AAAA-MM-DD)
     */
    public static boolean validarData(String data) {
        if (data == null || data.trim().isEmpty()) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(data.trim(), formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Valida se a data não é no passado
     */
    public static boolean validarDataFutura(String data) {
        if (!validarData(data)) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataEvento = LocalDate.parse(data.trim(), formatter);
            return !dataEvento.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Valida formato de hora (HH:MM)
     */
    public static boolean validarHora(String hora) {
        if (hora == null || hora.trim().isEmpty()) {
            return false;
        }
        return HORA_PATTERN.matcher(hora.trim()).matches();
    }

    /**
     * Valida se um valor numérico é positivo
     */
    public static boolean validarValorPositivo(double valor) {
        return valor > 0;
    }

    /**
     * Valida se uma string não está vazia
     */
    public static boolean validarCampoObrigatorio(String campo) {
        return campo != null && !campo.trim().isEmpty();
    }

    /**
     * Valida se um número de caracteres está dentro do limite
     */
    public static boolean validarTamanho(String campo, int min, int max) {
        if (campo == null) {
            return min == 0;
        }
        int tamanho = campo.trim().length();
        return tamanho >= min && tamanho <= max;
    }
}

