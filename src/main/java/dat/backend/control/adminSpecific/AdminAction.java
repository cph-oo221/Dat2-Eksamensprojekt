package dat.backend.control.adminSpecific;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8"); // NB: Important if action is search or else danish letters will not be found.
        int action = Integer.parseInt(request.getParameter("action"));
        if (action == 1) actionSort(request); // SORT
        if (action == 2) actionSearch(request); // SEARCH
        if (action == 3) actionReset(request); // RESET
        if (action == 4) actionDelete(request, response); // DELETE WOOD OR METAL
        if (action == 5) actionAddNewWood(request, response); // ADD NEW WOOD
        if (action == 6) actionChangePriceOfWood(request, response); // CHANGE PRICE FOR WOOD
        if (action == 7) actionChangePriceOfMetal(request, response); // CHANGE PRICE FOR METAL
        if (action == 8) actionAddNewMetal(request, response); // ADD NEW METAL
        request.getRequestDispatcher("WEB-INF/editItems.jsp").forward(request, response);
    }

    // ACTION 1. SORT
    private void actionSort(HttpServletRequest request)
    {
        int sortOption = Integer.parseInt(request.getParameter("sortOption"));

        try
        {
            if (sortOption == 1) // SORT ID
            {

                List<Wood> woodList = Facade.getAllWood(connectionPool);
                List<Metal> metalList = Facade.getAllMetal(connectionPool);
                Collections.sort(woodList, Comparator.comparing(Wood::getIdWood));
                Collections.sort(metalList, Comparator.comparing(Metal::getIdMetal));
                request.setAttribute("metalList", metalList);
                request.setAttribute("woodList", woodList);
            }

            if (sortOption == 2) // SORT NAME
            {

                List<Wood> woodList = Facade.getAllWood(connectionPool);
                List<Metal> metalList = Facade.getAllMetal(connectionPool);
                Collections.sort(woodList, Comparator.comparing(Wood::getName));
                Collections.sort(metalList, Comparator.comparing(Metal::getName));
                request.setAttribute("woodList", woodList);
                request.setAttribute("metalList", metalList);
            }

            if (sortOption == 3) // SORT VARIANT
            {

                List<Wood> woodList = Facade.getAllWood(connectionPool);
                List<Metal> metalList = Facade.getAllMetal(connectionPool);
                Collections.sort(woodList, Comparator.comparing(Wood::getVariant));
                Collections.sort(metalList, Comparator.comparing(Metal::getVariant));
                request.setAttribute("woodList", woodList);
                request.setAttribute("metalList", metalList);
            }
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }
    }

    // ACTION 2. SEARCH
    private void actionSearch(HttpServletRequest request)
    {
        String search = request.getParameter("search").toLowerCase();
        List<Wood> searchListWood = new ArrayList<>();
        List<Metal> searchListMetal = new ArrayList<>();

        try
        {
            List<Wood> woodList = Facade.getAllWood(connectionPool);
            for (Wood wood : woodList)
            {
                String woodName = wood.getName().toLowerCase();
                if (woodName.contains(search))
                {
                    searchListWood.add(wood);
                }
            }

            List<Metal> metalList = Facade.getAllMetal(connectionPool);
            for (Metal metal : metalList)
            {
                String metalName = metal.getName().toLowerCase();
                if (metalName.contains(search))
                {
                    searchListMetal.add(metal);
                }
            }

            request.setAttribute("metalList", searchListMetal);
            request.setAttribute("woodList", searchListWood);
        } catch (DatabaseException e)
        {
            e.printStackTrace();
        }
    }

    // ACTION 3. RESET
    private void actionReset(HttpServletRequest request)
    {
        getMetalAndWoodList(request);
    }

    //  ACTION 4. DELETE WOOD OR METAL
    private void actionDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int deleteOption = Integer.parseInt(request.getParameter("deleteOption"));

        if (deleteOption == 1)
        {
            int idWood = -1;
            if (!isRequestEmpty(request, response, request.getParameter("idWood")))
            {
                idWood = Integer.parseInt(request.getParameter("idWood"));
            }
            woodDoNotExist(idWood, request, response);
            try
            {
                Facade.deleteWood(idWood, connectionPool);
                getMetalAndWoodList(request);
            } catch (DatabaseException e)
            {
                e.printStackTrace();
            }
        }

        if (deleteOption == 2)
        {
            int idMetal = -1;
            if (!isRequestEmpty(request, response, request.getParameter("idMetal")))
            {
                idMetal = Integer.parseInt(request.getParameter("idMetal"));
            }
            metalDoNotExist(idMetal, request, response);
            try
            {
                Facade.deleteMetal(idMetal, connectionPool);
                getMetalAndWoodList(request);
            } catch (DatabaseException e)
            {
                e.printStackTrace();
            }
        }
    }

    // ACTION 5. ADD NEW WOOD
    private void actionAddNewWood(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int length = -1;
        int width = -1;
        int height = -1;
        int price = -1;
        String name = request.getParameter("name");
        String unit = request.getParameter("unit");
        String variant = request.getParameter("variant");

        if (!isRequestEmpty(request, response,
                request.getParameter("length"), request.getParameter("width"),
                request.getParameter("height"), request.getParameter("price")))
        {
            length = Integer.parseInt(request.getParameter("length"));
            width = Integer.parseInt(request.getParameter("width"));
            height = Integer.parseInt(request.getParameter("height"));
            price = Integer.parseInt(request.getParameter("price"));
        }

        if (length <= 0 || width <= 0 || height <= 0 || name.isEmpty() || unit.isEmpty() || price <= 0 || variant.isEmpty())
        {
            String msgError = "En eller flere parametre er tomme eller nul i Tilføje nyt træ";
            request.setAttribute("msgError", msgError);
            getMetalAndWoodList(request);
            request.getRequestDispatcher("WEB-INF/editItems.jsp").forward(request, response);
        }

        try
        {
            Wood product = Facade.createWood(length, width, height, name, unit, price, variant, connectionPool);
            request.setAttribute("product", product);
            getMetalAndWoodList(request);
        } catch (DatabaseException e)
        {
            e.printStackTrace();
        }
    }

    // ACTION 8. ADD NEW METAL
    private void actionAddNewMetal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String metalName = request.getParameter("metalName");
        int metalPrice = -1;
        String metalUnit = request.getParameter("metalUnit");
        String metalVariant = request.getParameter("metalVariant");

        if (!isRequestEmpty(request, response, request.getParameter("metalPrice")))
        {
            metalPrice = Integer.parseInt(request.getParameter("metalPrice"));
        }

        if (metalPrice <= 0 || metalName.isEmpty() || metalUnit.isEmpty() || metalVariant.isEmpty())
        {
            String msgError = "En eller flere parametre er tomme eller nul i Tilføje nyt metal";
            request.setAttribute("msgError", msgError);
            getMetalAndWoodList(request);
            request.getRequestDispatcher("WEB-INF/editItems.jsp").forward(request, response);
        }

        try
        {
            Metal product = Facade.createMetal(metalName, metalPrice, metalUnit, metalVariant, connectionPool);
            request.setAttribute("product", product);
            List<Metal> metalList = Facade.getAllMetal(connectionPool);
            request.setAttribute("metalList", metalList);
            List<Wood> woodList = Facade.getAllWood(connectionPool);
            request.setAttribute("woodList", woodList);
        } catch (DatabaseException e)
        {
            e.printStackTrace();
        }
    }

    // ACTION 6. CHANGE PRICE ON WOOD
    private void actionChangePriceOfWood(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int idWood = -1;
        int newPrice = -1;

        if (!isRequestEmpty(request, response, request.getParameter("idWood"), request.getParameter("newPrice")))
        {
            idWood = Integer.parseInt(request.getParameter("idWood"));
            newPrice = Integer.parseInt(request.getParameter("newPrice"));
        }

        woodDoNotExist(idWood, request, response);

        if (idWood <= 0 || newPrice <= 0)
        {
            String msgError = "En eller flere parametre er tomme eller nul i Ændre Pris!";
            getMetalAndWoodList(request);
            request.setAttribute("msgError", msgError);
            request.getRequestDispatcher("WEB-INF/editItems.jsp").forward(request, response);
        }

        try
        {
            Facade.updateWoodPrice(idWood, newPrice, connectionPool);
            getMetalAndWoodList(request);
        } catch (DatabaseException e)
        {
            e.printStackTrace();
        }
    }

    // ACTION 7. CHANGE PRICE ON METAL
    private void actionChangePriceOfMetal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int idMetal = -1;
        int newPrice = -1;

        if (!isRequestEmpty(request, response, request.getParameter("idMetal"), request.getParameter("newPriceMetal")))
        {
            idMetal = Integer.parseInt(request.getParameter("idMetal"));
            newPrice = Integer.parseInt(request.getParameter("newPriceMetal"));
        }

        metalDoNotExist(idMetal, request, response);

        if (idMetal <= 0 || newPrice <= 0)
        {
            String msgError = "En eller flere parametre er tomme eller nul i Ændre Pris!";
            getMetalAndWoodList(request);
            request.setAttribute("msgError", msgError);
            request.getRequestDispatcher("WEB-INF/editItems.jsp").forward(request, response);
        }

        try
        {
            Facade.updateMetalPrice(idMetal, newPrice, connectionPool);
            getMetalAndWoodList(request);
        } catch (DatabaseException e)
        {
            e.printStackTrace();
        }
    }

    // Get all the wood and metal from the database, and insert them into a list of wood and metal
    private void getMetalAndWoodList(HttpServletRequest request)
    {
        try
        {
            List<Wood> woodList = Facade.getAllWood(connectionPool);
            List<Metal> metalList = Facade.getAllMetal(connectionPool);
            request.setAttribute("woodList", woodList);
            request.setAttribute("metalList", metalList);
        } catch (DatabaseException e)
        {
            e.printStackTrace();
        }
    }

    // Checks if wood exist
    private void woodDoNotExist(int idWood, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            if (Facade.getWoodById(idWood, connectionPool) == null)
            {
                getMetalAndWoodList(request);
                String msgError = "Wood ID: " + idWood + " findes ikke på varelaget!";
                request.setAttribute("msgError", msgError);
                request.getRequestDispatcher("WEB-INF/editItems.jsp").forward(request, response);
            }
        } catch (DatabaseException e)
        {
            e.printStackTrace();
        }
    }

    // Checks if metal exist
    private void metalDoNotExist(int idMetal, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            if (Facade.getMetalById(idMetal, connectionPool) == null)
            {
                getMetalAndWoodList(request);
                String msgError = "Metal ID: " + idMetal + " findes ikke på varelaget!";
                request.setAttribute("msgError", msgError);
                request.getRequestDispatcher("WEB-INF/editItems.jsp").forward(request, response);
            }
        } catch (DatabaseException e)
        {
            e.printStackTrace();
        }
    }

    // Checks if request.getParameter is empty, this provinds a NumberFormatException if a form is submitted without datainput
    private boolean isRequestEmpty(HttpServletRequest request, HttpServletResponse response, String... strs) throws ServletException, IOException
    {
        for (String str : strs)
        {
            if (str.isEmpty())
            {
                getMetalAndWoodList(request);
                String msgError = "Et eller flere parametre er tomme!";
                request.setAttribute("msgError", msgError);
                request.getRequestDispatcher("WEB-INF/editItems.jsp").forward(request, response);
                return true;
            }
        }
        return false;
    }
}