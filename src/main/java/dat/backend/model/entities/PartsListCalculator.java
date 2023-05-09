package dat.backend.model.entities;

import dat.backend.model.persistence.Facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PartsListCalculator
{
    private static final int F = 50;
    private static final int M = 310;

    public static int poleCalc(int length, int width)
    {
        int poles = 4;
        poles += Math.floor((((length - (F*2) ) / M ) ) * 2);
        poles += Math.floor((((width - (F*2) ) / M ) ) * 2);
        return poles;
    }

    public static ArrayList<Wood> rafterCalc(int length, int width)
    {
        int poles = poleCalc(length, width);
        double polesDistance = poles/(M-(2*F));
        ArrayList<Wood> rafterList = Facade.getRafters();
        Collections.sort(rafterList, Collections.reverseOrder());

        ArrayList<Wood> firstResult = new ArrayList<>();
        ArrayList<Wood> result = new ArrayList<>();

        while(length > 0)
        {
            for (int i = 0; i < poles; i++)
            {
                while (polesDistance > 0)
                {
                    for (int j = 0; j < rafterList.size(); j++)
                    {
                        if (rafterList.get(j).length < polesDistance && j != 0)
                        {
                            firstResult.add(rafterList.get(j - 1));
                            polesDistance -= rafterList.get(j + 1).length;
                        }

                    }
                    if (polesDistance > 0)
                    {
                        firstResult.add(rafterList.get(rafterList.size()));
                        polesDistance -= rafterList.get(rafterList.size()).length;
                    }
                }
            }
        }


        while( length > 0)
        {
            for(int i = 0; i < firstResult.size(); i++)
            {
                if(firstResult.get(i).width < length)
                {
                    result.add(firstResult.get(i));
                    length -= firstResult.get(i).width;
                }
            }
            if(length > 0)
            {

            }
        }
        return result;
    }

    public ArrayList<Wood> getRafter(int width)
    {
        ArrayList<Wood> woods = Facade.getRafters();
        woods.sort(new Comparator<Wood>()
        {
            @Override
            public int compare(Wood s, Wood t1)
            {
                return t1.getLength() - s.getLength();
            }
        });

    }

}
