package br.com.teste.app;

import br.com.teste.model.Participante;
import br.com.teste.service.ParticipanteService;


import java.util.Scanner;

public class LoginParticipante {

    private static Scanner scanner = MenuInicial.getScanner();

    public void iniciarLoginParticipante() {
        boolean loginBemSucedido = false;
        while (!loginBemSucedido) {
            System.out.println("\nLogin de Participante");
            System.out.print("Informe seu nome: ");
            String nome = scanner.nextLine();

            System.out.print("Informe seu email: ");
            String email = scanner.nextLine();

            System.out.print("Informe seu CPF: ");
            String cpf = scanner.nextLine();

            ParticipanteService participanteService = new ParticipanteService();
            Participante participante = participanteService.validarLogin(nome, email, cpf);

            if (participante != null) {
                System.out.println("Login de Participante realizado com sucesso!");

                new MenuParticipante(scanner, participante).exibirMenuParticipante();
                loginBemSucedido = true;

            } else {
                System.out.println("Credenciais de Participante inválidas. Por favor, tente novamente.");
                System.out.print("Deseja tentar novamente? (sim/não): ");
                String tentarNovamente = scanner.nextLine();
                if (!tentarNovamente.equalsIgnoreCase("S")&& !tentarNovamente.equalsIgnoreCase("SIM")) {
                    loginBemSucedido = true;
                }
            }
        }
    }
}