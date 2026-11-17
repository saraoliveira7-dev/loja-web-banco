package br.edu.ifto.web.dao;

import br.edu.ifto.web.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private static final String COLUNAS = "id, nome, descricao, preco, icone";

    public List<Produto> listarTodos() {
        String sql = "SELECT " + COLUNAS + " FROM produto ORDER BY id";
        List<Produto> produtos = new ArrayList<>();

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar produtos.", e);
        }
        return produtos;
    }

    public Produto buscarPorId(int id) {
        String sql = "SELECT " + COLUNAS + " FROM produto WHERE id = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar produto " + id + ".", e);
        }
    }

    private Produto mapear(ResultSet rs) throws SQLException {
        String icone = rs.getString("icone");
        return new Produto(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getBigDecimal("preco").doubleValue(),
                icone == null ? "" : icone);
    }
}
