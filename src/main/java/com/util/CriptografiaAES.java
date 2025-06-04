package com.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.logging.Logger;

public class CriptografiaAES {
    private static final Logger logger = Logger.getLogger(CriptografiaAES.class.getName());
    private static final String CHAVE = System.getenv("CHAVE_AES"); // Evitar hardcoded!
    private static final String INIT_VECTOR = "RandomInitVector"; // 16 bytes

    public static String criptografar(String valor) throws Exception {
        if (CHAVE == null || CHAVE.length() != 16) {
            throw new IllegalArgumentException("Chave AES inválida.");
        }

        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
        SecretKeySpec key = new SecretKeySpec(CHAVE.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] encrypted = cipher.doFinal(valor.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String descriptografar(String valorCriptografado) throws Exception {
        if (CHAVE == null || CHAVE.length() != 16) {
            throw new IllegalArgumentException("Chave AES inválida.");
        }

        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
        SecretKeySpec key = new SecretKeySpec(CHAVE.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] original = cipher.doFinal(Base64.getDecoder().decode(valorCriptografado));
        return new String(original);
    }
}
