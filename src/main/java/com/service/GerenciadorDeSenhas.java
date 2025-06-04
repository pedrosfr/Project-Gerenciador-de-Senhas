package com.service;

import com.model.Credencial;
import com.util.CriptografiaAES;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class GerenciadorDeSenhas {
    private static final Logger logger = Logger.getLogger(GerenciadorDeSenhas.class.getName());
    private static final int OP_ADICIONAR = 1;
    private static final int OP_LISTAR = 2;
    private static final int OP_SAIR = 3;

    private final List<Credencial> credenciais = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        boolean executando = true;

        while (executando) {
            exibirMenu();
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    adicionarCredencial();
                    break;
                case "2":
                    listarCredenciais();
                    break;
                case "3":
                    executando = false;
                    logger.info("Encerrando o Gerenciador de Senhas.");
                    break;
                default:
                    logger.warning("Opção inválida.");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("1 - Adicionar credencial");
        System.out.println("2 - Listar credenciais");
        System.out.println("3 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void adicionarCredencial() {
        System.out.print("Serviço: ");
        String servico = scanner.nextLine();

        System.out.print("Usuário: ");
        String usuario = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        try {
            String senhaCriptografada = CriptografiaAES.criptografar(senha);
            credenciais.add(new Credencial(servico, usuario, senhaCriptografada));
            logger.info("Credencial adicionada com sucesso para o serviço: " + servico);
        } catch (Exception e) {
            logger.severe("Erro ao criptografar a senha: " + e.getMessage());
        }
    }

    private void listarCredenciais() {
        if (credenciais.isEmpty()) {
            logger.info("Nenhuma credencial salva.");
            return;
        }

        for (Credencial c : credenciais) {
            logger.info("Serviço: " + c.getServico());
            logger.info("Usuário: " + c.getUsuario());
            logger.info("Senha Criptografada: " + c.getSenha());
            logger.info("---");
        }
    }
}
