package br.com.teste.dao;

import br.com.teste.model.Evento;
import br.com.teste.model.Local;
import br.com.teste.config.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventoDao {

    private Connection conexao;

    public EventoDao() {
        this.conexao = Conexao.getInstance().getConn();
    }

    private Evento criarEventoDoResultSet(ResultSet rs) throws SQLException {
        Local localDoEvento = new Local(
                rs.getInt("id_local"),
                rs.getString("local_nome"),
                rs.getString("local_endereco"),
                rs.getInt("local_capacidade")
        );

        return new Evento(
                rs.getInt("id_evento"),
                rs.getString("nome"),
                rs.getString("tipo"),
                rs.getDate("data").toLocalDate(),
                rs.getTime("hora").toLocalTime(),
                rs.getString("descricao"),
                localDoEvento
        );
    }

    // --- Métodos de Leitura (Read) ---

    public Evento buscarPorId(int idEvento) {
        Evento evento = null;
        String sql = "SELECT e.id_evento, e.nome, e.tipo, e.data, e.hora, e.descricao, " +
                "l.id_local, l.nome AS local_nome, l.endereco AS local_endereco, l.capacidade AS local_capacidade " +
                "FROM evento e JOIN local l ON e.id_local = l.id_local " +
                "WHERE e.id_evento = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idEvento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    evento = criarEventoDoResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar evento por ID no DAO: " + e.getMessage());
            e.printStackTrace();
        }
        return evento;
    }

    public List<Evento> listarEventosDisponiveis() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT e.id_evento, e.nome, e.tipo, e.data, e.hora, e.descricao, " +
                "l.id_local, l.nome AS local_nome, l.endereco AS local_endereco, l.capacidade AS local_capacidade " +
                "FROM evento e JOIN local l ON e.id_local = l.id_local " +
                "WHERE e.data >= CURRENT_DATE ORDER BY e.data ASC, e.hora ASC";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                eventos.add(criarEventoDoResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar eventos disponíveis no DAO: " + e.getMessage());
            e.printStackTrace();
        }
        return eventos;
    }

    // LISTAR TODOS OS EVENTOS
    public List<Evento> listarTodos() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT e.id_evento, e.nome, e.tipo, e.data, e.hora, e.descricao, " +
                "l.id_local, l.nome AS local_nome, l.endereco AS local_endereco, l.capacidade AS local_capacidade " +
                "FROM evento e JOIN local l ON e.id_local = l.id_local " +
                "ORDER BY e.data DESC, e.hora DESC";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                eventos.add(criarEventoDoResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar todos os eventos no DAO: " + e.getMessage());
            e.printStackTrace();
        }
        return eventos;
    }

    // --- Método de Criação (Create) ---

    public boolean inserir(Evento evento) {
        boolean sucesso = false;
        String sql = "INSERT INTO evento (nome, tipo, data, hora, descricao, id_local) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getTipo());
            ps.setDate(3, Date.valueOf(evento.getData()));
            ps.setTime(4, Time.valueOf(evento.getHora()));
            ps.setString(5, evento.getDescricao());
            ps.setInt(6, evento.getLocal().getId_local()); // Agora usando getLocal()

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        evento.setId_evento(rs.getInt(1));
                    }
                }
                sucesso = true;
                System.out.println("Evento inserido no banco de dados!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao inserir evento no DAO: " + ex.getMessage());
        }
        return sucesso;
    }

    // --- Método de Edição (Update) ---

    // EDITAR EVENTO
    public boolean editar(Evento evento) {
        boolean sucesso = false;
        String sql = "UPDATE evento SET nome = ?, tipo = ?, data = ?, hora = ?, descricao = ?, id_local = ? " +
                "WHERE id_evento = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getTipo());
            ps.setDate(3, Date.valueOf(evento.getData()));
            ps.setTime(4, Time.valueOf(evento.getHora()));
            ps.setString(5, evento.getDescricao());
            ps.setInt(6, evento.getLocal().getId_local()); // Agora usando getLocal()
            ps.setInt(7, evento.getId_evento());

            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Evento editado no banco de dados!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao editar evento no DAO: " + ex.getMessage());
        }
        return sucesso;
    }

    // --- Método de Exclusão (Delete) ---

    // EXCLUIR EVENTO
    public boolean excluir(int idEvento) {
        boolean sucesso = false;
        String sql = "DELETE FROM evento WHERE id_evento = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idEvento);
            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Evento excluído do banco de dados!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao excluir evento no DAO: " + ex.getMessage());
        }
        return sucesso;
    }
}