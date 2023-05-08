package dat.backend.control;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.entities.Receipt;
import dat.backend.model.entities.User;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "updateReceipt", value = "/updatereceipt")
public class UpdateReceipt extends HttpServlet
{
    ConnectionPool connectionPool = ApplicationStart.getConnectionPool();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int idReceipt = Integer.parseInt(request.getParameter("idReceipt"));
        Facade.acceptReceipt(idReceipt, connectionPool);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ArrayList<Receipt> receiptList = Facade.getReceiptsByIdUser(user.getIdUser(), connectionPool);
        request.setAttribute("receiptList", receiptList);


        request.getRequestDispatcher("WEB-INF/receipts.jsp").forward(request,response);
    }
}
