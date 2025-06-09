package br.com.teste.app;

import br.com.teste.model.Participante;
import br.com.teste.model.Responsavel;
import br.com.teste.service.ParticipanteService;
import br.com.teste.service.ResponsavelService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class LoginParticipante {

    private static Scanner sc = MenuInicial.getScanner();

    public void iniciarLogin() {
        boolean loginBemSucedido = false;
        while (!loginBemSucedido) {
            System.out.println("Bem-vindo ao Sistema de Eventos!");
            System.out.print("Você é (1) Participante ou (2) Responsável ou (0) Voltar ao Menu Principal? ");
            int opcao = -1;
            try {
                opcao = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.nextLine();
                continue;
            }

            if (opcao == 1) {
                System.out.print("Informe seu nome: ");
                String nome = sc.nextLine();

                System.out.print("Informe seu email: ");
                String email = sc.nextLine();

                System.out.print("Informe seu CPF: ");
                String cpf = sc.nextLine();

                ParticipanteService participanteService = new ParticipanteService();
                Participante participante = participanteService.validarLogin(nome, email, cpf);

                if (participante != null) {
                    System.out.println("Login de Participante realizado com sucesso!");
                    new MenuParticipante(sc).exibirMenuParticipante();
                    loginBemSucedido = true;
                } else {
                    System.out.println("Credenciais de Participante inválidas. Por favor, tente novamente.");
                }

            } else if (opcao == 2) {
                System.out.print("Informe seu nome (Responsável): ");
                String nome = sc.nextLine();

                System.out.print("Informe seu email (Responsável): ");
                String email = sc.nextLine();

                ResponsavelService responsavelService = new ResponsavelService();
                Responsavel responsavel = responsavelService.validarLogin(nome, email);

                if (responsavel != null) {
                    System.out.println("Login de Responsável realizado com sucesso!");
                    new MenuResponsavel(sc).exibirMenuResponsavel(responsavel);
                    loginBemSucedido = true;
                } else {
                    System.out.println("Credenciais de Responsável inválidas. Por favor, tente novamente.");
                }
            } else if (opcao == 0) {
                System.out.println("Voltando ao Menu Principal...");
                loginBemSucedido = true;
            } else {
                System.out.println("Opção inválida.");
            }
        }
    }
}