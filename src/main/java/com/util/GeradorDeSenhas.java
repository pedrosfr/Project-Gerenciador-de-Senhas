package com.util;

import java.security.SecureRandom;

public class GeradorDeSenhas {
    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
    private static final SecureRandom random = new SecureRandom();

    public static String gerarSenha(int tamanho) {
        StringBuilder senha = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            int index = random.nextInt(CARACTERES.length());
            senha.append(CARACTERES.charAt(index));
        }
        return senha.toString();
    }
}
