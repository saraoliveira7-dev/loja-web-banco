package br.edu.ifto.web.servlet;

import br.edu.ifto.web.dao.ProdutoDAO;
import br.edu.ifto.web.model.ItemCarrinho;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/produtos")
public class ProdutoServlet extends HttpServlet {

    private final ProdutoDAO repositorio =
            new ProdutoDAO();

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("produtos", repositorio.listarTodos());

        int quantidadeTotal = 0;
        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            List<ItemCarrinho> carrinho =
                    (List<ItemCarrinho>) sessao.getAttribute("carrinho");
            if (carrinho != null) {
                for (ItemCarrinho item : carrinho) {
                    quantidadeTotal += item.getQuantidade();
                }
            }
        }
        request.setAttribute("quantidadeTotal", quantidadeTotal);

        request.getRequestDispatcher("/WEB-INF/views/produtos.jsp")
                .forward(request, response);
    }
}
