package com.util;

import java.util.Random;
import java.util.Scanner;

public class Autenticador2FA {
    private static final Random random = new Random();
    private static String codigoGerado;

    public static void enviarCodigo() {
        codigoGerado = String.format("%06d", random.nextInt(999999));
        // Aqui você poderia integrar com um serviço de email.
        System.out.println("Código 2FA enviado: " + codigoGerado); // Simulação
    }

    public static boolean validarCodigo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o código 2FA enviado: ");
        String entrada = scanner.nextLine();
        return entrada.equals(codigoGerado);
    }
}
