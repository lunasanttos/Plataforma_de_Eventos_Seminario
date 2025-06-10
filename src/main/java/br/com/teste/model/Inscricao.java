package br.com.teste.model;

import java.time.LocalDate;

public class Inscricao {
    private int id_inscricao;
    private Evento evento;
    private Participante participante;
    private LocalDate dataInscricao;
    private boolean ativa; // Campo para indicar se a inscrição está ativa

    public Inscricao(int id_inscricao, Evento evento, Participante participante, LocalDate dataInscricao, boolean ativa) {
        this.id_inscricao = id_inscricao;
        this.evento = evento;
        this.participante = participante;
        this.dataInscricao = dataInscricao;
        this.ativa = ativa;
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

    public boolean isAtiva() { // Getter para 'ativa'
        return ativa;
    }
    public void setAtiva(boolean ativa) { // Setter para 'ativa'
        this.ativa = ativa;
    }
}