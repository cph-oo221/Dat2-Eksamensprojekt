package dat.backend.control;

import dat.backend.model.entities.Receipt;
import dat.backend.model.persistence.Facade;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "deletereceipt", value = "/deletereceipt")
public class DeleteReceipt extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int idReceipt = Integer.parseInt(request.getParameter("idReceipt"));
        Facade.deleteReceipt(idReceipt);
        for(Receipt r : Receipts.receiptList)
        {
            if(r.getIdReceipt() == idReceipt)
            {
                Receipts.receiptList.remove(r);
            }
        }
        request.setAttribute("receiptList", Receipts.receiptList);

        request.getRequestDispatcher("WEB-INF/receipts.jsp").forward(request,response);
    }
}
