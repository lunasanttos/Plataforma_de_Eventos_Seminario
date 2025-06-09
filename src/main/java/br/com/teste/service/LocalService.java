package br.com.teste.service;
import br.com.teste.dao.LocalDao;
import br.com.teste.model.Local;

import java.util.List; // Adicionado: Import para List

public class LocalService {

    private LocalDao localDao;

    public LocalService(){
        localDao = new LocalDao();
    }

    public List<Local> listar(){ // Alterado de ResultSet para List<Local>
        return  localDao.listar();
    }

    public boolean inserir(Local local){

        if (!validar(local))
            return false;

        return localDao.inserir(local);
    }

    public boolean excluir(Local local){


        if (local.getId_local() == 0)
            return false;

        return localDao.excluir(local);
    }

    public boolean editar(Local local){

        if (!validar(local))
            return false;

        return localDao.editar(local);
    }
    public boolean validar(Local local){


        if (local.getNome() == null || local.getEndereco() == null || local.getCapacidade() == 0)
            return false;


        if (local.getNome().isEmpty() || local.getEndereco().isEmpty())
            return false;

        return true;
    }
}