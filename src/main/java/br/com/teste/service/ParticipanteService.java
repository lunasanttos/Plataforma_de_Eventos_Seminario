package br.com.teste.service;

import br.com.teste.model.Participante;
import br.com.teste.dao.ParticipanteDao;

import java.util.List; // Adicionado: Import para List

public class ParticipanteService {

    private ParticipanteDao participanteDao;

    public ParticipanteService(){
        participanteDao = new ParticipanteDao();
    }

    public Participante validarLogin(String nome, String email, String cpf) {
        if (nome == null || nome.isEmpty() ||
                email == null || email.isEmpty() ||
                cpf == null || cpf.isEmpty()) {
            return null;
        }

        return participanteDao.buscarPorLogin(nome, email, cpf);
    }

    public List<Participante> listar(){ // Alterado de ResultSet para List<Participante>
        return  participanteDao.listar();
    }

    public boolean inserir(Participante participante){

        if (!validar(participante))
            return false;

        return participanteDao.inserir(participante);
    }

    public boolean excluir(Participante participante){

        if (participante.getId_participante() == 0)
            return false;

        return participanteDao.excluir(participante);
    }

    public boolean editar(Participante participante){

        if (!validar(participante))
            return false;

        return participanteDao.editar(participante);
    }
    public boolean validar(Participante participante){

        if (participante.getNome() == null || participante.getEmail() == null || participante.getCpf() == null)
            return false;

        if (participante.getNome().isEmpty() || participante.getEmail().isEmpty() || participante.getCpf().isEmpty())
            return false;

        return true;
    }
}