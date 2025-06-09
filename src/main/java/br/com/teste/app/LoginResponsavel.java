package br.com.teste.app;

import br.com.teste.model.Responsavel;
import br.com.teste.service.ResponsavelService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class LoginResponsavel {

    private static Scanner sc = MenuInicial.getScanner();

    public void iniciarLogin() {
        boolean loginBemSucedido = false;
        while (!loginBemSucedido) {
            System.out.print("Informe seu nome: ");
            String nome = sc.nextLine();

            System.out.print("Informe seu email: ");
            String email = sc.nextLine();

            ResponsavelService responsavelService = new ResponsavelService();
            Responsavel responsavel = responsavelService.validarLogin(nome, email);

            if (responsavel != null) {
                System.out.println("Login de Responsável realizado com sucesso!");
                // Corrigido para chamar o novo MenuResponsavel e passar o responsável
                new MenuResponsavel(sc).exibirMenuResponsavel(responsavel);
                loginBemSucedido = true;
            } else {
                System.out.println("Credenciais inválidas. Por favor, tente novamente.");
                System.out.print("Deseja tentar novamente? (S/N): ");
                String tentarNovamente = sc.nextLine();
                if (!tentarNovamente.equalsIgnoreCase("S")) {
                    loginBemSucedido = true;
                }
            }
        }
    }
}