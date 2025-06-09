package br.com.teste.dao;

import br.com.teste.config.Conexao;
import br.com.teste.model.Responsavel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Importar Statement para RETURN_GENERATED_KEYS
import java.util.ArrayList; // Importar para List
import java.util.List;     // Importar para List

public class ResponsavelDao {

    private Conexao conexao;

    public ResponsavelDao() {
        this.conexao = Conexao.getInstance();
    }

    public List<Responsavel> listar() { // Alterado para retornar List<Responsavel>
        List<Responsavel> responsaveis = new ArrayList<>();
        String SQL = "SELECT * FROM responsavel";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL); // Usando try-with-resources
             ResultSet rs = ps.executeQuery()) { // Usando try-with-resources
            while (rs.next()) {
                Responsavel responsavel = new Responsavel();
                responsavel.setId_responsavel(rs.getInt("id_responsavel"));
                responsavel.setNome(rs.getString("nome"));
                responsavel.setEmail(rs.getString("email"));
                responsaveis.add(responsavel);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao listar responsáveis: " + ex.getMessage());
        }
        return responsaveis;
    }

    public boolean inserir(Responsavel responsavel) { // Alterado para retornar boolean
        boolean sucesso = false;
        // Ajustado o SQL para não incluir id_responsavel se ele for auto-incrementável no BD
        String SQL = "INSERT INTO responsavel(nome, email) VALUES (?, ?)";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) { // Usando try-with-resources e recuperando chaves geradas
            ps.setString(1, responsavel.getNome());
            ps.setString(2, responsavel.getEmail());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                // Recupera a chave gerada pelo banco de dados
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        responsavel.setId_responsavel(rs.getInt(1)); // Define o ID no objeto Responsavel
                    }
                }
                sucesso = true;
                System.out.println("Responsável inserido com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao inserir responsável: " + ex.getMessage());
        }
        return sucesso; // Retorna true ou false
    }

    public boolean excluir(Responsavel responsavel) { // Alterado para retornar boolean
        boolean sucesso = false;
        String SQL = "DELETE FROM responsavel WHERE id_responsavel = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) { // Usando try-with-resources
            ps.setInt(1, responsavel.getId_responsavel());

            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0; // True se alguma linha foi excluída
            if (sucesso) {
                System.out.println("Responsável excluído com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao excluir responsável.");
        }
        return sucesso; // Retorna true ou false
    }

    public boolean editar(Responsavel responsavel) { // Alterado para retornar boolean
        boolean sucesso = false;
        String SQL = "UPDATE responsavel SET nome = ?, email = ? WHERE id_responsavel = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) { // Usando try-with-resources
            ps.setString(1, responsavel.getNome());
            ps.setString(2, responsavel.getEmail());
            ps.setInt(3, responsavel.getId_responsavel());

            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0; // True se alguma linha foi editada
            if (sucesso) {
                System.out.println("Responsável editado com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao editar responsável.");
        }
        return sucesso; // Retorna true ou false
    }

    public Responsavel buscarPorLogin(String nome, String email) {
        Responsavel responsavel = null;
        String SQL = "SELECT * FROM responsavel WHERE nome = ? AND email = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) { // Usando try-with-resources
            ps.setString(1, nome);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) { // Usando try-with-resources
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

    // Adicionado método para buscar por ID para consistência
    public Responsavel buscarPorId(int id) {
        Responsavel responsavel = null;
        String SQL = "SELECT * FROM responsavel WHERE id_responsavel = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) { // Usando try-with-resources
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { // Usando try-with-resources
                if (rs.next()) {
                    responsavel = new Responsavel();
                    responsavel.setId_responsavel(rs.getInt("id_responsavel"));
                    responsavel.setNome(rs.getString("nome"));
                    responsavel.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao buscar responsável por ID.");
        }
        return responsavel;
    }
}