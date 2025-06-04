package com;

import com.model.Credencial;
import com.service.GerenciadorDeSenhas;
import com.util.Autenticador2FA;
import com.util.CriptografiaAES;

public class Main {
    public static void main(String[] args) {
        System.out.println("Gerenciador de Senhas iniciado!");

        Autenticador2FA.enviarCodigo();
        if (!Autenticador2FA.validarCodigo()) {
            System.out.println("Autenticação falhou. Encerrando o programa.");
            return;
        }

        GerenciadorDeSenhas gerenciador = new GerenciadorDeSenhas();
        gerenciador.iniciar();

        // Exemplo de teste com criptografia e descriptografia usando CBC com IV
        try {
            String senhaOriginal = "minhaSenhaSegura123!";
            String senhaCriptografada = CriptografiaAES.criptografar(senhaOriginal);
            String senhaDescriptografada = CriptografiaAES.descriptografar(senhaCriptografada);

            System.out.println("\n--- Teste de Criptografia AES CBC ---");
            System.out.println("Senha original: " + senhaOriginal);
            System.out.println("Senha criptografada (base64): " + senhaCriptografada);
            System.out.println("Senha descriptografada: " + senhaDescriptografada);
        } catch (Exception e) {
            System.out.println("Erro durante teste de criptografia: " + e.getMessage());
        }
    }
}
