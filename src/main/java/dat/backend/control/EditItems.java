package dat.backend.control;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.entities.Metal;
import dat.backend.model.entities.Wood;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "EditItems", value = "/edititems")
public class EditItems extends HttpServlet
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
        try
        {
            List<Wood> woodList = Facade.getAllWood(connectionPool);
            List<Metal> metalList = Facade.getAllMetal(connectionPool);
            request.setAttribute("woodList", woodList);
            request.setAttribute("metalList", metalList);
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }

        request.getRequestDispatcher("WEB-INF/editItems.jsp").forward(request,response);
    }
}
