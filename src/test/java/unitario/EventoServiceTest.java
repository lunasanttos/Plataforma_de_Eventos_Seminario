package unitario;

import br.com.teste.model.Evento;
import br.com.teste.model.Local;
import br.com.teste.service.EventoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventoServiceTest {
    private EventoService eventoService;

    @BeforeEach
    public void setUp() {
        eventoService = new EventoService();
    }

    @Test
    public void testBuscarLocalDoEventoValido() {
        Local local = eventoService.getLocalDoEvento(2);
        assertNotNull(local.getNome());
        assertNotNull(local.getEndereco());
        assertNotNull(local.getCapacidade());
        System.out.println("Nome do Local: " + local.getNome() + ", Endereço: " + local.getEndereco()+ ", Capacidade do Local:" + local.getCapacidade());
    }

    @Test
    public void testEventoComMaiorPublico(){
        Evento evento = eventoService.getEventoComMaiorPublico();
        assertNotNull(evento);
        System.out.println("Evento com maior publico: " + evento.getNome());
    }





}
