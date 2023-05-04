package dat.backend.control;

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

@WebServlet(name = "SignUp", value = "/signup")
public class SignUp extends HttpServlet
{
    private ConnectionPool connectionPool;

    @Override
    public void init() throws ServletException
    {
        this.connectionPool = ApplicationStart.getConnectionPool();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.sendRedirect("registerUser.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        session.setAttribute("user", null);

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        int zipCode = Integer.parseInt(request.getParameter("zipCode"));
        int phoneNumber = Integer.parseInt(request.getParameter("phoneNumber"));
        String role = "user";


        // TODO delete this when the userMapper method create user is done
        User user = Facade.createUser(email, password, address, city, zipCode, phoneNumber, role);
        session = request.getSession();
        session.setAttribute("user", user); // adding user object to session scope
        request.getRequestDispatcher("WEB-INF/userPage.jsp").forward(request, response);

        // TODO you this when the userMapper method create user is done
/*        try
        {
            User user = Facade.createUser(email, password, address, city, zipCode, phoneNumber, role);
            session = request.getSession();
            session.setAttribute("user", user); // adding user object to session scope
            request.getRequestDispatcher("WEB-INF/userPage.jsp").forward(request, response);
        }
        catch (DatabaseException e)
        {
            request.setAttribute("errormessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }*/
    }
}
