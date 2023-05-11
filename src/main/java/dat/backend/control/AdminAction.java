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

        if (action == 4) // DELETE Wood
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

        if(action == 5) // ADD NEW WOOD
        {
            int length = Integer.parseInt(request.getParameter("length"));
            int width = Integer.parseInt(request.getParameter("width"));
            int height = Integer.parseInt(request.getParameter("height"));
            int price = Integer.parseInt(request.getParameter("price"));
            String name = request.getParameter("name");
            String unit = request.getParameter("unit");
            String variant = request.getParameter("variant");

            if(length <= 0 || width <= 0 || height <= 0 || name.isEmpty() || unit.isEmpty() || price <= 0 || variant.isEmpty())
            {
                String msgError = "En eller flere parametre er tomme eller nul i Tilføje nyt træ";
                request.setAttribute("msgError", msgError);
                try
                {
                    List<Wood> woodList = Facade.getAllWood(connectionPool);
                    request.setAttribute("woodList", woodList);
                }
                catch (DatabaseException e)
                {
                    e.printStackTrace();
                }
                request.getRequestDispatcher("WEB-INF/editItems.jsp").forward(request, response);
            }

            try
            {
                Wood newWood = Facade.createWood(length, width, height, name, unit, price, variant, connectionPool);
                request.setAttribute("newWood", newWood);
                List<Wood> woodList = Facade.getAllWood(connectionPool);
                request.setAttribute("woodList", woodList);
            }
            catch (DatabaseException e)
            {
                e.printStackTrace();
            }
        }

        if(action == 6) // CHANGE PRICE ON WOOD
        {
            int idWood = Integer.parseInt(request.getParameter("idWood"));
            int newPrice = Integer.parseInt(request.getParameter("newPrice"));

            if(idWood <= 0 || newPrice <= 0)
            {
                String msgError = "En eller flere parametre er tomme eller nul i Ændre Pris!";
                try
                {
                    List<Wood> woodList = Facade.getAllWood(connectionPool);
                    request.setAttribute("woodList", woodList);
                }
                catch (DatabaseException e)
                {
                    e.printStackTrace();
                }
                request.setAttribute("msgError", msgError);
                request.getRequestDispatcher("WEB-INF/editItems.jsp").forward(request, response);
            }

            try
            {
                Facade.updateWoodPrice(idWood, newPrice, connectionPool);
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
