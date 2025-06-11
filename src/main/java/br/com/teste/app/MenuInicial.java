package br.com.teste.app;

import br.com.teste.config.Conexao; // responsavel por gerenciar a conexão com o banco de dados
import java.util.InputMismatchException; // tratamento de exceção nextInt
import java.util.Scanner;  // CLASSE SCANNER DO JAVA

public class MenuInicial {

    private static Scanner scanner = new Scanner(System.in); // declara uma instância estatica e privada da classe scanner
    // Cria o objeto Scanner que lê dados da entrada padrão (o console/teclado).
    public static void main(String[] args) { // metodo inicial que é executado quando inicia o programa
        System.out.println("Bem-vindo ao Sistema de Eventos!");

        boolean sairDoSistema = false; // variavel  boleana para controlar o loop do menu inicial, inicializamos ela com valor falso
        while (!sairDoSistema) { // executa quando o usuario sai do sistema
            try { // tratamento de erros para exibir oções no menu
                System.out.println("\nMenu Principal");
                System.out.println("O que você quer fazer:");
                System.out.println("1) Cadastrar-se");
                System.out.println("2) Fazer Login");
                System.out.println("0) Sair?");
                System.out.print("Sua opção: ");

                int opcaoPrincipal = scanner.nextInt(); // permite a capitura somente de numero inteiros
                scanner.nextLine(); // consome/pula uma linha
                // usado para que o usuario escola suas opções
                switch (opcaoPrincipal) {
                    case 1:
                        exibirMenuCadastro(); // chama o metodo menu para exibir o submenu
                        break;
                    case 2:
                        // Chama o novo MenuLogin para que ele lide com a escolha Participante/Responsável
                        new MenuLogin().exibirOpcoesLogin(); // cria uma instancia do menulogin e chama seu metodo exibir opções de login
                        break;
                    case 0:
                        System.out.println("Saindo do Sistema. Até logo!");
                        sairDoSistema = true; // recebe valor verdadeiro e sai do sistema
                        break;
                    default:
                        System.out.println("Opção inválida. Por favor, tente novamente.");
                }

            } catch (InputMismatchException e) { // tratamento de execeção
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();// limpa a entrada
            } catch (Exception e) { // captura qualquer outro tipo de erro e imprime o gastreamento da pilha para a depuração
                System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace(); // o que é?
            }
        }

        scanner.close(); // apos o loop principal encerra o scanner é fechado para pode liberar os recursos
          Conexao.getInstance().closeConnection(); // tbm fecha a conexão com o banco de dados
    }
// metodo exibir menuCadastro
    private static void exibirMenuCadastro() {
        boolean voltarAoMenuPrincipal = false; // variavel boleana com valor falso para controlar o loop
        while (!voltarAoMenuPrincipal) { // o loop continua enquanto o usuario nao escolher voltar ao menu principal
            try { // tratamento de erros para lidar com entras invalidas
                System.out.println("\n Menu de Cadastro");
                System.out.println("1. Cadastrar Participante");
                System.out.println("2. Cadastrar Responsável");
                System.out.println("0. Voltar ao Menu Principal");
                System.out.print("Sua opção: ");

                int tipoCadastro = scanner.nextInt(); // ler a opção de cadastro
                scanner.nextLine();

                switch (tipoCadastro) { // switch recebe o tipo de cadastro como argumento
                    case 1: // chama o metodo estatico executar cadastro participante do cadastro do participante
                        CadastroParticipante.executarCadastroParticipante();
                        voltarAoMenuPrincipal = true; // Volta ao menu principal após cadastro por receber valor verdadeiro
                        break; // encerra
                    case 2:
                        CadastroResponsavel.executarCadastroResponsavel(); // o mesmo acontece aqui porem chama o metodo estatico do cadastro do responsavel
                        voltarAoMenuPrincipal = true; // Volta ao menu principal após cadastro
                        break; // encerra
                    case 0:
                        System.out.println("Voltando ao Menu Principal...");
                        voltarAoMenuPrincipal = true; // volta pra o menu inicial
                        break; // encerra
                    default: // Trata opções inválidas
                        System.out.println("Opção inválida. Por favor, tente novamente."); // continua com o loop ate que uma opção verdadeira seja selecionada
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado durante o cadastro: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static Scanner getScanner() {
        return scanner;
    }
    /*Este método público e estático permite que outras classes (como LoginParticipante e LoginResponsavel que vimos antes)
              acessem e usem a mesma instância única de Scanner que MenuInicial está usando. Isso é crucial para evitar problemas quando diferentes
               partes do código tentam ler a entrada do usuário.*/
}