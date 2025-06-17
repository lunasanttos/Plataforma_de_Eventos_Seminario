package br.com.teste.service;

import br.com.teste.model.Inscricao;
import br.com.teste.dao.InscricaoDao;

import java.util.List;

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

    public List<Inscricao> listar(){
        return  inscricaoDao.listar();
    }

    public boolean inserir(Inscricao inscricao){
        if (!validar(inscricao))
            return false;
        return inscricaoDao.inserir(inscricao);
    }

    public boolean excluir(Inscricao inscricao){
        if (inscricao.getId_inscricao() == 0)
            return false;
        return inscricaoDao.excluir(inscricao);
    }

    public boolean editar(Inscricao inscricao){
        if (!validar(inscricao))
            return false;
        return inscricaoDao.editar(inscricao);
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

    public List<Inscricao> listarInscricoesPorParticipante(int idParticipante) {

        if (idParticipante <= 0) {
            System.out.println("Erro no Service: ID de participante inválido para listar inscrições.");
            return new java.util.ArrayList<>();
        }
        return inscricaoDao.listarPorParticipante(idParticipante);
    }

    public Inscricao buscarInscricaoPorId(int idInscricao) {
        if (idInscricao <= 0) {
            System.out.println("Erro no Service: ID de inscrição inválido para busca.");
            return null;
        }
        return inscricaoDao.buscarPorId(idInscricao);
    }

    public Evento buscarEventoPorId(int idEvento) {
        return eventoDao.buscarPorId(idEvento);
    }

    public Participante buscarParticipantePorId(int idParticipante) {
        return participanteDao.buscarPorId(idParticipante);
    }
}