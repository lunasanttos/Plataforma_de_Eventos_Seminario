package br.com.teste.app;

import br.com.teste.model.Responsavel;
import br.com.teste.service.ResponsavelService;
import java.util.Scanner;

public class LoginResponsavel {

    private static Scanner scanner = MenuInicial.getScanner();


    public void iniciarLoginResponsavel() {
        boolean loginBemSucedido = false;
        while (!loginBemSucedido) {
            System.out.println("\n--- Login de Responsável ---");
            System.out.print("Informe seu nome: ");
            String nome = scanner.nextLine();

            System.out.print("Informe seu email: ");
            String email = scanner.nextLine();

            ResponsavelService responsavelService = new ResponsavelService();
            Responsavel responsavel = responsavelService.validarLogin(nome, email);

            if (responsavel != null) {
                System.out.println("Login de Responsável realizado com sucesso!");

                new MenuResponsavel(scanner, responsavel).exibirMenuResponsavel();
                loginBemSucedido = true;
            } else {
                System.out.println("Credenciais de Responsável inválidas. Por favor, tente novamente.");
                System.out.print("Deseja tentar novamente? (sim/não): ");
                String tentarNovamente = scanner.nextLine();
                if (!tentarNovamente.equalsIgnoreCase("S")&& !tentarNovamente.equalsIgnoreCase("SIM")) {
                    loginBemSucedido = true;
                }
            }
        }
    }
}