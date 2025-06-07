package com.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Classe utilitária para criptografia e descriptografia de dados
 * utilizando o algoritmo AES com modo GCM (autenticado e seguro).
 *
 * Requer a variável de ambiente CHAVE_SECRETA_AES definida.
 */
public class CriptografiaAES {

    private static final String ALGORITMO = "AES/GCM/NoPadding";
    private static final String SEGREDO;

    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 12;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int GCM_TAG_LENGTH = 128;

    // Bloco estático para validação imediata da chave secreta
    static {
        SEGREDO = System.getenv("CHAVE_SECRETA_AES");
        if (SEGREDO == null || SEGREDO.isEmpty()) {
            System.err.println("[ERRO] Variável de ambiente CHAVE_SECRETA_AES não está definida.");
            throw new IllegalStateException("A variável de ambiente CHAVE_SECRETA_AES deve ser configurada para que a criptografia funcione.");
        }
    }

    /**
     * Criptografa uma string usando AES-GCM.
     *
     * @param valor Texto puro a ser criptografado
     * @return Texto criptografado em Base64 (com salt e IV incluídos)
     */
    public static String criptografar(String valor) {
        try {
            byte[] salt = new byte[SALT_LENGTH];
            byte[] ivBytes = new byte[IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);
            random.nextBytes(ivBytes);

            SecretKeySpec chave = gerarChave(SEGREDO, salt);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, ivBytes);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, chave, gcmParameterSpec);
            byte[] criptografado = cipher.doFinal(valor.getBytes());

            byte[] combinado = new byte[salt.length + ivBytes.length + criptografado.length];
            System.arraycopy(salt, 0, combinado, 0, salt.length);
            System.arraycopy(ivBytes, 0, combinado, salt.length, ivBytes.length);
            System.arraycopy(criptografado, 0, combinado, salt.length + ivBytes.length, criptografado.length);

            return Base64.getEncoder().encodeToString(combinado);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar: " + e.getMessage(), e);
        }
    }

    /**
     * Descriptografa uma string Base64 criptografada por este mesmo sistema.
     *
     * @param valorCriptografado Texto criptografado em Base64
     * @return Texto original
     */
    public static String descriptografar(String valorCriptografado) {
        try {
            byte[] combinado = Base64.getDecoder().decode(valorCriptografado);

            byte[] salt = new byte[SALT_LENGTH];
            byte[] ivBytes = new byte[IV_LENGTH];
            byte[] criptografado = new byte[combinado.length - SALT_LENGTH - IV_LENGTH];

            System.arraycopy(combinado, 0, salt, 0, SALT_LENGTH);
            System.arraycopy(combinado, SALT_LENGTH, ivBytes, 0, IV_LENGTH);
            System.arraycopy(combinado, SALT_LENGTH + IV_LENGTH, criptografado, 0, criptografado.length);

            SecretKeySpec chave = gerarChave(SEGREDO, salt);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, ivBytes);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, chave, gcmParameterSpec);
            byte[] descriptografado = cipher.doFinal(criptografado);

            return new String(descriptografado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar: " + e.getMessage(), e);
        }
    }

    /**
     * Gera uma chave AES segura derivada de uma senha e salt utilizando PBKDF2.
     *
     * @param segredo Senha base
     * @param salt Salt aleatório
     * @return Chave AES derivada
     */
    private static SecretKeySpec gerarChave(String segredo, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(segredo.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        byte[] chaveBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(chaveBytes, "AES");
    }
}
