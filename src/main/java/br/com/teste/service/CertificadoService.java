package br.com.teste.service;
import br.com.teste.model.Certificado;
import br.com.teste.dao.CertificadoDao;

import java.util.List; // Adicionado: Import para List

public class CertificadoService {

    private CertificadoDao certificadoDao;

    public CertificadoService(){
        certificadoDao = new CertificadoDao();
    }

    // metodo para retornar a certificado de um determinado participante em um determinado evento.
    public Certificado buscarCertificadoPorEventoEParticipante(int idEvento, int idParticipante) {
        return certificadoDao.buscarCertificado(idEvento, idParticipante);
    }

    public List<Certificado> listar(){ // Alterado de ResultSet para List<Certificado>
        return  certificadoDao.listar();
    }

    public boolean inserir(Certificado certificado){
        if (!validar(certificado))
            return false;

        return certificadoDao.inserir(certificado);
    }

    public boolean excluir(Certificado certificado){
        if (certificado.getId_Certificado() == 0) // Cuidado com a capitalização aqui, como já mencionei.
            return false;

        return certificadoDao.excluir(certificado);
    }

    public boolean editar(Certificado certificado){
        if (!validar(certificado))
            return false;

        return certificadoDao.editar(certificado);
    }

    public boolean validar(Certificado certificado) {
        if (certificado.getData_emissao() == null || certificado.getCodigo_verificacao() == null)
            return false;

        if (certificado.getCodigo_verificacao().isEmpty())
            return false;

        return true;
    }
}