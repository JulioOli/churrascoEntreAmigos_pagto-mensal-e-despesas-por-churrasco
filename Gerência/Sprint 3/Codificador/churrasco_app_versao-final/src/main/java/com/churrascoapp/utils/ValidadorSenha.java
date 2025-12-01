
package com.churrascoapp.utils;

public class ValidadorSenha {
    public static boolean validar(String senha) {
        if (senha == null) return false;
        if (senha.length() < 10) return false;
        boolean upper=false, lower=false, special=false;
        for (char c: senha.toCharArray()){
            if (Character.isUpperCase(c)) upper=true;
            else if (Character.isLowerCase(c)) lower=true;
            else if (!Character.isDigit(c) && !Character.isLetter(c)) special=true;
        }
        return upper && lower && special;
    }
}
