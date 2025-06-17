package br.com.teste.app;
// Vamos importar tudo que precisamos para o nosso menu funcionar
import br.com.teste.model.Participante;
import br.com.teste.model.Inscricao;
import br.com.teste.model.Evento;
import br.com.teste.service.ParticipanteService;
import br.com.teste.service.GerarCertificado;
import br.com.teste.service.InscricaoService;
import br.com.teste.service.EventoService;

import java.time.LocalDate;
import java.util.InputMismatchException; // Para pegar erros de entrada de números
import java.util.List;
import java.util.Scanner; // Para ler o que o usuário digita

public class MenuParticipante {
    // Aqui criamos variáveis que só essa classe vai usar
    private Scanner scanner;
    private ParticipanteService participanteService;
    private Participante participanteLogado; // O participante que está usando o menu
    private GerarCertificado gerarCertificadoService;
    private InscricaoService inscricaoService;
    private EventoService eventoService;

    // Construtor: Quando criamos um MenuParticipante, passamos o scanner e o participante logado
    public MenuParticipante(Scanner scanner, Participante participanteLogado) {
        this.scanner = scanner;
        this.participanteLogado = participanteLogado;
        // Criamos os "serviços" que vão nos ajudar a conversar com o banco de dados
        this.participanteService = new ParticipanteService();
        this.gerarCertificadoService = new GerarCertificado();
        this.inscricaoService = new InscricaoService();
        this.eventoService = new EventoService();
    }

    // Este é o método principal que mostra o menu para o participante
    public void exibirMenuParticipante() {
        boolean sairDoMenu = false; // Uma variável para controlar se o usuário quer sair

        // Enquanto o usuário não quiser sair, o menu continua aparecendo
        while (!sairDoMenu) {
            // Se por algum motivo o participante não estiver mais logado, saímos
            if (participanteLogado == null) {
                System.out.println("\nVocê foi deslogado ou algo deu errado. Voltando para o login.");
                sairDoMenu = true;
                continue; // Volta para o início do loop (e sai)
            }
            // Mostra as opções para o participante
            System.out.println("\n--- Olá, " + participanteLogado.getNome() + "! Escolha o que fazer: ---");
            System.out.println("1. Editar Meu Perfil");
            System.out.println("2. Excluir Minha Conta");
            System.out.println("3. Emitir Certificado");
            System.out.println("4. Gerenciar Minhas Inscrições em Eventos");
            System.out.println("0. Deslogar");
            System.out.print("Digite sua opção: ");

            try {
                int opcaoEscolhida = scanner.nextInt(); // Lê a opção que o usuário digitou
                scanner.nextLine(); // Limpa o "enter" que fica no teclado

                // Usa um "switch" para decidir o que fazer com a opção escolhida
                switch (opcaoEscolhida) {
                    case 1:
                        editarMeuPerfil(); // Chama a função de editar perfil
                        break;
                    case 2:
                        excluirMinhaConta(); // Chama a função de excluir conta
                        // Se a conta foi excluída, o participante é deslogado
                        if (participanteLogado == null) {
                            System.out.println("Sua conta foi excluída. Você saiu.");
                            sairDoMenu = true; // Sair do menu
                        }
                        break;
                    case 3:
                        emitirCertificado(); // Chama a função de emitir certificado
                        break;
                    case 4:
                        gerenciarMinhasInscricoesEmEventos(); // Chama a função de gerenciar inscrições
                        break;
                    case 0:
                        System.out.println("Deslogando do sistema...");
                        this.participanteLogado = null; // Tira o participante logado
                        sairDoMenu = true; // Sair do menu
                        break;
                    default:
                        // Se a opção não for nenhuma das válidas
                        System.out.println("Opção errada. Por favor, digite um número que esteja na lista.");
                }
            } catch (InputMismatchException e) {
                // Se o usuário digitou texto em vez de número
                System.out.println("Entrada inválida. Por favor, digite APENAS um número.");
                scanner.nextLine(); // Limpa o que o usuário digitou de errado para não travar
            }
        }
    }

    // Função para editar o perfil do participante
    private void editarMeuPerfil() {
        if (participanteLogado == null) {
            System.out.println("Ops! Você não está logado para editar o perfil.");
            return;
        }

        System.out.println("\n--- Editando Meu Perfil ---");
        System.out.println("Seu ID: " + participanteLogado.getId_participante());
        System.out.println("Nome atual: " + participanteLogado.getNome());
        System.out.print("Novo Nome (deixe em branco se não quiser mudar): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isEmpty()) { // Se o usuário digitou algo
            participanteLogado.setNome(novoNome); // Atualiza o nome
        }

        System.out.println("Email atual: " + participanteLogado.getEmail());
        System.out.print("Novo Email (deixe em branco se não quiser mudar): ");
        String novoEmail = scanner.nextLine();
        if (!novoEmail.isEmpty()) {
            participanteLogado.setEmail(novoEmail);
        }

        System.out.println("CPF atual: " + participanteLogado.getCpf());
        System.out.print("Novo CPF (deixe em branco se não quiser mudar): ");
        String novoCpf = scanner.nextLine();
        if (!novoCpf.isEmpty()) {
            participanteLogado.setCpf(novoCpf);
        }

        // Tenta salvar as mudanças no banco de dados
        boolean editadoComSucesso = participanteService.editar(participanteLogado);

        if (editadoComSucesso) {
            System.out.println("Seu perfil foi atualizado!");
        } else {
            System.out.println("Não consegui editar seu perfil. Verifique os dados.");
        }
    }

    // Função para excluir a conta do participante
    private void excluirMinhaConta() {
        if (participanteLogado == null) {
            System.out.println("Ops! Você não está logado para excluir a conta.");
            return;
        }

        System.out.println("\n--- Excluindo Minha Conta ---");
        System.out.print("Tem certeza que quer apagar sua conta e todas as inscrições (S/N)? ISSO NÃO PODE SER DESFEITO! ");
        String confirmacaoExclusao = scanner.nextLine();

        if (confirmacaoExclusao.equalsIgnoreCase("S")) { // Se o usuário digitou "S" ou "s"
            boolean excluidoDeVerdade = participanteService.excluir(participanteLogado);
            if (excluidoDeVerdade) {
                System.out.println("Sua conta e inscrições foram apagadas!");
                participanteLogado = null; // Desloga o participante
            } else {
                System.out.println("Não consegui apagar sua conta.");
            }
        } else {
            System.out.println("Ufa! Exclusão da conta cancelada.");
        }
    }

    // Função para emitir certificado
    @SuppressWarnings("unused") // Ignora um aviso do Java que essa função talvez não seja usada diretamente
    private void emitirCertificado() {
        if (participanteLogado == null) {
            System.out.println("Você precisa estar logado para pegar seu certificado.");
            return;
        }

        System.out.println("\n--- Emissão de Certificado ---");

        // Pega a lista de todas as inscrições do participante
        List<Inscricao> minhasInscricoes = inscricaoService.listarInscricoesPorParticipante(participanteLogado.getId_participante());

        if (minhasInscricoes.isEmpty()) {
            System.out.println("Você não tem nenhuma inscrição para pegar certificado.");
            return;
        }

        System.out.println("Suas Inscrições que podem gerar Certificado:");
        // Mostra cada inscrição para o usuário escolher
        for (Inscricao inscricao : minhasInscricoes) {
            System.out.println("ID Inscrição: " + inscricao.getId_inscricao() +
                    " - Evento: " + (inscricao.getEvento() != null ? inscricao.getEvento().getNome() : "N/A") +
                    " - Ativa: " + (inscricao.isAtiva() ? "Sim" : "Não")); // Mostra se a inscrição está ativa
        }

        System.out.print("Digite o ID da inscrição para qual quer o certificado: ");
        int idInscricaoParaCertificado = -1;
        try {
            idInscricaoParaCertificado = scanner.nextInt(); // Lê o ID da inscrição
            scanner.nextLine(); // Limpa o "enter"
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número para o ID da inscrição.");
            scanner.nextLine();
            return;
        }

        boolean inscricaoExisteEPertence = false;
        // Verifica se o ID digitado é de uma inscrição do participante
        for (Inscricao inscricao : minhasInscricoes) {
            if (inscricao.getId_inscricao() == idInscricaoParaCertificado) {
                inscricaoExisteEPertence = true;
                break; // Achou! Pode parar de procurar
            }
        }

        if (!inscricaoExisteEPertence) {
            System.out.println("Erro: A inscrição que você digitou não é sua ou não existe.");
            return;
        }

        // Tenta gerar o certificado
        boolean certificadoFeito = gerarCertificadoService.gerarEInserirCertificado(idInscricaoParaCertificado);

        if (certificadoFeito) {
            System.out.println("Certificado gerado e guardado com sucesso!");
        } else {
            System.out.println("Não consegui gerar o certificado para a inscrição ID: " + idInscricaoParaCertificado + ".");
            System.out.println("Verifique se a inscrição está ok e se todos os dados estão lá.");
        }
    }

    // Função para gerenciar as inscrições (ver, inscrever, cancelar)
    private void gerenciarMinhasInscricoesEmEventos() {
        if (participanteLogado == null) {
            System.out.println("Ops! Você não está logado para gerenciar inscrições.");
            return;
        }

        boolean voltarMenuPrincipal = false;
        // Enquanto não quiser voltar para o menu principal do participante
        while (!voltarMenuPrincipal) {
            System.out.println("\n--- Gerenciando Minhas Inscrições em Eventos ---");
            System.out.println("1. Ver Minhas Inscrições");
            System.out.println("2. Me Inscrever em um Novo Evento");
            System.out.println("3. Cancelar Uma Inscrição");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Digite sua opção: ");

            try {
                int opcaoSubMenu = scanner.nextInt(); // Lê a opção do sub-menu
                scanner.nextLine(); // Limpa o "enter"

                switch (opcaoSubMenu) {
                    case 1:
                        visualizarMinhasInscricoes(); // Chama a função para ver inscrições
                        break;
                    case 2:
                        inscreverEmNovoEvento(); // Chama a função para inscrever
                        break;
                    case 3:
                        cancelarInscricaoEmEvento(); // Chama a função para cancelar
                        break;
                    case 0:
                        System.out.println("Voltando ao menu principal...");
                        voltarMenuPrincipal = true; // Sair do sub-menu
                        break;
                    default:
                        System.out.println("Opção errada. Tente de novo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite APENAS um número.");
                scanner.nextLine();
            }
        }
    }

    // Função para ver as inscrições do participante
    private void visualizarMinhasInscricoes() {
        if (participanteLogado == null) {
            System.out.println("Ops! Você não está logado para ver suas inscrições.");
            return;
        }

        System.out.println("\n--- Minhas Inscrições em Eventos ---");
        // Pega a lista de inscrições do participante
        List<Inscricao> minhasInscricoes = inscricaoService.listarInscricoesPorParticipante(participanteLogado.getId_participante());

        if (minhasInscricoes.isEmpty()) {
            System.out.println("Você ainda não se inscreveu em nenhum evento.");
        } else {
            // Mostra os detalhes de cada inscrição
            for (Inscricao inscricao : minhasInscricoes) {
                String nomeEvento = (inscricao.getEvento() != null) ? inscricao.getEvento().getNome() : "Evento Desconhecido";
                String dataEvento = (inscricao.getEvento() != null && inscricao.getEvento().getData() != null) ? inscricao.getEvento().getData().toString() : "Data Desconhecida";
                String nomeLocal = (inscricao.getEvento() != null && inscricao.getEvento().getLocal() != null) ? inscricao.getEvento().getLocal().getNome() : "Local Desconhecido";

                System.out.println("ID Inscrição: " + inscricao.getId_inscricao() +
                        " | Evento: " + nomeEvento +
                        " | Data: " + dataEvento +
                        " | Local: " + nomeLocal +
                        " | Status: " + (inscricao.isAtiva() ? "Ativa" : "Cancelada")); // Mostra se está ativa ou cancelada
            }
        }
    }

    // Função para o participante se inscrever em um novo evento
    private void inscreverEmNovoEvento() {
        if (participanteLogado == null) {
            System.out.println("Ops! Você não está logado para se inscrever em eventos.");
            return;
        }

        System.out.println("\n--- Me Inscrever em um Novo Evento ---");
        // Pega todos os eventos que estão disponíveis
        List<Evento> eventosDisponiveis = eventoService.listarEventosDisponiveis();

        if (eventosDisponiveis.isEmpty()) {
            System.out.println("Não tem eventos abertos para inscrição agora.");
            return;
        }

        System.out.println("Eventos que você pode se Inscrever:");
        // Mostra os eventos disponíveis para o usuário escolher
        for (Evento evento : eventosDisponiveis) {
            String nomeLocal = (evento.getLocal() != null) ? evento.getLocal().getNome() : "Local Desconhecido";
            System.out.println("ID: " + evento.getId_evento() +
                    " | Nome: " + evento.getNome() +
                    " | Data: " + evento.getData() +
                    " | Local: " + nomeLocal);
        }

        System.out.print("Digite o ID do evento que você quer se inscrever (digite 0 para desistir): ");
        int idEventoQueOUsuarioQuer = -1; // Variável para guardar o ID do evento
        try {
            idEventoQueOUsuarioQuer = scanner.nextInt(); // Lê o número que o usuário digitou
            scanner.nextLine(); // Limpa o "enter"
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Digite um NÚMERO para o ID do evento, por favor.");
            scanner.nextLine(); // Limpa o teclado
            return; // Sai da função
        }

        if (idEventoQueOUsuarioQuer == 0) {
            System.out.println("Inscrição cancelada.");
            return;
        }

        // Busca o evento no banco de dados usando o ID que o usuário digitou
        Evento eventoParaInscricao = eventoService.buscarPorId(idEventoQueOUsuarioQuer);

        if (eventoParaInscricao == null) {
            System.out.println("Evento com ID " + idEventoQueOUsuarioQuer + " não foi encontrado.");
            return;
        }

        // Agora, vamos ver se o participante já está inscrito neste evento
        boolean usuarioJaInscrito = false;
        List<Inscricao> minhasInscricoesAgora = inscricaoService.listarInscricoesPorParticipante(participanteLogado.getId_participante());
        for (Inscricao inscricaoAtual : minhasInscricoesAgora) {
            if (inscricaoAtual.getEvento() != null && inscricaoAtual.getEvento().getId_evento() == idEventoQueOUsuarioQuer) {
                usuarioJaInscrito = true; // Sim, ele já está inscrito
                break; // Pode parar de procurar
            }
        }

        if (usuarioJaInscrito) {
            System.out.println("Você já está inscrito neste evento.");
            return;
        }

        // Se tudo estiver ok, cria uma nova inscrição
        Inscricao novaInscricao = new Inscricao(
                0, // O ID vai ser gerado pelo banco de dados
                eventoParaInscricao, // O evento escolhido
                participanteLogado, // O participante logado
                LocalDate.now(), // A data de hoje
                true // A inscrição começa como ativa (verdadeira)
        );

        // Tenta salvar a nova inscrição no banco de dados
        boolean deuCertoSalvar = inscricaoService.inserir(novaInscricao);

        if (deuCertoSalvar) {
            System.out.println("Inscrição no evento '" + eventoParaInscricao.getNome() + "' feita com sucesso!");
        } else {
            System.out.println("Não consegui fazer sua inscrição no evento. Tente de novo.");
        }
    }

    // Função para o participante cancelar uma inscrição
    private void cancelarInscricaoEmEvento() {
        if (participanteLogado == null) {
            System.out.println("Ops! Você não está logado para cancelar inscrições.");
            return;
        }

        System.out.println("\n--- Cancelar Inscrição em Evento ---");
        // Pega as inscrições ativas do participante
        List<Inscricao> minhasInscricoesAtivas = inscricaoService.listarInscricoesPorParticipante(participanteLogado.getId_participante());

        if (minhasInscricoesAtivas.isEmpty()) {
            System.out.println("Você não tem nenhuma inscrição para cancelar.");
            return;
        }

        System.out.println("Suas Inscrições Ativas:");
        // Mostra as inscrições ativas para o usuário escolher qual cancelar
        for (Inscricao inscricao : minhasInscricoesAtivas) {
            if (inscricao.isAtiva()) { // Só mostra as que estão ativas
                String nomeDoEvento = (inscricao.getEvento() != null) ? inscricao.getEvento().getNome() : "Evento Desconhecido";
                String nomeDoLocal = (inscricao.getEvento() != null && inscricao.getEvento().getLocal() != null) ? inscricao.getEvento().getLocal().getNome() : "Local Desconhecido";
                System.out.println("ID Inscrição: " + inscricao.getId_inscricao() +
                        " - Evento: " + nomeDoEvento +
                        " - Local: " + nomeDoLocal);
            }
        }

        System.out.print("Digite o ID da inscrição que você quer cancelar (0 para voltar): ");
        int idInscricaoParaCancelar = -1;
        try {
            idInscricaoParaCancelar = scanner.nextInt(); // Lê o ID da inscrição
            scanner.nextLine(); // Limpa o "enter"
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Digite um NÚMERO para o ID da inscrição, por favor.");
            scanner.nextLine();
            return;
        }

        if (idInscricaoParaCancelar == 0) {
            System.out.println("Operação de cancelamento cancelada.");
            return;
        }

        Inscricao inscricaoParaApagar = null;
        // Procura a inscrição na lista do participante
        for (Inscricao inscricao : minhasInscricoesAtivas) {
            if (inscricao.getId_inscricao() == idInscricaoParaCancelar && inscricao.isAtiva()) {
                inscricaoParaApagar = inscricao; // Achou a inscrição que quer cancelar
                break;
            }
        }

        if (inscricaoParaApagar == null) {
            System.out.println("Essa inscrição com ID " + idInscricaoParaCancelar + " não foi encontrada, já está cancelada ou não é sua.");
            return;
        }

        System.out.print("Tem certeza que quer cancelar a inscrição no evento '" + inscricaoParaApagar.getEvento().getNome() + "' (S/N)? ");
        String confirmacaoFinal = scanner.nextLine();

        if (confirmacaoFinal.equalsIgnoreCase("S")) {
            inscricaoParaApagar.setAtiva(false); // Define a inscrição como inativa (cancelada)
            boolean canceladoComSucesso = inscricaoService.editar(inscricaoParaApagar); // Tenta atualizar no banco

            if (canceladoComSucesso) {
                System.out.println("Inscrição no evento '" + inscricaoParaApagar.getEvento().getNome() + "' cancelada com sucesso!");
            } else {
                System.out.println("Não consegui cancelar a inscrição. Tente de novo.");
            }
        } else {
            System.out.println("Cancelamento da inscrição não feito.");
        }
    }
}
