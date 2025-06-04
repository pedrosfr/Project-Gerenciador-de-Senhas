import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

public class CriptografiaAES {

    private static final String ALGORITMO = "AES/GCM/NoPadding";
    private static final int TAMANHO_CHAVE = 128; // Pode ser 192 ou 256 bits
    private static final int TAMANHO_IV = 12;     // 96 bits é o recomendado para GCM
    private static final int TAMANHO_TAG = 128;   // 128 bits para autenticação (MAC)

    // Gera uma chave segura AES
    public static SecretKey gerarChave() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(TAMANHO_CHAVE);
        return keyGen.generateKey();
    }

    // Criptografa texto e retorna base64(iv + texto criptografado)
    public static String criptografar(String texto, SecretKey chave) throws Exception {
        byte[] iv = new byte[TAMANHO_IV];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ALGORITMO);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAMANHO_TAG, iv);
        cipher.init(Cipher.ENCRYPT_MODE, chave, gcmSpec);

        byte[] criptografado = cipher.doFinal(texto.getBytes("UTF-8"));

        byte[] combinado = new byte[iv.length + criptografado.length];
        System.arraycopy(iv, 0, combinado, 0, iv.length);
        System.arraycopy(criptografado, 0, combinado, iv.length, criptografado.length);

        return Base64.getEncoder().encodeToString(combinado);
    }

    // Descriptografa base64(iv + criptografado)
    public static String descriptografar(String textoBase64, SecretKey chave) throws Exception {
        byte[] combinado = Base64.getDecoder().decode(textoBase64);

        byte[] iv = new byte[TAMANHO_IV];
        byte[] criptografado = new byte[combinado.length - TAMANHO_IV];

        System.arraycopy(combinado, 0, iv, 0, TAMANHO_IV);
        System.arraycopy(combinado, TAMANHO_IV, criptografado, 0, criptografado.length);

        Cipher cipher = Cipher.getInstance(ALGORITMO);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAMANHO_TAG, iv);
        cipher.init(Cipher.DECRYPT_MODE, chave, gcmSpec);

        byte[] descriptografado = cipher.doFinal(criptografado);

        return new String(descriptografado, "UTF-8");
    }
}
