package dat.backend.model.entities;

import dat.backend.model.persistence.Facade;

import java.util.ArrayList;

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

    public static int rafterCalc(int length, int width)
    {
        int rafters = 0;
        ArrayList<Wood> rafterList = Facade.getRafters();


        return rafters;
    }

}
