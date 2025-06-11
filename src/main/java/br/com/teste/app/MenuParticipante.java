package br.com.teste.app;
// importamos todas as classes que iremos precisar para fazer a logica do menu
import br.com.teste.model.Participante;
import br.com.teste.model.Inscricao;
import br.com.teste.model.Evento;
import br.com.teste.service.ParticipanteService;
import br.com.teste.service.GerarCertificado;
import br.com.teste.service.InscricaoService;
import br.com.teste.service.EventoService;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuParticipante {
 // criamos variaveis privadas para serem utilizadas somente aqui nessa classe e nao nos objetos
    private Scanner scanner;
    private ParticipanteService participanteService;
    private Participante participanteLogado;
    private GerarCertificado gerarCertificadoService;
    private InscricaoService inscricaoService;
    private EventoService eventoService;

    public MenuParticipante(Scanner scanner, Participante participanteLogado) {
        this.scanner = scanner;
        this.participanteLogado = participanteLogado;
        this.participanteService = new ParticipanteService(); // criamos uma intancia para fazer com que o service possa usar
        this.gerarCertificadoService = new GerarCertificado(); // o mesmo acontece aqui
        this.inscricaoService = new InscricaoService(); // o mesmo aqui
        this.eventoService = new EventoService(); // o mesmo aqui
    }

    public void exibirMenuParticipante() {
        boolean deslogarOuSair = false; // inicializamos a variavel bolena com valor falso para controlar o loop
        while (!deslogarOuSair) {
            if (participanteLogado == null) { // se o participante for igual ao valor vazio ele desloga
                System.out.println("\nVocê foi deslogado ou houve um erro. Voltando ao menu de login.");
                deslogarOuSair = true; // recebe verdadeiro e sai da aplicação
                continue;
            }
            //.getNome captura o nome do participante
            System.out.println("\nOlá, " + participanteLogado.getNome() + "! Escolha uma ação:");
            System.out.println("1. Editar Meu Perfil");
            System.out.println("2. Excluir Minha Conta");
            System.out.println("3. Emitir Certificado");
            System.out.println("4. Gerenciar Minhas Inscrições em Eventos");
            System.out.println("0. Deslogar");
            System.out.print("Sua opção: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        editarMeuPerfil(); // chama o metodo editar menu
                        break;
                    case 2:
                        excluirMinhaConta();
                        if (participanteLogado == null) { // se participante for igual a nulo
                            System.out.println("Sua conta foi excluída. Você foi deslogado.");
                            deslogarOuSair = true;
                        }
                        break;
                    case 3:
                        emitirCertificado(); // chama o metodo emitir certificado 
                        break;
                    case 4:
                        gerenciarMinhasInscricoesEmEventos();
                        break;
                    case 0:
                        System.out.println("Deslogando...");
                        this.participanteLogado = null;
                        deslogarOuSair = true;
                        break;
                    default:
                        System.out.println("Opção inválida. Por favor, tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
            }
        }
    }

    private void editarMeuPerfil() {
        if (participanteLogado == null) {
            System.out.println("Erro: Participante não logado.");
            return;
        }

        System.out.println("\n--- Editar Meu Perfil ---");
        System.out.println("Seu ID: " + participanteLogado.getId_participante());
        System.out.println("Nome atual: " + participanteLogado.getNome());
        System.out.print("Novo Nome (deixe em branco para manter): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isEmpty()) {
            participanteLogado.setNome(novoNome);
        }

        System.out.println("Email atual: " + participanteLogado.getEmail());
        System.out.print("Novo Email (deixe em branco para manter): ");
        String novoEmail = scanner.nextLine();
        if (!novoEmail.isEmpty()) {
            participanteLogado.setEmail(novoEmail);
        }

        System.out.println("CPF atual: " + participanteLogado.getCpf());
        System.out.print("Novo CPF (deixe em branco para manter): ");
        String novoCpf = scanner.nextLine();
        if (!novoCpf.isEmpty()) {
            participanteLogado.setCpf(novoCpf);
        }

        boolean editado = participanteService.editar(participanteLogado);

        if (editado) {
            System.out.println("Perfil editado com sucesso!");
        } else {
            System.out.println("Falha ao editar perfil. Verifique os dados.");
        }
    }

    private void excluirMinhaConta() {
        if (participanteLogado == null) {
            System.out.println("Erro: Participante não logado.");
            return;
        }

        System.out.println("\n--- Excluir Minha Conta ---");
        System.out.print("Tem certeza que deseja EXCLUIR sua conta e todas as suas inscrições em eventos (S/N)? ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("S")) {
            boolean excluido = participanteService.excluir(participanteLogado);
            if (excluido) {
                System.out.println("Conta e inscrições excluídas com sucesso!");
                participanteLogado = null;
            } else {
                System.out.println("Falha ao excluir conta.");
            }
        } else {
            System.out.println("Exclusão de conta cancelada.");
        }
    }

    @SuppressWarnings("unused")
    private void emitirCertificado() {
        if (participanteLogado == null) {
            System.out.println("Você precisa estar logado para emitir o certificado.");
            return;
        }

        System.out.println("\n--- Emissão de Certificado ---");

        List<Inscricao> minhasInscricoes = inscricaoService.listarInscricoesPorParticipante(participanteLogado.getId_participante());

        if (minhasInscricoes.isEmpty()) {
            System.out.println("Você não possui inscrições em eventos para emitir certificados.");
            return;
        }

        System.out.println("Suas Inscrições Disponíveis para Certificado:");
        for (Inscricao inscricao : minhasInscricoes) {
            System.out.println("ID Inscrição: " + inscricao.getId_inscricao() +
                    " - Evento: " + (inscricao.getEvento() != null ? inscricao.getEvento().getNome() : "N/A") +
                    " - Ativa: " + (inscricao.isAtiva() ? "Sim" : "Não"));
        }

        System.out.print("Informe o ID da inscrição para a qual deseja emitir o certificado: ");
        int idInscricao = -1;
        try {
            idInscricao = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número para o ID da inscrição.");
            scanner.nextLine();
            return;
        }

        boolean inscricaoPertenceAoParticipante = false;
        for (Inscricao inscricao : minhasInscricoes) {
            if (inscricao.getId_inscricao() == idInscricao) {
                inscricaoPertenceAoParticipante = true;
                break;
            }
        }

        if (!inscricaoPertenceAoParticipante) {
            System.out.println("Erro: O ID da inscrição informado não pertence à sua conta ou é inválido.");
            return;
        }

        boolean certificadoGerado = gerarCertificadoService.gerarEInserirCertificado(idInscricao);

        if (certificadoGerado) {
        } else {
            System.out.println("Não foi possível emitir o certificado para a inscrição ID: " + idInscricao + ".");
            System.out.println("Verifique se a inscrição existe e se todos os dados estão completos.");
        }
    }

    private void gerenciarMinhasInscricoesEmEventos() {
        if (participanteLogado == null) {
            System.out.println("Erro: Participante não logado.");
            return;
        }

        boolean voltarAoMenuParticipante = false;
        while (!voltarAoMenuParticipante) {
            System.out.println("\n--- Gerenciar Minhas Inscrições em Eventos ---");
            System.out.println("1. Visualizar Minhas Inscrições");
            System.out.println("2. Inscrever-me em um Novo Evento");
            System.out.println("3. Cancelar Inscrição em Evento");
            System.out.println("0. Voltar ao Menu do Participante");
            System.out.print("Sua opção: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        visualizarMinhasInscricoes();
                        break;
                    case 2:
                        inscreverEmNovoEvento();
                        break;
                    case 3:
                        cancelarInscricaoEmEvento();
                        break;
                    case 0:
                        System.out.println("Voltando ao Menu do Participante...");
                        voltarAoMenuParticipante = true;
                        break;
                    default:
                        System.out.println("Opção inválida. Por favor, tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
            }
        }
    }

    private void visualizarMinhasInscricoes() {
        if (participanteLogado == null) {
            System.out.println("Erro: Participante não logado.");
            return;
        }

        System.out.println("\n--- Minhas Inscrições em Eventos ---");
        List<Inscricao> minhasInscricoes = inscricaoService.listarInscricoesPorParticipante(participanteLogado.getId_participante());

        if (minhasInscricoes.isEmpty()) {
            System.out.println("Você não está inscrito(a) em nenhum evento.");
        } else {
            for (Inscricao inscricao : minhasInscricoes) {
                String nomeEvento = (inscricao.getEvento() != null) ? inscricao.getEvento().getNome() : "Evento Desconhecido";
                String dataEvento = (inscricao.getEvento() != null && inscricao.getEvento().getData() != null) ? inscricao.getEvento().getData().toString() : "Data Desconhecida";
                // CORREÇÃO AQUI: evento.getLocal().getNome()
                String nomeLocal = (inscricao.getEvento() != null && inscricao.getEvento().getLocal() != null) ? inscricao.getEvento().getLocal().getNome() : "Local Desconhecido";

                System.out.println("ID Inscrição: " + inscricao.getId_inscricao() +
                        " | Evento: " + nomeEvento +
                        " | Data: " + dataEvento +
                        " | Local: " + nomeLocal +
                        " | Status: " + (inscricao.isAtiva() ? "Ativa" : "Cancelada"));
            }
        }
    }

    private void inscreverEmNovoEvento() {
        if (participanteLogado == null) {
            System.out.println("Erro: Participante não logado.");
            return;
        }

        System.out.println("\n--- Inscrever-me em um Novo Evento ---");
        List<Evento> eventosDisponiveis = eventoService.listarEventosDisponiveis();

        if (eventosDisponiveis.isEmpty()) {
            System.out.println("Não há eventos disponíveis para inscrição no momento.");
            return;
        }

        System.out.println("Eventos Disponíveis:");
        for (Evento evento : eventosDisponiveis) {
            // CORREÇÃO AQUI: evento.getLocal().getNome()
            String nomeLocal = (evento.getLocal() != null) ? evento.getLocal().getNome() : "Local Desconhecido";
            System.out.println("ID: " + evento.getId_evento() +
                    " | Nome: " + evento.getNome() +
                    " | Data: " + evento.getData() +
                    " | Local: " + nomeLocal);
        }

        System.out.print("Informe o ID do evento para se inscrever (0 para cancelar): ");
        int idEventoEscolhido = -1;
        try {
            idEventoEscolhido = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número para o ID do evento.");
            scanner.nextLine();
            return;
        }

        if (idEventoEscolhido == 0) {
            System.out.println("Inscrição cancelada.");
            return;
        }

        Evento eventoEscolhido = eventoService.buscarPorId(idEventoEscolhido);

        if (eventoEscolhido == null) {
            System.out.println("Evento com ID " + idEventoEscolhido + " não encontrado.");
            return;
        }

        boolean jaInscrito = false;
        List<Inscricao> minhasInscricoesAtuais = inscricaoService.listarInscricoesPorParticipante(participanteLogado.getId_participante());
        for (Inscricao inscricao : minhasInscricoesAtuais) {
            if (inscricao.getEvento() != null && inscricao.getEvento().getId_evento() == idEventoEscolhido) {
                jaInscrito = true;
                break;
            }
        }

        if (jaInscrito) {
            System.out.println("Você já está inscrito neste evento.");
            return;
        }

        Inscricao novaInscricao = new Inscricao(
                0,
                eventoEscolhido,
                participanteLogado,
                LocalDate.now(),
                true
        );

        boolean sucesso = inscricaoService.inserir(novaInscricao);

        if (sucesso) {
            System.out.println("Inscrição no evento '" + eventoEscolhido.getNome() + "' realizada com sucesso!");
        } else {
            System.out.println("Falha ao realizar inscrição no evento.");
        }
    }

    private void cancelarInscricaoEmEvento() {
        if (participanteLogado == null) {
            System.out.println("Erro: Participante não logado.");
            return;
        }

        System.out.println("\n--- Cancelar Inscrição em Evento ---");
        List<Inscricao> minhasInscricoesAtivas = inscricaoService.listarInscricoesPorParticipante(participanteLogado.getId_participante());

        if (minhasInscricoesAtivas.isEmpty()) {
            System.out.println("Você não possui inscrições ativas para cancelar.");
            return;
        }

        System.out.println("Suas Inscrições Ativas:");
        for (Inscricao inscricao : minhasInscricoesAtivas) {
            if (inscricao.isAtiva()) {
                String nomeEvento = (inscricao.getEvento() != null) ? inscricao.getEvento().getNome() : "Evento Desconhecido";
                // CORREÇÃO AQUI: evento.getLocal().getNome()
                String nomeLocal = (inscricao.getEvento() != null && inscricao.getEvento().getLocal() != null) ? inscricao.getEvento().getLocal().getNome() : "Local Desconhecido";
                System.out.println("ID Inscrição: " + inscricao.getId_inscricao() +
                        " - Evento: " + nomeEvento +
                        " - Local: " + nomeLocal);
            }
        }

        System.out.print("Informe o ID da inscrição que deseja cancelar (0 para voltar): ");
        int idInscricaoParaCancelar = -1;
        try {
            idInscricaoParaCancelar = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número para o ID da inscrição.");
            scanner.nextLine();
            return;
        }

        if (idInscricaoParaCancelar == 0) {
            System.out.println("Operação de cancelamento cancelada.");
            return;
        }

        Inscricao inscricaoACancelar = null;
        for (Inscricao inscricao : minhasInscricoesAtivas) {
            if (inscricao.getId_inscricao() == idInscricaoParaCancelar && inscricao.isAtiva()) {
                inscricaoACancelar = inscricao;
                break;
            }
        }

        if (inscricaoACancelar == null) {
            System.out.println("Inscrição com ID " + idInscricaoParaCancelar + " não encontrada ou já está cancelada/não pertence à sua conta.");
            return;
        }

        System.out.print("Confirma o cancelamento da inscrição no evento '" + inscricaoACancelar.getEvento().getNome() + "' (S/N)? ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("S")) {
            inscricaoACancelar.setAtiva(false);
            boolean sucesso = inscricaoService.editar(inscricaoACancelar);

            if (sucesso) {
                System.out.println("Inscrição no evento '" + inscricaoACancelar.getEvento().getNome() + "' cancelada com sucesso!");
            } else {
                System.out.println("Falha ao cancelar inscrição.");
            }
        } else {
            System.out.println("Cancelamento da inscrição abortado.");
        }
    }
}