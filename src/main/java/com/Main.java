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

        Autenticador2FA.enviarCodigo();
        if (!Autenticador2FA.validarCodigo()) {
            logger.warning("Autenticação falhou. Encerrando o programa.");
            return;
        }

        GerenciadorDeSenhas gerenciador = new GerenciadorDeSenhas();
        gerenciador.iniciar();

        // Teste de descriptografia (evite expor senhas)
        Credencial c = new Credencial("gmail", "usuario@gmail.com", "senhaCriptografadaAqui");
        try {
            String resultado = CriptografiaAES.descriptografar(c.getSenha());
            logger.info("Teste de descriptografia executado com sucesso.");
        } catch (Exception e) {
            logger.severe("Erro ao descriptografar: " + e.getMessage());
        }
    }
}
