package br.com.teste.dao;

import br.com.teste.model.Certificado;
import br.com.teste.model.Inscricao;
import br.com.teste.config.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CertificadoDao {

    private Conexao conexao;

    public CertificadoDao() {
        this.conexao = Conexao.getInstance();
    }

    public List<Certificado> listar() {
        List<Certificado> certificados = new ArrayList<>();
        String SQL = "SELECT * FROM certificado";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Certificado certificado = new Certificado(
                        rs.getInt("id_certificado"),
                        null,
                        rs.getDate("data_emissao").toLocalDate(),
                        rs.getString("codigo_verificacao")
                );
                certificados.add(certificado);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao listar certificados: " + ex.getMessage());
        }
        return certificados;
    }

    public boolean inserir(Certificado certificado) {
        boolean sucesso = false;
        String SQL = "INSERT INTO certificado(data_emissao, codigo_verificacao, id_inscricao) " +
                "VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(certificado.getData_emissao()));
            ps.setString(2, certificado.getCodigo_verificacao());
            ps.setInt(3, certificado.getInscricao().getId_inscricao());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        certificado.setId_certificado(rs.getInt(1));
                    }
                }
                sucesso = true;
                System.out.println("Certificado inserido com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao inserir certificado: " + ex.getMessage());
        }
        return sucesso;
    }

    public boolean excluir(Certificado certificado) {
        boolean sucesso = false;
        String SQL = "DELETE FROM certificado WHERE id_certificado = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, certificado.getId_certificado());

            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Certificado excluído com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao excluir certificado: " + ex.getMessage());
        }
        return sucesso;
    }

    public boolean editar(Certificado certificado) {
        boolean sucesso = false;
        String SQL = "UPDATE certificado SET " +
                "data_emissao=?, codigo_verificacao=?, id_inscricao=? " +
                "WHERE id_certificado=?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setDate(1, Date.valueOf(certificado.getData_emissao()));
            ps.setString(2, certificado.getCodigo_verificacao());
            ps.setInt(3, certificado.getInscricao().getId_inscricao());
            ps.setInt(4, certificado.getId_certificado());

            int linhasAfetadas = ps.executeUpdate();
            sucesso = linhasAfetadas > 0;
            if (sucesso) {
                System.out.println("Certificado editado com sucesso!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao editar certificado: " + ex.getMessage());
        }
        return sucesso;
    }

    public Certificado buscarPorId(int id) {
        Certificado certificado = null;
        String SQL = "SELECT * FROM certificado WHERE id_certificado = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    certificado = new Certificado(
                            rs.getInt("id_certificado"),
                            null,
                            rs.getDate("data_emissao").toLocalDate(),
                            rs.getString("codigo_verificacao")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao buscar certificado por ID: " + ex.getMessage());
        }
        return certificado;
    }
}