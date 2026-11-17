package br.edu.ifto.web.dao;

import br.edu.ifto.web.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public List<Usuario> listarTodos() {
        String sql = "SELECT id, nome, email, senha FROM usuario ORDER BY id";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar usuários.", e);
        }
        return usuarios;
    }

    public Usuario buscarPorEmail(String email) {
        if (email == null) {
            return null;
        }
        String sql = "SELECT id, nome, email, senha FROM usuario WHERE LOWER(email) = LOWER(?)";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar usuário.", e);
        }
    }

    public Usuario autenticar(String email, String senha) {
        if (senha == null) {
            return null;
        }
        Usuario usuario = buscarPorEmail(email);
        if (usuario != null && usuario.getSenha().equals(hash(senha))) {
            return usuario;
        }
        return null;
    }

    /** Gera o hash SHA-256 (hexadecimal) da senha. */
    public static String hash(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha"));
    }
}
