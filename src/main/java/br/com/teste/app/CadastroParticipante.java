package br.com.teste.app;

import java.util.Scanner;
import br.com.teste.model.Participante;
import br.com.teste.service.ParticipanteService;

public class CadastroParticipante {
    public static void main(String[] args) {
        executarCadastroParticipante();
    }

    private static Scanner scanner = MenuInicial.getScanner();

    public static void executarCadastroParticipante() {
        ParticipanteService service = new ParticipanteService();

        System.out.println("Cadastro de Participante ");

        System.out.print("Nome do Participante: ");
        String nomeParticipante = scanner.nextLine();

        System.out.print("Email do Participante: ");
        String emailParticipante = scanner.nextLine();

        System.out.print("CPF do Participante (apenas números): ");
        String cpfParticipante = scanner.nextLine();

        Participante novoParticipante = new Participante(
                0,
                nomeParticipante,
                emailParticipante,
                cpfParticipante
        );

        boolean participanteFoiSalvo = service.inserir(novoParticipante);

        if (participanteFoiSalvo) {
            System.out.println("\nParticipante cadastrado com sucesso!");
            System.out.println("Detalhes:");
            System.out.println("Nome: " + novoParticipante.getNome());
            System.out.println("Email: " + novoParticipante.getEmail());
            System.out.println("CPF: " + novoParticipante.getCpf());
            System.out.println("ID : " + novoParticipante.getId_participante());

            System.out.println("\nPor favor, faça login com seus dados de participante.");
            new LoginParticipante().iniciarLoginParticipante();
        } else {
            System.out.println("\nFalha ao cadastrar participante. Verifique os dados.");
        }
    }


}