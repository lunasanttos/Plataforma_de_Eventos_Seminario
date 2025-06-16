package br.com.teste.model;

public class Local {
    private int id_local;
    private String nome;
    private String endereco;
    private int capacidade;

    public Local(int id_local, String nome, String endereco, int capacidade) {
        this.id_local = id_local;
        this.nome = nome;
        this.endereco = endereco;
        this.capacidade = capacidade;
    }

    public int getId_local() {
        return id_local;
    }

    public void setId_local(int id_local) {
        this.id_local = id_local;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getCapacidade() {
        return capacidade;
    }


    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    @Override
    public String toString() {
        return "Local [ID=" + id_local + ", Nome=" + nome + ", Endereco=" + endereco + ", Capacidade=" + capacidade + "]";
    }
}