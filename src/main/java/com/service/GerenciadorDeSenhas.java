package com.service;

import com.model.Credencial;
import com.util.CriptografiaAES;
import com.util.GeradorDeSenhas;
import com.util.VerificadorVazamentoSenha;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GerenciadorDeSenhas {
    private final String caminhoArquivo = "credenciais.txt";
    private final List<Credencial> credenciais = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public GerenciadorDeSenhas() {
        carregarCredenciais();
    }

    public void iniciar() {
        int opcao;
        do {
            System.out.println("\n==== MENU GERENCIADOR DE SENHAS ====");
            System.out.println("1. Adicionar nova credencial");
            System.out.println("2. Listar credenciais");
            System.out.println("3. Buscar por serviço");
            System.out.println("4. Remover credencial");
            System.out.println("5. Verificar se uma senha foi vazada");
            System.out.println("6. Gerar senha segura");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> adicionarCredencial();
                case 2 -> listarCredenciais();
                case 3 -> buscarCredencial();
                case 4 -> removerCredencial();
                case 5 -> verificarSenhaVazada();
                case 6 -> gerarSenhaSegura();
                case 0 -> {
                    salvarCredenciais();
                    System.out.println("Saindo...");
                }
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
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
            System.out.println("Credencial adicionada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao criptografar a senha: " + e.getMessage());
        }
    }

    private void listarCredenciais() {
        if (credenciais.isEmpty()) {
            System.out.println("Nenhuma credencial salva.");
            return;
        }

        System.out.println("\nCredenciais:");
        for (Credencial c : credenciais) {
            try {
                String senhaDescriptografada = CriptografiaAES.descriptografar(c.getSenha());
                System.out.println("Serviço: " + c.getServico());
                System.out.println("Usuário: " + c.getUsuario());
                System.out.println("Senha: " + senhaDescriptografada);
                System.out.println("-----------------------------");
            } catch (Exception e) {
                System.out.println("Erro ao descriptografar senha de " + c.getServico());
            }
        }
    }

    private void buscarCredencial() {
        System.out.print("Digite o nome do serviço: ");
        String servicoBusca = scanner.nextLine();

        for (Credencial c : credenciais) {
            if (c.getServico().equalsIgnoreCase(servicoBusca)) {
                try {
                    String senhaDescriptografada = CriptografiaAES.descriptografar(c.getSenha());
                    System.out.println("Usuário: " + c.getUsuario());
                    System.out.println("Senha: " + senhaDescriptografada);
                    return;
                } catch (Exception e) {
                    System.out.println("Erro ao descriptografar a senha.");
                    return;
                }
            }
        }

        System.out.println("Serviço não encontrado.");
    }

    private void removerCredencial() {
        System.out.print("Digite o nome do serviço para remover: ");
        String servicoRemover = scanner.nextLine();

        for (Credencial c : credenciais) {
            if (c.getServico().equalsIgnoreCase(servicoRemover)) {
                credenciais.remove(c);
                System.out.println("Credencial removida com sucesso.");
                return;
            }
        }

        System.out.println("Serviço não encontrado.");
    }

    private void salvarCredenciais() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminhoArquivo))) {
            for (Credencial c : credenciais) {
                writer.println(c);
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar credenciais: " + e.getMessage());
        }
    }

    private void carregarCredenciais() {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Credencial c = Credencial.fromString(linha);
                if (c != null) {
                    credenciais.add(c);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar credenciais: " + e.getMessage());
        }
    }

    private void gerarSenhaSegura() {
        System.out.print("Informe o tamanho da senha: ");
        int tamanho = scanner.nextInt();
        scanner.nextLine(); // consumir quebra de linha
        String senhaSegura = GeradorDeSenhas.gerarSenha(tamanho);
        System.out.println("Senha gerada: " + senhaSegura);
    }

    private void verificarSenhaVazada() {
        System.out.print("Digite a senha que deseja verificar: ");
        String senha = scanner.nextLine();
        VerificadorVazamentoSenha.foiVazada(senha);
    }
}
