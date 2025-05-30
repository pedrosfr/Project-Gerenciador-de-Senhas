package util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;

public class VerificadorVazamentoSenha {

    public static boolean foiVazada(String senha) {
        try {
            String sha1 = sha1(senha).toUpperCase();
            String prefixo = sha1.substring(0, 5);
            String sufixo = sha1.substring(5);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.pwnedpasswords.com/range/" + prefixo))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String[] linhas = response.body().split("\n");
            for (String linha : linhas) {
                String[] partes = linha.split(":");
                if (partes[0].equalsIgnoreCase(sufixo)) {
                    int vezes = Integer.parseInt(partes[1].trim());
                    System.out.println("Senha encontrada em vazamentos! Repetida " + vezes + " vezes.");
                    return true;
                }
            }

            System.out.println("Senha segura: não encontrada em vazamentos.");
            return false;

        } catch (Exception e) {
            System.out.println("Erro ao verificar vazamento: " + e.getMessage());
            return false;
        }
    }

    private static String sha1(String senha) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(senha.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
