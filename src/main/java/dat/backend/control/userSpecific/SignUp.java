package dat.backend.control.userSpecific;

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
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        session.setAttribute("user", null);

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String role = "user";
        boolean validNumber = true;
        int phoneNumber;
        try
        {
            phoneNumber = Integer.parseInt(request.getParameter("phoneNumber"));
            if (phoneNumber <= 10000000 || phoneNumber >= 100000000)
            {
                validNumber = false;
                throw new NumberFormatException();
            }
        }

        catch (NumberFormatException e)
        {
            wrongParameterFailure("Indtast venligst et telefonnummer på 8 cifre!", request, response);
        }


        if (!email.contains("@") )
        {
            wrongParameterFailure("Indtast venligst en brugbar email-adresse", request, response);
        }

        else if(password.isEmpty() || address.isEmpty()
                || city.isEmpty() || !validNumber)
        {
            wrongParameterFailure("Et eller flere parametre er tomme", request, response);
        }

        else
        {
            try
            {
                phoneNumber = Integer.parseInt(request.getParameter("phoneNumber"));
                User user = Facade.createUser(email, password, address, city, phoneNumber, role, connectionPool);
                session = request.getSession();
                session.setAttribute("user", user); // adding user object to session scope
                request.getRequestDispatcher("WEB-INF/userPage.jsp").forward(request, response);
            }
            catch (DatabaseException e)
            {
                request.setAttribute("errormessage", e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
            catch (IllegalArgumentException e)
            {
                wrongParameterFailure(e.getMessage(), request, response);
            }
        }
    }

    private void wrongParameterFailure(String msg, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("errorMSG", msg);
        request.getRequestDispatcher("registerUser.jsp").forward(request, response);
    }
}
