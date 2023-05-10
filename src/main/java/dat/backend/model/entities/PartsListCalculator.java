package dat.backend.model.entities;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import java.util.*;

public class PartsListCalculator
{
    public static ConnectionPool connectionPool = ApplicationStart.getConnectionPool();
    private static final int CARPORT_HANG = 50;
    private static final int MAX_POLE_DIST = 310;




    public static List<WoodOrderItem> finalCalc(double length, double width) throws DatabaseException
    {
        WoodOrderItem rafters = calcRafter(width, length);
        WoodOrderItem roofing = roofingCalc(length, width);
        WoodOrderItem poles = poleCalc(length, width);
        WoodOrderItem rems = remCalc(length, width);
        List<WoodOrderItem> woodOrderItemList = new ArrayList<>();
        woodOrderItemList.add(rafters);
        woodOrderItemList.add(roofing);
        woodOrderItemList.add(poles);
        woodOrderItemList.add(rems);
        return woodOrderItemList;
    }


    private static WoodOrderItem roofingCalc(double length, double width) throws DatabaseException
    {
        String desc = "Tagplader skrues fast i spær";
        double area = length*width;
        // 250 * 250 = 62500 cm^2
        // 1 plade er 100 * 100 = 10.000 cm^2
        // 62500 / 10.000 = 6.25
        List<Wood> roofing = Facade.getWoodByVariant("Tag", connectionPool);
        Wood roof = roofing.get(0);
        int amount = (int) Math.ceil(area/10000);
        return new WoodOrderItem(amount, roof, desc);
    }


    private static WoodOrderItem poleCalc(double length, double width) throws DatabaseException
    {
        String desc = "Stolper graves 90 cm ned i jord";
        int poles = 4;
        poles += Math.floor((((length - (CARPORT_HANG *2) ) / MAX_POLE_DIST) ) * 2);
        poles += Math.floor((((width - (CARPORT_HANG *2) ) / MAX_POLE_DIST) ) * 2);
        List<Wood> poleList = Facade.getWoodByVariant("Stolpe", connectionPool);
        Wood pole = poleList.get(0);
        return new WoodOrderItem(poles, pole, desc);
    }

    private static WoodOrderItem calcRafter(double width, double length) throws DatabaseException
    {
        String desc = "Spær placers på tværs af bygningen på tværs af remme med ca 55 cm mellemrum";
        List<Wood> woods = Facade.getWoodByVariant("Spær", connectionPool);

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

        return new WoodOrderItem(amount, rafter, desc);
    }

    private static WoodOrderItem remCalc(double length, double width) throws DatabaseException
    {
        String desc = "Remme boltes fast på stolper langs længden af kontruktionen";
        int remAmount = 2;

        List<Wood> woods = Facade.getWoodByVariant("Rem", connectionPool);

        woods.sort(new Comparator<Wood>()
        {
            @Override
            public int compare(Wood s, Wood t1)
            {
                return t1.getLength() - s.getLength();
            }
        });

        Wood rem = selectWood(woods, length);

        if (rem == null)
        {
            Wood buffer = null;
            double amountBuffer = 1000000;
            double wasteBuffer = 100000;

            for (Wood w : woods)
            {
                double amount= length / w.getLength();
                double waste = length % (w.getLength());
                waste = w.getLength() - waste;

                if (waste < wasteBuffer || amount <= amountBuffer)
                {
                    amountBuffer = amount;
                    wasteBuffer = waste;
                    buffer = w;
                }
            }
            rem = buffer;
            remAmount = (int) Math.ceil(amountBuffer) * 2;
        }
        return new WoodOrderItem(remAmount, rem, desc);
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

    private static double getRafterAmount(double length, int modifier)
    {
        return (length / 55) * modifier;
    }



    //TODO Old version, delete before launch
       /*public Object getRafters(int width)
    {
        List<Wood> woods = new ArrayList<>();
        try
        {
            woods = Facade.getWoodByVariant("Spær", connectionPool);
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }
        woods.sort(new Comparator<Wood>()
        {
            @Override
            public int compare(Wood s, Wood t1)
            {
                return t1.getLength() - s.getLength();
            }
        });

        int modifier = 1;
        Wood rafter = null;
        while(rafter == null)
        {
            rafter = selectWood(woods, width, modifier);
            modifier++;
        }
        ArrayList<Wood> rafters = new ArrayList<>();

        int amount = width/55;
        int finalamount = amount*modifier;

        Map woodAmount = new HashMap<Integer, Integer>();
        woodAmount.put(rafter.getIdWood(), modifier);
        return woodAmount;
    } //Alternativ måde at finde amount


    */ //Alternativ måde at regne rafters
}




