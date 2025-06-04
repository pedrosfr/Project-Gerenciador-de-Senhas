package com;

import com.model.Credencial;
import com.service.GerenciadorDeSenhas;
import com.util.Autenticador2FA;
import com.util.CriptografiaAES;

import java.util.logging.Level;
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
        gerenciador.iniciar(); // chama o menu interativo

        // Testar descriptografia com senha vinda de fonte segura (exemplo)
        String senhaCriptografada = obterSenhaCriptografadaSegura(); // evitar hardcoded
        Credencial c = new Credencial("gmail", "usuario@gmail.com", senhaCriptografada);

        try {
            String senhaDescriptografada = CriptografiaAES.descriptografar(c.getSenha());
            logger.info("Senha descriptografada com sucesso.");
            // Nunca exibir a senha em texto no log!
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Erro ao descriptografar: entrada inválida.", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro inesperado ao descriptografar.", e);
        }
    }

    // Exemplo de função segura (simulada)
    private static String obterSenhaCriptografadaSegura() {
        // Em produção, isso deveria vir de uma fonte segura, não estar no código.
        return "senhaCriptografadaAqui";
    }
}
