package br.com.teste.dao;

import br.com.teste.config.Conexao;
import br.com.teste.model.Local;

import java.sql.Connection; // Importar java.sql.Connection
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LocalDao {

    // Altere o tipo de Conexao para Connection, se for usá-la diretamente
    // Ou mantenha Conexao e sempre chame .getConn()
    // A opção mais segura é manter 'Conexao conexao;' e chamar 'conexao.getConn()'
    private Conexao conexao; // Mantém a referência ao objeto Conexao

    public LocalDao() {
        // CORREÇÃO: Pega a conexão SQL real do objeto Conexao
        this.conexao = Conexao.getInstance(); // Pega a instância do singleton
    }

    public List<Local> listar() {
        List<Local> locais = new ArrayList<>();
        String SQL = "SELECT * FROM local";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL); // Usa conexao.getConn()
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Local local = new Local(
                        rs.getInt("id_local"),
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getInt("capacidade")
                );
                locais.add(local);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao listar locais: " + ex.getMessage());
        }
        return locais;
    }

    public boolean inserir(Local local) {
        boolean sucesso = false;
        String SQL = "INSERT INTO local(nome, endereco, capacidade) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) { // Usa conexao.getConn()
            ps.setString(1, local.getNome());
            ps.setString(2, local.getEndereco());
            ps.setInt(3, local.getCapacidade());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        local.setId_local(rs.getInt(1));
                    }
                }
                sucesso = true;
                System.out.println("Local inserido com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao inserir local: " + ex.getMessage());
        }
        return sucesso;
    }

    public boolean excluir(Local local) {
        boolean sucesso = false;
        String SQL = "DELETE FROM local WHERE id_local = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) { // Usa conexao.getConn()
            ps.setInt(1, local.getId_local());

            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Local excluído com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao excluir local: " + ex.getMessage());
        }
        return sucesso;
    }

    public boolean editar(Local local) {
        boolean sucesso = false;
        String SQL = "UPDATE local SET nome = ?, endereco = ?, capacidade = ? WHERE id_local = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) { // Usa conexao.getConn()
            ps.setString(1, local.getNome());
            ps.setString(2, local.getEndereco());
            ps.setInt(3, local.getCapacidade());
            ps.setInt(4, local.getId_local());

            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Local editado com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao editar local: " + ex.getMessage());
        }
        return sucesso;
    }

    public Local buscarPorId(int id) {
        Local local = null;
        String SQL = "SELECT * FROM local WHERE id_local = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) { // Usa conexao.getConn()
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    local = new Local(
                            rs.getInt("id_local"),
                            rs.getString("nome"),
                            rs.getString("endereco"),
                            rs.getInt("capacidade")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao buscar local por ID: " + ex.getMessage());
        }
        return local;
    }
}