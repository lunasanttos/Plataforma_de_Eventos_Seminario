package br.com.teste.model;

import java.time.LocalDate;

public class Certificado {
    private int id_certificado;
    private Inscricao inscricao;
    private LocalDate data_emissao;
    private String codigo_verificacao;


    public Certificado(int id_certificado, Inscricao inscricao, LocalDate data_emissao, String codigo_verificacao) {
        this.id_certificado = id_certificado;
        this.inscricao = inscricao;
        this.data_emissao = data_emissao;
        this.codigo_verificacao = codigo_verificacao;
    }

    public int getId_certificado() {
        return id_certificado;
    }
    public void setId_certificado(int id_certificado) {
        this.id_certificado = id_certificado;
    }

    public Inscricao getInscricao() {
        return inscricao;
    }
    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }


    public LocalDate getData_emissao() {
        return data_emissao;
    }
    public void setData_emissao(LocalDate data_emissao) {
        this.data_emissao = data_emissao;
    }


    public String getCodigo_verificacao() {
        return codigo_verificacao;
    }

    public void setCodigo_verificacao(String codigo_verificacao) {
        this.codigo_verificacao = codigo_verificacao;
    }

    public int getId_Certificado() {
        return id_certificado;
    }

    public void setIdCertificado(int idCertificado) {
        this.id_certificado = idCertificado;
    }
}