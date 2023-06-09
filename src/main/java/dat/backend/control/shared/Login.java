package dat.backend.control.shared;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.entities.User;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "login", urlPatterns = {"/login"} )
public class Login extends HttpServlet
{
    private ConnectionPool connectionPool;

    @Override
    public void init() throws ServletException
    {
        this.connectionPool = ApplicationStart.getConnectionPool();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        // You shouldn't end up here with a GET-request, thus you get sent back to frontpage
        response.sendRedirect("index.jsp");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        session.setAttribute("user", null); // invalidating user object in session scope
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try
        {
            if(Facade.login(email, password, connectionPool) == null)
            {
                String errorMSG = "Din email eller kode er forkert!";
                request.setAttribute("errorMSG", errorMSG);
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }

            User user = Facade.login(email, password, connectionPool);
            session = request.getSession();
            session.setAttribute("user", user); // adding user object to session scope

            if(user.getRole().equals("admin"))
            {
                request.getRequestDispatcher("WEB-INF/adminPanel.jsp").forward(request, response);
            }
            else
            {
                request.getRequestDispatcher("WEB-INF/userPage.jsp").forward(request, response);
            }
        }
        catch (DatabaseException e)
        {
            request.setAttribute("errormessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}