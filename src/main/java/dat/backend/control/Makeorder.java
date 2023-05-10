package dat.backend.control;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.entities.PartsListCalculator;
import dat.backend.model.entities.Receipt;
import dat.backend.model.entities.User;
import dat.backend.model.entities.WoodOrderItem;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
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
        int width = Integer.parseInt(request.getParameter("width"));
        int length = Integer.parseInt(request.getParameter("length"));
        try
        {
            WoodOrderItem rafters = PartsListCalculator.calcRafter(width, length);
            WoodOrderItem roofing = PartsListCalculator.roofingCalc(length, width);
            WoodOrderItem poles = PartsListCalculator.poleCalc(length, width);
            WoodOrderItem rems = PartsListCalculator.remCalc(length, width);
            List<WoodOrderItem> woodOrderItemList = new ArrayList<>();
            woodOrderItemList.add(rafters);
            woodOrderItemList.add(roofing);
            woodOrderItemList.add(poles);
            woodOrderItemList.add(rems);

            String comment = request.getParameter("comment");
            User user = (User) request.getSession().getAttribute("user");

            int receiptId = Facade.createReceipt(user.getIdUser(), width, length, comment, connectionPool);
            int orderId = Facade.createOrder(receiptId, woodOrderItemList, connectionPool);

            HttpSession session = request.getSession();
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
