package br.edu.ifto.web.servlet;

import br.edu.ifto.web.dao.UsuarioDAO;
import br.edu.ifto.web.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO repositorio =
            new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        Usuario usuario = repositorio.autenticar(email, senha);

        if (usuario == null) {
            request.setAttribute("erro", "E-mail ou senha inválida.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }

        HttpSession sessao = request.getSession();
        request.changeSessionId();
        sessao.setAttribute("usuarioLogado", usuario);

        response.sendRedirect(request.getContextPath() + "/");
    }
}
