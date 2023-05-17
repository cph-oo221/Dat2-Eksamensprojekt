package dat.backend.control.adminSpecific;

import dat.backend.model.entities.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "RedirectAdminPanel", value = "/redirectadminpanel")
public class RedirectAdminPanel extends HttpServlet
{
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

        if(user.getRole().equalsIgnoreCase("Admin"))
        {
            request.getRequestDispatcher("WEB-INF/adminPanel.jsp").forward(request,response);
        }
        else
        {
            request.getRequestDispatcher("WEB-INF/userPage.jsp").forward(request,response);
        }
    }
}
