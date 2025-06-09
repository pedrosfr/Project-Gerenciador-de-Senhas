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
 * Esta classe implementa criptografia e descriptografia usando AES (Advanced Encryption Standard)
 * no modo GCM (Galois/Counter Mode), com derivação de chave usando PBKDF2.
 */
public class CriptografiaAES {

    // Frase base usada para derivar a chave criptográfica
    private static final String CHAVE_SECRETA_AES = "minhaChaveSuperSegura123!";

    // Algoritmo AES com GCM (autenticado e mais seguro que CBC)
    private static final String ALGORITMO = "AES/GCM/NoPadding";

    // Tamanhos constantes para salt, IV (vetor de inicialização) e chave
    private static final int SALT_LENGTH = 16; // bytes
    private static final int IV_LENGTH = 12;   // recomendado para GCM
    private static final int ITERATIONS = 65536; // número de iterações do PBKDF2
    private static final int KEY_LENGTH = 256;   // comprimento da chave AES
    private static final int GCM_TAG_LENGTH = 128; // comprimento do tag de autenticação (em bits)

    /**
     * Criptografa uma string usando AES/GCM.
     * @param valor O texto original a ser criptografado.
     * @return O texto criptografado em Base64.
     */
    public static String criptografar(String valor) {
        try {
            // Gera salt e IV aleatórios
            byte[] salt = new byte[SALT_LENGTH];
            byte[] ivBytes = new byte[IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);
            random.nextBytes(ivBytes);

            // Deriva uma chave a partir da senha e do salt
            SecretKeySpec chave = gerarChave(CHAVE_SECRETA_AES, salt);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, ivBytes);

            // Inicializa o cipher para criptografar
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, chave, gcmParameterSpec);

            // Criptografa o valor
            byte[] criptografado = cipher.doFinal(valor.getBytes());

            // Concatena salt + IV + texto criptografado para armazenar tudo junto
            byte[] combinado = new byte[salt.length + ivBytes.length + criptografado.length];
            System.arraycopy(salt, 0, combinado, 0, salt.length);
            System.arraycopy(ivBytes, 0, combinado, salt.length, ivBytes.length);
            System.arraycopy(criptografado, 0, combinado, salt.length + ivBytes.length, criptografado.length);

            // Codifica tudo em Base64 para facilitar o armazenamento e leitura
            return Base64.getEncoder().encodeToString(combinado);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar: " + e.getMessage(), e);
        }
    }

    /**
     * Descriptografa uma string em Base64 criptografada com AES/GCM.
     * @param valorCriptografado O valor criptografado em Base64.
     * @return O texto original (descriptografado).
     */
    public static String descriptografar(String valorCriptografado) {
        try {
            // Decodifica o valor criptografado
            byte[] combinado = Base64.getDecoder().decode(valorCriptografado);

            // Extrai o salt, IV e dados criptografados
            byte[] salt = new byte[SALT_LENGTH];
            byte[] ivBytes = new byte[IV_LENGTH];
            byte[] criptografado = new byte[combinado.length - SALT_LENGTH - IV_LENGTH];

            System.arraycopy(combinado, 0, salt, 0, SALT_LENGTH);
            System.arraycopy(combinado, SALT_LENGTH, ivBytes, 0, IV_LENGTH);
            System.arraycopy(combinado, SALT_LENGTH + IV_LENGTH, criptografado, 0, criptografado.length);

            // Deriva a chave com base no salt e na chave secreta
            SecretKeySpec chave = gerarChave(CHAVE_SECRETA_AES, salt);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, ivBytes);

            // Inicializa o cipher para descriptografar
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, chave, gcmParameterSpec);
            byte[] descriptografado = cipher.doFinal(criptografado);

            // Retorna o valor original como string
            return new String(descriptografado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar: " + e.getMessage(), e);
        }
    }

    /**
     * Gera uma chave criptográfica AES a partir de uma senha (frase secreta) e um salt.
     * Usa PBKDF2 com HMAC-SHA256.
     * @param segredo Frase secreta base para gerar a chave.
     * @param salt Salt aleatório.
     * @return Chave derivada para criptografia AES.
     */
    private static SecretKeySpec gerarChave(String segredo, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(segredo.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        byte[] chaveBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(chaveBytes, "AES");
    }
}
