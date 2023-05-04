package dat.backend.control;

import dat.backend.model.entities.Receipt;
import dat.backend.model.persistence.Facade;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "updateReceipt", value = "/updatereceipt")
public class updateReceipt extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int idReceipt = Integer.parseInt(request.getParameter("idReceipt"));
        Receipt receipt = Facade.acceptReceipt(idReceipt);
        for(Receipt r : Receipts.receiptList)
        {
            if(r.getIdReceipt() == idReceipt)
            {
                Receipts.receiptList.remove(r);
            }
        }
        Receipts.receiptList.add(receipt);
        request.setAttribute("receiptList", Receipts.receiptList);

        request.getRequestDispatcher("WEB-INF/receipts.jsp").forward(request,response);
    }
}
