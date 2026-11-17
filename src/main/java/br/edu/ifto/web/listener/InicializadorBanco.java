package br.edu.ifto.web.listener;

import br.edu.ifto.web.dao.ConexaoBD;
import br.edu.ifto.web.dao.UsuarioDAO;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Executado na subida da aplicação: cria as tabelas (se não existirem)
 * e insere os dados iniciais quando as tabelas estão vazias.
 */
@WebListener
public class InicializadorBanco implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        try (Connection con = ConexaoBD.getConexao()) {
            criarTabelas(con);
            if (estaVazia(con, "usuario")) {
                popularUsuarios(con);
            }
            if (estaVazia(con, "produto")) {
                popularProdutos(con);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao inicializar o banco de dados.", e);
        }
    }

    private void criarTabelas(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS usuario ("
                    + "id INTEGER PRIMARY KEY, "
                    + "nome VARCHAR(100) NOT NULL, "
                    + "email VARCHAR(150) NOT NULL UNIQUE, "
                    + "senha VARCHAR(64) NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS produto ("
                    + "id INTEGER PRIMARY KEY, "
                    + "nome VARCHAR(100) NOT NULL, "
                    + "descricao VARCHAR(255) NOT NULL, "
                    + "preco NUMERIC(10,2) NOT NULL, "
                    + "icone VARCHAR(20))");
        }
    }

    private boolean estaVazia(Connection con, String tabela) throws SQLException {
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + tabela)) {
            rs.next();
            return rs.getInt(1) == 0;
        }
    }

    private void popularUsuarios(Connection con) throws SQLException {
        String sql = "INSERT INTO usuario (id, nome, email, senha) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            inserirUsuario(ps, 1, "Carlos Lima", "carlos.lima@email.com", "carlos123");
            inserirUsuario(ps, 2, "Ana Beatriz Santos", "ana.santos@email.com", "ana123");
        }
    }

    private void inserirUsuario(PreparedStatement ps, int id, String nome,
                                String email, String senha) throws SQLException {
        ps.setInt(1, id);
        ps.setString(2, nome);
        ps.setString(3, email);
        ps.setString(4, UsuarioDAO.hash(senha));
        ps.executeUpdate();
    }

    private void popularProdutos(Connection con) throws SQLException {
        String sql = "INSERT INTO produto (id, nome, descricao, preco, icone) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            inserirProduto(ps, 1, "Notebook", "Notebook para estudos e trabalho", "3500.00", "");
            inserirProduto(ps, 2, "Mouse", "Mouse para computador", "80.00", "");
            inserirProduto(ps, 3, "Teclado Mecânico", "Teclado mecânico para digitação e jogos", "250.00", "");
            inserirProduto(ps, 4, "Monitor", "Monitor para computador", "1200.00", "");
            inserirProduto(ps, 5, "Headset", "Headset para áudio e comunicação", "180.00", "");
        }
    }

    private void inserirProduto(PreparedStatement ps, int id, String nome, String descricao,
                                String preco, String icone) throws SQLException {
        ps.setInt(1, id);
        ps.setString(2, nome);
        ps.setString(3, descricao);
        ps.setBigDecimal(4, new java.math.BigDecimal(preco));
        ps.setString(5, icone);
        ps.executeUpdate();
    }
}
