package br.com.teste.model;

import java.time.LocalDate;

public class Inscricao {
    private int id_inscricao;
    private Evento evento;
    private Participante participante;
    private LocalDate dataInscricao;

    public Inscricao(int id_inscricao, Evento evento, Participante participante, LocalDate dataInscricao) {
        this.id_inscricao = id_inscricao;
        this.evento = evento;
        this.participante = participante;
        this.dataInscricao = dataInscricao;
    }

    public int getId_inscricao() {
        return id_inscricao;
    }
    public void setId_inscricao(int id_inscricao) {
        this.id_inscricao = id_inscricao;
    }

    public Evento getEvento() {
        return evento;
    }
    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Participante getParticipante() {
        return participante;
    }
    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public LocalDate getDataInscricao() {
        return dataInscricao;
    }
    public void setDataInscricao(LocalDate dataInscricao) {
        this.dataInscricao = dataInscricao;
    }
}