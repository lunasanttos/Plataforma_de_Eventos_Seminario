package br.com.teste.model;

public class Responsavel {
    private int id_responsavel;
    private String nome;
    private String email;


    public Responsavel(int id_responsavel, String nome, String email) {
        this.id_responsavel = id_responsavel;
        this.nome = nome;
        this.email = email;
    }

    public Responsavel() {
    }

    public int getId_responsavel() {
        return id_responsavel;
    }

    public void setId_responsavel(int id_responsavel) {
        this.id_responsavel = id_responsavel;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}