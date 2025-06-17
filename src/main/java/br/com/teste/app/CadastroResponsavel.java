package br.com.teste.app;

import br.com.teste.model.Responsavel;
import br.com.teste.service.ResponsavelService;

import java.util.Scanner;

public class

CadastroResponsavel {

    private static Scanner scanner = MenuInicial.getScanner();

    public static void executarCadastroResponsavel() {
        ResponsavelService service = new ResponsavelService();

        System.out.println("Cadastro de Responsável");

        System.out.print("Nome do Responsável: ");
        String nomeResponsavel = scanner.nextLine();

        System.out.print("Email do Responsável: ");
        String emailResponsavel = scanner.nextLine();

        Responsavel novoResponsavel = new Responsavel(
                0,
                nomeResponsavel,
                emailResponsavel
        );

        boolean responsavelFoiSalvo = service.inserir(novoResponsavel);

        if (responsavelFoiSalvo) {
            System.out.println("\nResponsável Cadastrado com Sucesso");
            System.out.println("Detalhes:");
            System.out.println("Nome: " + novoResponsavel.getNome());
            System.out.println("Email: " + novoResponsavel.getEmail());
            System.out.println("ID : " + novoResponsavel.getId_responsavel());

            System.out.println("\nPor favor, faça login com seus dados de responsável.");
            new LoginResponsavel().iniciarLoginResponsavel();

        } else {
            System.out.println("\nFalha ao cadastrar responsável. Verifique os dados.");
        }

        System.out.println("\nCadastro concluído!");
    }
}