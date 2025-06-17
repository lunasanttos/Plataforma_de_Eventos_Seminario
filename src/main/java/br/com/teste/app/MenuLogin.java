package br.com.teste.app;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuLogin {

    private static Scanner scanner = MenuInicial.getScanner();

    public void exibirOpcoesLogin() {
        boolean voltarAoMenuPrincipal = false;
        while (!voltarAoMenuPrincipal) {
            System.out.println("\nBem-vindo ao Login");
            System.out.println("Você é:");
            System.out.println("1. Participante");
            System.out.println("2. Responsável");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");


            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        new LoginParticipante().iniciarLoginParticipante();
                        voltarAoMenuPrincipal = true;
                        break;
                    case 2:
                        new LoginResponsavel().iniciarLoginResponsavel();
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
                continue;
            }
        }
    }
}