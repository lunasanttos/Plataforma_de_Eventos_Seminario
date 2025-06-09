package br.com.teste.service;

import br.com.teste.dao.ResponsavelDao;
import br.com.teste.model.Responsavel;

import java.sql.ResultSet;

public class ResponsavelService {
    private ResponsavelDao responsavelDao;

    public ResponsavelService() {
        responsavelDao = new ResponsavelDao();
    }

    public ResultSet listar() {
        return responsavelDao.listar();
    }

    public boolean inserir(Responsavel responsavel) {
        if (!validar(responsavel))
            return false;
        responsavelDao.inserir(responsavel);
        return true;
    }

    public boolean excluir(Responsavel responsavel) {
        if (responsavel.getId_responsavel() == 0)
            return false;
        responsavelDao.excluir(responsavel);
        return true;
    }

    public boolean editar(Responsavel responsavel) {
        if (!validar(responsavel))
            return false;
        responsavelDao.editar(responsavel);
        return true;
    }

    public boolean validar(Responsavel responsavel) {
        return responsavel.getNome() != null && responsavel.getEmail() != null &&
                !responsavel.getNome().isEmpty() && !responsavel.getEmail().isEmpty();
    }

    public Responsavel validarLogin(String nome, String email) {
        return responsavelDao.buscarPorLogin(nome, email);
    }
}