package com;

import com.model.Credencial;
import com.service.GerenciadorDeSenhas;
import com.util.Autenticador2FA;
import com.util.CriptografiaAES;

import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.info("Gerenciador de Senhas iniciado!");

        // Autenticação 2FA
        Autenticador2FA.enviarCodigo();
        if (!Autenticador2FA.validarCodigo()) {
            logger.warning("Autenticação falhou. Encerrando o programa.");
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
            logger.info("Senha descriptografada com sucesso: " + senhaDescriptografada);
        } catch (Exception e) {
            logger.severe("Erro ao descriptografar: " + e.getMessage());
        }
    }
}
