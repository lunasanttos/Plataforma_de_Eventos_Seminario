package br.com.teste.dao;

import br.com.teste.config.Conexao; // Importa a classe de configuração da conexão com o banco de dados (padrão Singleton).
import br.com.teste.model.Local;      // Importa a classe de modelo Local, que representa uma localização física.

import java.sql.Connection;        // Importa a interface Connection, para gerenciar a conexão com o banco. (OBSERVAÇÃO: Este import 'Connection' é desnecessário se a classe 'Conexao' já fornece a Connection e esta classe 'LocalDao' usa apenas 'conexao.getConn()'. É um código 'morto' aqui.)
import java.sql.PreparedStatement; // Usado para executar comandos SQL pré-compilados e seguros.
import java.sql.ResultSet;       // Usado para armazenar os resultados de consultas SQL.
import java.sql.SQLException;    // Importa a exceção para erros relacionados a SQL.
import java.sql.Statement;       // Usado para obter chaves geradas automaticamente pelo banco (auto-incremento).
import java.util.ArrayList;      // Usado para criar listas dinâmicas de objetos.
import java.util.List;           // Interface List para coleções de objetos.

/*
 * A classe LocalDao (Data Access Object) é responsável por todas as operações de
 * persistência de dados (CRUD - Create, Read, Update, Delete) para a entidade Local
 * no banco de dados. Ela atua como uma camada de abstração entre a aplicação Java
 * e a tabela 'local' no seu banco de dados.
 */
public class LocalDao {

    // Atributo para armazenar a instância da Conexão com o banco de dados.
    // O tipo 'Conexao' está correto, pois você quer a instância do objeto que gerencia a conexão.
    private Conexao conexao;

    /*
     * Construtor da classe LocalDao.
     * Ao criar um objeto LocalDao, ele obtém a única instância da conexão
     * com o banco de dados através de Conexao.getInstance() (padrão Singleton).
     */
    public LocalDao() {
        this.conexao = Conexao.getInstance(); // Pega a instância do singleton de Conexao.
    }

    /*
     * Método para listar todos os locais presentes no banco de dados.
     * @return Uma lista de objetos Local.
     */
    public List<Local> listar() {
        List<Local> locais = new ArrayList<>(); // Cria uma lista vazia para armazenar os objetos Local.
        String SQL = "SELECT * FROM local";     // Define a consulta SQL para selecionar todas as linhas da tabela 'local'.

        // O 'try-with-resources' garante que PreparedStatement e ResultSet sejam fechados automaticamente,
        // mesmo que ocorram erros. Isso é uma boa prática para evitar vazamento de recursos.
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL); // Prepara a declaração SQL usando a conexão atual.
             ResultSet rs = ps.executeQuery()) { // Executa a consulta e armazena os resultados no ResultSet.

            while (rs.next()) { // Itera sobre cada linha (registro) retornada pelo ResultSet.
                // Para cada linha, cria um novo objeto Local e popula seus atributos
                // com os valores das colunas correspondentes do banco de dados.
                Local local = new Local(
                        rs.getInt("id_local"),      // Obtém o ID do local da coluna 'id_local'.
                        rs.getString("nome"),       // Obtém o nome do local da coluna 'nome'.
                        rs.getString("endereco"),   // Obtém o endereço do local da coluna 'endereco'.
                        rs.getInt("capacidade")     // Obtém a capacidade do local da coluna 'capacidade'.
                );
                locais.add(local); // Adiciona o objeto Local à lista.
            }
        } catch (SQLException ex) { // Captura exceções SQL que podem ocorrer durante a interação com o banco de dados.
            ex.printStackTrace(); // Imprime o rastreamento da pilha do erro para depuração (útil em desenvolvimento).
            System.out.println("Erro ao listar locais: " + ex.getMessage()); // Exibe uma mensagem de erro amigável.
        }
        return locais; // Retorna a lista de locais encontrada.
    }

    /*
     * Método para inserir um novo local no banco de dados.
     * @param local O objeto Local a ser inserido.
     * @return true se a inserção for bem-sucedida, false caso contrário.
     */
    public boolean inserir(Local local) {
        boolean sucesso = false; // Flag para indicar o sucesso da operação.
        // SQL para inserir um novo local. Os '?' são placeholders que serão preenchidos
        // com os valores do objeto Local, prevenindo SQL Injection.
        String SQL = "INSERT INTO local(nome, endereco, capacidade) VALUES (?, ?, ?)";

        // 'try-with-resources' para PreparedStatement.
        // Statement.RETURN_GENERATED_KEYS é usado para instruir o banco de dados a retornar
        // as chaves geradas automaticamente (como o ID auto-incrementado) após a inserção.
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, local.getNome());     // Define o valor para o 1º placeholder (nome).
            ps.setString(2, local.getEndereco()); // Define o valor para o 2º placeholder (endereco).
            ps.setInt(3, local.getCapacidade());   // Define o valor para o 3º placeholder (capacidade).

            int linhasAfetadas = ps.executeUpdate(); // Executa a inserção e retorna o número de linhas afetadas.
            if (linhasAfetadas > 0) { // Se pelo menos uma linha foi afetada (o que indica sucesso na inserção).
                // Tenta obter o ID que foi gerado automaticamente pelo banco de dados.
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) { // Se há um resultado (o ID gerado).
                        local.setId_local(rs.getInt(1)); // Atribui o ID gerado ao objeto Local.
                    }
                }
                sucesso = true; // Marca a operação como bem-sucedida.
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI,
                // não no DAO, que deve focar apenas na persistência dos dados. É um código 'morto' aqui.
                System.out.println("Local inserido com sucesso!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) { // Captura exceções SQL.
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Erro ao inserir local: " + ex.getMessage()); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    /*
     * Método para excluir um local do banco de dados com base em seu ID.
     * @param local O objeto Local a ser excluído (apenas o ID é necessário).
     * @return true se a exclusão for bem-sucedida, false caso contrário.
     */
    public boolean excluir(Local local) {
        boolean sucesso = false; // Flag de sucesso.
        String SQL = "DELETE FROM local WHERE id_local = ?"; // SQL para excluir um local pelo ID.

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, local.getId_local()); // Define o ID do local a ser excluído.

            int linhasAfetadas = ps.executeUpdate(); // Executa a exclusão.
            sucesso = linhasAfetadas > 0; // Se mais de 0 linhas foram afetadas, a exclusão foi bem-sucedida.
            if (sucesso) {
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI.
                System.out.println("Local excluído com sucesso!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) { // Captura exceções SQL.
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Erro ao excluir local: " + ex.getMessage()); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    /*
     * Método para editar (atualizar) um local existente no banco de dados.
     * @param local O objeto Local com os dados atualizados (o ID deve existir no banco).
     * @return true se a edição for bem-sucedida, false caso contrário.
     */
    public boolean editar(Local local) {
        boolean sucesso = false; // Flag de sucesso.
        // SQL para atualizar os dados de um local com base no seu ID.
        String SQL = "UPDATE local SET nome = ?, endereco = ?, capacidade = ? WHERE id_local = ?";

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setString(1, local.getNome());     // Define o novo nome.
            ps.setString(2, local.getEndereco()); // Define o novo endereço.
            ps.setInt(3, local.getCapacidade());   // Define a nova capacidade.
            ps.setInt(4, local.getId_local());     // Define o ID do local a ser editado (na cláusula WHERE).

            int linhasAfetadas = ps.executeUpdate(); // Executa a atualização.
            sucesso = linhasAfetadas > 0; // Se mais de 0 linhas foram afetadas, a edição foi bem-sucedida.
            if (sucesso) {
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI.
                System.out.println("Local editado com sucesso!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) { // Captura exceções SQL.
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Erro ao editar local: " + ex.getMessage()); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    /*
     * Método para buscar um local no banco de dados pelo seu ID.
     * @param id O ID do local a ser buscado.
     * @return O objeto Local encontrado, ou null se não for encontrado.
     */
    public Local buscarPorId(int id) {
        Local local = null; // Inicializa o local como null (nenhum encontrado ainda).
        String SQL = "SELECT * FROM local WHERE id_local = ?"; // SQL para buscar um local pelo ID.

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, id); // Define o ID do local a ser buscado.

            // Aninhado 'try-with-resources' para o ResultSet, garantindo seu fechamento.
            try (ResultSet rs = ps.executeQuery()) { // Executa a consulta.
                if (rs.next()) { // Se encontrou uma linha (um local com o ID).
                    // Cria e popula o objeto Local com os dados do ResultSet.
                    local = new Local(
                            rs.getInt("id_local"),
                            rs.getString("nome"),
                            rs.getString("endereco"),
                            rs.getInt("capacidade")
                    );
                }
            }
        } catch (SQLException ex) { // Captura exceções SQL.
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Erro ao buscar local por ID: " + ex.getMessage()); // Mensagem de erro.
        }
        return local; // Retorna o local encontrado ou null.
    }
}