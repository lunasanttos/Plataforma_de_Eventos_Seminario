package br.com.teste.service;

import br.com.teste.dao.ResponsavelDao;
import br.com.teste.model.Responsavel;

import java.util.List; // Adicionado: Import para List

public class ResponsavelService {
    private ResponsavelDao responsavelDao;

    public ResponsavelService() {
        responsavelDao = new ResponsavelDao();
    }

    public List<Responsavel> listar() { // Alterado de ResultSet para List<Responsavel>
        return responsavelDao.listar();
    }

    public boolean inserir(Responsavel responsavel) {
        if (!validar(responsavel))
            return false;
        return responsavelDao.inserir(responsavel);
    }

    public boolean excluir(Responsavel responsavel) {
        if (responsavel.getId_responsavel() == 0)
            return false;
        return responsavelDao.excluir(responsavel);
    }

    public boolean editar(Responsavel responsavel) {
        if (!validar(responsavel))
            return false;
        return responsavelDao.editar(responsavel);
    }

    public boolean validar(Responsavel responsavel) {
        return responsavel.getNome() != null && responsavel.getEmail() != null &&
                !responsavel.getNome().isEmpty() && !responsavel.getEmail().isEmpty();
    }

    public Responsavel validarLogin(String nome, String email) {
        return responsavelDao.buscarPorLogin(nome, email);
    }
}