package com.util;

import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeradorDeSenhas {
    private static final Logger logger = Logger.getLogger(GeradorDeSenhas.class.getName());

    // Conjunto de caracteres permitidos na senha
    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz"
            + "0123456789"
            + "!@#$%&*";
    private static final SecureRandom random = new SecureRandom();
    private static final int TAMANHO_MINIMO = 4;   // Defina um tamanho mínimo aceitável para segurança
    private static final int TAMANHO_PADRAO = 12;  // Tamanho padrão, se nenhuma indicação for dada

    /**
     * Gera uma senha aleatória usando caracteres seguros.
     * @param tamanho O número de caracteres desejado para a senha. Deve ser >= TAMANHO_MINIMO.
     * @return Uma string representando a senha gerada.
     * @throws IllegalArgumentException se o tamanho for menor que o mínimo.
     */
    public static String gerarSenha(int tamanho) {
        if (tamanho < TAMANHO_MINIMO) {
            String msg = "Tamanho da senha inválido: deve ser no mínimo " + TAMANHO_MINIMO + " caracteres.";
            logger.log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }

        StringBuilder senha = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            int index = random.nextInt(CARACTERES.length());
            senha.append(CARACTERES.charAt(index));
        }

        String resultado = senha.toString();
        logger.log(Level.INFO, "Senha de tamanho {0} gerada com sucesso.", tamanho);
        return resultado;
    }

    /**
     * Gera uma senha utilizando o tamanho padrão predefinido.
     * @return Uma senha aleatória de tamanho TAMANHO_PADRAO.
     */
    public static String gerarSenhaPadrao() {
        return gerarSenha(TAMANHO_PADRAO);
    }
}
