package br.com.teste.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import br.com.teste.model.Inscricao;
import br.com.teste.model.Evento;
import br.com.teste.model.Participante;
import br.com.teste.config.Conexao;

public class InscricaoDao {

    private Conexao conexao;
    private PreparedStatement ps;

    public InscricaoDao(){
        conexao = new Conexao();
    }

    public ResultSet listar(){
        try {
            return conexao.getConn()
                    .createStatement().executeQuery("SELECT * FROM inscricao");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao listar inscrições.");
        }
        return null;
    }

    public void inserir(Inscricao inscricao){
        try {

            String SQL = "INSERT INTO inscricao(id_evento, id_participante, data_inscricao) "+ " VALUES (?, ?, ?)";

            ps = conexao.getConn().prepareStatement(SQL);

            ps.setInt(1, inscricao.getEvento().getId_evento());
            ps.setInt(2, inscricao.getParticipante().getId_participante());
            ps.setDate(3, Date.valueOf(inscricao.getDataInscricao()));

            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao inserir inscrição.");
        }
    }

    public void excluir(Inscricao inscricao){
        try {
            String SQL = "DELETE FROM inscricao WHERE id_inscricao = ?";

            ps = conexao.getConn().prepareStatement(SQL);

            ps.setInt(1, inscricao.getId_inscricao());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao excluir inscrição.");
        }
    }

    public void editar(Inscricao inscricao){
        try {

            String SQL = "UPDATE inscricao SET " +
                    "id_evento = ?, id_participante = ?, data_inscricao = ? " +
                    "WHERE id_inscricao = ?";

            ps = conexao.getConn().prepareStatement(SQL);

            ps.setInt(1, inscricao.getEvento().getId_evento());
            ps.setInt(2, inscricao.getParticipante().getId_participante());
            ps.setDate(3, Date.valueOf(inscricao.getDataInscricao()));
            ps.setInt(4, inscricao.getId_inscricao());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao editar inscrição.");
        }
    }
}