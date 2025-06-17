package br.com.teste.app;

import br.com.teste.model.Participante; //objeto do dados
import br.com.teste.service.ParticipanteService; // validação de login e logica de negocios


import java.util.Scanner;

public class LoginParticipante {

    private static Scanner scanner = MenuInicial.getScanner(); // Padronizado para 'scanner', ou seja reutilizamos essa instancia para garantir que o sistama use um unico scanner para evitar conflitos e fechamentos inadequados.

    //
    public void iniciarLoginParticipante() {
        boolean loginBemSucedido = false; // variavel boleana inicializada com false para controlar o loop de login, necessario ser false pq ela ainda nao teve um login bem sucedido
        while (!loginBemSucedido) { // usamos o loop while pq enquanto o login for falso ele continuará a ser executado até que seja verdadeiro
            System.out.println("\nLogin de Participante");
            System.out.print("Informe seu nome: ");
            String nome = scanner.nextLine(); //netLine lê o nome digitado

            System.out.print("Informe seu email: ");
            String email = scanner.nextLine();

            System.out.print("Informe seu CPF: ");
            String cpf = scanner.nextLine();

            ParticipanteService participanteService = new ParticipanteService(); // criamos uma nova instancia para chamar o metodo la no participanteservice que fará a validação de login
            Participante participante = participanteService.validarLogin(nome, email, cpf);
            // por meio das variaveis do tipo string nome, email e cpf chamamos o metodo de validar o login e dando esse dados como argumento
            if (participante != null) { // se  o participante for diferente de null
                System.out.println("Login de Participante realizado com sucesso!");
                // O MenuParticipante agora recebe o Participante logado diretamente
                new MenuParticipante(scanner, participante).exibirMenuParticipante();
                loginBemSucedido = true; // isso faz com que a vareavel boleana login bem
                // Sai do loop após login bem-sucedido
                // cria uma instancia de menu participante, é aqui que diferecionamos o menu especifico para esse tipo de usuario.
                //ele passa as informações do scanner compartilhado e o objeto participante logado
            } else {
                System.out.println("Credenciais de Participante inválidas. Por favor, tente novamente.");
                System.out.print("Deseja tentar novamente? (sim/não): ");
                String tentarNovamente = scanner.nextLine();
                if (!tentarNovamente.equalsIgnoreCase("S")&& !tentarNovamente.equalsIgnoreCase("SIM")) {
                    loginBemSucedido = true; // Sai do loop se o usuário não quiser tentar novamente
                }
            }
        }
    }
}