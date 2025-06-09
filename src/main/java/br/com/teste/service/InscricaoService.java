package br.com.teste.service;
import br.com.teste.model.Inscricao;
import br.com.teste.dao.InscricaoDao;

import java.sql.ResultSet;
import br.com.teste.model.Evento;
import br.com.teste.model.Participante;
import br.com.teste.dao.EventoDao;
import br.com.teste.dao.ParticipanteDao;


public class InscricaoService {

    private InscricaoDao inscricaoDao;
    private EventoDao eventoDao;
    private ParticipanteDao participanteDao;

    public InscricaoService(){
        inscricaoDao = new InscricaoDao();
        eventoDao = new EventoDao();
        participanteDao = new ParticipanteDao();
    }

    public ResultSet listar(){
        return  inscricaoDao.listar();
    }

    public boolean inserir(Inscricao inscricao){
        if (!validar(inscricao))
            return false;
        inscricaoDao.inserir(inscricao);
        return true;
    }

    public boolean excluir(Inscricao inscricao){
        if (inscricao.getId_inscricao() == 0)
            return false;
        inscricaoDao.excluir(inscricao);
        return true;
    }

    public boolean editar(Inscricao inscricao){
        if (!validar(inscricao))
            return false;
        inscricaoDao.editar(inscricao);
        return true;
    }
    public boolean validar(Inscricao inscricao){
        if (inscricao.getDataInscricao() == null ||
                inscricao.getEvento() == null || inscricao.getParticipante() == null)
            return false;

        if (inscricao.getEvento().getId_evento() == 0 ||
                inscricao.getParticipante().getId_participante() == 0) {
            return false;
        }

        return true;
    }

    public Evento buscarEventoPorId(int idEvento) {
        return null;
    }

    public Participante buscarParticipantePorId(int idParticipante) {
        return null;
    }
}