package br.com.teste.service;

import br.com.teste.dao.ResponsavelDao;
import br.com.teste.model.Responsavel;

import java.util.List; // Import para List

public class ResponsavelService {
    private ResponsavelDao responsavelDao;

    public ResponsavelService() {
        responsavelDao = new ResponsavelDao();
    }

    public List<Responsavel> listar() {
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

    // NOVO MÉTODO: Lista os responsáveis associados a um evento específico
    public List<Responsavel> listarResponsaveisPorEvento(int idEvento) {
        // Validação de negócio: garante que o ID do evento é válido
        if (idEvento <= 0) {
            System.out.println("Erro no ResponsavelService: ID de evento inválido para listar responsáveis.");
            return new java.util.ArrayList<>(); // Retorna uma lista vazia, não null
        }
        // Delega a busca ao DAO, que fará a consulta no banco de dados
        return responsavelDao.listarResponsaveisPorEventoId(idEvento);
    }

    // Você também pode querer um método buscarPorId para Responsável aqui,
    // caso outras partes do seu Service ou App precisem buscar um responsável por ID.
    // public Responsavel buscarPorId(int idResponsavel) {
    //     return responsavelDao.buscarPorId(idResponsavel);
    // }
}