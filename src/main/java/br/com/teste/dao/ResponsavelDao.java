package br.com.teste.dao;

import br.com.teste.config.Conexao;
import br.com.teste.model.Responsavel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ResponsavelDao {


    private Connection conexao;

    public ResponsavelDao() {

        this.conexao = Conexao.getInstance().getConn();
    }

    public List<Responsavel> listar() {
        List<Responsavel> responsaveis = new ArrayList<>();
        String SQL = "SELECT * FROM responsavel";
        try (PreparedStatement ps = conexao.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
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

    public boolean inserir(Responsavel responsavel) {
        boolean sucesso = false;
        String SQL = "INSERT INTO responsavel(nome, email) VALUES (?, ?)";
        try (PreparedStatement ps = conexao.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, responsavel.getNome());
            ps.setString(2, responsavel.getEmail());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        responsavel.setId_responsavel(rs.getInt(1));
                    }
                }
                sucesso = true;
                System.out.println("Responsável inserido com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao inserir responsável: " + ex.getMessage());
        }
        return sucesso;
    }

    public boolean excluir(Responsavel responsavel) {
        boolean sucesso = false;
        String SQL = "DELETE FROM responsavel WHERE id_responsavel = ?";
        try (PreparedStatement ps = conexao.prepareStatement(SQL)) {
            ps.setInt(1, responsavel.getId_responsavel());

            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Responsável excluído com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao excluir responsável.");
        }
        return sucesso;
    }

    public boolean editar(Responsavel responsavel) {
        boolean sucesso = false;
        String SQL = "UPDATE responsavel SET nome = ?, email = ? WHERE id_responsavel = ?";
        try (PreparedStatement ps = conexao.prepareStatement(SQL)) {
            ps.setString(1, responsavel.getNome());
            ps.setString(2, responsavel.getEmail());
            ps.setInt(3, responsavel.getId_responsavel());

            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Responsável editado com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao editar responsável.");
        }
        return sucesso;
    }

    public Responsavel buscarPorLogin(String nome, String email) {
        Responsavel responsavel = null;
        String SQL = "SELECT * FROM responsavel WHERE nome = ? AND email = ?";
        try (PreparedStatement ps = conexao.prepareStatement(SQL)) {
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

    public Responsavel buscarPorId(int id) {
        Responsavel responsavel = null;
        String SQL = "SELECT * FROM responsavel WHERE id_responsavel = ?";
        try (PreparedStatement ps = conexao.prepareStatement(SQL)) {
            ps.setInt(1, id);
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
            System.out.println("Erro ao buscar responsável por ID.");
        }
        return responsavel;
    }

    // NOVO MÉTODO IMPLEMENTADO: Listar responsáveis associados a um evento específico
    public List<Responsavel> listarResponsaveisPorEventoId(int idEvento) {
        List<Responsavel> responsaveis = new ArrayList<>();
        // Query que faz JOIN com a tabela de associação evento_responsavel
        String SQL = "SELECT r.id_responsavel, r.nome, r.email " +
                "FROM responsavel r " +
                "JOIN evento_responsavel er ON r.id_responsavel = er.id_responsavel " +
                "WHERE er.id_evento = ?";
        try (PreparedStatement ps = conexao.prepareStatement(SQL)) {
            ps.setInt(1, idEvento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Responsavel r = new Responsavel();
                    r.setId_responsavel(rs.getInt("id_responsavel"));
                    r.setNome(rs.getString("nome"));
                    r.setEmail(rs.getString("email"));
                    // Adicione outros campos do Responsavel se existirem e forem selecionados na query
                    responsaveis.add(r);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao buscar responsáveis do evento ID " + idEvento + ": " + ex.getMessage());
            ex.printStackTrace();
        }
        return responsaveis;
    }
}