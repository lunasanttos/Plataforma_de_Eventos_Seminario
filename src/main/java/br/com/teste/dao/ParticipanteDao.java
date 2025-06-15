package br.com.teste.dao;

import br.com.teste.config.Conexao;      // Importa a classe de configuração da conexão com o banco de dados (padrão Singleton).
import br.com.teste.model.Participante;   // Importa a classe de modelo Participante, que representa um participante.

import java.sql.PreparedStatement;      // Usado para executar comandos SQL pré-compilados e seguros (evita SQL Injection).
import java.sql.ResultSet;              // Usado para armazenar os resultados de consultas SQL.
import java.sql.SQLException;           // Importa a exceção para erros relacionados a SQL.
import java.sql.Statement;              // Usado para obter chaves geradas automaticamente pelo banco (auto-incremento).
import java.util.ArrayList;             // Usado para criar listas dinâmicas de objetos.
import java.util.List;                  // Interface List para coleções de objetos.

/*
 * A classe ParticipanteDao (Data Access Object) é responsável por todas as operações de
 * persistência de dados (CRUD - Create, Read, Update, Delete) para a entidade Participante
 * no banco de dados. Ela atua como uma camada de abstração entre a aplicação Java
 * e a tabela 'participante' no seu banco de dados.
 */
public class ParticipanteDao {

    // Atributo para armazenar a instância da Conexão com o banco de dados.
    private Conexao conexao;
    // private PreparedStatement ps; // REMOVER: Este atributo é desnecessário. 'ps' deve ser declarado localmente dentro de cada método
    // usando o bloco 'try-with-resources' para garantir seu fechamento automático e correto.
    // MANTIDO APENAS PARA AVISAR, CONFORME REQUISITADO.

    /*
     * Construtor da classe ParticipanteDao.
     * Ao criar um objeto ParticipanteDao, ele obtém a única instância da conexão
     * com o banco de dados através de Conexao.getInstance() (padrão Singleton).
     */
    public ParticipanteDao() {
        this.conexao = Conexao.getInstance();
    }

    /*
     * Método para buscar um participante no banco de dados com base em suas credenciais de login.
     * @param nome O nome do participante.
     * @param email O email do participante.
     * @param cpf O CPF do participante.
     * @return O objeto Participante encontrado se as credenciais forem válidas, ou null caso contrário.
     */
    public Participante buscarPorLogin(String nome, String email, String cpf) {
        Participante participante = null; // Inicializa o participante como null (não encontrado por padrão).
        // SQL para selecionar um participante usando nome, email e CPF como critérios de busca.
        // O '?' são placeholders para os valores a serem preenchidos, garantindo segurança.
        try (PreparedStatement ps = conexao.getConn().prepareStatement("SELECT * FROM participante WHERE nome = ? AND email = ? AND cpf = ?")) {
            ps.setString(1, nome);  // Define o valor para o 1º placeholder (nome).
            ps.setString(2, email); // Define o valor para o 2º placeholder (email).
            ps.setString(3, cpf);   // Define o valor para o 3º placeholder (cpf).

            // Aninhado 'try-with-resources' para o ResultSet, garantindo seu fechamento automático.
            try (ResultSet rs = ps.executeQuery()) { // Executa a consulta e armazena os resultados.
                if (rs.next()) { // Se encontrou uma linha (um participante com as credenciais).
                    participante = new Participante(); // Cria um novo objeto Participante.
                    participante.setId_participante(rs.getInt("id_participante")); // Popula o ID.
                    participante.setNome(rs.getString("nome"));                   // Popula o nome.
                    participante.setEmail(rs.getString("email"));                 // Popula o email.
                    participante.setCpf(rs.getString("cpf"));                     // Popula o CPF.
                }
            }
        } catch (SQLException ex) { // Captura exceções SQL que podem ocorrer durante a interação com o banco.
            ex.printStackTrace(); // Imprime o rastreamento da pilha do erro para depuração.
            System.out.println("Erro ao buscar participante por login."); // Mensagem de erro amigável.
        }
        return participante; // Retorna o participante encontrado ou null.
    }

    /*
     * Método para listar todos os participantes presentes no banco de dados.
     * @return Uma lista de objetos Participante.
     */
    public List<Participante> listar() {
        List<Participante> participantes = new ArrayList<>(); // Cria uma lista vazia para armazenar os participantes.
        String SQL = "SELECT * FROM participante ORDER BY nome"; // SQL para selecionar todos e ordenar por nome.

        // 'try-with-resources' para PreparedStatement e ResultSet.
        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { // Itera sobre cada linha do resultado.
                Participante participante = new Participante(); // Cria um novo objeto Participante.
                participante.setId_participante(rs.getInt("id_participante")); // Popula o ID.
                participante.setNome(rs.getString("nome"));                   // Popula o nome.
                participante.setEmail(rs.getString("email"));                 // Popula o email.
                participante.setCpf(rs.getString("cpf"));                     // Popula o CPF.
                participantes.add(participante); // Adiciona o participante à lista.
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Erro ao listar participantes."); // Mensagem de erro.
        }
        return participantes; // Retorna a lista de participantes.
    }

    /*
     * Método para inserir um novo participante no banco de dados.
     * @param participante O objeto Participante a ser inserido.
     * @return true se a inserção for bem-sucedida, false caso contrário.
     */
    public boolean inserir(Participante participante) {
        boolean sucesso = false; // Flag para indicar o sucesso da operação.
        // SQL para inserir um novo participante.
        try (PreparedStatement ps = conexao.getConn().prepareStatement(
                "INSERT INTO participante (nome, cpf, email) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) { // Usado para obter o ID gerado automaticamente.
            ps.setString(1, participante.getNome());  // Define o nome.
            ps.setString(2, participante.getCpf());   // Define o CPF.
            ps.setString(3, participante.getEmail()); // Define o email.

            int linhasAfetadas = ps.executeUpdate(); // Executa a inserção.
            if (linhasAfetadas > 0) { // Se a inserção afetou linhas (sucesso).
                // Tenta obter o ID gerado.
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        participante.setId_participante(rs.getInt(1)); // Atribui o ID gerado ao objeto.
                    }
                }
                sucesso = true; // Marca como sucesso.
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI,
                // não no DAO, que deve focar apenas na persistência dos dados. É um código 'morto' aqui.
                System.out.println("Participante inserido com sucesso!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Erro ao inserir participante."); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    /*
     * Método para excluir um participante do banco de dados com base em seu ID.
     * @param participante O objeto Participante a ser excluído (apenas o ID é necessário).
     * @return true se a exclusão for bem-sucedida, false caso contrário.
     */
    public boolean excluir(Participante participante) {
        boolean sucesso = false; // Flag de sucesso.
        // SQL para excluir um participante.
        try (PreparedStatement ps = conexao.getConn().prepareStatement("DELETE FROM participante WHERE id_participante = ?")) {
            ps.setInt(1, participante.getId_participante()); // Define o ID do participante a ser excluído.
            int linhasAfetadas = ps.executeUpdate(); // Executa a exclusão.
            sucesso = linhasAfetadas > 0; // Se linhas foram afetadas, sucesso.
            if (sucesso) {
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI.
                System.out.println("Participante excluído com sucesso!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Erro ao excluir participante."); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    /*
     * Método para editar (atualizar) os dados de um participante existente no banco de dados.
     * @param participante O objeto Participante com os dados atualizados (o ID deve existir no banco).
     * @return true se a edição for bem-sucedida, false caso contrário.
     */
    public boolean editar(Participante participante) {
        boolean sucesso = false; // Flag de sucesso.
        // SQL para atualizar os dados de um participante.
        try (PreparedStatement ps = conexao.getConn().prepareStatement("UPDATE participante SET nome = ?, cpf = ?, email = ? WHERE id_participante = ?")) {
            ps.setString(1, participante.getNome());  // Define o novo nome.
            ps.setString(2, participante.getCpf());   // Define o novo CPF.
            ps.setString(3, participante.getEmail()); // Define o novo email.
            ps.setInt(4, participante.getId_participante()); // Define o ID do participante a ser editado (na cláusula WHERE).
            int linhasAfetadas = ps.executeUpdate(); // Executa a atualização.
            sucesso = linhasAfetadas > 0; // Se linhas foram afetadas, sucesso.
            if (sucesso) {
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI.
                System.out.println("Participante editado com sucesso!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Erro ao editar participante."); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    /*
     * Método para buscar um participante no banco de dados pelo seu ID.
     * @param id O ID do participante a ser buscado.
     * @return O objeto Participante encontrado, ou null se não for encontrado.
     */
    public Participante buscarPorId(int id) {
        Participante participante = null; // Inicializa o participante como null.
        String SQL = "SELECT * FROM participante WHERE id_participante = ?"; // SQL para buscar por ID.

        try (PreparedStatement ps = conexao.getConn().prepareStatement(SQL)) {
            ps.setInt(1, id); // Define o ID do participante a ser buscado.

            // Aninhado 'try-with-resources' para o ResultSet.
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Se encontrou uma linha.
                    participante = new Participante(); // Cria um novo objeto Participante.
                    participante.setId_participante(rs.getInt("id_participante")); // Popula o ID.
                    participante.setNome(rs.getString("nome"));                   // Popula o nome.
                    participante.setEmail(rs.getString("email"));                 // Popula o email.
                    participante.setCpf(rs.getString("cpf"));                     // Popula o CPF.
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Erro ao buscar participante por ID."); // Mensagem de erro.
        }
        return participante; // Retorna o participante encontrado ou null.
    }
}
