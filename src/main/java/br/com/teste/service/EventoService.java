package br.com.teste.service;

import br.com.teste.dao.EventoDao;
import br.com.teste.dao.InscricaoDao;
import br.com.teste.model.Evento;
import br.com.teste.model.Local;
import br.com.teste.model.Responsavel;
import java.time.LocalDate;
import java.util.List;

public class EventoService {

    private EventoDao eventoDao;
    private ResponsavelService responsavelService;

    public EventoService() {
        this.eventoDao = new EventoDao();
        this.responsavelService = new ResponsavelService();
    }


    public Local getLocalDoEvento(int id_evento) {
        Evento evento = buscarPorId(id_evento);
        if (evento != null) {
            return evento.getLocal();
        }
        return null;
    }


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


    public Evento buscarPorId(int idEvento) {
        if (idEvento <= 0) {
            System.out.println("Erro no EventoService: ID de evento inválido para busca.");
            return null;
        }
        Evento evento = eventoDao.buscarPorId(idEvento);
        if (evento != null) {
            evento.setResponsavelLista(responsavelService.listarResponsaveisPorEvento(evento.getId_evento()));
        }
        return evento;
    }

    public List<Evento> listarEventosDisponiveis() {
        List<Evento> eventos = eventoDao.listarEventosDisponiveis();

        for (Evento evento : eventos) {
            evento.setResponsavelLista(responsavelService.listarResponsaveisPorEvento(evento.getId_evento()));
        }
        return eventos;
    }

    public List<Evento> listarTodosEventos() {
        List<Evento> eventos = eventoDao.listarTodos();

        for (Evento evento : eventos) {
            evento.setResponsavelLista(responsavelService.listarResponsaveisPorEvento(evento.getId_evento()));
        }
        return eventos;
    }

    public boolean inserir(Evento evento) {
        if (!validar(evento)) {
            System.out.println("EventoService: Validação falhou. Evento não será inserido.");
            return false;
        }
        boolean sucesso = eventoDao.inserir(evento);

        return sucesso;
    }

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

        return sucesso;
    }

    public boolean excluir(int idEvento) {
        if (idEvento <= 0) {
            System.out.println("Erro no EventoService: ID do evento inválido para exclusão.");
            return false;
        }

        boolean sucesso = eventoDao.excluir(idEvento);
        if (!sucesso) {
            System.out.println("EventoService: Falha ao excluir evento. Verifique se não há dependências no banco de dados.");
        }
        return sucesso;
    }

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