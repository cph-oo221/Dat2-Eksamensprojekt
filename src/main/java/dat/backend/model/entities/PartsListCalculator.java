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



    public static WoodOrderItem roofingCalc(int length, int width) throws DatabaseException
    {
        String desc = "Roofing etc.";
        double area = length*width;
        // 250 * 250 = 62500 cm^2
        // 1 plade er 100 * 100 = 10.000 cm^2
        // 62500 / 10.000 = 6.25
        List<Wood> roofing = Facade.getWoodByVariant("Tag", connectionPool);
        Wood roof = roofing.get(0);
        int amount = (int) Math.ceil(area/10000);
        return new WoodOrderItem(amount, roof, desc);
    }


    public static WoodOrderItem poleCalc(int length, int width) throws DatabaseException
    {
        String desc = "Poles! descdesc";
        int poles = 4;
        poles += Math.floor((((length - (CARPORT_HANG *2) ) / MAX_POLE_DIST) ) * 2);
        poles += Math.floor((((width - (CARPORT_HANG *2) ) / MAX_POLE_DIST) ) * 2);
        List<Wood> poleList = Facade.getWoodByVariant("Stolpe", connectionPool);
        Wood pole = poleList.get(0);
        return new WoodOrderItem(poles, pole, desc);
    }

    public static WoodOrderItem calcRafter(int width, int length) throws DatabaseException
    {
        String desc = "Spær! etc.";
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

    public static WoodOrderItem remCalc(int length, int width) throws DatabaseException
    {
        String desc = "Remme! desc";
        int remAmount = 2;

        Wood expected = new Wood(1, 410, 55, 20, "Spærtræ 410x55x20", "stk", 200, "Rem");

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

        while (rem == null)
        {
            length = length / 2;
            rem = selectWood(woods, length);
            remAmount = remAmount * 2;
        }

        return new WoodOrderItem(remAmount, rem, desc);
    }

    private static Wood selectWood(List<Wood> woods, int length)
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
        return null;
    }

    private static float getRafterAmount(float length, int modifier)
    {
        return (length / 55) * modifier;
    }



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


    private Wood selectWood(List<Wood> woods, int width, int modifier)
    {
        Wood buffer = null;
        for (Wood w: woods)
        {
            if (w.getLength() >= width/modifier)
            {
                buffer = w;
            }

            else
            {
                return buffer;
            }
        }
        return null;
    }

    */ //Alternativ måde at regne rafters
}




