package br.com.teste.app;

import br.com.teste.config.Conexao;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuInicial {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao Sistema de Eventos!");

        boolean sairDoSistema = false;
        while (!sairDoSistema) {
            try {
                System.out.println("\n--- Menu Principal ---");
                System.out.println("O que você quer fazer: (1) Cadastrar-se ou (2) Login de Participante ou (3) Login de Responsável ou (0) Sair? ");
                System.out.print("Sua opção: ");

                int opcaoPrincipal = scanner.nextInt();
                scanner.nextLine();

                switch (opcaoPrincipal) {
                    case 1:
                        exibirMenuCadastro();
                        break;
                    case 2:
                        handleLoginParticipante();
                        break;
                    case 3:
                        handleLoginResponsavel();
                        break;
                    case 0:
                        System.out.println("Saindo do Sistema. Até logo!");
                        sairDoSistema = true;
                        break;
                    default:
                        System.out.println("Opção inválida. Por favor, tente novamente.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }

        scanner.close();
      //  Conexao.getInstance().closeConnection();
    }

    private static void exibirMenuCadastro() {
        boolean voltarAoMenuPrincipal = false;
        while (!voltarAoMenuPrincipal) {
            try {
                System.out.println("\n--- Menu de Cadastro ---");
                System.out.println("1. Cadastrar Participante");
                System.out.println("2. Cadastrar Responsável");
                System.out.println("0. Voltar ao Menu Principal");
                System.out.print("Sua opção: ");

                int tipoCadastro = scanner.nextInt();
                scanner.nextLine();

                switch (tipoCadastro) {
                    case 1:
                        CadastroParticipante.executarCadastroParticipante();
                        voltarAoMenuPrincipal = true;
                        break;
                    case 2:
                        CadastroResponsavel.executarCadastroResponsavel();
                        voltarAoMenuPrincipal = true;
                        break;
                    case 0:
                        System.out.println("Voltando ao Menu Principal...");
                        voltarAoMenuPrincipal = true;
                        break;
                    default:
                        System.out.println("Opção inválida. Por favor, tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado durante o cadastro: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void handleLoginParticipante() {
        LoginParticipante loginParticipante = new LoginParticipante();
        loginParticipante.iniciarLogin();
    }

    private static void handleLoginResponsavel() {
        LoginResponsavel loginResponsavel = new LoginResponsavel();
        loginResponsavel.iniciarLogin();
    }

    public static Scanner getScanner() {
        return scanner;
    }
}