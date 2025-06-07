package com;

import com.model.Credencial;
import com.service.GerenciadorDeSenhas;
import com.util.Autenticador2FA;
import com.util.CriptografiaAES;

public class Main {
    public static void main(String[] args) {
        System.out.println("Gerenciador de Senhas iniciado!");

        // Autenticação 2FA
        Autenticador2FA.enviarCodigo();
        if (!Autenticador2FA.validarCodigo()) {
            System.err.println("Autenticação falhou. Encerrando o programa.");
            return;
        }

        // Inicializa o gerenciador de senhas
        GerenciadorDeSenhas gerenciador = new GerenciadorDeSenhas();
        gerenciador.iniciar();

        // Exemplo: Criptografa uma senha real
        String senhaOriginal = "MinhaSenhaUltraSecreta123";
        String senhaCriptografada = CriptografiaAES.criptografar(senhaOriginal);

        // Cria credencial com senha criptografada
        Credencial credencial = new Credencial("gmail", "usuario@gmail.com", senhaCriptografada);

        try {
            // Descriptografa a senha criptografada (somente para teste)
            String senhaDescriptografada = CriptografiaAES.descriptografar(credencial.getSenha());
            System.out.println("Senha descriptografada com sucesso: " + senhaDescriptografada);
        } catch (Exception e) {
            System.err.println("Erro ao descriptografar: " + e.getMessage());
        }
    }
}
