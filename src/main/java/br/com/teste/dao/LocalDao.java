package br.com.teste.dao;
import br.com.teste.config.Conexao;
import br.com.teste.model.Local;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocalDao {

    private Conexao conexao;
    private PreparedStatement ps;

    public LocalDao() {
        this.conexao = new Conexao();
    }

    public ResultSet listar() {
        try {
            return conexao.getConn()
                    .createStatement().executeQuery("SELECT * FROM local");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao listar locais.");
        }
        return null;
    }


    public void inserir(Local local) {
        try {
            String SQL = "INSERT INTO local(id_local, nome, endereco, capacidade) VALUES (?, ?, ?, ?)";
            ps = conexao.getConn().prepareStatement(SQL);

            ps.setInt(1, local.getId_local());
            ps.setString(2, local.getNome());
            ps.setString(3, local.getEndereco());
            ps.setInt(4, local.getCapacidade());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao inserir local.");
        }
    }


    public void excluir(Local local) {
        try {
            String SQL = "DELETE FROM local WHERE id_local = ?";
            ps = conexao.getConn().prepareStatement(SQL);

            ps.setInt(1, local.getId_local());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao excluir local.");
        }
    }


    public void editar(Local local) {
        try {
            String SQL = "UPDATE local SET nome = ?, endereco = ?, capacidade = ? WHERE id_local = ?";
            ps = conexao.getConn().prepareStatement(SQL);

            ps.setString(1, local.getNome());
            ps.setString(2, local.getEndereco());
            ps.setInt(3, local.getCapacidade());
            ps.setInt(4, local.getId_local());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao editar local.");
        }
    }
}