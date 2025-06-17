package br.com.teste.app;

import br.com.teste.model.Responsavel;
import br.com.teste.model.Evento;
import br.com.teste.model.Local;
import br.com.teste.service.EventoService;
import br.com.teste.service.LocalService;
import br.com.teste.service.ResponsavelService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuResponsavel {

    private Scanner scanner;
    private Responsavel responsavelLogado;
    private EventoService eventoService;
    private LocalService localService;
    private ResponsavelService responsavelService;

    public MenuResponsavel(Scanner scanner, Responsavel responsavel) {
        this.scanner = scanner;
        this.responsavelLogado = responsavel;
        this.eventoService = new EventoService();
        this.localService = new LocalService();
        this.responsavelService = new ResponsavelService();
    }

    public void exibirMenuResponsavel() {
        boolean deslogar = false;

        while (!deslogar) {
            if (responsavelLogado == null) {
                System.out.println("\nVocê foi deslogado ou houve um erro. Voltando ao menu de login.");
                deslogar = true;
                continue;
            }

            System.out.println("\n--- Olá, Responsável " + responsavelLogado.getNome() + "! Escolha uma ação: ---");
            System.out.println("1. Visualizar Eventos");
            System.out.println("2. Criar Novo Evento");
            System.out.println("3. Editar Evento");
            System.out.println("4. Excluir Evento");
            System.out.println("5. Cadastrar Novo Local");
            System.out.println("0. Deslogar");
            System.out.print("Sua opção: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        visualizarEventos();
                        break;
                    case 2:
                        criarNovoEvento();
                        break;
                    case 3:
                        editarEvento();
                        break;
                    case 4:
                        excluirEvento();
                        break;
                    case 5:
                        cadastrarLocal();
                        break;
                    case 0:
                        System.out.println("Deslogando do sistema de Responsável...");
                        this.responsavelLogado = null;
                        deslogar = true;
                        break;
                    default:
                        System.out.println("Opção inválida. Por favor, tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void visualizarEventos() {
        System.out.println("\n--- Visualizar Eventos ---");
        List<Evento> eventos = eventoService.listarTodosEventos();
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento encontrado no sistema.");
        } else {
            System.out.println("Lista de Eventos:");
            for (Evento evento : eventos) {
                String nomeLocal = (evento.getLocal() != null) ? evento.getLocal().getNome() : "Local Desconhecido";
                System.out.println("ID: " + evento.getId_evento() +
                        " | Nome: " + evento.getNome() +
                        " | Tipo: " + evento.getTipo() +
                        " | Data: " + evento.getData() +
                        " | Hora: " + evento.getHora() +
                        " | Local: " + nomeLocal);
                System.out.println("   Descrição: " + evento.getDescricao());
                System.out.println("------------------------------------");
            }
        }
    }

    private void criarNovoEvento() {
        System.out.println("\n--- Criar Novo Evento ---");

        System.out.print("Nome do Evento: ");
        String nomeEvento = scanner.nextLine();

        System.out.print("Tipo do Evento (ex: Palestra, Workshop): ");
        String tipoEvento = scanner.nextLine();

        LocalDate dataEvento = null;
        boolean dataValida = false;
        while (!dataValida) {
            System.out.print("Data do Evento (AAAA-MM-DD): ");
            String dataStr = scanner.nextLine();
            try {
                dataEvento = LocalDate.parse(dataStr);
                dataValida = true;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Use AAAA-MM-DD.");
            }
        }

        LocalTime horaEvento = null;
        boolean horaValida = false;
        while (!horaValida) {
            System.out.print("Hora do Evento (HH:MM): ");
            String horaStr = scanner.nextLine();
            try {
                horaEvento = LocalTime.parse(horaStr);
                horaValida = true;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de hora inválido. Use HH:MM.");
            }
        }

        System.out.print("Descrição do Evento: ");
        String descricaoEvento = scanner.nextLine();

        System.out.println("\n--- Escolha o Local do Evento ---");
        List<Local> locaisDisponiveis = localService.listar();

        if (locaisDisponiveis.isEmpty()) {
            System.out.println("Não há locais cadastrados. Por favor, cadastre um local primeiro.");
            return;
        }

        System.out.println("Locais Disponíveis:");
        for (Local local : locaisDisponiveis) {
            System.out.println("ID: " + local.getId_local() + " | Nome: " + local.getNome() + " | Endereço: " + local.getEndereco() + " | Capacidade: " + local.getCapacidade());
        }

        Local localEscolhido = null;
        boolean localValido = false;
        while (!localValido) {
            System.out.print("Informe o ID do Local do Evento: ");
            int idLocal = -1;
            try {
                idLocal = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número para o ID do local.");
                scanner.nextLine();
                continue;
            }

            localEscolhido = localService.buscarPorId(idLocal); // Usar buscarPorId do LocalService
            if (localEscolhido != null) {
                localValido = true;
            } else {
                System.out.println("Local com ID " + idLocal + " não encontrado. Tente novamente.");
            }
        }

        Evento novoEvento = new Evento(
                0,
                nomeEvento,
                tipoEvento,
                dataEvento,
                horaEvento,
                descricaoEvento,
                localEscolhido
        );

        boolean sucesso = eventoService.inserir(novoEvento);

        if (sucesso) {
            System.out.println("Evento '" + novoEvento.getNome() + "' criado com sucesso!");
        } else {
            System.out.println("Falha ao criar evento. Verifique os dados.");
        }
    }

    private void editarEvento() {
        System.out.println("\n--- Editar Evento ---");
        List<Evento> eventos = eventoService.listarTodosEventos();

        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento para editar.");
            return;
        }

        visualizarEventos();

        System.out.print("Informe o ID do evento que deseja editar (0 para cancelar): ");
        int idEventoEditar = -1;
        try {
            idEventoEditar = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.nextLine();
            return;
        }

        if (idEventoEditar == 0) {
            System.out.println("Edição de evento cancelada.");
            return;
        }

        Evento eventoParaEditar = eventoService.buscarPorId(idEventoEditar);

        if (eventoParaEditar == null) {
            System.out.println("Evento com ID " + idEventoEditar + " não encontrado.");
            return;
        }

        System.out.println("\n--- Editando Evento: " + eventoParaEditar.getNome() + " ---");
        System.out.println("Deixe em branco para manter o valor atual.");

        System.out.print("Nome atual (" + eventoParaEditar.getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isEmpty()) {
            eventoParaEditar.setNome(novoNome);
        }

        System.out.print("Tipo atual (" + eventoParaEditar.getTipo() + "): ");
        String novoTipo = scanner.nextLine();
        if (!novoTipo.isEmpty()) {
            eventoParaEditar.setTipo(novoTipo);
        }

        LocalDate novaData = null;
        boolean dataValida = false;
        while (!dataValida) {
            System.out.print("Data atual (" + eventoParaEditar.getData() + ") (AAAA-MM-DD): ");
            String novaDataStr = scanner.nextLine();
            if (novaDataStr.isEmpty()) {
                novaData = eventoParaEditar.getData();
                dataValida = true;
            } else {
                try {
                    novaData = LocalDate.parse(novaDataStr);
                    dataValida = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data inválido. Use AAAA-MM-DD.");
                }
            }
        }
        eventoParaEditar.setData(novaData);


        LocalTime novaHora = null;
        boolean horaValida = false;
        while (!horaValida) {
            System.out.print("Hora atual (" + eventoParaEditar.getHora() + ") (HH:MM): ");
            String novaHoraStr = scanner.nextLine();
            if (novaHoraStr.isEmpty()) {
                novaHora = eventoParaEditar.getHora();
                horaValida = true;
            } else {
                try {
                    novaHora = LocalTime.parse(novaHoraStr);
                    horaValida = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de hora inválido. Use HH:MM.");
                }
            }
        }
        eventoParaEditar.setHora(novaHora);

        System.out.print("Descrição atual (" + eventoParaEditar.getDescricao() + "): ");
        String novaDescricao = scanner.nextLine();
        if (!novaDescricao.isEmpty()) {
            eventoParaEditar.setDescricao(novaDescricao);
        }


        System.out.println("\n--- Escolha o NOVO Local do Evento (ID atual: " + eventoParaEditar.getLocal().getId_local() + " - " + eventoParaEditar.getLocal().getNome() + ") ---");
        System.out.print("Informe o ID do NOVO Local (0 para manter o atual): ");
        int idNovoLocal = -1;
        try {
            idNovoLocal = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Mantendo o local atual.");
            scanner.nextLine();
        }

        if (idNovoLocal != 0) {
            Local novoLocalEscolhido = localService.buscarPorId(idNovoLocal);
            if (novoLocalEscolhido != null) {
                eventoParaEditar.setLocal(novoLocalEscolhido);
                System.out.println("Local do evento atualizado para: " + novoLocalEscolhido.getNome());
            } else {
                System.out.println("Local com ID " + idNovoLocal + " não encontrado. Mantendo o local anterior.");
            }
        } else {
            System.out.println("Local do evento mantido.");
        }


        boolean editado = eventoService.editar(eventoParaEditar);

        if (editado) {
            System.out.println("Evento editado com sucesso!");
        } else {
            System.out.println("Falha ao editar evento. Verifique os dados.");
        }
    }

    private void excluirEvento() {
        System.out.println("\n--- Excluir Evento ---");
        List<Evento> eventos = eventoService.listarTodosEventos();

        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento para excluir.");
            return;
        }

        visualizarEventos();

        System.out.print("Informe o ID do evento que deseja excluir (0 para cancelar): ");
        int idEventoExcluir = -1;
        try {
            idEventoExcluir = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.nextLine();
            return;
        }

        if (idEventoExcluir == 0) {
            System.out.println("Exclusão de evento cancelada.");
            return;
        }

        Evento eventoParaExcluir = eventoService.buscarPorId(idEventoExcluir);

        if (eventoParaExcluir == null) {
            System.out.println("Evento com ID " + idEventoExcluir + " não encontrado.");
            return;
        }

        System.out.print("Tem certeza que deseja excluir o evento '" + eventoParaExcluir.getNome() + "' (S/N)? Esta ação é irreversível e pode afetar inscrições! ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("S")) {
            boolean excluido = eventoService.excluir(idEventoExcluir);

            if (excluido) {
                System.out.println("Evento '" + eventoParaExcluir.getNome() + "' excluído com sucesso!");
            } else {
                System.out.println("Falha ao excluir evento. Verifique se não há dependências (ex: inscrições ativas).");
            }
        } else {
            System.out.println("Exclusão de evento cancelada.");
        }
    }


    private void cadastrarLocal() {
        System.out.println("\n--- Cadastrar Novo Local ---");

        System.out.print("Nome do Local: ");
        String nomeLocal = scanner.nextLine();

        System.out.print("Endereço do Local: ");
        String enderecoLocal = scanner.nextLine();

        int capacidadeLocal = -1;
        boolean capacidadeValida = false;
        while (!capacidadeValida) {
            System.out.print("Capacidade do Local (somente números): ");
            try {
                capacidadeLocal = scanner.nextInt();
                scanner.nextLine();
                if (capacidadeLocal > 0) {
                    capacidadeValida = true;
                } else {
                    System.out.println("A capacidade deve ser um número positivo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número para a capacidade.");
                scanner.nextLine();
            }
        }

        Local novoLocal = new Local(
                0,
                nomeLocal,
                enderecoLocal,
                capacidadeLocal
        );

        boolean sucesso = localService.inserir(novoLocal);

        if (sucesso) {
            System.out.println("Local '" + novoLocal.getNome() + "' cadastrado com sucesso!");
        } else {
            System.out.println("Falha ao cadastrar local. Verifique os dados.");
        }
    }
}