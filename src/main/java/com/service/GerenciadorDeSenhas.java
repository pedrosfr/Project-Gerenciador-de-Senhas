package com.service;

import com.model.Credencial;
import com.util.CriptografiaAES;
import com.util.GeradorDeSenhas;
import com.util.VerificadorVazamentoSenha;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GerenciadorDeSenhas {
    private static final Logger logger = Logger.getLogger(GerenciadorDeSenhas.class.getName());
    private final String caminhoArquivo = "credenciais.txt";
    private final List<Credencial> credenciais = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public GerenciadorDeSenhas() {
        carregarCredenciais();
    }

    public void iniciar() {
        int opcao;
        do {
            exibirMenu();
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                processarOpcao(opcao);
            } catch (NumberFormatException e) {
                logger.warning("Opção inválida. Digite um número.");
                opcao = -1;
            }
        } while (opcao != 0);
    }

    private void exibirMenu() {
        logger.info("\n==== MENU GERENCIADOR DE SENHAS ====");
        logger.info("1. Adicionar nova credencial");
        logger.info("2. Listar credenciais");
        logger.info("3. Buscar por serviço");
        logger.info("4. Remover credencial");
        logger.info("5. Verificar se uma senha foi vazada");
        logger.info("6. Gerar senha segura");
        logger.info("0. Sair");
        logger.info("Escolha uma opção: ");
    }

    private void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> adicionarCredencial();
            case 2 -> listarCredenciais();
            case 3 -> buscarCredencial();
            case 4 -> removerCredencial();
            case 5 -> verificarSenhaVazada();
            case 6 -> gerarSenhaSegura();
            case 0 -> {
                salvarCredenciais();
                logger.info("Saindo...");
            }
            default -> logger.warning("Opção inválida.");
        }
    }

    private void adicionarCredencial() {
        logger.info("Serviço: ");
        String servico = scanner.nextLine();
        logger.info("Usuário: ");
        String usuario = scanner.nextLine();
        logger.info("Senha: ");
        String senha = scanner.nextLine();

        try {
            String senhaCriptografada = CriptografiaAES.criptografar(senha);
            credenciais.add(new Credencial(servico, usuario, senhaCriptografada));
            logger.info("Credencial adicionada com sucesso!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao criptografar a senha: ", e);
        }
    }

    private void listarCredenciais() {
        if (credenciais.isEmpty()) {
            logger.info("Nenhuma credencial salva.");
            return;
        }

        logger.info("\nCredenciais:");
        for (Credencial c : credenciais) {
            try {
                String senhaDescriptografada = CriptografiaAES.descriptografar(c.getSenha());
                logger.log(Level.INFO, "Serviço: {0}", c.getServico());
                logger.log(Level.INFO, "Usuário: {0}", c.getUsuario());
                logger.log(Level.INFO, "Senha: {0}", mascararSenha(senhaDescriptografada));
                logger.info("-----------------------------");
            } catch (Exception e) {
                logger.log(Level.WARNING, "Erro ao descriptografar senha de {0}", c.getServico());
            }
        }
    }

    private void buscarCredencial() {
        logger.info("Digite o nome do serviço: ");
        String servicoBusca = scanner.nextLine();

        for (Credencial c : credenciais) {
            if (c.getServico().equalsIgnoreCase(servicoBusca)) {
                try {
                    String senhaDescriptografada = CriptografiaAES.descriptografar(c.getSenha());
                    logger.log(Level.INFO, "Usuário: {0}", c.getUsuario());
                    logger.log(Level.INFO, "Senha: {0}", mascararSenha(senhaDescriptografada));
                    return;
                } catch (Exception e) {
                    logger.warning("Erro ao descriptografar a senha.");
                    return;
                }
            }
        }

        logger.info("Serviço não encontrado.");
    }

    private void removerCredencial() {
        logger.info("Digite o nome do serviço para remover: ");
        String servicoRemover = scanner.nextLine();

        boolean removido = credenciais.removeIf(c -> c.getServico().equalsIgnoreCase(servicoRemover));

        if (removido) {
            logger.info("Credencial removida com sucesso.");
        } else {
            logger.info("Serviço não encontrado.");
        }
    }

    private void salvarCredenciais() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminhoArquivo))) {
            for (Credencial c : credenciais) {
                writer.println(c);
            }
            logger.info("Credenciais salvas com sucesso.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao salvar credenciais: ", e);
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
            logger.info("Credenciais carregadas com sucesso.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao carregar credenciais: ", e);
        }
    }

    private void gerarSenhaSegura() {
        try {
            logger.info("Informe o tamanho da senha: ");
            int tamanho = Integer.parseInt(scanner.nextLine());
            String senhaSegura = GeradorDeSenhas.gerarSenha(tamanho);
            logger.log(Level.INFO, "Senha gerada: {0}", mascararSenha(senhaSegura));
        } catch (NumberFormatException e) {
            logger.warning("Tamanho inválido. Digite um número.");
        }
    }

    private void verificarSenhaVazada() {
        logger.info("Digite a senha que deseja verificar: ");
        String senha = scanner.nextLine();
        try {
            boolean vazada = VerificadorVazamentoSenha.foiVazada(senha);
            if (vazada) {
                logger.warning("ATENÇÃO: Esta senha foi vazada em bancos de dados públicos!");
            } else {
                logger.info("Senha segura: não encontrada em vazamentos conhecidos.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao verificar senha: ", e);
        }
    }

    private String mascararSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            return "[vazia]";
        }
        if (senha.length() <= 2) {
            return "****";
        }
        return senha.charAt(0) + "*****" + senha.charAt(senha.length() - 1);
    }

}
