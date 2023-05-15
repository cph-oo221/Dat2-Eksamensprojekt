package dat.backend;

import dat.backend.model.config.Env;
import dat.backend.model.entities.PartsListCalculator;
import dat.backend.model.entities.Wood;
import dat.backend.model.entities.WoodOrderItem;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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

    @BeforeEach
    void setUp()
    {
        try (Connection testConnection = connectionPool.getConnection())
        {
            try (Statement stmt = testConnection.createStatement())
            {
                stmt.execute("use fog_test;");
                stmt.execute("delete from fog_test.receipt");
                stmt.execute("delete from fog_test.order");
                stmt.execute("delete from fog_test.ordermetal");
                stmt.execute("delete from fog_test.orderwood");
                stmt.execute("delete from fog_test.wood");
                stmt.execute("delete from fog_test.metal");
                stmt.execute("delete from fog_test.user");


                stmt.execute("ALTER TABLE fog_test.user DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.user AUTO_INCREMENT = 1");
                stmt.execute("INSERT INTO fog_test.metal (idmetal, name, price, unit, variant) VALUES " +
                        "(1, '4,5x60 mm. skruer 200 stk.', 10, 'Pakke', 'Skruer')," +
                        "(2, '5,6x80 mm. skruer 400 stk.', 60, 'Pakke', 'Skruer');");
                stmt.execute("ALTER TABLE fog_test.metal ENABLE KEYS");


                stmt.execute("ALTER TABLE fog_test.wood DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.wood AUTO_INCREMENT = 1");
                stmt.execute("insert into fog_test.wood VALUES " +
                        "(1, 410, 55, 20, 'Spærtræ', 'stk', 200, 'Rem')," +
                        "(2, 205, 55, 20, 'Spærtræ', 'stk', 100, 'Rem')," +
                        "(3, 300, 97, 97, 'Stolpe', 'stk', 100, 'Stolpe')," +
                        "(4, 410, 55, 20, 'Spærtræ', 'stk', 200, 'Spær')," +
                        "(5, 205, 55, 20, 'Spærtræ', 'stk', 100, 'Spær')," +
                        "(6, 100, 100, 1, 'Trapezplade', 'stk', 30, 'Tag')," +
                        "(8, 205, 200, 30, 'Brædt', 'stk', 150, 'Stern')," +
                        "(9, 410, 200, 30, 'Brædt', 'stk', 200, 'Stern')");

                stmt.execute("ALTER TABLE fog_test.wood ENABLE KEYS");



            }
        } catch (SQLException throwables)
        {
            System.out.println(throwables.getMessage());
            fail("Database connection failed");
        }
    }



    @Test
    void shedTest()
    {
        double width = 500;
        double length = 500;

        double shedWidth = 420;
        double shedLength = 420;

        double amount = 0;
        double amountL = 0;
        double amountW = 0;
        double rafterLengthAmountL = 1;
        double rafterWidthAmountL = 1;

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

            Wood rafterLength = selectWood(woods , shedLength);

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
            amountL = (int) (rafterLengthAmountL * Math.ceil(210 / rafterLengthWidth)); // 210 = Pole height - the buried 90.
            //1 * (210/55) = 3.82 = 4


            Wood rafterWidth = selectWood(woods , shedWidth);

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
            amountW = (int) (rafterWidthAmountL * Math.ceil(210 / rafterWidthWidth)); // 210 = Pole height - the buried 90.
            //1 * (210/55) = 3.82 = 4

            amount = amountL + amountW;
            int poles;
            if(shedWidth > 310) { poles = 3; }
            else{ poles = 4; }
            assertNotNull(rafterLength);
            assertNotNull(rafterWidth);
            assertEquals(24, amount);

        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void shedMethodTest()
    {
        int shedLength = 300;
        double width = 600;

        try
        {
            List<Wood> woods = Facade.getWoodByVariant("Spær", connectionPool);

            List<WoodOrderItem> itemList = PartsListCalculator.getShed(width, shedLength, connectionPool);

            assertEquals(3, itemList.size());

            assertEquals(8, itemList.get(0).getAmount());
            assertEquals(8, itemList.get(1).getAmount());
            assertEquals(3, itemList.get(2).getAmount());
        }

        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    void calcRemTest()
    {
        double width = 240;
        double length = 20000;
        int remAmount = 2;

        try
        {
            Wood expected = new Wood(1, 410, 55, 20, "Spærtræ", "stk", 200, "Rem");
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

    @Test
     void poleCalcTest()
    {
        try
        {
            WoodOrderItem item = PartsListCalculator.poleCalc(1450, 1450, connectionPool);

            assertEquals(20, item.getAmount());
        }
        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    void sternCalcTest()
    {
        try
        {
            List<WoodOrderItem> list = PartsListCalculator.sternCalc(360, 570, connectionPool);
            Wood expected1 = new Wood(9,410,200,30,"Brædt","stk",200,"Stern");
            Wood expected = new Wood(8,205,200,30,"Brædt","stk",150,"Stern");

            assertEquals(2, list.size());
            assertEquals(expected1, list.get(0).getWood()); // length
            assertEquals(expected, list.get(1).getWood()); // width

            assertEquals(4, list.get(0).getAmount());
            assertEquals(6, list.get(1).getAmount());
        }
        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }
}
