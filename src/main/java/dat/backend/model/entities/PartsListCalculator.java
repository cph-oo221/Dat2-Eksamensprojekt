package dat.backend.model.entities;

import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;

import java.util.*;

public class PartsListCalculator
{
    private static final int CARPORT_HANG = 50;
    private static final int MAX_POLE_DIST = 310;




    public static List<WoodOrderItem> finalCalc(double length, double width, ConnectionPool connectionPool) throws DatabaseException
    {
        WoodOrderItem rafters = calcRafter(width, length, connectionPool);
        WoodOrderItem roofing = roofingCalc(length, width, connectionPool);
        WoodOrderItem poles = poleCalc(length, width, connectionPool);
        WoodOrderItem rems = remCalc(length, width, connectionPool);
        List<WoodOrderItem> woodOrderItemList = new ArrayList<>();
        woodOrderItemList.add(rafters);
        woodOrderItemList.add(roofing);
        woodOrderItemList.add(poles);
        woodOrderItemList.add(rems);
        return woodOrderItemList;
    }


    private static WoodOrderItem roofingCalc(double length, double width, ConnectionPool connectionPool) throws DatabaseException
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


    public static WoodOrderItem poleCalc(double length, double width, ConnectionPool connectionPool) throws DatabaseException
    {
        String desc = "Stolper graves 90 cm ned i jord";
        int poles = 4;

        double lenPoles = ((length - CARPORT_HANG *2) / MAX_POLE_DIST) + 1;
        double widthPoles = ((width - CARPORT_HANG *2) / MAX_POLE_DIST) + 1;

        double extraLen = Math.ceil(lenPoles - 2) * 2;
        double extraWidth = Math.ceil(widthPoles - 2) * 2;

        poles += extraLen;
        poles += extraWidth;


        List<Wood> poleList = Facade.getWoodByVariant("Stolpe", connectionPool);
        Wood pole = poleList.get(0);
        return new WoodOrderItem(poles, pole, desc);
    }

    private static WoodOrderItem calcRafter(double width, double length, ConnectionPool connectionPool) throws DatabaseException
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

    private static WoodOrderItem remCalc(double length, double width, ConnectionPool connectionPool) throws DatabaseException
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

    private static List<WoodOrderItem> getShed(double length, double width, double shedLength, ConnectionPool connectionPool) throws DatabaseException
    {

        double shedWidth = width / 2;
        double amountL;
        double amountW;
        double rafterLengthAmountL = 1;
        double rafterWidthAmountL = 1;

        List<WoodOrderItem> woodOrderItemList = new ArrayList<>();

        List<Wood> woods = Facade.getWoodByVariant("Spær", connectionPool);

        woods.sort(new Comparator<Wood>()
        {
            @Override
            public int compare(Wood s, Wood t1)
            {
                return t1.getLength() - s.getLength();
            }
        });

        Wood rafterLength = selectWood(woods, shedLength);

        if (rafterLength == null)
        {
            Wood buffer = null;
            double amountBuffer = 1000000;
            double wasteBuffer = 100000;

            for (Wood w : woods)
            {
                amountL = shedLength / w.getLength();
                double waste = shedLength % (w.getLength());
                waste = w.getLength() - waste;

                if (waste < wasteBuffer || amountL <= amountBuffer)
                {
                    amountBuffer = amountL;
                    wasteBuffer = waste;
                    buffer = w;
                }
            }
            rafterLength = buffer;
            rafterLengthAmountL = (int) Math.ceil(amountBuffer); //1
        }

        double rafterLengthWidth = rafterLength.getWidth();
        amountL = (int) (rafterLengthAmountL * Math.ceil(210 / rafterLengthWidth)); // 210 = Pole height - the buried 90. //1 * (210/55) = 3.82 = 4
        WoodOrderItem rafterLengthWOI = new WoodOrderItem((int) amountL, rafterLength, "Spærtræ til beklædning af skur i længden");
        woodOrderItemList.add(rafterLengthWOI);


        Wood rafterWidth = selectWood(woods, shedWidth);

        if (rafterWidth == null)
        {
            Wood buffer = null;
            double amountBuffer = 1000000;
            double wasteBuffer = 100000;

            for (Wood w : woods)
            {
                amountW = shedWidth / w.getLength();
                double waste = shedWidth % (w.getLength());
                waste = w.getLength() - waste;

                if (waste < wasteBuffer || amountW <= amountBuffer)
                {
                    amountBuffer = amountW;
                    wasteBuffer = waste;
                    buffer = w;
                }
            }
            rafterWidth = buffer;
            rafterWidthAmountL = (int) Math.ceil(amountBuffer);
        }
        double rafterWidthWidth = rafterWidth.getWidth();
        amountW = (int) (rafterWidthAmountL * Math.ceil(210 / rafterWidthWidth)); // 210 = Pole height - the buried 90. //1 * (210/55) = 3.82 = 4
        WoodOrderItem rafterWidthWOI = new WoodOrderItem((int) amountW, rafterWidth, "Spærtræ til beklædning af skur i bredden");
        woodOrderItemList.add(rafterWidthWOI);

        int poles;
        if (shedWidth > 310)
        {
            poles = 3;
        } else
        {
            poles = 4;
        }
        List<Wood> poleWoodList = Facade.getWoodByVariant("Stolpe", connectionPool);
        WoodOrderItem polesShed = new WoodOrderItem(poles, poleWoodList.get(0), "Stolper til skur");
        woodOrderItemList.add(polesShed);
        return woodOrderItemList;
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




