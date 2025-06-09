package br.com.teste.app;

import br.com.teste.model.Participante;
import br.com.teste.model.Responsavel;
import br.com.teste.service.ParticipanteService;
import br.com.teste.service.ResponsavelService;

import java.util.Scanner;

public class LoginParticipante {

    public void iniciarLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Bem-vindo ao Sistema de Eventos!");
        System.out.print("Você é (1) Participante ou (2) Responsável? ");
        int opcao = sc.nextInt();
        sc.nextLine(); // consumir quebra de linha

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
                System.out.println("Login realizado com sucesso!");
                menuParticipante(participante);
            } else {
                System.out.println("Credenciais inválidas.");
            }

        } else if (opcao == 2) {
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

        } else {
            System.out.println("Opção inválida.");
        }

        sc.close();
    }

    private void menuParticipante(Participante participante) {
        System.out.println("Menu do Participante: " + participante.getNome());
        // implementar menu real aqui
    }

    private void menuResponsavel(Responsavel responsavel) {
        System.out.println("Menu do Responsável: " + responsavel.getNome());
        // implementar menu real aqui
    }
}