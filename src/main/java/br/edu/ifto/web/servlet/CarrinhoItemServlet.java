
package br.edu.ifto.web.servlet;

import br.edu.ifto.web.dao.ProdutoDAO;
import br.edu.ifto.web.model.ItemCarrinho;
import br.edu.ifto.web.model.Produto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/carrinho/itens")
@SuppressWarnings("unchecked")
public class CarrinhoItemServlet extends HttpServlet {

    private final ProdutoDAO repositorio =
            new ProdutoDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String idParametro = request.getParameter("id");

        try {
            int id = Integer.parseInt(idParametro);
            Produto produto = repositorio.buscarPorId(id);

            if (produto == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Produto não encontrado.");
                return;
            }

            HttpSession sessao = request.getSession();
            List<ItemCarrinho> carrinho =
                    (List<ItemCarrinho>) sessao.getAttribute("carrinho");

            if (carrinho == null) {
                carrinho = new ArrayList<>();
                sessao.setAttribute("carrinho", carrinho);
            }

            boolean encontrado = false;

            for (ItemCarrinho item : carrinho) {
                if (item.getProduto().getId() == id) {
                    item.aumentarQuantidade();
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                carrinho.add(new ItemCarrinho(produto, 1));
            }

            response.sendRedirect(
                    request.getContextPath() + "/carrinho");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "ID de produto inválido.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {

        String idParametro = request.getParameter("id");

        try {
            int id = Integer.parseInt(idParametro);

            HttpSession sessao = request.getSession(false);

            if (sessao != null) {
                List<ItemCarrinho> carrinho =
                        (List<ItemCarrinho>) sessao.getAttribute("carrinho");

                if (carrinho != null) {
                    carrinho.removeIf(
                            item -> item.getProduto().getId() == id);
                }
            }

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "ID de produto inválido.");
        }
    }
}
