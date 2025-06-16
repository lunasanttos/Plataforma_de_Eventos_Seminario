package br.com.teste.dao;

import br.com.teste.config.Conexao;      // Importa a classe de configuração da conexão com o banco de dados.
import br.com.teste.model.Evento;         // Importa a classe de modelo Evento, que representa um evento.
import br.com.teste.model.Inscricao;      // Importa a classe de modelo Inscricao, que representa uma inscrição.
import br.com.teste.model.Local;          // Importa a classe de modelo Local, que representa o local de um evento.
import br.com.teste.model.Participante;   // Importa a classe de modelo Participante, que representa um participante.

import java.sql.Date;                   // Usado para mapear datas Java para o tipo SQL DATE.
import java.sql.PreparedStatement;      // Usado para executar comandos SQL pré-compilados e seguros.
import java.sql.ResultSet;              // Usado para armazenar os resultados de consultas SQL.
import java.sql.SQLException;           // Importa a exceção para erros relacionados a SQL.
import java.sql.Statement;              // Usado para obter chaves geradas automaticamente pelo banco.
import java.time.LocalTime;             // Importa LocalTime para lidar com horas. (OBSERVAÇÃO: Este import 'LocalTime' pode ser desnecessário nesta classe, pois parece ser usado apenas em 'Evento', que já é tratada por um método auxiliar. É um código 'morto' aqui.)
import java.util.ArrayList;             // Usado para criar listas dinâmicas de objetos.
import java.util.List;                  // Interface List para coleções de objetos.

/*
 * A classe InscricaoDao (Data Access Object) é responsável por todas as operações de
 * persistência de dados (CRUD - Create, Read, Update, Delete) para a entidade Inscricao
 * no banco de dados. Ela gerencia a comunicação entre o aplicativo e a tabela 'inscricao'.
 * Além disso, lida com a recuperação de objetos Evento e Participante associados a uma inscrição.
 */
public class InscricaoDao {

    private Conexao conexao; // Atributo para armazenar a instância da conexão com o banco de dados.

    /*
     * Construtor da classe InscricaoDao.
     * Ao criar um objeto InscricaoDao, ele obtém a única instância da conexão
     * com o banco de dados através de Conexao.getInstance() (padrão Singleton).
     */
    public InscricaoDao(){
        this.conexao = Conexao.getInstance();
    }

    /*
     * Método auxiliar privado para criar um objeto Evento a partir de um ResultSet.
     * Este método é utilizado para popular o objeto Evento aninhado dentro de uma Inscrição,
     * garantindo que os dados do evento sejam corretamente extraídos das colunas do JOIN.
     * @param rs O ResultSet contendo as colunas do evento (com aliases para evitar conflitos).
     * @return Um objeto Evento populado.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private Evento criarEventoDoResultSet(ResultSet rs) throws SQLException {
        // Cria um objeto Local, que é parte do Evento. As colunas são extraídas do ResultSet
        // usando os aliases definidos na query SQL principal (ex: 'local_nome_evento').
        Local localDoEvento = new Local(
                rs.getInt("id_local_evento"),
                rs.getString("local_nome_evento"),
                rs.getString("local_endereco_evento"),
                rs.getInt("local_capacidade_evento")
        );

        // Cria e retorna o objeto Evento, usando as colunas do ResultSet e o objeto Local criado acima.
        return new Evento(
                rs.getInt("id_evento_inscricao"),
                rs.getString("nome_evento_inscricao"),
                rs.getString("tipo_evento_inscricao"),
                rs.getDate("data_evento_inscricao").toLocalDate(),      // Converte java.sql.Date para java.time.LocalDate.
                rs.getTime("hora_evento_inscricao").toLocalTime(),      // Converte java.sql.Time para java.time.LocalTime.
                rs.getString("descricao_evento_inscricao"),
                localDoEvento                                           // Associa o objeto Local ao Evento.
        );
    }

    /*
     * Método auxiliar privado para criar um objeto Participante a partir de um ResultSet.
     * Utilizado para popular o objeto Participante aninhado dentro de uma Inscrição,
     * extraindo os dados do participante das colunas do JOIN.
     * @param rs O ResultSet contendo as colunas do participante (com aliases).
     * @return Um objeto Participante populado.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private Participante criarParticipanteDoResultSet(ResultSet rs) throws SQLException {
        // Cria e retorna o objeto Participante, usando as colunas do ResultSet e seus aliases.
        return new Participante(
                rs.getInt("id_participante_inscricao"),
                rs.getString("nome_participante_inscricao"),
                rs.getString("email_participante_inscricao"),
                rs.getString("cpf_participante_inscricao")
        );
    }

    /*
     * Método para listar todas as inscrições presentes no banco de dados.
     * Inclui JOINs com as tabelas 'evento', 'local' e 'participante' para carregar
     * todos os dados relacionados em uma única consulta.
     * @return Uma lista de objetos Inscricao completos.
     */
    public List<Inscricao> listar(){
        List<Inscricao> inscricoes = new ArrayList<>(); // Cria uma lista vazia para armazenar as inscrições.
        // Query SQL COMPLETA: Seleciona todos os campos da inscrição (i), evento (e), local (l) e participante (p).
        // ALIASES são usados para evitar conflitos de nomes de colunas (ex: nome do evento vs. nome do participante).
        String SQL = "SELECT i.id_inscricao, i.data_inscricao, i.ativa, " +
                "e.id_evento AS id_evento_inscricao, e.nome AS nome_evento_inscricao, e.tipo AS tipo_evento_inscricao, " +
                "e.data AS data_evento_inscricao, e.hora AS hora_evento_inscricao, e.descricao AS descricao_evento_inscricao, " +
                "l.id_local AS id_local_evento, l.nome AS local_nome_evento, l.endereco AS local_endereco_evento, l.capacidade AS local_capacidade_evento, " +
                "p.id_participante AS id_participante_inscricao, p.nome AS nome_participante_inscricao, " +
                "p.email AS email_participante_inscricao, p.cpf AS cpf_participante_inscricao " +
                "FROM inscricao i " +
                "JOIN evento e ON i.id_evento = e.id_evento " +       // Junta 'inscricao' com 'evento' pelo id_evento.
                "JOIN local l ON e.id_local = l.id_local " +         // Junta 'evento' com 'local' pelo id_local.
                "JOIN participante p ON i.id_participante = p.id_participante"; // Junta 'inscricao' com 'participante' pelo id_participante.

        // O 'try-with-resources' garante que PreparedStatement e ResultSet sejam fechados automaticamente.
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { // Itera sobre cada linha do resultado da consulta.
                // Cria um novo objeto Inscricao, usando os métodos auxiliares para popular Evento e Participante.
                Inscricao inscricao = new Inscricao(
                        rs.getInt("id_inscricao"),
                        criarEventoDoResultSet(rs),      // Chama o método auxiliar para criar o Evento.
                        criarParticipanteDoResultSet(rs),// Chama o método auxiliar para criar o Participante.
                        rs.getDate("data_inscricao").toLocalDate(), // Converte a data de inscrição para LocalDate.
                        rs.getBoolean("ativa")           // Obtém o status de ativação da inscrição.
                );
                inscricoes.add(inscricao); // Adiciona a inscrição à lista.
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha do erro para depuração.
            System.out.println("Ocorreu um erro ao listar inscrições no DAO: " + ex.getMessage()); // Mensagem de erro para o console.
        }
        return inscricoes; // Retorna a lista de inscrições.
    }

    /*
     * Método para inserir uma nova inscrição no banco de dados.
     * @param inscricao O objeto Inscricao a ser inserido.
     * @return true se a inserção for bem-sucedida, false caso contrário.
     */
    public boolean inserir(Inscricao inscricao){
        boolean sucesso = false;
        // SQL para inserir uma inscrição. Os '?' são placeholders para os valores.
        String SQL = "INSERT INTO inscricao(id_evento, id_participante, data_inscricao, ativa) VALUES (?, ?, ?, ?)";

        // 'try-with-resources' para PreparedStatement. Statement.RETURN_GENERATED_KEYS é usado para
        // obter o ID gerado automaticamente pelo banco de dados (auto-incremento) para a nova inscrição.
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, inscricao.getEvento().getId_evento());         // Define o ID do Evento associado.
            ps.setInt(2, inscricao.getParticipante().getId_participante()); // Define o ID do Participante associado.
            ps.setDate(3, Date.valueOf(inscricao.getDataInscricao()));  // Define a data da inscrição.
            ps.setBoolean(4, inscricao.isAtiva());                      // Define o status de ativação.

            int linhasAfetadas = ps.executeUpdate(); // Executa a inserção e retorna o número de linhas afetadas.
            if (linhasAfetadas > 0) { // Se pelo menos uma linha foi afetada (inserida).
                // Tenta obter o ID gerado para a nova inscrição.
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        inscricao.setId_inscricao(rs.getInt(1)); // Define o ID gerado no objeto Inscricao.
                    }
                }
                sucesso = true; // A inserção foi um sucesso.
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI,
                // não no DAO, que deve focar apenas na persistência.
                System.out.println("Inscrição inserida com sucesso no banco de dados!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha do erro.
            System.out.println("Ocorreu um erro ao inserir inscrição no DAO: " + ex.getMessage()); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    /*
     * Método para excluir uma inscrição do banco de dados com base em seu ID.
     * @param inscricao O objeto Inscricao a ser excluído (apenas o ID é necessário).
     * @return true se a exclusão for bem-sucedida, false caso contrário.
     */
    public boolean excluir(Inscricao inscricao){
        boolean sucesso = false;
        String SQL = "DELETE FROM inscricao WHERE id_inscricao = ?"; // SQL para excluir uma inscrição pelo ID.

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, inscricao.getId_inscricao()); // Define o ID da inscrição a ser excluída.

            int linhasAfetadas = ps.executeUpdate(); // Executa a exclusão.
            sucesso = linhasAfetadas > 0; // Se mais de 0 linhas foram afetadas, a exclusão foi bem-sucedida.
            if (sucesso) {
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI.
                System.out.println("Inscrição excluída com sucesso do banco de dados!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha do erro.
            System.out.println("Ocorreu um erro ao excluir inscrição no DAO: " + ex.getMessage()); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    /*
     * Método para editar (atualizar) uma inscrição existente no banco de dados.
     * @param inscricao O objeto Inscricao com os dados atualizados (o ID deve existir no banco).
     * @return true se a edição for bem-sucedida, false caso contrário.
     */
    public boolean editar(Inscricao inscricao){
        boolean sucesso = false;
        // SQL para atualizar uma inscrição com base no seu ID.
        String SQL = "UPDATE inscricao SET " +
                "id_evento = ?, id_participante = ?, data_inscricao = ?, ativa = ? " +
                "WHERE id_inscricao = ?";

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, inscricao.getEvento().getId_evento());          // Define o novo ID do Evento.
            ps.setInt(2, inscricao.getParticipante().getId_participante()); // Define o novo ID do Participante.
            ps.setDate(3, Date.valueOf(inscricao.getDataInscricao()));   // Define a nova data da inscrição.
            ps.setBoolean(4, inscricao.isAtiva());                       // Define o novo status de ativação.
            ps.setInt(5, inscricao.getId_inscricao());                   // O ID da inscrição a ser atualizada (na cláusula WHERE).

            int linhasAfetadas = ps.executeUpdate(); // Executa a atualização.
            sucesso = linhasAfetadas > 0; // Se mais de 0 linhas foram afetadas, a edição foi bem-sucedida.
            if (sucesso) {
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI.
                System.out.println("Inscrição editada com sucesso no banco de dados!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Ocorreu um erro ao editar inscrição no DAO: " + ex.getMessage()); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    /*
     * Método para buscar uma inscrição no banco de dados pelo seu ID.
     * Carrega todos os dados da inscrição, evento e participante associados.
     * @param id O ID da inscrição a ser buscada.
     * @return O objeto Inscricao encontrado, ou null se não for encontrado.
     */
    public Inscricao buscarPorId(int id) {
        Inscricao inscricao = null; // Inicializa a inscrição como null.
        // Query COMPLETA com JOINs e ALIASES para todas as colunas necessárias, buscando por ID da inscrição.
        String SQL = "SELECT i.id_inscricao, i.data_inscricao, i.ativa, " +
                "e.id_evento AS id_evento_inscricao, e.nome AS nome_evento_inscricao, e.tipo AS tipo_evento_inscricao, " +
                "e.data AS data_evento_inscricao, e.hora AS hora_evento_inscricao, e.descricao AS descricao_evento_inscricao, " +
                "l.id_local AS id_local_evento, l.nome AS local_nome_evento, l.endereco AS local_endereco_evento, l.capacidade AS local_capacidade_evento, " +
                "p.id_participante AS id_participante_inscricao, p.nome AS nome_participante_inscricao, " +
                "p.email AS email_participante_inscricao, p.cpf AS cpf_participante_inscricao " +
                "FROM inscricao i " +
                "JOIN evento e ON i.id_evento = e.id_evento " +
                "JOIN local l ON e.id_local = l.id_local " +
                "JOIN participante p ON i.id_participante = p.id_participante " +
                "WHERE i.id_inscricao = ?"; // Placeholder para o ID da inscrição.

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, id); // Define o ID da inscrição a ser buscada.

            // Aninhado try-with-resources para o ResultSet.
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Se encontrou uma linha.
                    inscricao = new Inscricao(
                            rs.getInt("id_inscricao"),
                            criarEventoDoResultSet(rs),
                            criarParticipanteDoResultSet(rs),
                            rs.getDate("data_inscricao").toLocalDate(),
                            rs.getBoolean("ativa")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Erro ao buscar inscrição por ID no DAO: " + ex.getMessage()); // Mensagem de erro.
        }
        return inscricao; // Retorna a inscrição encontrada ou null.
    }

    /*
     * Método para listar inscrições de um participante específico.
     * Utilizado, por exemplo, no MenuParticipante para exibir as inscrições do usuário logado.
     * @param idParticipante O ID do participante cujas inscrições se deseja listar.
     * @return Uma lista de objetos Inscricao pertencentes ao participante especificado.
     */
    public List<Inscricao> listarPorParticipante(int idParticipante) {
        List<Inscricao> inscricoes = new ArrayList<>();
        // Query COMPLETA com JOINs e ALIASES, filtrando por ID do participante.
        String SQL = "SELECT i.id_inscricao, i.data_inscricao, i.ativa, " +
                "e.id_evento AS id_evento_inscricao, e.nome AS nome_evento_inscricao, e.tipo AS tipo_evento_inscricao, " +
                "e.data AS data_evento_inscricao, e.hora AS hora_evento_inscricao, e.descricao AS descricao_evento_inscricao, " +
                "l.id_local AS id_local_evento, l.nome AS local_nome_evento, l.endereco AS local_endereco_evento, l.capacidade AS local_capacidade_evento, " +
                "p.id_participante AS id_participante_inscricao, p.nome AS nome_participante_inscricao, " +
                "p.email AS email_participante_inscricao, p.cpf AS cpf_participante_inscricao " +
                "FROM inscricao i " +
                "JOIN evento e ON i.id_evento = e.id_evento " +
                "JOIN local l ON e.id_local = l.id_local " +
                "JOIN participante p ON i.id_participante = p.id_participante " +
                "WHERE i.id_participante = ?"; // Placeholder para o ID do participante.

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, idParticipante); // Define o ID do participante.
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inscricao inscricao = new Inscricao(
                            rs.getInt("id_inscricao"),
                            criarEventoDoResultSet(rs),
                            criarParticipanteDoResultSet(rs),
                            rs.getDate("data_inscricao").toLocalDate(),
                            rs.getBoolean("ativa")
                    );
                    inscricoes.add(inscricao);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao listar inscrições por participante no DAO: " + ex.getMessage());
        }
        return inscricoes;
    }

    /*
     * Método para listar inscrições relacionadas a um evento específico.
     * Pode ser útil para verificar dependências antes de excluir um evento,
     * ou para listar todos os participantes de um evento.
     * @param idEvento O ID do evento cujas inscrições se deseja listar.
     * @return Uma lista de objetos Inscricao relacionadas ao evento especificado.
     */
    public List<Inscricao> listarPorEvento(int idEvento) {
        List<Inscricao> inscricoes = new ArrayList<>();
        // Query COMPLETA com JOINs e ALIASES, filtrando por ID do evento.
        String SQL = "SELECT i.id_inscricao, i.data_inscricao, i.ativa, " +
                "e.id_evento AS id_evento_inscricao, e.nome AS nome_evento_inscricao, e.tipo AS tipo_evento_inscricao, " +
                "e.data AS data_evento_inscricao, e.hora AS hora_evento_inscricao, e.descricao AS descricao_evento_inscricao, " +
                "l.id_local AS id_local_evento, l.nome AS local_nome_evento, l.endereco AS local_endereco_evento, l.capacidade AS local_capacidade_evento, " +
                "p.id_participante AS id_participante_inscricao, p.nome AS nome_participante_inscricao, " +
                "p.email AS email_participante_inscricao, p.cpf AS cpf_participante_inscricao " +
                "FROM inscricao i " +
                "JOIN evento e ON i.id_evento = e.id_evento " +
                "JOIN local l ON e.id_local = l.id_local " +
                "JOIN participante p ON i.id_participante = p.id_participante " +
                "WHERE i.id_evento = ?"; // Placeholder para o ID do evento.

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, idEvento); // Define o ID do evento.
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inscricao inscricao = new Inscricao(
                            rs.getInt("id_inscricao"),
                            criarEventoDoResultSet(rs),
                            criarParticipanteDoResultSet(rs),
                            rs.getDate("data_inscricao").toLocalDate(),
                            rs.getBoolean("ativa")
                    );
                    inscricoes.add(inscricao);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao listar inscrições por evento no DAO: " + ex.getMessage());
        }
        return inscricoes;
    }

    // metodo para conseguir verificar o evento com mais publico
    public int contarInscricoesPorEvento(int idEvento) {
        int total = 0;
        String SQL = "SELECT COUNT(*) AS total FROM inscricao WHERE id_evento = ?";
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, idEvento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt("total");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Erro ao contar inscrições por evento no DAO: " + ex.getMessage());
        }
        return total;
    }

}