package br.com.teste.dao;

import br.com.teste.config.Conexao;
import br.com.teste.model.Participante;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ParticipanteDao {

    private Conexao conexao;
    // private PreparedStatement ps; // REMOVER: ps deve ser declarado localmente com try-with-resources

    public ParticipanteDao() {
        this.conexao = Conexao.getInstance();
    }

    public Participante buscarPorLogin(String nome, String email, String cpf) {
        Participante participante = null;
        try (PreparedStatement ps = conexao.getConn().prepareStatement("SELECT * FROM participante WHERE nome = ? AND email = ? AND cpf = ?")) {
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, cpf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    participante = new Participante();
                    participante.setId_participante(rs.getInt("id_participante"));
                    participante.setNome(rs.getString("nome"));
                    participante.setEmail(rs.getString("email"));
                    participante.setCpf(rs.getString("cpf"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao buscar participante por login.");
        }
        return participante;
    }

    // Alterado para retornar List<Participante> e usar try-with-resources
    public List<Participante> listar() {
        List<Participante> participantes = new ArrayList<>();
        String SQL = "SELECT * FROM participante ORDER BY nome";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Participante participante = new Participante();
                participante.setId_participante(rs.getInt("id_participante"));
                participante.setNome(rs.getString("nome"));
                participante.setEmail(rs.getString("email"));
                participante.setCpf(rs.getString("cpf"));
                participantes.add(participante);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao listar participantes.");
        }
        return participantes;
    }

    // Já retorna boolean e usa try-with-resources corretamente
    public boolean inserir(Participante participante) {
        boolean sucesso = false;
        try (PreparedStatement ps = conexao.getConn().prepareStatement(
                "INSERT INTO participante (nome, cpf, email) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, participante.getNome());
            ps.setString(2, participante.getCpf());
            ps.setString(3, participante.getEmail());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        participante.setId_participante(rs.getInt(1));
                    }
                }
                sucesso = true;
                System.out.println("Participante inserido com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao inserir participante.");
        }
        return sucesso;
    }

    // Já retorna boolean e usa try-with-resources corretamente
    public boolean excluir(Participante participante) {
        boolean sucesso = false;
        try (PreparedStatement ps = conexao.getConn().prepareStatement("DELETE FROM participante WHERE id_participante = ?")) {
            ps.setInt(1, participante.getId_participante());
            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Participante excluído com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao excluir participante.");
        }
        return sucesso;
    }

    // Já retorna boolean e usa try-with-resources corretamente
    public boolean editar(Participante participante) {
        boolean sucesso = false;
        try (PreparedStatement ps = conexao.getConn().prepareStatement("UPDATE participante SET nome = ?, cpf = ?, email = ? WHERE id_participante = ?")) {
            ps.setString(1, participante.getNome());
            ps.setString(2, participante.getCpf());
            ps.setString(3, participante.getEmail());
            ps.setInt(4, participante.getId_participante());
            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Participante editado com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao editar participante.");
        }
        return sucesso;
    }

    // Adicionado método para buscar por ID para consistência
    public Participante buscarPorId(int id) {
        Participante participante = null;
        String SQL = "SELECT * FROM participante WHERE id_participante = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    participante = new Participante();
                    participante.setId_participante(rs.getInt("id_participante"));
                    participante.setNome(rs.getString("nome"));
                    participante.setEmail(rs.getString("email"));
                    participante.setCpf(rs.getString("cpf"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao buscar participante por ID.");
        }
        return participante;
    }
}