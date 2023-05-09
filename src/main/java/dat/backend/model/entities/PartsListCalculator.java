package dat.backend.model.entities;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import java.util.*;

public class PartsListCalculator
{
    ConnectionPool connectionPool = ApplicationStart.getConnectionPool();
    private static final int F = 50;
    private static final int M = 310;

    public static int poleCalc(int length, int width)
    {
        int poles = 4;
        poles += Math.floor((((length - (F*2) ) / M ) ) * 2);
        poles += Math.floor((((width - (F*2) ) / M ) ) * 2);
        return poles;
    }


   public Object getRafters(int width)
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
    }

     //Alternativ måde at finde amount


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
}
