package br.com.teste.app; // caminho do nosso arquivo
import java.util.Scanner; // importação da clsse scanner para obter dados por meio dessa entrada
import br.com.teste.model.Participante; // modal do participante: modelo de dados que precisaremos
import br.com.teste.service.ParticipanteService; // service do participante: empacotar tudo para enivar para o service pois é ele que faz todo o trabalho de cadastrar o participante a aplicar a regra de negocio negocio
// nao precisamos importar classes de login e menu pois fazem parte da mesma pasta.
public class CadastroParticipante { // nossa classe principal, o main é o ponto de partida do nosso projeto

         private static Scanner scanner = MenuInicial.getScanner(); // criamos uma stancia do scanner para podermos utiliza-lo
// no caso estamos instanciando a classe scanner no metodo do menuinicial.
    public static void executarCadastroParticipante() {
        ParticipanteService service = new ParticipanteService(); // criamos uma instancia do participante servece para podermos chamar os metodos dessa classe e realizar a operação de inserir / cadastrar parcipante
// coleta de dados por meio do scanner.nextline
        System.out.println("Cadastro de Participante ");

        System.out.print("Nome do Participante: ");
        String nomeParticipante = scanner.nextLine();

        System.out.print("Email do Participante: ");
        String emailParticipante = scanner.nextLine();

        System.out.print("CPF do Participante (apenas números): ");
        String cpfParticipante = scanner.nextLine();
// depois de colertamos os dados criamos um objeto para ele, . inicializamos o id 0, porem quando o objeto é criado no banco esse valor é substituido pelo o autoincrement
        Participante novoParticipante = new Participante(
                0,
                nomeParticipante,
                emailParticipante,
                cpfParticipante
        );
// aqui criamos uma variavel bolena (recebe valores de verdadeiro ou falso) para inserir por meio do metodo inserir da classe participante service passando o novo participante como argumento o objeto e que
        boolean participanteFoiSalvo = service.inserir(novoParticipante); // insere no banco de dados
// verificação de resultado se foi salvo ou nao
        if (participanteFoiSalvo) { // se for verdadeiro
            System.out.println("\nParticipante cadastrado com sucesso!");
            System.out.println("Detalhes:");
            System.out.println("Nome: " + novoParticipante.getNome());
            System.out.println("Email: " + novoParticipante.getEmail());
            System.out.println("CPF: " + novoParticipante.getCpf());
            System.out.println("ID : " + novoParticipante.getId_participante());

            System.out.println("\nPor favor, faça login com seus dados de participante.");
            new LoginParticipante().iniciarLoginParticipante(); // cria uma instancia da classe loginparticipante e chama o metodo de iniciar login
        } else {
            System.out.println("\nFalha ao cadastrar participante. Verifique os dados.");
        }
    }


}