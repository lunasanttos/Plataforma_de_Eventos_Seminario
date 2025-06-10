package br.com.teste.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List; // Importar se a lista de responsáveis for usada

public class Evento {

    private int id_evento;
    private String nome;
    private String tipo;
    private LocalDate data;
    private LocalTime hora;
    private String descricao;
    private Local local; // Atributo chamado 'local'

    // Opcional: private List<Responsavel> responsavelLista; // Manter se for usar

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

    // RENOMEADO: Agora retorna o objeto Local e tem um nome intuitivo
    public Local getLocal() {
        return local;
    }
    // RENOMEADO: Setter também renomeado para consistência
    public void setLocal(Local local) {
        this.local = local;
    }

    // Se estiver usando a lista de responsáveis, mantenha estes métodos
    /*
    public List<Responsavel> getResponsavelLista(){
        return responsavelLista;
    }
    public void setResponsavelLista(List<Responsavel> responsavelLista) {
        this.responsavelLista = responsavelLista;
    }
    */

    @Override
    public String toString() {
        return "Evento [ID=" + id_evento + ", Nome=" + nome + ", Tipo=" + tipo +
                ", Data=" + data + ", Hora=" + hora + ", Descricao=" + descricao +
                ", Local=" + (local != null ? local.getNome() : "N/A") + "]";
    }
}