package br.com.teste.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private Connection conn;

    public Conexao() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/plataforma_de_eventos", "root", "luna2550");

            System.out.println("Conexão criada com sucesso!");
        }
        catch ( ClassNotFoundException | SQLException e ){
            System.out.println("Falha na conexão");
            e.printStackTrace();
        }
    }

    public Connection getConn(){
        return conn;
    }

    public static void main(String[] args) {

        Conexao c = new Conexao();

    }
}