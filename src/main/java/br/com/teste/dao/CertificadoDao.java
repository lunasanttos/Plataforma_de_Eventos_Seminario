package br.com.teste.dao;

import br.com.teste.model.Certificado; // Importa a classe de modelo Certificado
import br.com.teste.model.Inscricao; // Importa a classe de modelo Inscricao (será usada na relação com Certificado)
import br.com.teste.config.Conexao; // Importa a classe de configuração da conexão com o banco de dados

import java.sql.PreparedStatement; // Usado para executar comandos SQL pré-compilados e seguros
import java.sql.ResultSet;       // Usado para armazenar os resultados de uma consulta SQL
import java.sql.SQLException;    // Importa a exceção para erros de SQL
import java.sql.Date;            // Usado para converter datas Java para o formato SQL Date
import java.sql.Statement;       // Usado para executar comandos SQL (aqui, para obter chaves geradas)
import java.util.ArrayList;      // Usado para criar listas dinâmicas de objetos
import java.util.List;           // Interface List para coleções de objetos

/*
 * A classe CertificadoDao (Data Access Object) é responsável por todas as operações de
 * persistência de dados relacionadas aos certificados no banco de dados.
 * Ela atua como uma ponte entre a aplicação Java e a tabela 'certificado' no banco.
 */
public class CertificadoDao {

    private Conexao conexao; // Atributo para armazenar a instância da conexão com o banco de dados

    /*
     * Construtor da classe CertificadoDao.
     * Ao criar um objeto CertificadoDao, ele obtém a única instância da conexão
     * com o banco de dados através de Conexao.getInstance().
     */
    public CertificadoDao() {
        this.conexao = Conexao.getInstance();
    }

    /*
     * Método para listar todos os certificados presentes no banco de dados.
     * Retorna uma lista de objetos Certificado.
     */
    public List<Certificado> listar() {
        List<Certificado> certificados = new ArrayList<>(); // Cria uma lista vazia para armazenar os certificados
        String SQL = "SELECT * FROM certificado"; // Define a consulta SQL para selecionar todos os certificados

        // O 'try-with-resources' garante que PreparedStatement e ResultSet sejam fechados automaticamente
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL); // Prepara a declaração SQL
             ResultSet rs = ps.executeQuery()) { // Executa a consulta e obtém o resultado

            while (rs.next()) { // Itera sobre cada linha do resultado da consulta
                Certificado certificado = new Certificado(
                        rs.getInt("id_certificado"), // Obtém o ID do certificado da coluna 'id_certificado'
                        null, // OBSERVAÇÃO: O campo 'inscricao' no construtor do Certificado está sendo passado como 'null'.
                        // Se a intenção é carregar a Inscrição associada ao Certificado, este é um ponto
                        // onde a lógica precisaria ser expandida para buscar a Inscrição com base em 'id_inscricao'
                        // da tabela de certificado, ou então o relacionamento não está sendo totalmente aproveitado
                        // no carregamento. No método 'inserir', 'id_inscricao' é usado, mas não recuperado aqui.
                        rs.getDate("data_emissao").toLocalDate(), // Obtém a data de emissão e converte para LocalDate
                        rs.getString("codigo_verificacao") // Obtém o código de verificação
                );
                certificados.add(certificado); // Adiciona o certificado à lista
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha do erro para depuração
            System.out.println("Ocorreu um erro ao listar certificados: " + ex.getMessage()); // Mensagem de erro para o usuário
        }
        return certificados; // Retorna a lista de certificados
    }

    /*
     * Método para inserir um novo certificado no banco de dados.
     * Retorna true se a inserção for bem-sucedida, false caso contrário.
     */
    public boolean inserir(Certificado certificado) {
        boolean sucesso = false;
        String SQL = "INSERT INTO certificado(data_emissao, codigo_verificacao, id_inscricao) " +
                "VALUES (?, ?, ?)"; // SQL para inserir um certificado, usando placeholders (?) para segurança

        // O 'try-with-resources' garante que PreparedStatement seja fechado automaticamente.
        // Statement.RETURN_GENERATED_KEYS é usado para obter o ID gerado automaticamente pelo banco.
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(certificado.getData_emissao())); // Define o valor para o 1º placeholder (data_emissao)
            ps.setString(2, certificado.getCodigo_verificacao());       // Define o valor para o 2º placeholder (codigo_verificacao)
            ps.setInt(3, certificado.getInscricao().getId_inscricao()); // Define o valor para o 3º placeholder (id_inscricao da inscrição associada)

            int linhasAfetadas = ps.executeUpdate(); // Executa a inserção e retorna o número de linhas afetadas
            if (linhasAfetadas > 0) { // Se pelo menos uma linha foi afetada (inserida)
                // Tenta obter as chaves geradas automaticamente pelo banco de dados (o ID do novo certificado)
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) { // Se houver uma chave gerada
                        certificado.setId_certificado(rs.getInt(1)); // Define o ID gerado no objeto Certificado
                    }
                }
                sucesso = true; // A inserção foi um sucesso
                System.out.println("Certificado inserido com sucesso!"); // Mensagem de sucesso
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha do erro
            System.out.println("Ocorreu um erro ao inserir certificado: " + ex.getMessage()); // Mensagem de erro
        }
        return sucesso; // Retorna o status do sucesso
    }

    /*
     * Método para excluir um certificado do banco de dados com base em seu ID.
     * Retorna true se a exclusão for bem-sucedida, false caso contrário.
     */
    public boolean excluir(Certificado certificado) {
        boolean sucesso = false;
        String SQL = "DELETE FROM certificado WHERE id_certificado = ?"; // SQL para excluir um certificado pelo ID

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, certificado.getId_certificado()); // Define o ID do certificado a ser excluído

            int linhasAfetadas = ps.executeUpdate(); // Executa a exclusão
            sucesso = linhasAfetadas > 0; // Se mais de 0 linhas foram afetadas, a exclusão foi bem-sucedida
            if (sucesso) {
                System.out.println("Certificado excluído com sucesso!"); // Mensagem de sucesso
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha do erro
            System.out.println("Ocorreu um erro ao excluir certificado: " + ex.getMessage()); // Mensagem de erro
        }
        return sucesso; // Retorna o status do sucesso
    }

    /*
     * Método para editar um certificado existente no banco de dados.
     * Retorna true se a edição for bem-sucedida, false caso contrário.
     */
    public boolean editar(Certificado certificado) {
        boolean sucesso = false;
        String SQL = "UPDATE certificado SET " +
                "data_emissao=?, codigo_verificacao=?, id_inscricao=? " +
                "WHERE id_certificado=?"; // SQL para atualizar um certificado pelo ID

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setDate(1, Date.valueOf(certificado.getData_emissao())); // Define o novo valor para data_emissao
            ps.setString(2, certificado.getCodigo_verificacao());       // Define o novo valor para codigo_verificacao
            ps.setInt(3, certificado.getInscricao().getId_inscricao()); // Define o novo valor para id_inscricao
            ps.setInt(4, certificado.getId_certificado());               // Define o ID do certificado a ser editado (cláusula WHERE)

            int linhasAfetadas = ps.executeUpdate(); // Executa a atualização
            sucesso = linhasAfetadas > 0; // Se mais de 0 linhas foram afetadas, a edição foi bem-sucedida
            if (sucesso) {
                System.out.println("Certificado editado com sucesso!"); // Mensagem de sucesso
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha do erro
            System.out.println("Ocorreu um erro ao editar certificado: " + ex.getMessage()); // Mensagem de erro
        }
        return sucesso; // Retorna o status do sucesso
    }

    /*
     * Método para buscar um certificado no banco de dados pelo seu ID.
     * Retorna o objeto Certificado encontrado ou null se não for encontrado.
     */
    public Certificado buscarPorId(int id) {
        Certificado certificado = null; // Inicializa o certificado como null (nenhum encontrado ainda)
        String SQL = "SELECT * FROM certificado WHERE id_certificado = ?"; // SQL para buscar por ID

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, id); // Define o ID a ser buscado

            // Aninhado try-with-resources para o ResultSet
            try (ResultSet rs = ps.executeQuery()) { // Executa a consulta
                if (rs.next()) { // Se encontrou uma linha (um certificado com o ID)
                    certificado = new Certificado(
                            rs.getInt("id_certificado"), // Obtém o ID
                            null, // OBSERVAÇÃO: Assim como no método 'listar', a 'inscricao' é passada como 'null'.
                            // Se o Certificado precisa ter a Inscrição completa, a lógica de carregamento
                            // da Inscrição (usando seu próprio DAO, por exemplo) precisaria ser adicionada aqui.
                            rs.getDate("data_emissao").toLocalDate(), // Obtém e converte a data
                            rs.getString("codigo_verificacao") // Obtém o código
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha do erro
            System.out.println("Erro ao buscar certificado por ID: " + ex.getMessage()); // Mensagem de erro
        }
        return certificado; // Retorna o certificado encontrado ou null
    }
}