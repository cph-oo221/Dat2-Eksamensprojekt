package dat.backend.model.utilities;

import dat.backend.model.entities.Material;
import dat.backend.model.entities.Metal;
import dat.backend.model.entities.OrderItem;
import dat.backend.model.entities.Wood;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import java.util.ArrayList;
import java.util.List;

public class MetalCalculator
{

    public static List<OrderItem> getRafterMetal(int amount, int height, ConnectionPool connectionPool) throws DatabaseException
    {
        // 4 beslag pr spær, og 9 skruer pr beslag

        List<OrderItem> output = new ArrayList<>();
        List<Metal> screws = Facade.getMetalByVariant("Skrue", connectionPool);
        List<Metal> rfitting = Facade.getMetalByVariant("Beslag Højre", connectionPool);
        List<Metal> lfitting = Facade.getMetalByVariant("Beslag Venstre", connectionPool);

        Material screw = null;

        if (height > 50)
        {
            for (Material m: screws)
            {
                if (m.getName().contains("100mm"))
                {
                    screw = m;
                }
            }
        }

        else
        {
            for (Material m: screws)
            {
                if (m.getName().contains("50mm"))
                {
                    screw = m;
                }
            }
        }

        OrderItem rfittingOrder = new OrderItem(amount * 2, rfitting.get(0), "Spær monteres med beslag");
        OrderItem lfittingOrder = new OrderItem(amount * 2, lfitting.get(0), "Spær monteres med beslag");

        // 9 skruer til hvert beslag
        int screwAmount = (rfittingOrder.getAmount() + lfittingOrder.getAmount()) * 9;

        OrderItem screwOrder = new OrderItem(screwAmount, screw, "Beslag monteres med 3 skruer pr overflade");

        output.add(rfittingOrder);
        output.add(lfittingOrder);
        output.add(screwOrder);

        return output;
    }

    public static List<OrderItem> getRoofingMetal(int amount, ConnectionPool connectionPool) throws DatabaseException
    {
        // 12 skruer pr m^2
        List<OrderItem> output = new ArrayList<>();

        List<Metal> screws = Facade.getMetalByVariant("Skrue" , connectionPool);

        Metal screw = null;

        for(Metal m : screws)
        {
            if(m.getName().contains("50mm"))
            {
                screw = m;
            }
        }

        int screwAmount = amount * 12;
        OrderItem screwOrder = new OrderItem(screwAmount , screw , "Skruer til tagplader");

        output.add(screwOrder);

        return output;
    }

    public static OrderItem getSternMetal(OrderItem rafters , ConnectionPool connectionPool) throws DatabaseException
    {
        List<Metal> screws = Facade.getMetalByVariant("Skrue",connectionPool);
        Metal screw = null;
        for(Metal m : screws)
        {
            if(m.getName().contains("100mm"))
            {
                screw = m;
            }
        }
        //4 skruer pr. rafter
        int amount = rafters.getAmount() * 4;

        return new OrderItem(amount, screw, "skruer til montering af stern");
    }

    public static OrderItem getPoleMetal(int amount, ConnectionPool connectionPool) throws DatabaseException
    {
        List<Metal> bolts = Facade.getMetalByVariant("Bræddebolt", connectionPool);

        return new OrderItem(amount * 2, bolts.get(0), "Remme sadles ned i stolper, og fastgøre med bræddebolte");
    }
}
