package dat.backend.model.entities;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import java.util.*;

public class PartsListCalculator
{
    public static ConnectionPool connectionPool = ApplicationStart.getConnectionPool();
    private static final int F = 50;
    private static final int M = 310;



    public static WoodOrderItem roofingCalc(int length, int width) throws DatabaseException
    {
        double area = length*width;
        // 250 * 250 = 62500 cm^2
        // 1 plade er 100 * 100 = 10.000 cm^2
        // 62500 / 10.000 = 6.25
        List<Wood> roofing = Facade.getWoodByVariant("Tag", connectionPool);
        Wood roof = roofing.get(0);
        int amount = (int) Math.ceil(area/10000);
        String desc = "Placeholder";
        return new WoodOrderItem(amount, roof, desc);
    }


    public static WoodOrderItem poleCalc(int length, int width) throws DatabaseException
    {
        int poles = 4;
        poles += Math.floor((((length - (F*2) ) / M ) ) * 2);
        poles += Math.floor((((width - (F*2) ) / M ) ) * 2);
        List<Wood> poleList = Facade.getWoodByVariant("Stolpe", connectionPool);
        Wood pole = poleList.get(0);
        String desc = "Placeholder";
        return new WoodOrderItem(poles, pole, desc);
    }

    public static WoodOrderItem calcRafter(int width, int length) throws DatabaseException
    {
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

        int amount = (int) Math.floor(getAmount(length, raftAmountModifier));

        String desc = "Placeholder";
        return new WoodOrderItem(amount, rafter, desc);
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

    private static float getAmount(float length, int modifier)
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




