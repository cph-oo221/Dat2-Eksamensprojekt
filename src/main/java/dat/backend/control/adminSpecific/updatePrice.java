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
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "updatePrice", value = "/updateprice")
public class updatePrice extends HttpServlet
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
        request.setCharacterEncoding("UTF-8");

        String newPrice = request.getParameter("pris");
        int idReceipt = Integer.parseInt(request.getParameter("idReceipt"));
        int netPrice = Integer.parseInt(request.getParameter("netPrice"));

        try
        {
            Receipt r = Facade.getReceiptById(idReceipt, connectionPool);
            int price;

            if (!newPrice.equals(""))
            {
                price = Integer.parseInt(newPrice);
            }

            else if (r.getPrice() != netPrice)
            {
                price = r.getPrice();
            }

            else
            {
                price = netPrice;
            }

            int rowsAffected = Facade.updateReceiptPrice(price, idReceipt, connectionPool);

            if (rowsAffected > 0)
            {
                List<Receipt> receiptsList = Facade.getAllReceipts(connectionPool);
                request.setAttribute("receiptsList", receiptsList);

                List<User> usersList = Facade.getAllUsers(connectionPool);
                request.setAttribute("usersList", usersList);

                request.getRequestDispatcher("WEB-INF/receiptsAdmin.jsp").forward(request, response);

            }
        } catch (DatabaseException e)
        {
            e.printStackTrace();
        }

    }
}
