package br.com.teste.dao;

import br.com.teste.model.Evento;
import br.com.teste.model.Local;
import br.com.teste.config.Conexao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;


public class EventoDao {

    private Conexao conexao;
    private PreparedStatement ps;

    public EventoDao(){
        this.conexao = new Conexao();
    }

    public ResultSet listar() {
        try {
            return conexao.getConn()
                    .createStatement().executeQuery("SELECT * FROM evento");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao listar eventos.");
        }
        return null;
    }

    public void inserir(Evento evento) {
        try {
            String SQL = "INSERT INTO evento(nome, tipo, data, hora, descricao, id_local) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            ps = conexao.getConn().prepareStatement(SQL);


            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getTipo());
            ps.setDate(3, Date.valueOf(evento.getData()));
            ps.setTime(4, Time.valueOf(evento.getHora()));
            ps.setString(5, evento.getDescricao());
            ps.setInt(6, evento.getId_Local().getId_local());


            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();

            System.out.println("Ocorreu um erro ao inserir evento.");
        }
    }

    public void excluir(Evento evento) {
        try {
            String SQL = "DELETE FROM evento WHERE id_evento = ?";
            ps = conexao.getConn().prepareStatement(SQL);
            ps.setInt(1, evento.getId_evento());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao excluir evento.");
        }
    }

    public void editar(Evento evento) {
        try {
            String SQL = "UPDATE evento SET nome = ?, tipo = ?, data = ?, hora = ?, descricao = ?, id_local = ? " +
                    "WHERE id_evento = ?";
            ps = conexao.getConn().prepareStatement(SQL);

            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getTipo());
            ps.setDate(3, Date.valueOf(evento.getData()));
            ps.setTime(4, Time.valueOf(evento.getHora()));
            ps.setString(5, evento.getDescricao());
            ps.setInt(6, evento.getId_Local().getId_local());
            ps.setInt(7, evento.getId_evento());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao editar evento.");
        }
    }
}