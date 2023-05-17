package dat.backend.control.shared;

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
import java.util.Collections;

@WebServlet(name = "Receipts", value = "/receipts")
public class Receipts extends HttpServlet
{
    static ArrayList<Receipt> receiptList;

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        receiptList = Facade.getReceiptsByIdUser(user.getIdUser(), connectionPool);
        Collections.reverse(receiptList);

        request.setAttribute("receiptList", receiptList);
        request.getRequestDispatcher("WEB-INF/receipts.jsp").forward(request,response);
    }
}
