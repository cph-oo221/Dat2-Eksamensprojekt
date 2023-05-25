package dat.backend.model.utilities;

import dat.backend.model.config.Env;
import dat.backend.model.entities.Material;
import dat.backend.model.entities.Metal;
import dat.backend.model.entities.OrderItem;
import dat.backend.model.entities.Wood;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartsListCalculatorTest
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
                stmt.execute("delete from fog_test.ordermetal");
                stmt.execute("delete from fog_test.orderwood");
                stmt.execute("delete from fog_test.wood");
                stmt.execute("delete from fog_test.metal");
                stmt.execute("delete from fog_test.user");


                stmt.execute("ALTER TABLE fog_test.user DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.user AUTO_INCREMENT = 1");
                stmt.execute("INSERT INTO fog_test.metal (idmetal, name, price, unit, variant) VALUES " +
                        "(1, '100mm skruer', 2, 'Stk', 'Skrue')," +
                        "(3, '50mm skruer', 1, 'Stk', 'Skrue')," +
                        "(4,'Hulbånd' , 20 , 'Rulle', 'Hulbånd')," +
                        "(5, 'Bræddebolt', 500 , 'Stk', 'Bræddebolt')," +
                        "(6, 'Firkantskiver' , 20 , 'Stk', 'Firkantskiver')," +
                        "(7, 'Stalddørsgreb' , 80, 'Sæt', 'Lås')," +
                        "(8, 'T hængsel' , 50, 'Stk', 'Hængsel')," +
                        "(9, 'Vinkelbeslag' , 23, 'Stk', 'Vinkelbeslag')," +
                        "(10, 'Universalbeslag højre' ,15 , 'Stk' , 'Beslag Højre')," +
                        "(11, 'Universalbeslag venstre' , 15 , 'Stk', 'Beslag Venstre');");
                stmt.execute("ALTER TABLE fog_test.metal ENABLE KEYS");


                stmt.execute("ALTER TABLE fog_test.wood DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.wood AUTO_INCREMENT = 1");
                stmt.execute("insert into fog_test.wood VALUES " +
                        "(1, 410, 55, 20, 'Spærtræ', 'stk', 200, 'Rem')," +
                        "(2, 205, 55, 20, 'Spærtræ', 'stk', 100, 'Rem')," +
                        "(3, 300, 97, 97, 'Stolpe', 'stk', 100, 'Stolpe')," +
                        "(4, 410, 40, 20, 'Spærtræ', 'stk', 200, 'Spær')," +
                        "(5, 205, 40, 20, 'Spærtræ', 'stk', 100, 'Spær')," +
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
    void poleCalcTest()
    {
        int length = 780;
        int width = 240;
        Material wexpected = new Wood(3, 300, 97, 97, "Stolpe", "stk", 100, "Stolpe");
        Material mexpected = new Metal(5, "Bræddebolt", 500 , "Stk", "Bræddebolt");

        try
        {
            List<OrderItem> items = PartsListCalculator.poleCalc(length, width, connectionPool);

            // Wood
            assertEquals(8, items.get(0).getAmount());
            assertEquals(wexpected, items.get(0).getMaterial());

            // Metal
            assertEquals(16, items.get(1).getAmount());
            assertEquals(mexpected, items.get(1).getMaterial());
        }
        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    void calcRemTest()
    {
        double length = 819;
        Material wexpected = new Wood(1, 410, 55, 20, "Spærtræ", "stk", 200, "Rem");
        Material wexpected1 = new Wood(2, 205, 55, 20, "Spærtræ", "stk", 100, "Rem");

        try
        {
            OrderItem item = PartsListCalculator.remCalc(length, connectionPool);

            assertEquals(wexpected, item.getMaterial());
            assertEquals(4, item.getAmount());
        }
        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }


    @Test
    void calcRafterTest()
    {
        int length = 240;
        int width = 240;
    }

    @Test
    void shedMethodTest()
    {
        // FIXME: VÆRSGO SEB
        int shedLength = 300;
        double width = 600;

        try
        {
            List<Wood> woods = Facade.getWoodByVariant("Spær", connectionPool);

            List<OrderItem> itemList = PartsListCalculator.getShed(width, shedLength, connectionPool);

            assertEquals(6, itemList.size());

            assertEquals(12, itemList.get(0).getAmount());
            assertEquals(12, itemList.get(1).getAmount());
            assertEquals(4, itemList.get(2).getAmount());
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
            List<OrderItem> list = PartsListCalculator.sternCalc(360, 570, connectionPool);
            Wood expected1 = new Wood(9,410,200,30,"Brædt","stk",200,"Stern");
            Wood expected = new Wood(8,205,200,30,"Brædt","stk",150,"Stern");

            assertEquals(2, list.size());
            assertEquals(expected1, list.get(0).getMaterial()); // length
            assertEquals(expected, list.get(1).getMaterial()); // width

            assertEquals(2, list.get(0).getAmount());
            assertEquals(6, list.get(1).getAmount());
        }
        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }



    @Test
    void roofingCalcTest()
    {
        int length = 240;
        int width = 240;
        Material wexpected = new Wood(6, 100, 100, 1, "Trapezplade", "stk", 30, "Tag");
        Material mexpected = new Metal(3, "50mm skruer", 1, "Stk", "Skrue");

        try
        {
            List<OrderItem> itemList = PartsListCalculator.roofingCalc(length, width, connectionPool);

            assertEquals(2, itemList.size());
            assertEquals(6, itemList.get(0).getAmount());
            assertEquals(wexpected, itemList.get(0).getMaterial());

            assertEquals(72, itemList.get(1).getAmount());
            assertEquals(mexpected, itemList.get(1).getMaterial());
        }

        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }

    // TODO: RELOCATE TO METAL TEST CLASS
    @Test
    void rafterMetalTest()
    {
        try
        {
            List<OrderItem> list = MetalCalculator.getRafterMetal(10, 60, connectionPool);
            Material expected = new Metal(1, "100mm skruer", 2, "Stk", "Skrue");

            assertEquals(3, list.size());
            assertEquals(20, list.get(0).getAmount());
            assertEquals(20, list.get(1).getAmount());
            assertEquals(360, list.get(2).getAmount());

            assertEquals(expected, list.get(2).getMaterial());
        }

        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }
}