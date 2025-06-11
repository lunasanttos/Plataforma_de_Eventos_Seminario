//mesma logica do loginParticipante
package br.com.teste.app;

import br.com.teste.model.Responsavel;
import br.com.teste.service.ResponsavelService;
import java.util.Scanner;

public class LoginResponsavel {

    private static Scanner scanner = MenuInicial.getScanner(); // Padronizado para 'scanner'

    // O nome do método foi alterado para refletir que ele é específico para responsável
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
                // O MenuResponsavel agora recebe o Responsavel logado diretamente
                new MenuResponsavel(scanner, responsavel).exibirMenuResponsavel();
                loginBemSucedido = true; // Sai do loop após login bem-sucedido
            } else {
                System.out.println("Credenciais de Responsável inválidas. Por favor, tente novamente.");
                System.out.print("Deseja tentar novamente? (sim/não): ");
                String tentarNovamente = scanner.nextLine();
                if (!tentarNovamente.equalsIgnoreCase("S")&& !tentarNovamente.equalsIgnoreCase("SIM")) {
                    loginBemSucedido = true; // Sai do loop se o usuário não quiser tentar novamente
                }
            }
        }
    }
}