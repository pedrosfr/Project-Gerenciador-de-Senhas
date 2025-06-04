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
            gerenciador.iniciar(); // chama o menu interativo

            // Exemplo para testar descriptografia
            Credencial c = new Credencial("gmail", "usuario@gmail.com", "senhaCriptografadaAqui");

            try {
                String senhaDescriptografada = CriptografiaAES.descriptografar(c.getSenha());
                System.out.println("Senha descriptografada: " + senhaDescriptografada);
            } catch (Exception e) {
                System.out.println("Erro ao descriptografar: " + e.getMessage());
            }
        }
    }


