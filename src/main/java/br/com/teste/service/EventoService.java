package br.com.teste.service;

import br.com.teste.dao.EventoDao;
import br.com.teste.dao.InscricaoDao;
import br.com.teste.model.Evento;
import br.com.teste.model.Local;
import br.com.teste.model.Responsavel; // Importe a classe Responsavel
import java.time.LocalDate;
import java.util.List;

public class EventoService {

    private EventoDao eventoDao;
    private ResponsavelService responsavelService; // Adicione esta dependência para carregar responsáveis

    public EventoService() {
        this.eventoDao = new EventoDao();
        this.responsavelService = new ResponsavelService(); // Inicialize o ResponsavelService
    }

    //metodo de teste para obter o local de um evento

    public Local getLocalDoEvento(int id_evento) {
        Evento evento = buscarPorId(id_evento);
        if (evento != null) {
            return evento.getLocal();
        }
        return null;
    }

    //metodo de teste para obter o evento com maior numero de inscritos

    public Evento getEventoComMaiorPublico(){
        List<Evento> eventos = eventoDao.listarTodos();
        Evento eventoMaiorPublico = null;
        int maxParticipantes = -1;

        InscricaoDao inscricaoDao = new InscricaoDao();

        for (Evento evento : eventos) {
            int inscritos = inscricaoDao.contarInscricoesPorEvento(evento.getId_evento());
            if (inscritos > maxParticipantes) {
                maxParticipantes = inscritos;
                eventoMaiorPublico = evento;
            }
        }

        return eventoMaiorPublico;
    }


    // Read: Busca um evento por ID e carrega seus responsáveis
    public Evento buscarPorId(int idEvento) {
        if (idEvento <= 0) {
            System.out.println("Erro no EventoService: ID de evento inválido para busca.");
            return null;
        }
        Evento evento = eventoDao.buscarPorId(idEvento); // Busca o evento sem os responsáveis
        if (evento != null) {
            // CARREGA A LISTA DE RESPONSÁVEIS AQUI, usando o ResponsavelService
            evento.setResponsavelLista(responsavelService.listarResponsaveisPorEvento(evento.getId_evento()));
        }
        return evento;
    }

    // Read: Lista eventos que estão disponíveis (ex: futuros) e carrega seus responsáveis
    public List<Evento> listarEventosDisponiveis() {
        List<Evento> eventos = eventoDao.listarEventosDisponiveis(); // Lista eventos sem os responsáveis
        // Carregar responsáveis para cada evento após a listagem
        for (Evento evento : eventos) {
            evento.setResponsavelLista(responsavelService.listarResponsaveisPorEvento(evento.getId_evento()));
        }
        return eventos;
    }

    // Read: Lista TODOS os eventos (independentemente da data) e carrega seus responsáveis
    public List<Evento> listarTodosEventos() {
        List<Evento> eventos = eventoDao.listarTodos(); // Lista eventos sem os responsáveis
        // Carregar responsáveis para cada evento após a listagem
        for (Evento evento : eventos) {
            evento.setResponsavelLista(responsavelService.listarResponsaveisPorEvento(evento.getId_evento()));
        }
        return eventos;
    }

    // Create: Insere um novo evento
    public boolean inserir(Evento evento) {
        if (!validar(evento)) {
            System.out.println("EventoService: Validação falhou. Evento não será inserido.");
            return false;
        }
        boolean sucesso = eventoDao.inserir(evento);
        // Lógica para associar responsáveis na tabela evento_responsavel (se houver)
        // Isso normalmente aconteceria aqui, APÓS o evento ser inserido e ter um ID.
        /*
        if (sucesso && evento.getResponsavelLista() != null && !evento.getResponsavelLista().isEmpty()) {
            // Exemplo de como você chamaria o ResponsavelService para associar:
            // responsavelService.associarResponsaveisAoEvento(evento.getId_evento(), evento.getResponsavelLista());
        }
        */
        return sucesso;
    }

    // Update: Edita um evento existente
    public boolean editar(Evento evento) {
        if (evento.getId_evento() <= 0) {
            System.out.println("Erro no EventoService: ID do evento inválido para edição.");
            return false;
        }
        if (!validar(evento)) {
            System.out.println("EventoService: Validação falhou. Evento não será editado.");
            return false;
        }
        boolean sucesso = eventoDao.editar(evento);
        // Lógica para atualizar associações de responsáveis (se a lista for editável)
        /*
        if (sucesso) {
            // Exemplo: desassociar todos os antigos e associar os novos
            // responsavelService.desassociarTodosResponsaveisDoEvento(evento.getId_evento());
            // responsavelService.associarResponsaveisAoEvento(evento.getId_evento(), evento.getResponsavelLista());
        }
        */
        return sucesso;
    }

    // Delete: Exclui um evento
    public boolean excluir(int idEvento) {
        if (idEvento <= 0) {
            System.out.println("Erro no EventoService: ID do evento inválido para exclusão.");
            return false;
        }
        // ANTES de excluir o evento principal, é CRUCIAL lidar com as dependências:
        // 1. Excluir associações na tabela evento_responsavel (se não usar ON DELETE CASCADE no BD)
        //    responsavelService.desassociarTodosResponsaveisDoEvento(idEvento); // Você precisaria implementar este método no ResponsavelService/Dao

        // 2. Excluir inscrições relacionadas (se não usar ON DELETE CASCADE no BD)
        //    (Você precisaria de um InscricaoService aqui e um método para excluir inscrições por evento)
        //    InscricaoService inscricaoService = new InscricaoService(); // Cuidado com a criação dentro do método, pode ser melhor injetar no construtor
        //    inscricaoService.excluirInscricoesPorEvento(idEvento); // Você precisaria implementar este método no InscricaoService/Dao

        boolean sucesso = eventoDao.excluir(idEvento);
        if (!sucesso) {
            System.out.println("EventoService: Falha ao excluir evento. Verifique se não há dependências no banco de dados.");
        }
        return sucesso;
    }

    // Método de validação para um objeto Evento
    private boolean validar(Evento evento) {
        if (evento == null) {
            System.out.println("Erro de validação: Evento é nulo.");
            return false;
        }
        if (evento.getNome() == null || evento.getNome().isEmpty()) {
            System.out.println("Erro de validação: Nome do evento é obrigatório.");
            return false;
        }
        if (evento.getTipo() == null || evento.getTipo().isEmpty()) {
            System.out.println("Erro de validação: Tipo do evento é obrigatório.");
            return false;
        }
        if (evento.getData() == null) {
            System.out.println("Erro de validação: Data do evento é obrigatória.");
            return false;
        }
        if (evento.getData().isBefore(LocalDate.now()) && evento.getId_evento() == 0) {
            System.out.println("Erro de validação: Data de novo evento não pode ser no passado.");
            return false;
        }
        if (evento.getHora() == null) {
            System.out.println("Erro de validação: Hora do evento é obrigatória.");
            return false;
        }
        if (evento.getDescricao() == null || evento.getDescricao().isEmpty()) {
            System.out.println("Erro de validação: Descrição do evento é obrigatória.");
            return false;
        }
        if (evento.getLocal() == null || evento.getLocal().getId_local() <= 0) {
            System.out.println("Erro de validação: Local do evento inválido ou não selecionado.");
            return false;
        }
        return true;
    }
}