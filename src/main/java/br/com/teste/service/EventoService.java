package br.com.teste.service;

import br.com.teste.model.Evento;
import br.com.teste.model.Local;
import br.com.teste.model.Responsavel;
import br.com.teste.dao.EventoDao;
import br.com.teste.dao.LocalDao;

import java.util.List;

public class EventoService {

    private EventoDao eventoDao;
    private LocalDao localDao;

    public EventoService() {
        eventoDao = new EventoDao();
        localDao = new LocalDao();
    }

    public List<Evento> listar() {
        return eventoDao.listar();
    }

    public boolean inserir(Evento evento) {
        if (!validar(evento)) {
            System.out.println("DEBUG: EventoService.inserir() - Validação inicial falhou.");
            return false;
        }

        if (evento.getId_Local() != null && evento.getId_Local().getId_local() != 0) {
            Local localExistente = localDao.buscarPorId(evento.getId_Local().getId_local());
            if (localExistente == null) {
                System.out.println("DEBUG: EventoService.inserir() - O ID do local informado não existe no banco de dados.");
                return false;
            }
        } else {
            System.out.println("DEBUG: EventoService.inserir() - ID do local nulo ou zero.");
            return false;
        }

        boolean inseridoComSucesso = eventoDao.inserir(evento);
        if (!inseridoComSucesso) {
            System.out.println("DEBUG: EventoService.inserir() - Falha reportada pelo EventoDao.");
        }


        return inseridoComSucesso;
    }

    public boolean excluir(Evento evento) {
        if (evento.getId_evento() == 0)
            return false;
        return eventoDao.excluir(evento);
    }

    public boolean editar(Evento evento) {
        if (!validar(evento))
            return false;
        return eventoDao.editar(evento);
    }

    public boolean validar(Evento evento) {
        if (evento.getNome() == null || evento.getTipo() == null ||
                evento.getData() == null || evento.getHora() == null ||
                evento.getDescricao() == null || evento.getId_Local() == null) {
            System.out.println("DEBUG: EventoService.validar() - Campos nulos/vazios detectados (etapa 1).");
            return false;
        }

        if (evento.getNome().isEmpty() || evento.getTipo().isEmpty() ||
                evento.getDescricao().isEmpty()) {
            System.out.println("DEBUG: EventoService.validar() - Campos vazios detectados (etapa 2).");
            return false;
        }

        if (evento.getId_Local().getId_local() == 0) {
            System.out.println("DEBUG: EventoService.validar() - ID do Local é zero.");
            return false;
        }

        if (evento.getResponsavelLista() != null) {
            for (Responsavel r : evento.getResponsavelLista()) {
                if (r.getId_responsavel() == 0) {
                    System.out.println("DEBUG: EventoService.validar() - Responsável com ID inválido.");
                    return false;
                }
            }
        }

        return true;
    }
}
