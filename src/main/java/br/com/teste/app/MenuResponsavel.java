package br.com.teste.app;

import br.com.teste.model.Responsavel;
// Importe os Services e DAOs necessários para as futuras funcionalidades do responsável
// import br.com.teste.service.EventoService; // Exemplo: se o responsável gerenciar eventos

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuResponsavel {

    private Scanner scanner;
    private Responsavel responsavelLogado;
    // Adicione serviços que o responsável usaria, por exemplo:
    // private EventoService eventoService;

    public MenuResponsavel(Scanner scanner) {
        this.scanner = scanner;
        // this.eventoService = new EventoService(); // Exemplo: Inicialize serviços aqui
    }

    // Este método é chamado após o login bem-sucedido
    public void exibirMenuResponsavel(Responsavel responsavel) {
        this.responsavelLogado = responsavel; // Define o responsável logado
        boolean deslogar = false;

        while (!deslogar) {
            if (responsavelLogado == null) { // Caso o responsável deslogue ou haja algum erro
                System.out.println("\nVocê não está logado como Responsável. Voltando ao menu principal.");
                deslogar = true;
                continue;
            }

            System.out.println("\n--- Olá, Responsável " + responsavelLogado.getNome() + "! Escolha uma ação: ---");
            System.out.println("1. Visualizar Eventos (Funcionalidade de exemplo)");
            System.out.println("2. Criar Novo Evento (Funcionalidade de exemplo)");
            System.out.println("0. Deslogar");
            System.out.print("Sua opção: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha

                switch (opcao) {
                    case 1:
                        System.out.println("\nExibindo eventos...");
                        // Lógica para visualizar eventos (chamar EventoService.listar())
                        break;
                    case 2:
                        System.out.println("\nCriando novo evento...");
                        // Lógica para criar evento (chamar EventoService.inserir())
                        break;
                    case 0:
                        System.out.println("Deslogando do sistema de Responsável...");
                        this.responsavelLogado = null; // Limpa o responsável logado
                        deslogar = true; // Sai do loop do menu do responsável
                        break;
                    default:
                        System.out.println("Opção inválida. Por favor, tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine(); // Limpar o buffer
            } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}