package dat.backend;

import dat.backend.model.config.Env;
import dat.backend.model.entities.Wood;
import dat.backend.model.entities.WoodOrderItem;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarportCalcTest
{
    private static String USER;
    private static String PASSWORD;
    private static String TESTURL;

    private static ConnectionPool connectionPool;

    @BeforeAll
    public static void setUpClass() throws SQLException
    {
        String deployed = System.getenv("DEPLOYED");
        if (deployed != null)
        {
            // Prod: hent variabler fra setenv.sh i Tomcats bin folder
            USER = System.getenv("JDBC_USER");
            PASSWORD = System.getenv("JDBC_PASSWORD");
            TESTURL = System.getenv("JDBC_CONNECTION_TEST");
        }
        else
        {
            if (Env.class != null)
            {
                USER = Env.USER;
                PASSWORD = Env.PASSWORD;
                TESTURL = Env.TESTURL;
            } else
            {
                throw new RuntimeException("Env class needed, but not found!");
            }
        }

        connectionPool = new ConnectionPool(USER, PASSWORD, TESTURL);
    }

    @Test
    void calcRemTest()
    {
        double width = 240;
        double length = 20000;
        int remAmount = 2;

        try
        {
            Wood expected = new Wood(1, 410, 55, 20, "Spærtræ 410x55x20", "stk", 200, "Rem");
            //Wood expected = new Wood(2, 205, 55, 20, "Spærtræ 205x55x20", "stk", 100, "Rem");
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


            assertNotNull(rem);
            assertEquals(98, remAmount);
            assertEquals(expected, rem);

        }

        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    void calcRafter()
    {
        int width = 240;
        int length = 720;
        try
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

            int amount = (int) Math.floor(getRafterAmount(length, raftAmountModifier));

            assertNotNull(rafter);
            assertEquals(13, amount);
        }

        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }



    private Wood selectWood(List<Wood> woods, double length)
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

    private float getRafterAmount(float length, int modifier)
    {
        return (length / 55) * modifier;
    }

    @Test
    void roofingCalc()
    {
        int length = 250;
        int width = 250;
        double area = length*width;
        // 250 * 250 = 62500 cm^2
        // 1 plade er 100 * 100 = 10.000 cm^2
        // 62500 / 10.000 = 6.25
        List<Wood> roofing = null;
        try
        {
            roofing = Facade.getWoodByVariant("Tag", connectionPool);
        }
        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
        Wood roof = roofing.get(0);
        int amount= (int) Math.ceil(area/10000);
        String desc = "Placeholder";
        WoodOrderItem actual = new WoodOrderItem(amount, roof, desc);
        int expectedId = 6;
        int expectedAmount = 7;
        assertEquals(expectedId , actual.getWood().getIdWood());
        assertEquals(expectedAmount , actual.getAmount());


        //6,100,100,1,Trapezplade 1x1m,stk,30,Tag


    }
}
