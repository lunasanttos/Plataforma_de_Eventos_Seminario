package br.com.teste.dao;

// Importa a classe de modelo Evento, que representa um evento no sistema.
import br.com.teste.model.Evento;
// Importa a classe de modelo Local, que representa o local de um evento.
import br.com.teste.model.Local;
// Importa a classe de configuração da conexão com o banco de dados.
import br.com.teste.config.Conexao;

import java.sql.Connection;        // Usado para gerenciar a conexão com o banco de dados.
import java.sql.PreparedStatement; // Usado para executar comandos SQL pré-compilados (seguros contra SQL Injection).
import java.sql.ResultSet;       // Usado para armazenar os resultados de consultas SQL.
import java.sql.SQLException;    // Importa a exceção para erros relacionados a SQL.
import java.sql.Date;            // Usado para mapear datas Java para o tipo SQL DATE.
import java.sql.Time;            // Usado para mapear horas Java para o tipo SQL TIME.
import java.sql.Statement;       // Usado para executar comandos SQL (aqui, especificamente para obter chaves geradas).
import java.time.LocalDate;      // Usado para trabalhar com datas sem informações de tempo.
import java.time.LocalTime;      // Usado para trabalhar com horas sem informações de data.
import java.util.ArrayList;      // Usado para criar listas dinâmicas de objetos.
import java.util.List;           // Interface List para coleções de objetos.

/*
 * A classe EventoDao (Data Access Object) é responsável por todas as operações de
 * persistência de dados (CRUD - Create, Read, Update, Delete) para a entidade Evento
 * no banco de dados. Ela isola a lógica de acesso a dados da lógica de negócio.
 */
public class EventoDao {

    // Atributo para armazenar a conexão com o banco de dados.
    private Connection conexao;

    /*
     * Construtor da classe EventoDao.
     * Ao criar uma instância de EventoDao, ela obtém a conexão ativa do banco de dados
     * através da instância única da classe Conexao (padrão Singleton).
     */
    public EventoDao() {
        this.conexao = Conexao.getInstance().getConn();
    }

    /*
     * Método auxiliar privado que cria um objeto Evento (e o Local associado)
     * a partir de um ResultSet. Isso evita duplicação de código nas consultas.
     * @param rs O ResultSet contendo os dados do evento e do local.
     * @return Um objeto Evento populado com os dados do ResultSet.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private Evento criarEventoDoResultSet(ResultSet rs) throws SQLException {
        // Cria um objeto Local com base nas colunas do ResultSet que contêm informações do local.
        Local localDoEvento = new Local(
                rs.getInt("id_local"),
                rs.getString("local_nome"),    // O alias 'local_nome' é usado na query SQL.
                rs.getString("local_endereco"),
                rs.getInt("local_capacidade")
        );

        // Cria e retorna um objeto Evento com base nas colunas do ResultSet e o objeto Local criado.
        return new Evento(
                rs.getInt("id_evento"),
                rs.getString("nome"),
                rs.getString("tipo"),
                rs.getDate("data").toLocalDate(),    // Converte java.sql.Date para java.time.LocalDate.
                rs.getTime("hora").toLocalTime(),    // Converte java.sql.Time para java.time.LocalTime.
                rs.getString("descricao"),
                localDoEvento                       // Associa o objeto Local ao Evento.
        );
    }

    // --- Métodos de Leitura (Read) ---

    /*
     * Busca um evento específico no banco de dados pelo seu ID.
     * @param idEvento O ID do evento a ser buscado.
     * @return O objeto Evento correspondente, ou null se não for encontrado.
     */
    public Evento buscarPorId(int idEvento) {
        Evento evento = null; // Inicializa o evento como null.
        // SQL para selecionar um evento, fazendo um JOIN com a tabela 'local' para obter
        // todas as informações necessárias em uma única consulta. Alias são usados para evitar conflitos de nomes.
        String sql = "SELECT e.id_evento, e.nome, e.tipo, e.data, e.hora, e.descricao, " +
                "l.id_local, l.nome AS local_nome, l.endereco AS local_endereco, l.capacidade AS local_capacidade " +
                "FROM evento e JOIN local l ON e.id_local = l.id_local " +
                "WHERE e.id_evento = ?"; // O '?' é um placeholder para o ID do evento.

        // O 'try-with-resources' garante que o PreparedStatement seja fechado automaticamente.
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idEvento); // Define o valor do placeholder para o ID do evento.

            // Outro 'try-with-resources' para garantir que o ResultSet seja fechado.
            try (ResultSet rs = stmt.executeQuery()) { // Executa a consulta.
                if (rs.next()) { // Se encontrou uma linha (um evento com o ID correspondente).
                    evento = criarEventoDoResultSet(rs); // Cria o objeto Evento usando o método auxiliar.
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar evento por ID no DAO: " + e.getMessage()); // Mensagem de erro.
            e.printStackTrace(); // Imprime o rastreamento da pilha para depuração.
        }
        return evento; // Retorna o evento encontrado ou null.
    }

    /*
     * Lista todos os eventos que estão disponíveis (data atual ou futura).
     * @return Uma lista de objetos Evento que ainda não ocorreram.
     */
    public List<Evento> listarEventosDisponiveis() {
        List<Evento> eventos = new ArrayList<>(); // Cria uma lista vazia para armazenar os eventos.
        // SQL para selecionar eventos futuros ou de hoje, juntando com a tabela 'local' e ordenando por data e hora.
        String sql = "SELECT e.id_evento, e.nome, e.tipo, e.data, e.hora, e.descricao, " +
                "l.id_local, l.nome AS local_nome, l.endereco AS local_endereco, l.capacidade AS local_capacidade " +
                "FROM evento e JOIN local l ON e.id_local = l.id_local " +
                "WHERE e.data >= CURRENT_DATE ORDER BY e.data ASC, e.hora ASC";

        // 'try-with-resources' para PreparedStatement e ResultSet.
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) { // Executa a consulta.
            while (rs.next()) { // Itera sobre cada linha do resultado.
                eventos.add(criarEventoDoResultSet(rs)); // Adiciona o evento criado à lista.
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar eventos disponíveis no DAO: " + e.getMessage()); // Mensagem de erro.
            e.printStackTrace(); // Imprime o rastreamento da pilha.
        }
        return eventos; // Retorna a lista de eventos.
    }

    /*
     * Lista todos os eventos cadastrados no banco de dados, independentemente da data.
     * @return Uma lista de todos os objetos Evento.
     */
    public List<Evento> listarTodos() {
        List<Evento> eventos = new ArrayList<>(); // Cria uma lista vazia.
        // SQL para selecionar todos os eventos, juntando com 'local' e ordenando por data (descendente) e hora (descendente).
        String sql = "SELECT e.id_evento, e.nome, e.tipo, e.data, e.hora, e.descricao, " +
                "l.id_local, l.nome AS local_nome, l.endereco AS local_endereco, l.capacidade AS local_capacidade " +
                "FROM evento e JOIN local l ON e.id_local = l.id_local " +
                "ORDER BY e.data DESC, e.hora DESC";

        // 'try-with-resources' para PreparedStatement e ResultSet.
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                eventos.add(criarEventoDoResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar todos os eventos no DAO: " + e.getMessage()); // Mensagem de erro.
            e.printStackTrace(); // Imprime o rastreamento da pilha.
        }
        return eventos; // Retorna a lista de eventos.
    }

    // --- Método de Criação (Create) ---

    /*
     * Insere um novo evento no banco de dados.
     * @param evento O objeto Evento a ser inserido.
     * @return true se o evento foi inserido com sucesso, false caso contrário.
     */
    public boolean inserir(Evento evento) {
        boolean sucesso = false;
        // SQL para inserir um evento. Os '?' são placeholders para os valores.
        String sql = "INSERT INTO evento (nome, tipo, data, hora, descricao, id_local) VALUES (?, ?, ?, ?, ?, ?)";

        // 'try-with-resources' para PreparedStatement. Statement.RETURN_GENERATED_KEYS é crucial para
        // obter o ID gerado automaticamente pelo banco de dados (auto-incremento).
        try (PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getTipo());
            ps.setDate(3, Date.valueOf(evento.getData()));     // Converte LocalDate para java.sql.Date.
            ps.setTime(4, Time.valueOf(evento.getHora()));     // Converte LocalTime para java.sql.Time.
            ps.setString(5, evento.getDescricao());
            ps.setInt(6, evento.getLocal().getId_local()); // Pega o ID do Local associado ao Evento.

            int linhasAfetadas = ps.executeUpdate(); // Executa a inserção.
            if (linhasAfetadas > 0) { // Se a inserção afetou pelo menos uma linha.
                // Tenta recuperar o ID gerado para o novo evento.
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        evento.setId_evento(rs.getInt(1)); // Define o ID gerado no objeto Evento.
                    }
                }
                sucesso = true; // Marca como sucesso.
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI,
                // não no DAO, que deve focar apenas na persistência.
                System.out.println("Evento inserido no banco de dados!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha em caso de erro.
            System.out.println("Ocorreu um erro ao inserir evento no DAO: " + ex.getMessage()); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    // --- Método de Edição (Update) ---

    /*
     * Edita um evento existente no banco de dados.
     * @param evento O objeto Evento com os dados atualizados (o ID deve existir no banco).
     * @return true se o evento foi editado com sucesso, false caso contrário.
     */
    public boolean editar(Evento evento) {
        boolean sucesso = false;
        // SQL para atualizar um evento com base no seu ID.
        String sql = "UPDATE evento SET nome = ?, tipo = ?, data = ?, hora = ?, descricao = ?, id_local = ? " +
                "WHERE id_evento = ?";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getTipo());
            ps.setDate(3, Date.valueOf(evento.getData()));
            ps.setTime(4, Time.valueOf(evento.getHora()));
            ps.setString(5, evento.getDescricao());
            ps.setInt(6, evento.getLocal().getId_local()); // Pega o ID do Local associado.
            ps.setInt(7, evento.getId_evento());           // O ID do evento a ser atualizado (na cláusula WHERE).

            int linhasAfetadas = ps.executeUpdate(); // Executa a atualização.
            sucesso = linhasAfetadas > 0; // Se mais de 0 linhas foram afetadas, a edição foi bem-sucedida.
            if (sucesso) {
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI.
                System.out.println("Evento editado no banco de dados!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Ocorreu um erro ao editar evento no DAO: " + ex.getMessage()); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }

    // --- Método de Exclusão (Delete) ---

    /*
     * Exclui um evento do banco de dados pelo seu ID.
     * @param idEvento O ID do evento a ser excluído.
     * @return true se o evento foi excluído com sucesso, false caso contrário.
     */
    public boolean excluir(int idEvento) {
        boolean sucesso = false;
        // SQL para excluir um evento com base no seu ID.
        String sql = "DELETE FROM evento WHERE id_evento = ?";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idEvento); // Define o ID do evento a ser excluído.
            int linhasAfetadas = ps.executeUpdate(); // Executa a exclusão.
            sucesso = linhasAfetadas > 0; // Se mais de 0 linhas foram afetadas, a exclusão foi bem-sucedida.
            if (sucesso) {
                // OBSERVAÇÃO: Mensagens de sucesso como esta geralmente são manipuladas na camada de Serviço ou de UI.
                System.out.println("Evento excluído do banco de dados!"); // MENSAGEM DESNECESSÁRIA NO DAO.
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime o rastreamento da pilha.
            System.out.println("Ocorreu um erro ao excluir evento no DAO: " + ex.getMessage()); // Mensagem de erro.
        }
        return sucesso; // Retorna o status do sucesso.
    }
}