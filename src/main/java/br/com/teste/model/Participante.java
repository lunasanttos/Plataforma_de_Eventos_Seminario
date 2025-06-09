package br.com.teste.model;

public class Participante {
    private int id_participante;
    private String nome;
    private String email;
    private String cpf;

    public Participante(int id_participante, String nome, String email, String cpf) {
        this.id_participante = id_participante;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
    }

    public Participante() {
    }

    public int getId_participante() {
        return id_participante;
    }
    public void setId_participante(int id_participante) {
        this.id_participante = id_participante;
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

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}