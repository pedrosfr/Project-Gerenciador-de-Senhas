package com.util;

public class VerificadorVazamentoSenha {
    public static void foiVazada(String senha) {
        // Simulação de verificação — substituir por chamada real à API no futuro
        if (senha.equalsIgnoreCase("123456") || senha.length() < 6) {
            System.out.println("Senha potencialmente vazada ou fraca.");
        } else {
            System.out.println("Senha parece segura (não verificada com API real).");
        }
    }
}
