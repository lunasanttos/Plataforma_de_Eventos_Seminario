package br.com.teste.app;

import br.com.teste.model.Participante;
import br.com.teste.service.ParticipanteService;


import java.util.Scanner;

public class LoginParticipante {

    private static Scanner scanner = MenuInicial.getScanner(); // Padronizado para 'scanner'

    // O nome do método foi alterado para refletir que ele é específico para participante
    public void iniciarLoginParticipante() {
        boolean loginBemSucedido = false;
        while (!loginBemSucedido) {
            System.out.println("\n--- Login de Participante ---");
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
                // O MenuParticipante agora recebe o Participante logado diretamente
                new MenuParticipante(scanner, participante).exibirMenuParticipante();
                loginBemSucedido = true; // Sai do loop após login bem-sucedido
            } else {
                System.out.println("Credenciais de Participante inválidas. Por favor, tente novamente.");
                System.out.print("Deseja tentar novamente? (S/N): ");
                String tentarNovamente = scanner.nextLine();
                if (!tentarNovamente.equalsIgnoreCase("S")) {
                    loginBemSucedido = true; // Sai do loop se o usuário não quiser tentar novamente
                }
            }
        }
    }
}