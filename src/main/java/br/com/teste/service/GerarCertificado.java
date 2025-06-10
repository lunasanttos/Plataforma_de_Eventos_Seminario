package br.com.teste.service;

import br.com.teste.model.Certificado;
import br.com.teste.model.Inscricao;

import java.time.LocalDate;
import java.util.UUID;

public class GerarCertificado {

    private InscricaoService inscricaoService;
    private CertificadoService certificadoService;

    public GerarCertificado() {
        this.inscricaoService = new InscricaoService();
        this.certificadoService = new CertificadoService();
    }

    public boolean gerarEInserirCertificado(int idInscricao) {
        Inscricao inscricao = inscricaoService.buscarInscricaoPorId(idInscricao);

        if (inscricao == null) {
            System.out.println("Erro: Inscrição com ID " + idInscricao + " não encontrada para gerar certificado.");
            return false;
        }

        if (inscricao.getEvento() == null || inscricao.getParticipante() == null) {
            System.out.println("Erro: Dados de Evento ou Participante incompletos para a inscrição ID " + idInscricao + ".");
            System.out.println("Verifique se o Evento e o Participante associados a esta inscrição existem e foram carregados corretamente.");
            return false;
        }

        LocalDate dataEmissao = LocalDate.now();
        String codigoVerificacao = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Certificado novoCertificado = new Certificado(
                0,
                inscricao,
                dataEmissao,
                codigoVerificacao
        );

        boolean sucesso = certificadoService.inserir(novoCertificado);

        if (sucesso) {
            System.out.println("\nCertificado gerado e inserido com sucesso!");

            System.out.println(novoCertificado.toString());

        } else {
            System.out.println("Falha ao gerar e inserir certificado para a inscrição ID: " + idInscricao);
        }

        return sucesso;
    }
}