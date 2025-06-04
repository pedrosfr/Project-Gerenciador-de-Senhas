package com.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class CriptografiaAES {

    private static final String ALGORITMO = "AES/CBC/PKCS5Padding";
    private static final String SEGREDO = "FraseSecretaSuperForte"; // Ideal usar variável de ambiente
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128;

    public static String criptografar(String valor) {
        try {
            // Geração de salt e IV aleatórios
            byte[] salt = new byte[SALT_LENGTH];
            byte[] ivBytes = new byte[IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);
            random.nextBytes(ivBytes);

            // Deriva chave com PBKDF2
            SecretKeySpec chave = gerarChave(SEGREDO, salt);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            // Inicializa cipher
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, chave, iv);
            byte[] criptografado = cipher.doFinal(valor.getBytes());

            // Concatena salt + IV + criptografado
            byte[] combinado = new byte[salt.length + ivBytes.length + criptografado.length];
            System.arraycopy(salt, 0, combinado, 0, salt.length);
            System.arraycopy(ivBytes, 0, combinado, salt.length, ivBytes.length);
            System.arraycopy(criptografado, 0, combinado, salt.length + ivBytes.length, criptografado.length);

            return Base64.getEncoder().encodeToString(combinado);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar", e);
        }
    }

    public static String descriptografar(String valorCriptografado) {
        try {
            byte[] combinado = Base64.getDecoder().decode(valorCriptografado);

            // Extrai salt, IV e conteúdo criptografado
            byte[] salt = new byte[SALT_LENGTH];
            byte[] ivBytes = new byte[IV_LENGTH];
            byte[] criptografado = new byte[combinado.length - SALT_LENGTH - IV_LENGTH];

            System.arraycopy(combinado, 0, salt, 0, SALT_LENGTH);
            System.arraycopy(combinado, SALT_LENGTH, ivBytes, 0, IV_LENGTH);
            System.arraycopy(combinado, SALT_LENGTH + IV_LENGTH, criptografado, 0, criptografado.length);

            SecretKeySpec chave = gerarChave(SEGREDO, salt);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, chave, iv);
            byte[] descriptografado = cipher.doFinal(criptografado);

            return new String(descriptografado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar", e);
        }
    }

    private static SecretKeySpec gerarChave(String segredo, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(segredo.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        byte[] chaveBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(chaveBytes, "AES");
    }
}

