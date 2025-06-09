package br.com.teste.app;

import java.util.Scanner;                // Import Scanner
import br.com.teste.model.Participante; // Ajuste para seu pacote
import br.com.teste.service.ParticipanteService;

public class CadastroParticipante {

    public static void executarCadastroParticipante() {
        Scanner scanner = new Scanner(System.in);
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
            System.out.println("ID (informado, não o gerado pelo BD): " + novoParticipante.getId_participante());
        } else {
            System.out.println("\nFalha ao cadastrar participante. Verifique os dados.");
        }

        // Não feche o scanner se for usado em outros lugares
    }

    public static void main(String[] args) {
        executarCadastroParticipante();
    }
}