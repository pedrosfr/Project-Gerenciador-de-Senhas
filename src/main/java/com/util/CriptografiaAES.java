package com.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

public class CriptografiaAES {
    private static final String ALGORITMO = "AES/CBC/PKCS5Padding";
    private static final String CHAVE = "1234567890123456"; // substitua por algo mais seguro em produção

    public static String criptografar(String valor) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            SecretKeySpec chave = new SecretKeySpec(CHAVE.getBytes(), "AES");

            byte[] ivBytes = new byte[16];
            new SecureRandom().nextBytes(ivBytes); // IV aleatório
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.ENCRYPT_MODE, chave, iv);
            byte[] criptografado = cipher.doFinal(valor.getBytes());

            // IV + criptografia concatenados
            byte[] combinado = new byte[ivBytes.length + criptografado.length];
            System.arraycopy(ivBytes, 0, combinado, 0, ivBytes.length);
            System.arraycopy(criptografado, 0, combinado, ivBytes.length, criptografado.length);

            return Base64.getEncoder().encodeToString(combinado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar", e);
        }
    }

    public static String descriptografar(String valor) {
        try {
            byte[] combinado = Base64.getDecoder().decode(valor);
            byte[] ivBytes = new byte[16];
            byte[] criptografado = new byte[combinado.length - 16];

            System.arraycopy(combinado, 0, ivBytes, 0, 16);
            System.arraycopy(combinado, 16, criptografado, 0, criptografado.length);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            SecretKeySpec chave = new SecretKeySpec(CHAVE.getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.DECRYPT_MODE, chave, iv);
            byte[] descriptografado = cipher.doFinal(criptografado);

            return new String(descriptografado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar", e);
        }
    }
}
