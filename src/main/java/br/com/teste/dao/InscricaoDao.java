package br.com.teste.dao;

import br.com.teste.config.Conexao;
import br.com.teste.model.Evento;
import br.com.teste.model.Inscricao;
import br.com.teste.model.Local;
import br.com.teste.model.Participante;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InscricaoDao {

    private Conexao conexao;

    public InscricaoDao(){
        this.conexao = Conexao.getInstance();
    }

    // Método auxiliar para criar um objeto Evento a partir de um ResultSet
    private Evento criarEventoDoResultSet(ResultSet rs) throws SQLException {
        // As colunas para o Local devem vir do JOIN com a tabela 'local'
        Local localDoEvento = new Local(
                rs.getInt("id_local_evento"),
                rs.getString("local_nome_evento"),
                rs.getString("local_endereco_evento"),
                rs.getInt("local_capacidade_evento")
        );

        // As colunas para o Evento devem vir da tabela 'evento'
        return new Evento(
                rs.getInt("id_evento_inscricao"),
                rs.getString("nome_evento_inscricao"),
                rs.getString("tipo_evento_inscricao"),
                rs.getDate("data_evento_inscricao").toLocalDate(),
                rs.getTime("hora_evento_inscricao").toLocalTime(),
                rs.getString("descricao_evento_inscricao"),
                localDoEvento
        );
    }

    // Método auxiliar para criar um objeto Participante a partir de um ResultSet
    private Participante criarParticipanteDoResultSet(ResultSet rs) throws SQLException {
        return new Participante(
                rs.getInt("id_participante_inscricao"),
                rs.getString("nome_participante_inscricao"),
                rs.getString("email_participante_inscricao"),
                rs.getString("cpf_participante_inscricao")
        );
    }

    public List<Inscricao> listar(){
        List<Inscricao> inscricoes = new ArrayList<>();
        // Query COMPLETA com JOINs e ALIASES para todas as colunas necessárias
        String SQL = "SELECT i.id_inscricao, i.data_inscricao, i.ativa, " +
                "e.id_evento AS id_evento_inscricao, e.nome AS nome_evento_inscricao, e.tipo AS tipo_evento_inscricao, " +
                "e.data AS data_evento_inscricao, e.hora AS hora_evento_inscricao, e.descricao AS descricao_evento_inscricao, " +
                "l.id_local AS id_local_evento, l.nome AS local_nome_evento, l.endereco AS local_endereco_evento, l.capacidade AS local_capacidade_evento, " +
                "p.id_participante AS id_participante_inscricao, p.nome AS nome_participante_inscricao, " +
                "p.email AS email_participante_inscricao, p.cpf AS cpf_participante_inscricao " +
                "FROM inscricao i " +
                "JOIN evento e ON i.id_evento = e.id_evento " +
                "JOIN local l ON e.id_local = l.id_local " +
                "JOIN participante p ON i.id_participante = p.id_participante";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inscricao inscricao = new Inscricao(
                        rs.getInt("id_inscricao"),
                        criarEventoDoResultSet(rs),
                        criarParticipanteDoResultSet(rs),
                        rs.getDate("data_inscricao").toLocalDate(),
                        rs.getBoolean("ativa")
                );
                inscricoes.add(inscricao);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao listar inscrições no DAO: " + ex.getMessage());
        }
        return inscricoes;
    }

    public boolean inserir(Inscricao inscricao){
        boolean sucesso = false;
        String SQL = "INSERT INTO inscricao(id_evento, id_participante, data_inscricao, ativa) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, inscricao.getEvento().getId_evento());
            ps.setInt(2, inscricao.getParticipante().getId_participante());
            ps.setDate(3, Date.valueOf(inscricao.getDataInscricao()));
            ps.setBoolean(4, inscricao.isAtiva());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        inscricao.setId_inscricao(rs.getInt(1));
                    }
                }
                sucesso = true;
                System.out.println("Inscrição inserida com sucesso no banco de dados!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao inserir inscrição no DAO: " + ex.getMessage());
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
                System.out.println("Inscrição excluída com sucesso do banco de dados!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao excluir inscrição no DAO: " + ex.getMessage());
        }
        return sucesso;
    }

    public boolean editar(Inscricao inscricao){
        boolean sucesso = false;
        String SQL = "UPDATE inscricao SET " +
                "id_evento = ?, id_participante = ?, data_inscricao = ?, ativa = ? " +
                "WHERE id_inscricao = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, inscricao.getEvento().getId_evento());
            ps.setInt(2, inscricao.getParticipante().getId_participante());
            ps.setDate(3, Date.valueOf(inscricao.getDataInscricao()));
            ps.setBoolean(4, inscricao.isAtiva());
            ps.setInt(5, inscricao.getId_inscricao());

            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Inscrição editada com sucesso no banco de dados!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao editar inscrição no DAO: " + ex.getMessage());
        }
        return sucesso;
    }

    public Inscricao buscarPorId(int id) {
        Inscricao inscricao = null;
        // Query para buscar uma inscrição por ID, também com JOINs para dados completos
        String SQL = "SELECT i.id_inscricao, i.data_inscricao, i.ativa, " +
                "e.id_evento AS id_evento_inscricao, e.nome AS nome_evento_inscricao, e.tipo AS tipo_evento_inscricao, " +
                "e.data AS data_evento_inscricao, e.hora AS hora_evento_inscricao, e.descricao AS descricao_evento_inscricao, " +
                "l.id_local AS id_local_evento, l.nome AS local_nome_evento, l.endereco AS local_endereco_evento, l.capacidade AS local_capacidade_evento, " +
                "p.id_participante AS id_participante_inscricao, p.nome AS nome_participante_inscricao, " +
                "p.email AS email_participante_inscricao, p.cpf AS cpf_participante_inscricao " +
                "FROM inscricao i " +
                "JOIN evento e ON i.id_evento = e.id_evento " +
                "JOIN local l ON e.id_local = l.id_local " +
                "JOIN participante p ON i.id_participante = p.id_participante " +
                "WHERE i.id_inscricao = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    inscricao = new Inscricao(
                            rs.getInt("id_inscricao"),
                            criarEventoDoResultSet(rs),
                            criarParticipanteDoResultSet(rs),
                            rs.getDate("data_inscricao").toLocalDate(),
                            rs.getBoolean("ativa")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao buscar inscrição por ID no DAO: " + ex.getMessage());
        }
        return inscricao;
    }

    // Método para listar inscrições por ID do Participante (utilizado no MenuParticipante)
    public List<Inscricao> listarPorParticipante(int idParticipante) {
        List<Inscricao> inscricoes = new ArrayList<>();
        // Query COMPLETA com JOINs e ALIASES para todas as colunas necessárias
        String SQL = "SELECT i.id_inscricao, i.data_inscricao, i.ativa, " +
                "e.id_evento AS id_evento_inscricao, e.nome AS nome_evento_inscricao, e.tipo AS tipo_evento_inscricao, " +
                "e.data AS data_evento_inscricao, e.hora AS hora_evento_inscricao, e.descricao AS descricao_evento_inscricao, " +
                "l.id_local AS id_local_evento, l.nome AS local_nome_evento, l.endereco AS local_endereco_evento, l.capacidade AS local_capacidade_evento, " +
                "p.id_participante AS id_participante_inscricao, p.nome AS nome_participante_inscricao, " +
                "p.email AS email_participante_inscricao, p.cpf AS cpf_participante_inscricao " +
                "FROM inscricao i " +
                "JOIN evento e ON i.id_evento = e.id_evento " +
                "JOIN local l ON e.id_local = l.id_local " +
                "JOIN participante p ON i.id_participante = p.id_participante " +
                "WHERE i.id_participante = ?";

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, idParticipante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inscricao inscricao = new Inscricao(
                            rs.getInt("id_inscricao"),
                            criarEventoDoResultSet(rs),
                            criarParticipanteDoResultSet(rs),
                            rs.getDate("data_inscricao").toLocalDate(),
                            rs.getBoolean("ativa")
                    );
                    inscricoes.add(inscricao);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao listar inscrições por participante no DAO: " + ex.getMessage());
        }
        return inscricoes;
    }

    // Este método é novo (ou foi ajustado) para a funcionalidade de exclusão de evento
    // ou para a verificação de dependência de exclusão de evento.
    // Ele lista inscrições por ID de evento.
    public List<Inscricao> listarPorEvento(int idEvento) {
        List<Inscricao> inscricoes = new ArrayList<>();
        String SQL = "SELECT i.id_inscricao, i.data_inscricao, i.ativa, " +
                "e.id_evento AS id_evento_inscricao, e.nome AS nome_evento_inscricao, e.tipo AS tipo_evento_inscricao, " +
                "e.data AS data_evento_inscricao, e.hora AS hora_evento_inscricao, e.descricao AS descricao_evento_inscricao, " +
                "l.id_local AS id_local_evento, l.nome AS local_nome_evento, l.endereco AS local_endereco_evento, l.capacidade AS local_capacidade_evento, " +
                "p.id_participante AS id_participante_inscricao, p.nome AS nome_participante_inscricao, " +
                "p.email AS email_participante_inscricao, p.cpf AS cpf_participante_inscricao " +
                "FROM inscricao i " +
                "JOIN evento e ON i.id_evento = e.id_evento " +
                "JOIN local l ON e.id_local = l.id_local " +
                "JOIN participante p ON i.id_participante = p.id_participante " +
                "WHERE i.id_evento = ?";

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, idEvento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inscricao inscricao = new Inscricao(
                            rs.getInt("id_inscricao"),
                            criarEventoDoResultSet(rs),
                            criarParticipanteDoResultSet(rs),
                            rs.getDate("data_inscricao").toLocalDate(),
                            rs.getBoolean("ativa")
                    );
                    inscricoes.add(inscricao);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao listar inscrições por evento no DAO: " + ex.getMessage());
        }
        return inscricoes;
    }

    // metodo para conseguir verificar o evento com mais publico
    public int contarInscricoesPorEvento(int idEvento) {
        int total = 0;
        String SQL = "SELECT COUNT(*) AS total FROM inscricao WHERE id_evento = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, idEvento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt("total");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao contar inscrições por evento no DAO: " + ex.getMessage());
        }
        return total;
    }

}