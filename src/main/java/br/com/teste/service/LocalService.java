package br.com.teste.service;

import br.com.teste.dao.LocalDao;
import br.com.teste.model.Local;

import java.util.List;

public class LocalService {

    private LocalDao localDao;

    public LocalService(){
        localDao = new LocalDao();
    }

    public List<Local> listar(){
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
        if (local == null) {
            System.out.println("Erro de validação: Local é nulo.");
            return false;
        }
        if (local.getNome() == null || local.getNome().isEmpty()) {
            System.out.println("Erro de validação: Nome do local é obrigatório.");
            return false;
        }
        if (local.getEndereco() == null || local.getEndereco().isEmpty()) {
            System.out.println("Erro de validação: Endereço do local é obrigatório.");
            return false;
        }
        if (local.getCapacidade() <= 0) {
            System.out.println("Erro de validação: Capacidade do local deve ser um número positivo.");
            return false;
        }
        return true;
    }

    public Local buscarPorId(int idLocal) {
        if (idLocal <= 0) {
            System.out.println("Erro no LocalService: ID de local inválido para busca.");
            return null;
        }
        return localDao.buscarPorId(idLocal);
    }
}