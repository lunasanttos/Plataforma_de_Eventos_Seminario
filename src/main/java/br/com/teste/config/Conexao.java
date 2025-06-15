package br.com.teste.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static Conexao instance = null;
    private Connection conn;

    private Conexao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/plataforma_de_eventos_db", "root", "luna2550");
            System.out.println("Conexão criada com sucesso!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Falha na conexão");
            e.printStackTrace();
            throw new RuntimeException("Não foi possível estabelecer a conexão com o banco de dados.", e);
        }
    }

    public static Conexao getInstance() {
        if (instance == null) {
            synchronized (Conexao.class) {
                if (instance == null) {
                    instance = new Conexao();
                }
            }
        }
        return instance;
    }

    public Connection getConn() {
        return conn;
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexão fechada.");
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão.");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Conexao c = Conexao.getInstance();
        c.closeConnection();
    }
}