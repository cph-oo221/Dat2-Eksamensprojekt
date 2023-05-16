package dat.backend.control.shared;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.entities.*;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
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
            Receipt r = Facade.getReceiptById(idReceipt, connectionPool);
            List<OrderItem> woodList = Facade.getWoodOrderItemsByRecieptId(idReceipt, connectionPool);
            List<OrderItem> metalList = Facade.getMetalOrderItemsByReceiptId(idReceipt, connectionPool);

           /* List<OrderItem> itemList = new ArrayList<>();
            itemList.addAll(woodList);
            itemList.addAll(metalList);*/
//            List<OrderItem> orderItemList = new ArrayList<>();
//            orderItemList.addAll(itemList);
//            orderItemList.addAll(metalList);

            int totalPrice = 0;
            int netPrice = 0;

            for (OrderItem o : woodList)
            {
                netPrice += o.getMaterial().getPrice() * o.getAmount();
            }

            for (OrderItem o : metalList)
            {
                netPrice += o.getMaterial().getPrice() * o.getAmount();
            }


            if (r.getPrice() != 0)
            {
                totalPrice = r.getPrice();
            }

            else
            {
                totalPrice = netPrice;
            }


            request.setAttribute("idReceipt", idReceipt);
            request.setAttribute("totalPrice", totalPrice);
            request.setAttribute("netPrice", netPrice);
            request.setAttribute("woodList", woodList);
            request.setAttribute("metalList", metalList);
            // request.setAttribute("itemList", itemList);
            request.setAttribute("orderState", r.getOrderState());
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
