package dat.backend.control;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.entities.WoodOrderItem;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ItemsList", value = "/itemslist")
public class ItemsList extends HttpServlet
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
        int idReceipt = Integer.parseInt(request.getParameter("idReceipt"));

        try
        {
            List<WoodOrderItem> itemList = Facade.getWoodOrderItemsByRecieptId(idReceipt, connectionPool);

            int totalPrice = 0;

            for (WoodOrderItem o:itemList)
            {
                totalPrice += o.wood.getPrice() * o.amount;
            }

            request.setAttribute("idReceipt", idReceipt);
            request.setAttribute("totalPrice", totalPrice);
            request.setAttribute("itemList", itemList);
            request.getRequestDispatcher("WEB-INF/itemsList.jsp").forward(request, response);
        }

        catch (DatabaseException e)
        {
            request.setAttribute("errormessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }


        request.getRequestDispatcher("WEB-INF/itemsList.jsp").forward(request, response);
    }
}
