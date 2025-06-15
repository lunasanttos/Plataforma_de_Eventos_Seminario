package br.com.teste.dao;

import br.com.teste.config.Conexao;      // Importa a classe de configuração da conexão com o banco de dados (padrão Singleton).
import br.com.teste.model.Responsavel;   // Importa a classe de modelo Responsavel, que representa um responsável.

import java.sql.Connection;        // Importa a interface Connection, para representar a conexão SQL.
import java.sql.PreparedStatement; // Usado para executar comandos SQL pré-compilados e seguros (evita SQL Injection).
import java.sql.ResultSet;       // Usado para armazenar os resultados de consultas SQL.
import java.sql.SQLException;    // Importa a exceção para erros relacionados a SQL.
import java.sql.Statement;       // Usado para obter chaves geradas automaticamente pelo banco (auto-incremento).
import java.util.ArrayList;      // Usado para criar listas dinâmicas de objetos.
import java.util.List;           // Interface List para coleções de objetos.

/*
 * A classe ResponsavelDao (Data Access Object) é responsável por todas as operações de
 * persistência de dados (CRUD - Create, Read, Update, Delete) para a entidade Responsavel
 * no banco de dados. Ela serve como uma camada de abstração entre a aplicação Java
 * e a tabela 'responsavel' no seu banco de dados.
 */
public class ResponsavelDao {

    // Atributo para armazenar a conexão SQL real.
    // O tipo 'Connection' está correto aqui, pois este DAO usará diretamente os métodos de 'Connection'.
    private Connection conexao;

    /*
     * Construtor da classe ResponsavelDao.
     * Ao criar um objeto ResponsavelDao, ele obtém a conexão SQL ativa do banco de dados
     * através da instância única da classe Conexao (padrão Singleton) e de seu método getConn().
     */
    public ResponsavelDao() {
        // CORREÇÃO: Pega a conexão SQL real do objeto Conexao.
        // É importante que o DAO trabalhe com a 'Connection' diretamente para executar as queries.
        this.conexao = Conexao.getInstance().getConn();
    }

    /*
     * Método para listar todos os responsáveis presentes no banco de dados.
     * @return Uma lista de objetos Responsavel.
     */
    public List<Responsavel> listar() {
        List<Responsavel> responsaveis = new ArrayList<>(); // Cria uma lista vazia para armazenar os responsáveis.
        String SQL = "SELECT * FROM responsavel";           // Define a consulta SQL para selecionar todos os responsáveis.

        // O 'try-with-resources' garante que PreparedStatement e ResultSet sejam fechados automaticamente.
        try (PreparedStatement ps = conexao.prepareStatement(SQL); // Prepara a declaração SQL usando a conexão atual.
             ResultSet rs = ps.executeQuery()) { // Executa a consulta e armazena os resultados no ResultSet.

            while (rs.next()) { // Itera sobre cada linha (registro) retornada pelo ResultSet.
                Responsavel responsavel = new Responsavel(); // Cria um novo objeto Responsavel.
                responsavel.setId_responsavel(rs.getInt("id_responsavel")); // Popula o ID do responsável.
                responsavel.setNome(rs.getString("nome"));                   // Popula o nome do responsável.
                responsavel.setEmail(rs.getString("email"));                 // Popula o email do responsável.
                responsaveis.add(responsavel); // Adiciona o objeto Responsavel à lista.
            }
        } catch (SQLException ex) { // Captura exceções SQL que podem ocorrer durante a interação com o banco.
            ex.printStackTrace(); // Imprime o rastreamento da pilha do erro para depuração.
            System.out.println("Ocorreu um erro ao listar responsáveis: " + ex.getMessage()); // Mensagem de erro amigável.
        }
        return responsaveis; // Retorna a lista de responsáveis encontrada.
    }

    /*
     * Método para inserir um novo responsável no banco de dados.
     * @param responsavel O objeto Responsavel a ser inserido.
     * @return true se a inserção for bem-sucedida, false caso contrário.
     */
    public boolean inserir(Responsavel responsavel) {
        boolean sucesso = false; // Flag para indicar o sucesso da operação.
        String SQL = "INSERT INTO responsavel(nome, email) VALUES (?, ?)"; // SQL para inserir um novo responsável.

        // 'try-with-resources' para PreparedStatement.
        // Statement.RETURN_GENERATED_KEYS é usado para instruir o banco de dados a retornar
        // as chaves geradas automaticamente (como o ID auto-incrementado) após a inserção.
        try (PreparedStatement ps = conexao.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, responsavel.getNome());  // Define o valor para o 1º placeholder (nome).
            ps.setString(2, responsavel.getEmail()); // Define o valor para o 2º placeholder (email).

            int linhasAfetadas = ps.executeUpdate(); // Executa a inserção e retorna o número de linhas afetadas.
            if (linhasAfetadas > 0) { // Se pelo menos uma linha foi afetada (sucesso na inserção).
                // Tenta obter o ID que foi gerado automaticamente pelo banco de dados.
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) { // Se há um resultado (o ID gerado).
                        responsavel.setId_responsavel(rs.getInt(1)); // Atribui o ID gerado ao objeto Responsavel.
                    }
                }
                sucesso = true; // Marca a operação como bem-sucedida.
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI,
                // não no DAO, que deve focar apenas na persistência dos dados. É um código 'morto' aqui.
                System.out.println("Responsável inserido com sucesso!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) { // Captura exceções SQL.
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Ocorreu um erro ao inserir responsável: " + ex.getMessage()); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    /*
     * Método para excluir um responsável do banco de dados com base em seu ID.
     * @param responsavel O objeto Responsavel a ser excluído (apenas o ID é necessário).
     * @return true se a exclusão for bem-sucedida, false caso contrário.
     */
    public boolean excluir(Responsavel responsavel) {
        boolean sucesso = false; // Flag de sucesso.
        String SQL = "DELETE FROM responsavel WHERE id_responsavel = ?"; // SQL para excluir um responsável pelo ID.

        try (PreparedStatement ps = conexao.prepareStatement(SQL)) {
            ps.setInt(1, responsavel.getId_responsavel()); // Define o ID do responsável a ser excluído.

            int linhasAfetadas = ps.executeUpdate(); // Executa a exclusão.
            sucesso = linhasAfetadas > 0; // Se mais de 0 linhas foram afetadas, a exclusão foi bem-sucedida.
            if (sucesso) {
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI.
                System.out.println("Responsável excluído com sucesso!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) { // Captura exceções SQL.
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Ocorreu um erro ao excluir responsável."); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    /*
     * Método para editar (atualizar) os dados de um responsável existente no banco de dados.
     * @param responsavel O objeto Responsavel com os dados atualizados (o ID deve existir no banco).
     * @return true se a edição for bem-sucedida, false caso contrário.
     */
    public boolean editar(Responsavel responsavel) {
        boolean sucesso = false; // Flag de sucesso.
        // SQL para atualizar os dados de um responsável.
        String SQL = "UPDATE responsavel SET nome = ?, email = ? WHERE id_responsavel = ?";

        try (PreparedStatement ps = conexao.prepareStatement(SQL)) {
            ps.setString(1, responsavel.getNome());  // Define o novo nome.
            ps.setString(2, responsavel.getEmail()); // Define o novo email.
            ps.setInt(3, responsavel.getId_responsavel()); // Define o ID do responsável a ser editado (na cláusula WHERE).

            int linhasAfetadas = ps.executeUpdate(); // Executa a atualização.
            sucesso = linhasAfetadas > 0; // Se mais de 0 linhas foram afetadas, a edição foi bem-sucedida.
            if (sucesso) {
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI.
                System.out.println("Responsável editado com sucesso!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) { // Captura exceções SQL.
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Ocorreu um erro ao editar responsável."); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    /*
     * Método para buscar um responsável no banco de dados com base em suas credenciais de login.
     * @param nome O nome do responsável.
     * @param email O email do responsável.
     * @return O objeto Responsavel encontrado se as credenciais forem válidas, ou null caso contrário.
     */
    public Responsavel buscarPorLogin(String nome, String email) {
        Responsavel responsavel = null; // Inicializa o responsável como null (não encontrado por padrão).
        String SQL = "SELECT * FROM responsavel WHERE nome = ? AND email = ?"; // SQL para buscar por nome e email.

        try (PreparedStatement ps = conexao.prepareStatement(SQL)) {
            ps.setString(1, nome);  // Define o nome.
            ps.setString(2, email); // Define o email.

            // Aninhado 'try-with-resources' para o ResultSet.
            try (ResultSet rs = ps.executeQuery()) { // Executa a consulta.
                if (rs.next()) { // Se encontrou uma linha.
                    responsavel = new Responsavel(); // Cria um novo objeto Responsavel.
                    responsavel.setId_responsavel(rs.getInt("id_responsavel")); // Popula o ID.
                    responsavel.setNome(rs.getString("nome"));                   // Popula o nome.
                    responsavel.setEmail(rs.getString("email"));                 // Popula o email.
                }
            }
        } catch (SQLException ex) { // Captura exceções SQL.
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Erro ao buscar responsável por login."); // Mensagem de erro.
        }
        return responsavel; // Retorna o responsável encontrado ou null.
    }

    /*
     * Método para buscar um responsável no banco de dados pelo seu ID.
     * @param id O ID do responsável a ser buscado.
     * @return O objeto Responsavel encontrado, ou null se não for encontrado.
     */
    public Responsavel buscarPorId(int id) {
        Responsavel responsavel = null; // Inicializa o responsável como null.
        String SQL = "SELECT * FROM responsavel WHERE id_responsavel = ?"; // SQL para buscar por ID.

        try (PreparedStatement ps = conexao.prepareStatement(SQL)) {
            ps.setInt(1, id); // Define o ID do responsável.

            // Aninhado 'try-with-resources' para o ResultSet.
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Se encontrou uma linha.
                    responsavel = new Responsavel(); // Cria um novo objeto Responsavel.
                    responsavel.setId_responsavel(rs.getInt("id_responsavel")); // Popula o ID.
                    responsavel.setNome(rs.getString("nome"));                   // Popula o nome.
                    responsavel.setEmail(rs.getString("email"));                 // Popula o email.
                }
            }
        } catch (SQLException ex) { // Captura exceções SQL.
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Erro ao buscar responsável por ID."); // Mensagem de erro.
        }
        return responsavel; // Retorna o responsável encontrado ou null.
    }

    /*
     * NOVO MÉTODO IMPLEMENTADO: Este método busca e lista todos os responsáveis
     * associados a um evento específico. Ele faz um JOIN com uma tabela de associação
     * (presumivelmente 'evento_responsavel') para encontrar os responsáveis ligados ao evento.
     * @param idEvento O ID do evento cujos responsáveis se deseja listar.
     * @return Uma lista de objetos Responsavel associados ao evento.
     */
    public List<Responsavel> listarResponsaveisPorEventoId(int idEvento) {
        List<Responsavel> responsaveis = new ArrayList<>(); // Cria uma lista vazia.
        // Query que faz JOIN com a tabela de associação 'evento_responsavel' (er) para filtrar
        // os responsáveis (r) que estão ligados a um 'idEvento' específico.
        String SQL = "SELECT r.id_responsavel, r.nome, r.email " +
                "FROM responsavel r " +
                "JOIN evento_responsavel er ON r.id_responsavel = er.id_responsavel " +
                "WHERE er.id_evento = ?";

        try (PreparedStatement ps = conexao.prepareStatement(SQL)) {
            ps.setInt(1, idEvento); // Define o ID do evento para filtrar.
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Responsavel r = new Responsavel(); // Cria um novo objeto Responsavel para cada resultado.
                    r.setId_responsavel(rs.getInt("id_responsavel"));
                    r.setNome(rs.getString("nome"));
                    r.setEmail(rs.getString("email"));
                    // Adicione outros campos do Responsavel se existirem e forem selecionados na query.
                    responsaveis.add(r); // Adiciona o responsável à lista.
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao buscar responsáveis do evento ID " + idEvento + ": " + ex.getMessage()); // Mensagem de erro.
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
        }
        return responsaveis; // Retorna a lista de responsáveis do evento.
    }
}
