package br.com.teste.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.teste.model.Inscricao;
import br.com.teste.model.Evento;
import br.com.teste.model.Participante;
import br.com.teste.config.Conexao;

public class InscricaoDao {

    private Conexao conexao;

    public InscricaoDao(){
        this.conexao = Conexao.getInstance();
    }

    public List<Inscricao> listar(){
        List<Inscricao> inscricoes = new ArrayList<>();
        String SQL = "SELECT i.*, e.nome AS evento_nome, p.nome AS participante_nome FROM inscricao i JOIN evento e ON i.id_evento = e.id_evento JOIN participante p ON i.id_participante = p.id_participante";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Evento evento = new Evento(rs.getInt("id_evento"), rs.getString("evento_nome"), null, null, null, null, null);
                Participante participante = new Participante(rs.getInt("id_participante"), rs.getString("participante_nome"), null, null);
                Inscricao inscricao = new Inscricao(
                        rs.getInt("id_inscricao"),
                        evento,
                        participante,
                        rs.getDate("data_inscricao").toLocalDate()
                );
                inscricoes.add(inscricao);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao listar inscrições: " + ex.getMessage());
        }
        return inscricoes;
    }

    public boolean inserir(Inscricao inscricao){
        boolean sucesso = false;
        String SQL = "INSERT INTO inscricao(id_evento, id_participante, data_inscricao) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, inscricao.getEvento().getId_evento());
            ps.setInt(2, inscricao.getParticipante().getId_participante());
            ps.setDate(3, Date.valueOf(inscricao.getDataInscricao()));

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        inscricao.setId_inscricao(rs.getInt(1));
                    }
                }
                sucesso = true;
                System.out.println("Inscrição inserida com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao inserir inscrição: " + ex.getMessage());
        }
        return sucesso;
    }

    public boolean excluir(Inscricao inscricao){
        boolean sucesso = false;
        String SQL = "DELETE FROM inscricao WHERE id_inscricao = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, inscricao.getId_inscricao());

            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Inscrição excluída com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao excluir inscrição: " + ex.getMessage());
        }
        return sucesso;
    }

    public boolean editar(Inscricao inscricao){
        boolean sucesso = false;
        String SQL = "UPDATE inscricao SET " +
                "id_evento = ?, id_participante = ?, data_inscricao = ? " +
                "WHERE id_inscricao = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, inscricao.getEvento().getId_evento());
            ps.setInt(2, inscricao.getParticipante().getId_participante());
            ps.setDate(3, Date.valueOf(inscricao.getDataInscricao()));
            ps.setInt(4, inscricao.getId_inscricao());

            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Inscrição editada com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao editar inscrição: " + ex.getMessage());
        }
        return sucesso;
    }

    public Inscricao buscarPorId(int id) {
        Inscricao inscricao = null;
        String SQL = "SELECT i.*, e.nome AS evento_nome, p.nome AS participante_nome FROM inscricao i JOIN evento e ON i.id_evento = e.id_evento JOIN participante p ON i.id_participante = p.id_participante WHERE i.id_inscricao = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Evento evento = new Evento(rs.getInt("id_evento"), rs.getString("evento_nome"), null, null, null, null, null);
                    Participante participante = new Participante(rs.getInt("id_participante"), rs.getString("participante_nome"), null, null);
                    inscricao = new Inscricao(
                            rs.getInt("id_inscricao"),
                            evento,
                            participante,
                            rs.getDate("data_inscricao").toLocalDate()
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao buscar inscrição por ID: " + ex.getMessage());
        }
        return inscricao;
    }
}