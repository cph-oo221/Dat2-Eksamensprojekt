package dat.backend.control;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.entities.Wood;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "AdminAction", value = "/adminaction")
public class AdminAction extends HttpServlet
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
        request.setCharacterEncoding("UTF-8"); // NB: Important if action is search or else danish letters will not be found.
        int action = Integer.parseInt(request.getParameter("action"));

        if (action == 1) // SORT
        {
            int sortOption = Integer.parseInt(request.getParameter("sortOption")); // Can't be outside if or a numberFormatException will be thrown

            if (sortOption == 1) // SORT WOOD ID
            {
                try
                {
                    List<Wood> woodList = Facade.getAllWood(connectionPool);
                    Collections.sort(woodList, Comparator.comparing(Wood::getIdWood));
                    request.setAttribute("woodList", woodList);
                }
                catch (DatabaseException e)
                {
                    e.printStackTrace();
                }
            }

            if (sortOption == 2)// SORT NAME
            {
                try
                {
                    List<Wood> woodList = Facade.getAllWood(connectionPool);
                    Collections.sort(woodList, Comparator.comparing(Wood::getName));
                    request.setAttribute("woodList", woodList);
                }
                catch (DatabaseException e)
                {
                    e.printStackTrace();
                }
            }
        }

        if (action == 2) // SEARCH
        {
            String search = request.getParameter("search").toLowerCase();
            List<Wood> searchList = new ArrayList<>();

            try
            {
                List<Wood> woodList = Facade.getAllWood(connectionPool);

                for (Wood wood : woodList)
                {
                    String woodName = wood.getName().toLowerCase();

                    if (woodName.contains(search))
                    {
                        searchList.add(wood);
                    }
                }
                request.setAttribute("woodList", searchList);
            }
            catch (DatabaseException e)
            {
                e.printStackTrace();
            }
        }

        if (action == 3) // RESET
        {
            try
            {
                List<Wood> woodList = Facade.getAllWood(connectionPool);
                request.setAttribute("woodList", woodList);
            }
            catch (DatabaseException e)
            {
                e.printStackTrace();
            }
        }

        if (action == 4)
        {
            int idWood = Integer.parseInt(request.getParameter("idWood"));

            try
            {
                Facade.deleteWood(idWood, connectionPool);
                List<Wood> woodList = Facade.getAllWood(connectionPool);
                request.setAttribute("woodList", woodList);
            }
            catch (DatabaseException e)
            {
                e.printStackTrace();
            }
        }
        request.getRequestDispatcher("WEB-INF/editItems.jsp").forward(request, response);
    }
}
