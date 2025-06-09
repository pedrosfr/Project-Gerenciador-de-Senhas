package com.util;

/**
 * Esta classe simula a verificação de senhas que foram vazadas em bancos de dados públicos.
 * Idealmente, seria conectada a uma API real como o "Have I Been Pwned".
 */
public class VerificadorVazamentoSenha {

    /**
     * Verifica se uma senha foi potencialmente vazada ou é considerada fraca.
     *
     * @param senha A senha a ser verificada.
     * @return true se a senha é fraca ou comumente vazada; false se parece segura.
     */
    public static boolean foiVazada(String senha) {
        // Simulação simples de verificação — pode ser substituída futuramente por uma API real.

        // Se a senha for "123456" (comum e conhecida por estar em vazamentos) ou tiver menos de 6 caracteres:
        if (senha.equalsIgnoreCase("123456") || senha.length() < 6) {
            System.out.println("Senha potencialmente vazada ou fraca.");
            return true; // A senha é considerada insegura
        } else {
            // Caso contrário, assume-se que a senha é segura (sem consulta real a banco de dados)
            System.out.println("Senha parece segura (não verificada com API real).");
            return false; // A senha é considerada segura na simulação
        }
    }
}
