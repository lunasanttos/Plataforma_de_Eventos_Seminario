package unitario;

import br.com.teste.model.Certificado;
import br.com.teste.service.CertificadoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CertificadoServiceTest {

    private CertificadoService certificadoService;

    @BeforeEach
    public void setUp() {
        certificadoService = new CertificadoService();
    }

    @Test
    public void testBuscarCertificadoValido() {
        int id_evento = 1;
        int id_participante = 3;

        Certificado certificado = certificadoService.buscarCertificadoPorEventoEParticipante(id_evento, id_participante);

        assertNotNull(certificado, "Certificado deve ser encontrado.");
        assertEquals(id_evento, certificado.getInscricao().getEvento().getId_evento());
        assertEquals(id_participante, certificado.getInscricao().getParticipante().getId_participante());
        System.out.println("Nome do participante: " + certificado.getInscricao().getParticipante().getNome());
        System.out.println("Id Certificado: "+ certificado.getId_certificado() + ", Data de emissão: " + certificado.getData_emissao()+", Código de verificação: " + certificado.getCodigo_verificacao());
    }



}
