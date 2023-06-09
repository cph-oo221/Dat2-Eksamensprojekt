package dat.backend.control.userSpecific;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.entities.*;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;
import dat.backend.model.utilities.PartsListCalculator;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Makeorder", value = "/makeorder")
public class Makeorder extends HttpServlet
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
        double width = Double.parseDouble(request.getParameter("width"));
        double length = Double.parseDouble(request.getParameter("length"));
        int shedLength = Integer.parseInt(request.getParameter("shedLength"));
        boolean withRoof = Boolean.parseBoolean(request.getParameter("withRoof"));

        if (shedLength > length)
        {
            request.setAttribute("errorMSG", "Skurets længde kan ikke være længere end carportens længde!");
            request.getRequestDispatcher("WEB-INF/orderForm.jsp").forward(request, response);
        }

        try
        {
            List<OrderItem> orderItemList = PartsListCalculator.materialCalc(length, width, shedLength, withRoof, connectionPool);

            String comment = request.getParameter("comment");
            User user = (User) request.getSession().getAttribute("user");

            int receiptId = Facade.createReceipt(user.getIdUser(), width, length, comment, connectionPool);
            int orderId = Facade.createOrder(receiptId, orderItemList, connectionPool);

            int price = 0;
            for(OrderItem oi : orderItemList)
            {
                price+= (oi.getMaterial().getPrice())*oi.getAmount();
            }

            Facade.updateReceiptPrice(price, receiptId, connectionPool);

            List<Receipt> receiptList = Facade.getReceiptsByIdUser(user.getIdUser(), connectionPool);
            request.setAttribute("receiptList", receiptList);
            request.getRequestDispatcher("WEB-INF/receipts.jsp").forward(request, response);
        }
        catch (DatabaseException e)
        {
            request.setAttribute("errormessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
