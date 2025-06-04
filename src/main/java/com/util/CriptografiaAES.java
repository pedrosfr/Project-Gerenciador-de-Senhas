package com.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CriptografiaAES {
    private static final String CHAVE = "1234567890123456"; // 16 caracteres = 128 bits

    public static String criptografar(String valor) {
        try {
            SecretKeySpec chave = new SecretKeySpec(CHAVE.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, chave);
            byte[] criptografado = cipher.doFinal(valor.getBytes());
            return Base64.getEncoder().encodeToString(criptografado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar", e);
        }
    }

    public static String descriptografar(String valor) {
        try {
            SecretKeySpec chave = new SecretKeySpec(CHAVE.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, chave);
            byte[] decodificado = Base64.getDecoder().decode(valor);
            byte[] descriptografado = cipher.doFinal(decodificado);
            return new String(descriptografado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar", e);
        }
    }
}
