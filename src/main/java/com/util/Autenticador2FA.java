package com.util;

import java.util.Random;
import java.util.Scanner;

public class Autenticador2FA {
    // Objeto para gerar números aleatórios
    private static final Random random = new Random();

    // Armazena o código 2FA gerado
    private static String codigoGerado;

    /**
     * Gera um código aleatório de 6 dígitos e simula o envio ao usuário.
     * No contexto real, esse código seria enviado por e-mail ou SMS.
     */
    public static void enviarCodigo() {
        // Gera um número aleatório de 6 dígitos (com preenchimento de zeros à esquerda se necessário)
        codigoGerado = String.format("%06d", random.nextInt(999999));

        // Simula o envio do código 2FA. Em produção, aqui entraria o envio por e-mail, SMS, etc.
        System.out.println("Código 2FA enviado: " + codigoGerado); // Apenas simulação
    }

    /**
     * Solicita ao usuário que digite o código recebido e verifica se ele é válido.
     * @return true se o código digitado for igual ao gerado; false caso contrário.
     */
    public static boolean validarCodigo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o código 2FA enviado: ");
        String entrada = scanner.nextLine(); // Lê o código digitado pelo usuário

        // Compara o código digitado com o código gerado
        return entrada.equals(codigoGerado);
    }
}
