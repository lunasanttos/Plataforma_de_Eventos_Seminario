package br.com.teste.app;

import br.com.teste.model.Responsavel;
import br.com.teste.service.ResponsavelService;

import java.util.Scanner;

public class LoginResponsavel {

    public void iniciarLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Informe seu nome: ");
        String nome = sc.nextLine();

        System.out.print("Informe seu email: ");
        String email = sc.nextLine();

        ResponsavelService responsavelService = new ResponsavelService();
        Responsavel responsavel = responsavelService.validarLogin(nome, email);

        if (responsavel != null) {
            System.out.println("Login realizado com sucesso!");
            menuResponsavel(responsavel);
        } else {
            System.out.println("Credenciais inválidas.");
        }

        // Não feche o Scanner que usa System.in
    }

    private void menuResponsavel(Responsavel responsavel) {
        System.out.println("Menu do Responsável: " + responsavel.getNome());
        // Adicionar funcionalidades específicas do Responsável aqui
    }
}