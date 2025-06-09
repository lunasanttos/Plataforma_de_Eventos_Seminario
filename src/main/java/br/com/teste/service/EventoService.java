package br.com.teste.service;

import br.com.teste.model.Evento;
import br.com.teste.model.Local;
import br.com.teste.dao.EventoDao;


import java.sql.ResultSet;



public class EventoService {

    private EventoDao eventoDao;

    public EventoService(){

        eventoDao = new EventoDao();
    }

    public ResultSet listar(){

        return  eventoDao.listar();
    }

    public boolean inserir(Evento evento){
        if (!validar(evento))
            return false;
        eventoDao.inserir(evento);
        return true;
    }

    public boolean excluir(Evento evento){
        if (evento.getId_evento() == 0)
            return false;
        eventoDao.excluir(evento);
        return true;
    }
    public boolean validar(Evento evento){

        if (evento.getNome() == null || evento.getTipo() == null ||
                evento.getData() == null || evento.getHora() == null ||
                evento.getDescricao() == null || evento.getId_Local() == null) //
            return false;


        if (evento.getNome().isEmpty() || evento.getTipo().isEmpty() ||
                evento.getDescricao().isEmpty()) //
            return false;


        if (evento.getId_Local().getId_local() == 0) { //
            return false;
        }

        return true;
    }
}