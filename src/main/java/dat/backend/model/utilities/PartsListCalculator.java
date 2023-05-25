package dat.backend.model.utilities;

import dat.backend.model.entities.OrderItem;
import dat.backend.model.entities.Wood;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;
import java.util.*;

public class PartsListCalculator
{
    private static final int CARPORT_HANG = 50;
    private static final int MAX_POLE_DIST = 310;

    /**
     * Creates a list of all the orderItems needed to create a carport by calling all the methods. This is the final calculation.
     * @param  length- The full length of the carport
     * @param  width- The full width of the carport
     * @param  shedLength- The length of the shed. 0 if no shed selected
     * @param  withRoof- Whether a roof is ordered
     * @param  connectionPool
     * @throws DatabaseException
     *
     * @see #poleCalc(double, ConnectionPool)
     * @see #sternCalc(double, double, ConnectionPool)
     * @see #remCalc(double, ConnectionPool)
     * @see MetalCalculator#getWire(double, double, ConnectionPool)
     * @see #roofingCalc(double, double, ConnectionPool)
     * @see #getShed(double, double, ConnectionPool)
     * @return List of orderItems containing all the needed orderItems to make the carport
     */
    public static List<OrderItem> materialCalc(double length, double width, int shedLength, boolean withRoof, ConnectionPool connectionPool) throws DatabaseException
    {
        List<OrderItem> orderItemList = new ArrayList<>();
        List<OrderItem> rafters = calcRafter(width, length, connectionPool);

        List<OrderItem> poles = poleCalc(length, connectionPool);
        OrderItem rems = remCalc(length, connectionPool);
        List<OrderItem> sterns = sternCalc(length, width, connectionPool);
        OrderItem wires = MetalCalculator.getWire(length, width, connectionPool);

        if (withRoof)
        {
            List<OrderItem> roofing = roofingCalc(length, width, connectionPool);
            orderItemList.addAll(roofing);
        }
        if (shedLength > 0)
        {
            List<OrderItem> shed = getShed(width, shedLength, connectionPool);
            orderItemList.addAll(shed);
        }

        orderItemList.add(wires);
        orderItemList.addAll(rafters);
        orderItemList.addAll(poles);
        orderItemList.add(rems);
        orderItemList.addAll(sterns);

        return orderItemList;
    }

    /**
     * Creates a list of all the orderItems needed to create a stern.
     * @param  length- The full length of the carport
     * @param  width- The full width of the carport
     * @param  connectionPool
     * @throws DatabaseException
     * @return the orderItems needed to make the stern
     * @see Facade#getWoodByVariant(String, ConnectionPool) for the mapper method
     * @see #getOptimalItem(List, double, String, int, int)
     */
    public static List<OrderItem> sternCalc(double length, double width, ConnectionPool connectionPool) throws DatabaseException
    {
        String desc = "Sternbrædt placeres uden på tagkonstruktionen";
        List<Wood> sterns = Facade.getWoodByVariant("Stern", connectionPool);

        OrderItem lenSternItem = getOptimalItem(sterns, length, desc, 1, 2);


        OrderItem widthSternItem = getOptimalItem(sterns, width, desc, 1, 2);

        List<OrderItem> orderItems = new ArrayList<>();

        orderItems.add(lenSternItem);
        orderItems.add(widthSternItem);

        return orderItems;
    }

    /**
     * Creates a list of all the orderItems needed to create a roof
     * @param  length- The full length of the carport
     * @param  width- The full width of the carport
     * @param  connectionPool
     * @throws DatabaseException
     * @return the orderItems needed for roofing
     * @see Facade#getWoodByVariant(String, ConnectionPool) for the mapper method
     * @see MetalCalculator#getRoofingMetal(int, ConnectionPool) for the metal calculation
     * @see #getOptimalItem(List, double, String, int, int)
     */
    public static List<OrderItem> roofingCalc(double length, double width, ConnectionPool connectionPool) throws DatabaseException
    {
        String desc = "Tagplader skrues fast i spær";
        double area = length*width;
        // 250 * 250 = 62500 cm^2
        // 1 plade er 100 * 100 = 10.000 cm^2
        // 62500 / 10.000 = 6.25
        List<Wood> roofing = Facade.getWoodByVariant("Tag", connectionPool);
        Wood roof = roofing.get(0);
        int amount = (int) Math.ceil(area/10000);
        OrderItem roofOI = new OrderItem(amount, roof, desc);
        List<OrderItem> orderItemMetal = MetalCalculator.getRoofingMetal(amount , connectionPool);
        List<OrderItem> output = new ArrayList<>();

        output.add(roofOI);
        output.addAll(orderItemMetal);

        return output;
    }


    /**
     * Creates a list of all the orderItems needed for the poles.
     * @param  length- The full length of the carport
     * @param  connectionPool
     * @throws DatabaseException
     * @return List of orderItems containing metal and wood for the poles
     * @see Facade#getWoodByVariant(String, ConnectionPool) for the mapper method
     * @see MetalCalculator#getPoleMetal(int, ConnectionPool) for the metal calculations
     */
    public static List<OrderItem> poleCalc(double length, ConnectionPool connectionPool) throws DatabaseException
    {
        String desc = "Stolper graves 90 cm ned i jord";
        int amount = 4;

        double lenPoles = ((length - CARPORT_HANG *2) / MAX_POLE_DIST) + 1;
        //double widthPoles = ((width - CARPORT_HANG *2) / MAX_POLE_DIST) + 1;

        double extraLen = Math.ceil(lenPoles - 2) * 2;
        //double extraWidth = Math.ceil(widthPoles - 2) * 2;

        amount += extraLen;
        //amount += extraWidth;


        List<Wood> poleList = Facade.getWoodByVariant("Stolpe", connectionPool);
        Wood pole = poleList.get(0);

        OrderItem poleOrder = new OrderItem(amount, pole, desc);
        List<OrderItem> poleMetal = MetalCalculator.getPoleMetal(amount, connectionPool);

        List<OrderItem> output = new ArrayList<>();
        output.add(poleOrder);
        output.addAll(poleMetal);

        return output;
    }

    /**
     * Creates a list of all the orderItems needed for the rafters
     * @param  width- The full length of the carport
     * @param  length- The full width of the carport
     * @param  connectionPool
     * @throws DatabaseException
     * @return List of orderItems containing all the needed orderItems to make the rafters
     * @see Facade#getWoodByVariant(String, ConnectionPool) for the mapper method
     * @see #selectWood(List, double)
     * @see #getRafterAmount(double, int)
     * @see MetalCalculator#getRafterMetal(int, int, ConnectionPool) for the metal calculations for rafters
     * @see MetalCalculator#getSternMetal(OrderItem, ConnectionPool) (int, int, ConnectionPool) for the metal calculations for stern
     * @see #getOptimalItem(List, double, String, int, int)
     */
    public static List<OrderItem> calcRafter(double width, double length, ConnectionPool connectionPool) throws DatabaseException
    {
        String desc = "Spær placers på tværs af bygningen på tværs af remme med ca 55 cm mellemrum";
        List<Wood> woods = Facade.getWoodByVariant("Spær", connectionPool);
        List<OrderItem> output = new ArrayList<>();

        woods.sort(new Comparator<Wood>()
        {
            @Override
            public int compare(Wood s, Wood t1)
            {
                return t1.getLength() - s.getLength();
            }
        });

        Wood rafter = selectWood(woods, width);
        int raftAmountModifier = 1;

        while (rafter == null)
        {
            width = width / 2;
            raftAmountModifier = raftAmountModifier * 2;
            rafter = selectWood(woods, width);
        }

        int amount = (int) Math.floor(getRafterAmount(length, raftAmountModifier));

        OrderItem rafters = new OrderItem(amount, rafter, desc);
        output.add(rafters);
        OrderItem sternMetal = MetalCalculator.getSternMetal(rafters, connectionPool);
        output.add(sternMetal);
        output.addAll(MetalCalculator.getRafterMetal((int) Math.floor(getRafterAmount(length, 1)), rafter.getHeight(), connectionPool));

       return output;
    }

    /**
     * Creates a list of all the orderItems for the rems
     * @param  length- The full length of the carport
     * @param  connectionPool
     * @throws DatabaseException
     * @return an orderItem of the rems
     * @see Facade#getWoodByVariant(String, ConnectionPool) for the mapper method
     * @see #getOptimalItem(List, double, String, int, int)
     */
    public static OrderItem remCalc(double length, ConnectionPool connectionPool) throws DatabaseException
    {
        String desc = "Remme boltes fast på stolper langs længden af kontruktionen";

        List<Wood> woods = Facade.getWoodByVariant("Rem", connectionPool);

        return getOptimalItem(woods, length, desc, 1, 2);
    }

    /**
     * Creates a list of all the orderItems needed to create a stern.  This is the final calculation.
     * @param  width- The full width of the carport
     * @param shedLength- The length of the shed
     * @param  connectionPool
     * @throws DatabaseException
     * @return List of orderItems containing all the needed orderItems to make the shed
     * @see Facade#getWoodByVariant(String, ConnectionPool) for the mapper method
     * @see #getOptimalItem(List, double, String, int, int)
     * @see MetalCalculator#getShedMetal(OrderItem, OrderItem, ConnectionPool) for the metal calculation
     */
    public static List<OrderItem> getShed(double width, double shedLength, ConnectionPool connectionPool) throws DatabaseException
    {
        double shedWidth = (width - CARPORT_HANG * 2) / 2 ;
        double amountL;
        double amountW;
        double rafterLengthAmountL = 1;
        double rafterWidthAmountW = 1;

        List<OrderItem> itemList = new ArrayList<>();

        List<Wood> woods = Facade.getWoodByVariant("Spær", connectionPool);

        OrderItem lenBuffer = getOptimalItem(woods, shedLength, "", 1, 2);
        Wood rafterLength = (Wood) lenBuffer.getMaterial();
        rafterLengthAmountL = lenBuffer.getAmount();

        double rafterLengthWidth = rafterLength.getWidth();
        amountL = (int) (rafterLengthAmountL * Math.ceil(210 / rafterLengthWidth)); // 210 = Pole height - the buried 90. //1 * (210/40) = 5.25 = 6
        OrderItem rafterLengthWOI = new OrderItem((int) amountL, rafterLength, "Spærtræ til beklædning af skur i længden");
        itemList.add(rafterLengthWOI);

        OrderItem widthBuffer = getOptimalItem(woods, shedWidth, "", 1, 2);
        Wood rafterWidth = (Wood) widthBuffer.getMaterial();
        rafterWidthAmountW = widthBuffer.getAmount();

        double rafterWidthWidth = rafterWidth.getWidth();
        amountW = (int) (rafterWidthAmountW * Math.ceil(210 / rafterWidthWidth)); // 210 = Pole height - the buried 90. //1 * (210/40) = 5.25 = 6
        OrderItem rafterWidthWOI = new OrderItem((int) amountW, rafterWidth, "Spærtræ til beklædning af skur i bredden");
        itemList.add(rafterWidthWOI);

        int poles = 4; // Always 4 poles, three corners and one for the door.

        List<Wood> poleWoodList = Facade.getWoodByVariant("Stolpe", connectionPool);
        OrderItem polesShed = new OrderItem(poles, poleWoodList.get(0), "Stolper til skur");
        itemList.add(polesShed);
        List<OrderItem> metalOrderItem = MetalCalculator.getShedMetal(rafterLengthWOI, rafterWidthWOI, connectionPool);
        itemList.addAll(metalOrderItem);
        return itemList;
    }

    /**
     * finds the amount of rafters
     * @param  length- The full length of the carport
     * @param  modifier- The modifier defining how many sides of the carport look the same
     * @return a double describing the amount of rafters needed
     */
    private static double getRafterAmount(double length, int modifier)
    {
        double amount = Math.floor(length / 55);
        return amount * modifier;
    }

    /**
     * Creates a list of all the orderItems needed to create a stern.  This is the final calculation.
     * @param  woods- a List of the wood objects, we want to comparer
     * @param  length- The full length of the carport
     * @return the most optimal wood object for the given situation
     */
    private static Wood selectWood(List<Wood> woods, double length)
    {
        Wood buffer = null;
        for (Wood w: woods)
        {
            if (w.getLength() >= length)
            {
                buffer = w;
            }

            else
            {
                return buffer;
            }
        }
        return buffer;
    }

    /**
     * Finds the OrderItem best suited for a given situation
     * @param  woods- The List of wood we want to compare
     * @param  dist- the distance we're trying to cover. Often the full length of the carport
     * @param  desc- a string describing what the wood is made for
     * @param  initialAmount- if there's already a set amount of the given wood
     * @param  multiplier- the amount of sides that are completely alike. multiplies the amount needed for a side by the amount of sides
     * @return The optimal OrderItem for the given situation
     * @see #selectWood(List, double)
     */
    private static OrderItem getOptimalItem(List<Wood> woods, double dist, String desc, int initialAmount, int multiplier)
    {
        woods.sort(new Comparator<Wood>()
        {
            @Override
            public int compare(Wood s, Wood t1)
            {
                return t1.getLength() - s.getLength();
            }
        });

        Wood selection = selectWood(woods, dist);
        int selectionAmount = initialAmount;

        if (selection == null)
        {
            Wood buffer = null;
            double amountBuffer = 1000000;
            double wasteBuffer = 100000;


            for (Wood w : woods)
            {
                double amount = Math.ceil(dist / w.getLength());
               // double waste = dist % w.getLength();
                double waste = w.getLength() * amount - dist;

                if (waste < wasteBuffer || amount <= amountBuffer)
                {
                    amountBuffer = amount;
                    wasteBuffer = waste;
                    buffer = w;
                }
            }
            selection = buffer;
            selectionAmount = (int) Math.ceil(amountBuffer);
        }
            return new OrderItem(selectionAmount * multiplier, selection, desc);
    }
}