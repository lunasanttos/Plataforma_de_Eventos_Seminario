package br.com.teste.app;

import br.com.teste.model.Participante;
import br.com.teste.service.ParticipanteService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuParticipante {

    private Scanner scanner;
    private ParticipanteService participanteService;
    private Participante participanteLogado;

    public MenuParticipante(Scanner scanner) {
        this.scanner = scanner;
        this.participanteService = new ParticipanteService();
    }

    public void exibirMenuParticipante() {
        boolean saiuDoSistema = false;
        while (!saiuDoSistema) {
            if (participanteLogado == null) {
                System.out.println("\n--- Bem-vindo(a)! Escolha uma opção: ---");
                System.out.println("1. Entrar (Login)");
                System.out.println("2. Fazer Nova Inscrição");
                System.out.println("0. Sair do Sistema");
                System.out.print("Sua opção: ");

                try {
                    int opcao = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcao) {
                        case 1:
                            realizarLogin();
                            break;
                        case 2:
                            fazerNovaInscricao();
                            break;
                        case 0:
                            System.out.println("Saindo do sistema...");
                            saiuDoSistema = true;
                            break;
                        default:
                            System.out.println("Opção inválida. Por favor, tente novamente.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, digite um número.");
                    scanner.nextLine();
                }
            } else {
                System.out.println("\n--- Olá, " + participanteLogado.getNome() + "! Escolha uma ação: ---");
                System.out.println("1. Editar Minha Inscrição");
                System.out.println("2. Excluir Minha Inscrição");
                System.out.println("3. Emitir Certificado");
                System.out.println("0. Deslogar");
                System.out.print("Sua opção: ");

                try {
                    int opcao = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcao) {
                        case 1:
                            editarInscricao();
                            break;
                        case 2:
                            excluirInscricao();
                            if (participanteLogado == null) {
                                System.out.println("Você foi deslogado.");
                            }
                            break;
                        case 3:
                            emitirCertificado();
                            break;
                        case 0:
                            System.out.println("Deslogando...");
                            participanteLogado = null;
                            break;
                        default:
                            System.out.println("Opção inválida. Por favor, tente novamente.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, digite um número.");
                    scanner.nextLine();
                }
            }
        }
    }

    private void realizarLogin() {
        System.out.println("\n--- Realizar Login ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        participanteLogado = participanteService.validarLogin(nome, email, cpf);

        if (participanteLogado != null) {
            System.out.println("Login bem-sucedido! Bem-vindo(a), " + participanteLogado.getNome() + "!");
        } else {
            System.out.println("Credenciais inválidas. Verifique seu nome, email e CPF.");
        }
    }

    private void fazerNovaInscricao() {
        System.out.println("\n--- Fazer Nova Inscrição ---");
        System.out.print("Nome completo: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("CPF (apenas números): ");
        String cpf = scanner.nextLine();

        Participante novoParticipante = new Participante(0, nome, email, cpf);
        boolean salvo = participanteService.inserir(novoParticipante);

        if (salvo) {
            System.out.println("Inscrição realizada com sucesso!");
            System.out.println("Tentando login automático com seus dados de inscrição...");
            participanteLogado = participanteService.validarLogin(nome, email, cpf);
            if(participanteLogado != null) {
                System.out.println("Login automático realizado!");
            } else {
                System.out.println("Não foi possível realizar login automático. Use a opção 'Entrar' para acessar sua conta.");
            }
        } else {
            System.out.println("Falha na inscrição. Verifique os dados. Talvez o CPF já esteja cadastrado.");
        }
    }

    private void editarInscricao() {
        if (participanteLogado == null) {
            System.out.println("Você precisa estar logado para editar sua inscrição.");
            return;
        }

        System.out.println("\n--- Editar Minha Inscrição ---");
        System.out.println("Seu ID: " + participanteLogado.getId_participante());
        System.out.println("Nome atual: " + participanteLogado.getNome());
        System.out.print("Novo Nome (deixe em branco para manter): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isEmpty()) {
            participanteLogado.setNome(novoNome);
        }

        System.out.println("Email atual: " + participanteLogado.getEmail());
        System.out.print("Novo Email (deixe em branco para manter): ");
        String novoEmail = scanner.nextLine();
        if (!novoEmail.isEmpty()) {
            participanteLogado.setEmail(novoEmail);
        }

        System.out.println("CPF atual: " + participanteLogado.getCpf());
        System.out.print("Novo CPF (deixe em branco para manter): ");
        String novoCpf = scanner.nextLine();
        if (!novoCpf.isEmpty()) {
            participanteLogado.setCpf(novoCpf);
        }

        boolean editado = participanteService.editar(participanteLogado);

        if (editado) {
            System.out.println("Inscrição editada com sucesso!");
        } else {
            System.out.println("Falha ao editar inscrição. Verifique os dados.");
        }
    }

    private void excluirInscricao() {
        if (participanteLogado == null) {
            System.out.println("Você precisa estar logado para excluir sua inscrição.");
            return;
        }

        System.out.println("\n--- Excluir Minha Inscrição ---");
        System.out.print("Tem certeza que deseja excluir sua inscrição (S/N)? ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("S")) {
            boolean excluido = participanteService.excluir(participanteLogado);
            if (excluido) {
                System.out.println("Inscrição excluída com sucesso!");
                participanteLogado = null;
            } else {
                System.out.println("Falha ao excluir inscrição.");
            }
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private void emitirCertificado() {
        if (participanteLogado == null) {
            System.out.println("Você precisa estar logado para emitir o certificado.");
            return;
        }

        System.out.println("\n--- Emissão de Certificado ---");
        System.out.println("Gerando certificado para: " + participanteLogado.getNome());
        System.out.println("CPF: " + participanteLogado.getCpf());
        System.out.println("\nCertificado simulado gerado com sucesso! (Funcionalidade real de emissão não implementada)");
        System.out.println("Este seria o local onde o certificado real seria gerado/baixado.");
    }
}