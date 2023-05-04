package dat.backend.control;

import com.mysql.cj.Session;
import dat.backend.model.config.ApplicationStart;
import dat.backend.model.entities.Receipt;
import dat.backend.model.entities.User;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "Receipts", value = "/receipts")
public class Receipts extends HttpServlet
{
    ConnectionPool connectionPool = ApplicationStart.getConnectionPool();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ArrayList<Receipt> receiptList = Facade.getReceiptsByIdUser(user.getIdUser(), connectionPool);
        request.setAttribute("receiptList", receiptList);
        request.getRequestDispatcher("WEB-INF/receipts.jsp").forward(request,response);
    }
}
