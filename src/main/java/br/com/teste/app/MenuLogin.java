package br.com.teste.app;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuLogin {

    private static Scanner scanner = MenuInicial.getScanner(); // declara uma instacia do menu inicial

    public void exibirOpcoesLogin() { // metodo para exibir as opções de login
        boolean voltarAoMenuPrincipal = false; // controlar o loop
        while (!voltarAoMenuPrincipal) {
            System.out.println("\nBem-vindo ao Login");
            System.out.println("Você é:");
            System.out.println("1. Participante");
            System.out.println("2. Responsável");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");


            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consome o '\n'

                switch (opcao) {
                    case 1:
                        new LoginParticipante().iniciarLoginParticipante(); // Chama o login específico de Participante
                        voltarAoMenuPrincipal = true; // Assume que após o login/tentativa, volta para este menu
                        break;// encerra
                    case 2:
                        new LoginResponsavel().iniciarLoginResponsavel(); // cria uma instancia e Chama o login específico de Responsável
                        voltarAoMenuPrincipal = true; // Assume que após o login/tentativa, volta para este menu
                        break; //encerra
                    case 0:
                        System.out.println("Voltando ao Menu Principal...");
                        voltarAoMenuPrincipal = true;
                        break; //encerra
                    default:
                        System.out.println("Opção inválida. Por favor, tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine(); // Limpa o buffer em caso de erro
                continue;
            }
        }
    }
}