package br.com.teste.dao;

import br.com.teste.config.Conexao;
import br.com.teste.model.Responsavel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResponsavelDao {

    private Conexao conexao;

    public ResponsavelDao() {
        conexao = new Conexao();
    }

    public ResultSet listar() {
        try {
            return conexao.getConn()
                    .createStatement()
                    .executeQuery("SELECT * FROM responsavel");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao listar responsáveis.");
        }
        return null;
    }

    public void inserir(Responsavel responsavel) {
        String SQL = "INSERT INTO responsavel(id_responsavel, nome, email) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, responsavel.getId_responsavel());
            ps.setString(2, responsavel.getNome());
            ps.setString(3, responsavel.getEmail());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao inserir responsável.");
        }
    }

    public void excluir(Responsavel responsavel) {
        String SQL = "DELETE FROM responsavel WHERE id_responsavel = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, responsavel.getId_responsavel());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao excluir responsável.");
        }
    }

    public void editar(Responsavel responsavel) {
        String SQL = "UPDATE responsavel SET nome = ?, email = ? WHERE id_responsavel = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setString(1, responsavel.getNome());
            ps.setString(2, responsavel.getEmail());
            ps.setInt(3, responsavel.getId_responsavel());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao editar responsável.");
        }
    }

    public Responsavel buscarPorLogin(String nome, String email) {
        Responsavel responsavel = null;
        String SQL = "SELECT * FROM responsavel WHERE nome = ? AND email = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setString(1, nome);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    responsavel = new Responsavel();
                    responsavel.setId_responsavel(rs.getInt("id_responsavel"));
                    responsavel.setNome(rs.getString("nome"));
                    responsavel.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao buscar responsável por login.");
        }
        return responsavel;
    }
}