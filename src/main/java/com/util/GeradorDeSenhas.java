package com.util;

import java.security.SecureRandom;

/**
 * Classe utilitária para gerar senhas seguras aleatórias.
 * Utiliza letras maiúsculas, minúsculas, números e caracteres especiais.
 *
 * Essa classe pode ser usada para criação de senhas fortes em cadastros,
 * evitando o uso de senhas previsíveis.
 */
public class GeradorDeSenhas {

    // Conjunto de caracteres permitidos na senha (letras, números e símbolos)
    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";

    // Objeto de geração de números aleatórios seguro para uso criptográfico
    private static final SecureRandom random = new SecureRandom();

    /**
     * Gera uma senha aleatória com o tamanho especificado.
     *
     * @param tamanho Número de caracteres desejados na senha
     * @return Senha gerada aleatoriamente
     */
    public static String gerarSenha(int tamanho) {
        // StringBuilder é utilizado por ser mais eficiente para concatenação
        StringBuilder senha = new StringBuilder(tamanho);

        // Loop para adicionar caracteres aleatórios até atingir o tamanho desejado
        for (int i = 0; i < tamanho; i++) {
            // Gera um índice aleatório baseado na string de caracteres permitidos
            int index = random.nextInt(CARACTERES.length());
            // Adiciona o caractere correspondente ao índice sorteado
            senha.append(CARACTERES.charAt(index));
        }

        // Retorna a senha gerada como string
        return senha.toString();
    }
}
