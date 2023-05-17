package dat.backend.control.adminSpecific;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.entities.Receipt;
import dat.backend.model.entities.User;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "ReceiptsAdmin", value = "/receiptsadmin")
public class ReceiptsAdmin extends HttpServlet
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

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        try
        {
            List<Receipt> receiptsList = Facade.getAllReceipts(connectionPool);
            Collections.reverse(receiptsList);
            request.setAttribute("receiptsList", receiptsList);

            List<User> usersList = Facade.getAllUsers(connectionPool);
            request.setAttribute("usersList", usersList);

        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }

        request.getRequestDispatcher("WEB-INF/receiptsAdmin.jsp").forward(request,response);
    }
}
