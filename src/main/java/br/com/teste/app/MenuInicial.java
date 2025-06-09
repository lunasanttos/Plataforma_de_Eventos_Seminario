package br.com.teste.app;

import java.util.Scanner;

public class MenuInicial {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Bem-vindo ao Sistema de Eventos!");

        try {
            System.out.print("O que você quer fazer: (1) Cadastrar-se ou (2) Login de Participante ou (3) Login de Responsável ? ");
            int opcao = sc.nextInt();
            sc.nextLine(); // consome o Enter

            if (opcao == 1) {
                System.out.print("Cadastrar-se como: (1) Participante ou (2) Responsável ou (3) Sair ? ");
                int tipo = sc.nextInt();
                sc.nextLine(); // consome o Enter

                switch (tipo) {
                    case 1:
                        CadastroParticipante.executarCadastroParticipante();
                        break;
                    case 2:
                        CadastroResponsavel.executarCadastroResponsavel();
                        break;
                    case 3:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } else if (opcao == 2) {
                new LoginParticipante().iniciarLogin();
            } else if (opcao == 3) {
                new LoginResponsavel().iniciarLogin(); // Novo: classe específica para login do responsável
            } else {
                System.out.println("Opção inválida.");
            }

        } catch (Exception e) {
            System.out.println("Entrada inválida. Tente novamente.");
            sc.nextLine(); // limpa o buffer
        }

        // Não feche o Scanner ligado a System.in
        // sc.close();
    }
}