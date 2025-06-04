package com.util;

import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Autenticador2FA {
    private static final Logger logger = Logger.getLogger(Autenticador2FA.class.getName());
    private static String codigoGerado;

    public static void enviarCodigo() {
        codigoGerado = String.format("%06d", new Random().nextInt(999999));
        // Em produção, enviar por email/sms. Aqui simulado via logger
        logger.info("Código 2FA gerado (simulação): " + codigoGerado);
    }

    public static boolean validarCodigo() {
        Scanner scanner = new Scanner(System.in);
        logger.info("Digite o código de autenticação 2FA: ");
        String codigoDigitado = scanner.nextLine().trim();

        if (codigoDigitado.isEmpty()) {
            logger.warning("Código 2FA vazio.");
            return false;
        }

        boolean validado = codigoDigitado.equals(codigoGerado);
        if (!validado) {
            logger.warning("Código 2FA inválido.");
        }
        return validado;
    }
}
