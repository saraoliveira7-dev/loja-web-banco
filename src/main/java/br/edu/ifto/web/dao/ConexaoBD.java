package br.edu.ifto.web.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Fábrica de conexões JDBC. Lê as configurações de /db.properties.
 */
public final class ConexaoBD {

    private static final Properties CONFIG = new Properties();

    static {
        try (InputStream in = ConexaoBD.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                throw new IllegalStateException("Arquivo db.properties não encontrado.");
            }
            CONFIG.load(in);
            Class.forName(CONFIG.getProperty("db.driver"));
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConexaoBD() {
    }

    public static Connection getConexao() throws SQLException {
        String url = CONFIG.getProperty("db.url")
                .replace("${user.home}", System.getProperty("user.home").replace('\\', '/'));
        return DriverManager.getConnection(url,
                CONFIG.getProperty("db.user", ""),
                CONFIG.getProperty("db.password", ""));
    }
}
