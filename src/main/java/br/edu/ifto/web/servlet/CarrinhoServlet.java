
package br.edu.ifto.web.servlet;

import br.edu.ifto.web.model.ItemCarrinho;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/carrinho")
@SuppressWarnings("unchecked")
public class CarrinhoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao = request.getSession();
        List<ItemCarrinho> carrinho =
                (List<ItemCarrinho>) sessao.getAttribute("carrinho");

        if (carrinho == null) {
            carrinho = new ArrayList<>();
            sessao.setAttribute("carrinho", carrinho);
        }

        int quantidadeTotal = 0;
        double valorTotal = 0;

        for (ItemCarrinho item : carrinho) {
            quantidadeTotal += item.getQuantidade();
            valorTotal += item.getSubtotal();
        }

        request.setAttribute("carrinho", carrinho);
        request.setAttribute("quantidadeTotal", quantidadeTotal);
        request.setAttribute("valorTotal", valorTotal);

        request.getRequestDispatcher("/WEB-INF/views/carrinho.jsp")
                .forward(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao = request.getSession(false);

        if (sessao != null) {
            sessao.removeAttribute("carrinho");
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
