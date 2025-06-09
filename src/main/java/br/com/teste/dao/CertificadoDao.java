package br.com.teste.dao;
import br.com.teste.model.Certificado;
import br.com.teste.config.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;


public class CertificadoDao {

    private Conexao conexao;
    private PreparedStatement ps;



    public ResultSet listar(){
        try {
            return conexao.getConn()
                    .createStatement().executeQuery("SELECT * FROM certificado");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void inserir(Certificado certificado){
        try {
            String SQL = "INSERT INTO certificado( id_certificado,  data_emissao,  codigo_verificacao , id_inscricao) " +
                    "VALUES (?, ?, ? , ?)";

            ps = conexao.getConn().prepareStatement(SQL);

            ps.setInt(1, certificado.getId_certificado());
            ps.setDate(2, Date.valueOf(certificado.getData_emissao()));
            ps.setString(3, certificado.getCodigo_verificacao());
            ps.setInt(4, certificado.getInscricao().getId_inscricao());

            ps.executeUpdate();

            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Ocorreu um erro ao inserir certificado");
        }
    }

    public void excluir(Certificado certificado){
        try {
            String SQL = "DELETE FROM certificado WHERE id_certificado = ?";

            ps = conexao.getConn().prepareStatement(SQL);

            ps.setInt(1, certificado.getId_certificado());

            ps.executeUpdate();

            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void editar(Certificado certificado){
        try {
            String SQL = "UPDATE certificado SET " +
                    " data_emissao= ?, codigo_verificacao=? , id_inscricao = ? " +
                    "WHERE id_certificado=?";

            ps = conexao.getConn().prepareStatement(SQL);

            ps.setDate(1, Date.valueOf(certificado.getData_emissao()));
            ps.setString(2, certificado.getCodigo_verificacao());
            ps.setInt(3, certificado.getInscricao().getId_inscricao());
            ps.setInt(4, certificado.getId_certificado());


            ps.executeUpdate();

            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



}