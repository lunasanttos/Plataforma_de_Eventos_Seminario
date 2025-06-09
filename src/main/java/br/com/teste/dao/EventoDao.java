package br.com.teste.dao;

import br.com.teste.model.Evento;
import br.com.teste.model.Local;
import br.com.teste.config.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventoDao {

    private Conexao conexao;

    public EventoDao(){
        this.conexao = Conexao.getInstance();
    }

    public List<Evento> listar() {
        List<Evento> eventos = new ArrayList<>();
        String SQL = "SELECT e.*, l.nome AS local_nome, l.endereco AS local_endereco, l.capacidade AS local_capacidade FROM evento e JOIN local l ON e.id_local = l.id_local";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Local local = new Local(
                        rs.getInt("id_local"),
                        rs.getString("local_nome"),
                        rs.getString("local_endereco"),
                        rs.getInt("local_capacidade")
                );
                Evento evento = new Evento(
                        rs.getInt("id_evento"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getDate("data").toLocalDate(),
                        rs.getTime("hora").toLocalTime(),
                        rs.getString("descricao"),
                        local
                );
                eventos.add(evento);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao listar eventos: " + ex.getMessage());
        }
        return eventos;
    }

    public boolean inserir(Evento evento) {
        boolean sucesso = false;
        String SQL = "INSERT INTO evento(nome, tipo, data, hora, descricao, id_local) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getTipo());
            ps.setDate(3, Date.valueOf(evento.getData()));
            ps.setTime(4, Time.valueOf(evento.getHora()));
            ps.setString(5, evento.getDescricao());
            ps.setInt(6, evento.getId_Local().getId_local());

            System.out.println("EventoDao: Executando INSERT SQL: " + SQL);
            System.out.println("EventoDao: Parâmetros: Nome=" + evento.getNome() + ", Tipo=" + evento.getTipo() + ", Data=" + evento.getData() + ", Hora=" + evento.getHora() + ", Descricao=" + evento.getDescricao() + ", id_local=" + evento.getId_Local().getId_local());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        evento.setId_evento(rs.getInt(1));
                    }
                }
                sucesso = true;
                System.out.println("EventoDao: Evento inserido com sucesso!");
            } else {
                System.out.println("EventoDao: Nenhuma linha afetada pelo INSERT. Possível falha silenciosa.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("EventoDao: Ocorreu um erro SQL ao inserir evento: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("EventoDao: Ocorreu um erro inesperado ao inserir evento: " + ex.getMessage());
        }
        return sucesso;
    }

    public boolean excluir(Evento evento) {
        boolean sucesso = false;
        String SQL = "DELETE FROM evento WHERE id_evento = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, evento.getId_evento());
            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Evento excluído com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao excluir evento.");
        }
        return sucesso;
    }

    public boolean editar(Evento evento) {
        boolean sucesso = false;
        String SQL = "UPDATE evento SET nome = ?, tipo = ?, data = ?, hora = ?, descricao = ?, id_local = ? " +
                "WHERE id_evento = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getTipo());
            ps.setDate(3, Date.valueOf(evento.getData()));
            ps.setTime(4, Time.valueOf(evento.getHora()));
            ps.setString(5, evento.getDescricao());
            ps.setInt(6, evento.getId_Local().getId_local());
            ps.setInt(7, evento.getId_evento());

            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Evento editado com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao editar evento.");
        }
        return sucesso;
    }

    public Evento buscarPorId(int id) {
        Evento evento = null;
        String SQL = "SELECT e.*, l.nome AS local_nome, l.endereco AS local_endereco, l.capacidade AS local_capacidade FROM evento e JOIN local l ON e.id_local = l.id_local WHERE e.id_evento = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Local local = new Local(
                            rs.getInt("id_local"),
                            rs.getString("local_nome"),
                            rs.getString("local_endereco"),
                            rs.getInt("local_capacidade")
                    );
                    // Correção: Remova 'Evento' aqui, pois 'evento' já foi declarado no escopo do método.
                    evento = new Evento(
                            rs.getInt("id_evento"),
                            rs.getString("nome"),
                            rs.getString("tipo"),
                            rs.getDate("data").toLocalDate(),
                            rs.getTime("hora").toLocalTime(),
                            rs.getString("descricao"),
                            local
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao buscar evento por ID: " + ex.getMessage());
        }
        return evento;
    }
}