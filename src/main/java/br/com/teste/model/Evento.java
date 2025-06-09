package br.com.teste.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evento {

    private int id_evento;
    private String nome;
    private String tipo;
    private LocalDate data;
    private LocalTime hora;
    private String descricao;
    private Local local;

    public Evento(int id_evento, String nome, String tipo, LocalDate data, LocalTime hora, String descricao, Local local) {
        this.id_evento = id_evento;
        this.nome = nome;
        this.tipo = tipo;
        this.data = data;
        this.hora = hora;
        this.descricao = descricao;
        this.local = local;
    }

    public int getId_evento() {
        return id_evento;
    }
    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Local getId_Local() {
        return local;
    }
    public void setId_Local(Local id_Local) {
        this.local = id_Local;
    }
}