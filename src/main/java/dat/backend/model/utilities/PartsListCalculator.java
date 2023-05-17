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


    public static List<OrderItem> materialCalc(double length, double width, int shedLength, boolean withRoof, ConnectionPool connectionPool) throws DatabaseException
    {
        List<OrderItem> orderItemList = new ArrayList<>();
        // TODO: TEST MY METALCALC
        List<OrderItem> rafters = calcRafter(width, length, connectionPool);

        List<OrderItem> poles = poleCalc(length, width, connectionPool);
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

    public static List<OrderItem> sternCalc(double length, double width, ConnectionPool connectionPool) throws DatabaseException
    {
        String desc = "Sternbrædt placeres uden på tagkonstruktionen";
        List<Wood> sterns = Facade.getWoodByVariant("Stern", connectionPool);
         int lenSternAmount = 2;
         int widthSternAmount = 2;

        OrderItem lenSternItem = getOptimalItem(sterns, length, desc, 2, 2);


        OrderItem widthSternItem = getOptimalItem(sterns, width, desc, 2, 2);

        List<OrderItem> orderItems = new ArrayList<>();

        orderItems.add(lenSternItem);
        orderItems.add(widthSternItem);

        return orderItems;
    }

    private static List<OrderItem> roofingCalc(double length, double width, ConnectionPool connectionPool) throws DatabaseException
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

        output.addAll(orderItemMetal);
        output.add(roofOI);

        return output;
    }

    public static List<OrderItem> poleCalc(double length, double width, ConnectionPool connectionPool) throws DatabaseException
    {
        String desc = "Stolper graves 90 cm ned i jord";
        int amount = 4;

        double lenPoles = ((length - CARPORT_HANG *2) / MAX_POLE_DIST) + 1;
        double widthPoles = ((width - CARPORT_HANG *2) / MAX_POLE_DIST) + 1;

        double extraLen = Math.ceil(lenPoles - 2) * 2;
        double extraWidth = Math.ceil(widthPoles - 2) * 2;

        amount += extraLen;
        amount += extraWidth;


        List<Wood> poleList = Facade.getWoodByVariant("Stolpe", connectionPool);
        Wood pole = poleList.get(0);

        OrderItem poleOrder = new OrderItem(amount, pole, desc);
        List<OrderItem> poleMetal = MetalCalculator.getPoleMetal(amount, connectionPool);

        List<OrderItem> output = new ArrayList<>();
        output.add(poleOrder);
        output.addAll(poleMetal);

        return output;
    }

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

    private static OrderItem remCalc(double length, ConnectionPool connectionPool) throws DatabaseException
    {
        String desc = "Remme boltes fast på stolper langs længden af kontruktionen";
        int remAmount = 2;

        List<Wood> woods = Facade.getWoodByVariant("Rem", connectionPool);

        return getOptimalItem(woods, length, desc, 2, 2);
    }

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
        amountL = (int) (rafterLengthAmountL * Math.ceil(210 / rafterLengthWidth)); // 210 = Pole height - the buried 90. //1 * (210/55) = 3.82 = 4
        OrderItem rafterLengthWOI = new OrderItem((int) amountL, rafterLength, "Spærtræ til beklædning af skur i længden");
        itemList.add(rafterLengthWOI);

        OrderItem widthBuffer = getOptimalItem(woods, shedWidth, "", 1, 2);
        Wood rafterWidth = (Wood) widthBuffer.getMaterial();
        rafterWidthAmountW = widthBuffer.getAmount();

        double rafterWidthWidth = rafterWidth.getWidth();
        amountW = (int) (rafterWidthAmountW * Math.ceil(210 / rafterWidthWidth)); // 210 = Pole height - the buried 90. //1 * (210/55) = 3.82 = 4
        OrderItem rafterWidthWOI = new OrderItem((int) amountW, rafterWidth, "Spærtræ til beklædning af skur i bredden");
        itemList.add(rafterWidthWOI);

        int poles;
        if (shedWidth > 310)
        {
            poles = 3;
        }
        else
        {
            poles = 4;
        }

        List<Wood> poleWoodList = Facade.getWoodByVariant("Stolpe", connectionPool);
        OrderItem polesShed = new OrderItem(poles, poleWoodList.get(0), "Stolper til skur");
        itemList.add(polesShed);
        List<OrderItem> metalOrderItem = MetalCalculator.getShedMetal(rafterLengthWOI, rafterWidthWOI, polesShed, connectionPool);
        itemList.addAll(metalOrderItem);
        return itemList;
    }

    private static double getRafterAmount(double length, int modifier)
    {
        return (length / 55) * modifier;
    }

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
                double amount = dist / w.getLength();
                double waste = dist % w.getLength();
                waste = w.getLength() - waste;

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