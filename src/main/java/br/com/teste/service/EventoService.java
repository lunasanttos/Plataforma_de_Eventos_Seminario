package br.com.teste.service;

import br.com.teste.model.Evento;
import br.com.teste.dao.EventoDao;
import br.com.teste.model.Local;
import java.time.LocalDate;
import java.util.List;

public class EventoService {

    private EventoDao eventoDao;

    public EventoService() {
        this.eventoDao = new EventoDao();
    }

    // Read: Busca um evento por ID
    public Evento buscarPorId(int idEvento) {
        if (idEvento <= 0) {
            System.out.println("Erro no EventoService: ID de evento inválido para busca.");
            return null;
        }
        return eventoDao.buscarPorId(idEvento);
    }

    // Read: Lista eventos que estão disponíveis (ex: futuros)
    public List<Evento> listarEventosDisponiveis() {
        return eventoDao.listarEventosDisponiveis();
    }

    // Read: Lista TODOS os eventos (independentemente da data)
    // Este método é usado pelo MenuResponsavel para exibir eventos para edição/exclusão
    public List<Evento> listarTodosEventos() {
        return eventoDao.listarTodos();
    }

    // Create: Insere um novo evento
    public boolean inserir(Evento evento) {
        if (!validar(evento)) {
            System.out.println("EventoService: Validação falhou. Evento não será inserido.");
            return false;
        }
        return eventoDao.inserir(evento);
    }

    // Update: Edita um evento existente
    // Este método é usado pelo MenuResponsavel para editar um evento
    public boolean editar(Evento evento) {
        if (evento.getId_evento() <= 0) {
            System.out.println("Erro no EventoService: ID do evento inválido para edição.");
            return false;
        }
        if (!validar(evento)) {
            System.out.println("EventoService: Validação falhou. Evento não será editado.");
            return false;
        }
        return eventoDao.editar(evento);
    }

    // Delete: Exclui um evento
    // Este método é usado pelo MenuResponsavel para excluir um evento
    public boolean excluir(int idEvento) {
        if (idEvento <= 0) {
            System.out.println("Erro no EventoService: ID do evento inválido para exclusão.");
            return false;
        }
        // Regra de negócio: Você pode adicionar lógica aqui para verificar
        // se existem inscrições ativas para este evento antes de excluir.
        // Se houver, você precisaria de um InscricaoService aqui para verificar.
        /*
        // Exemplo de verificação de dependência (descomente e implemente se necessário)
        InscricaoService inscricaoService = new InscricaoService(); // Cuidado com a criação dentro do método, pode ser melhor injetar
        List<Inscricao> inscricoesDoEvento = inscricaoService.listarInscricoesPorEvento(idEvento); // Precisaria desse método no InscricaoService
        if (inscricoesDoEvento != null && !inscricoesDoEvento.isEmpty()) {
            System.out.println("Erro no EventoService: Não é possível excluir evento com inscrições ativas.");
            return false;
        }
        */
        return eventoDao.excluir(idEvento);
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
        // Para edição, talvez você queira permitir datas passadas se o evento já ocorreu.
        // Para novos eventos, essa validação é geralmente boa.
        if (evento.getData().isBefore(LocalDate.now()) && evento.getId_evento() == 0) { // Se for novo evento e data passada
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