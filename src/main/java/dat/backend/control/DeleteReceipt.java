package dat.backend.control;

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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "deletereceipt", value = "/deletereceipt")
public class DeleteReceipt extends HttpServlet
{
    ConnectionPool connectionPool;

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int idReceipt = Integer.parseInt(request.getParameter("idReceipt"));

        int idUser = Integer.parseInt(request.getParameter("idUser"));

        if(user.getIdUser() != idUser) // delete receipt from admin panel
        {

            try
            {
                List<Receipt> receiptsList = Facade.getAllReceipts(connectionPool);
                request.setAttribute("receiptsList", receiptsList);

                List<User> usersList = Facade.getAllUsers(connectionPool);
                request.setAttribute("usersList", usersList);

                Facade.deleteReceipt(idReceipt, connectionPool);

            }
            catch (DatabaseException e)
            {
                e.printStackTrace();
            }

            request.getRequestDispatcher("WEB-INF/receiptsAdmin.jsp").forward(request,response);
        }
        else
        {
            Facade.deleteReceipt(idReceipt, connectionPool);
            ArrayList<Receipt> receiptList = Facade.getReceiptsByIdUser(user.getIdUser(), connectionPool);
            request.setAttribute("receiptList", receiptList);

            request.getRequestDispatcher("WEB-INF/receipts.jsp").forward(request,response);
        }
    }
}
