package com.service;

import com.model.Credencial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class GerenciadorDeSenhas {
    private final List<Credencial> credenciais = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(GerenciadorDeSenhas.class.getName());
    private final Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        boolean executando = true;

        while (executando) {
            System.out.println("\n1 - Adicionar credencial");
            System.out.println("2 - Listar credenciais");
            System.out.println("3 - Sair");
            System.out.print("Escolha uma opção: ");

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
                    break;
                default:
                    logger.warning("Opção inválida.");
            }
        }
    }

    private void adicionarCredencial() {
        System.out.print("Serviço: ");
        String servico = scanner.nextLine();

        System.out.print("Usuário: ");
        String usuario = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        try {
            String senhaCriptografada = com.util.CriptografiaAES.criptografar(senha);
            credenciais.add(new Credencial(servico, usuario, senhaCriptografada));
            logger.info("Credencial adicionada com sucesso.");
        } catch (Exception e) {
            logger.severe("Erro ao criptografar a senha.");
        }
    }

    private void listarCredenciais() {
        if (credenciais.isEmpty()) {
            logger.info("Nenhuma credencial salva.");
            return;
        }

        for (Credencial c : credenciais) {
            System.out.println("Serviço: " + c.getServico());
            System.out.println("Usuário: " + c.getUsuario());
            System.out.println("Senha Criptografada: " + c.getSenha());
            System.out.println("---");
        }
    }
}
