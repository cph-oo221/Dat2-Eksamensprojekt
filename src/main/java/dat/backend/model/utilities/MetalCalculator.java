package dat.backend.model.utilities;

import dat.backend.model.entities.Material;
import dat.backend.model.entities.Metal;
import dat.backend.model.entities.OrderItem;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import java.util.ArrayList;
import java.util.List;

class MetalCalculator
{

    /**
     * Creates a list of all the orderItems for the rems
     * @param  amount- the amount of rafters
     * @param height- the thickness of the wood that need to be penetrated by the screws
     * @param  connectionPool
     * @throws DatabaseException
     * @return a list of orderItems containing metal and amount
     * @see Facade#getMetalByVariant(String, ConnectionPool) for the mapper method
     */
    public static List<OrderItem> getRafterMetal(int amount, int height, ConnectionPool connectionPool) throws DatabaseException
    {
        // 4 beslag pr spær, og 9 skruer pr beslag

        List<OrderItem> output = new ArrayList<>();

        //TODO færre kald til databasen via joins
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

    /**
     * Creates a list of all the orderItems of metal for the roof
     * @param  amount- The amount of roofing tiles
     * @param  connectionPool
     * @throws DatabaseException
     * @return a list of OrderItems containing metal and amount for roofing
     * @see Facade#getMetalByVariant(String, ConnectionPool) for the mapper method
     */
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

    /**
     * Creates an orderItems of metal for the sterns
     * @param  rafters- OrderItem containing the rafters and the amount of them
     * @param  connectionPool
     * @throws DatabaseException
     * @return an orderItem of the metal needed for the stern
     * @see Facade#getMetalByVariant(String, ConnectionPool) for the mapper method
     */
    static OrderItem getSternMetal(OrderItem rafters , ConnectionPool connectionPool) throws DatabaseException
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

    /**
     * Creates a list of all the orderItems of metal for the poles
     * @param  amount- The amount of poles
     * @param  connectionPool
     * @throws DatabaseException
     * @return a list of OrderItems containing metal and amount for the poles
     * @see Facade#getMetalByVariant(String, ConnectionPool) for the mapper method
     */
    static List<OrderItem> getPoleMetal(int amount, ConnectionPool connectionPool) throws DatabaseException
    {
        //TODO færre kald til databasen via joins
        List<Metal> bolts = Facade.getMetalByVariant("Bræddebolt", connectionPool);
        List<Metal> discs = Facade.getMetalByVariant("Firkantskiver", connectionPool);

        List<OrderItem> output = new ArrayList<>();
        output.add(new OrderItem(amount * 2, bolts.get(0), "Remme sadles ned i stolper, og fastgøre med bræddebolte"));
        output.add(new OrderItem(amount * 2, discs.get(0), "Skiver til fastgørelse af bolte"));

        return output;
    }

    /**
     * Creates an OrderItem with wires
     * @param  length- The length of the carport
     * @param  width- The width of the carport
     * @param  connectionPool
     * @throws DatabaseException
     * @return an orderItem containing two wires
     * @see Facade#getMetalByVariant(String, ConnectionPool) for the mapper method
     */
    public static OrderItem getWire(double length, double width, ConnectionPool connectionPool) throws DatabaseException
    {
        List<Metal> wires = Facade.getMetalByVariant("hulbånd", connectionPool);

        int amount = 0;
        Metal wire = wires.get(0);

        double hypotenuseSquared = (length*length) + (width*width);
        double hypotenuse = Math.sqrt(hypotenuseSquared);
        double bothHypotenuse = hypotenuse*2;

        while(bothHypotenuse > 0)
        {
            amount++;
            bothHypotenuse -= 1000;
        }

        OrderItem wireOI = new OrderItem(amount, wire, "Hulbånd monteres diagonalt under taget");
        return wireOI;
    }

    /**
     * Creates a list of all the orderItems of metal for the shed
     * @param  rafterLengthWOI- The OrderItem for the rafters needed to cover the length of the shed
     * @param  rafterWidthWOI-  The OrderItem for the rafters needed to cover the width of the shed
     * @param  connectionPool
     * @throws DatabaseException
     * @return a list of OrderItems containing metal and amount for the shed
     * @see Facade#getMetalById(int, ConnectionPool) for the mapper method
     */
    public static List<OrderItem> getShedMetal(OrderItem rafterLengthWOI, OrderItem rafterWidthWOI, ConnectionPool connectionPool) throws DatabaseException
    {
        List<OrderItem> output = new ArrayList<>();

        int screwAmount = (rafterLengthWOI.getAmount() + rafterWidthWOI.getAmount()) * 4; //4 skruer i hvert brædt

        Metal longScrew = Facade.getMetalById(1, connectionPool);

        OrderItem longScrews = new OrderItem(screwAmount, longScrew, "Skruer til beklædning ");

        //Alle skure har én dør med to hængsler og et håndtag
        Metal handle = Facade.getMetalById(6, connectionPool);
        Metal hinge = Facade.getMetalById(7, connectionPool);
        OrderItem handleOi = new OrderItem(1, handle, "Håndtag til skurets dør");
        OrderItem hingeOi = new OrderItem(2, hinge, "Hængsler til skurets dør");

        output.add(longScrews);
        output.add(handleOi);
        output.add(hingeOi);

        return output;
    }
}