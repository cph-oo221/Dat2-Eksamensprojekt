package dat.backend.control.userSpecific;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "Orderpage", value = "/orderpage")
public class Orderpage extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        if((request.getParameter("defaultSet")!= null))
        {
            boolean defaultSet = Boolean.parseBoolean(request.getParameter("defaultSet"));
            int defaultWidth = Integer.parseInt((request.getParameter("defaultWidth")));
            int defaultLength = Integer.parseInt((request.getParameter("defaultLength")));
            boolean defaultRoof = Boolean.parseBoolean(request.getParameter("defaultRoof"));
            String defaultRoofString;
            if (defaultRoof)
            {
                defaultRoofString = "Trapeztag";
            }
            else
            {
                defaultRoofString = "Uden tagplader";
            }
            request.setAttribute("defaultRoofString", defaultRoofString);
            request.setAttribute("defaultRoof", defaultRoof);
            request.setAttribute("defaultSet", defaultSet);
            request.setAttribute("defaultWidth", defaultWidth);
            request.setAttribute("defaultLength", defaultLength);
        }

        request.getRequestDispatcher("WEB-INF/orderForm.jsp").forward(request, response);
    }
}
