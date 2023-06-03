package dat.backend.model.persistence;

import dat.backend.model.config.Env;
import dat.backend.model.entities.Metal;
import dat.backend.model.entities.OrderItem;
import dat.backend.model.entities.Wood;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.utilities.PartsListCalculator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest
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

        try (Connection testConnection = connectionPool.getConnection())
        {
            try (Statement stmt = testConnection.createStatement())
            {
                // Create test database - if not exist
                stmt.execute("CREATE DATABASE IF NOT EXISTS fog_test;");

                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.user LIKE `Dat2-Eksamensopgave`.user;");
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.receipt LIKE `Dat2-Eksamensopgave`.receipt;");
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.metal LIKE `Dat2-Eksamensopgave`.metal;");
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.orderwood LIKE `Dat2-Eksamensopgave`.orderwood;");
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.ordermetal LIKE `Dat2-Eksamensopgave`.ordermetal;");
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.metal LIKE `Dat2-Eksamensopgave`.metal;");
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.wood LIKE `Dat2-Eksamensopgave`.wood;");


            } catch (SQLException throwables)
            {
                throwables.printStackTrace();
            }

        }
    }

    @BeforeEach
    void setUp()
    {
        try (Connection testConnection = connectionPool.getConnection())
        {
            try (Statement stmt = testConnection.createStatement())
            {
                stmt.execute("use fog_test;");
                stmt.execute("DELETE FROM fog_test.orderwood;");
                stmt.execute("DELETE FROM fog_test.ordermetal;");
                stmt.execute("DELETE FROM fog_test.receipt");
                stmt.execute("DELETE FROM fog_test.user");
                stmt.execute("DELETE FROM fog_test.wood;");
                stmt.execute("DELETE FROM fog_test.metal;");
                stmt.execute("ALTER TABLE fog_test.user DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.user AUTO_INCREMENT = 1");
                stmt.execute("ALTER TABLE fog_test.receipt DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.receipt AUTO_INCREMENT = 1");
                stmt.execute("ALTER TABLE fog_test.orderwood DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.orderwood AUTO_INCREMENT = 1");
                stmt.execute("ALTER TABLE fog_test.ordermetal DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.ordermetal AUTO_INCREMENT = 1");
                stmt.execute("ALTER TABLE fog_test.wood DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.wood AUTO_INCREMENT = 1");
                stmt.execute("ALTER TABLE fog_test.metal DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.metal AUTO_INCREMENT = 1");
                stmt.execute("INSERT INTO fog_test.wood VALUES " +
                        "(1, 410, 55, 20, 'Spærtræ', 'stk', 200, 'Rem')," +
                        "(2, 205, 55, 20, 'Spærtræ', 'stk', 100, 'Rem')," +
                        "(3, 300, 20, 20, 'Stolpe', 'stk', 100, 'Stolpe')," +
                        "(4, 410, 40, 20, 'Spærtræ', 'stk', 200, 'Spær')," +
                        "(5, 205, 40, 20, 'Spærtræ', 'stk', 100, 'Spær'), " +
                        "(6, 100, 100, 10, 'Trapezplade', 'stk', 30, 'Tag')," +
                        "(7, 205, 40, 10, 'Brædt', 'stk', 150, 'Stern')," +
                        "(8, 410, 40, 10, 'Brædt', 'stk', 200, 'Stern')");
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
                stmt.execute("insert into fog_test.user VALUES " +
                        "(1, 'user@user.com','user','user', 'uservej 1', 'Vice city', 12345678)," +
                        "(2, 'admin@admin.com','admin','admin', 'adminvej 2', 'San Andreas', 87654321)");
                stmt.execute("ALTER TABLE fog_test.user ENABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.receipt ENABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.orderwood ENABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.ordermetal ENABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.wood ENABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.metal ENABLE KEYS");
            }
        }
        catch (SQLException throwables)
        {
            System.out.println(throwables.getMessage());
            fail("Database connection failed");
        }
    }

    @Test
    void createValidOrderTest()
    {
        int length = 240;
        int width = 240;
        int shedLength = 0;
        boolean withRoof = true;
        String comment = "test1";

        List<OrderItem> orderItemList = null;
        try
        {
            orderItemList = PartsListCalculator.materialCalc(length, width, shedLength, withRoof, connectionPool);

            int receiptId = Facade.createReceipt(1, width, length, comment, connectionPool);
            int rowsUpdated = Facade.createOrder(receiptId, orderItemList, connectionPool);

            assertEquals(14, rowsUpdated);
        }

        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    void failOnNoReceiptTest()
    {
        int length = 240;
        int width = 240;
        int shedLength = 0;
        boolean withRoof = true;
        String comment = "test2";

        try
        {
            List<OrderItem> orderItemList = PartsListCalculator.materialCalc(length, width, shedLength, withRoof, connectionPool);
            assertThrows(DatabaseException.class, () -> Facade.createOrder(10, orderItemList, connectionPool));
        }

        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    void orderContainsBothMaterials()
    {
        int length = 240;
        int width = 240;
        int shedLength = 100;
        boolean withRoof = true;
        String comment = "test3";
        try
        {
            List<OrderItem> orderItemList = PartsListCalculator.materialCalc(length, width, shedLength, withRoof, connectionPool);

            int receiptId = Facade.createReceipt(1, width, length, comment, connectionPool);
            int rowsUpdated = Facade.createOrder(receiptId, orderItemList, connectionPool);

            List<OrderItem> retrievedWoodOrder = Facade.getWoodOrderItemsByReceiptId(receiptId, connectionPool);
            List<OrderItem> retrievedMetalOrder = Facade.getMetalOrderItemsByReceiptId(receiptId, connectionPool);

            assertTrue(retrievedWoodOrder.size() > 0);
            assertTrue(retrievedMetalOrder.size() > 0);

            assertInstanceOf(Wood.class, retrievedWoodOrder.get(0).getMaterial());
            assertInstanceOf(Metal.class, retrievedMetalOrder.get(0).getMaterial());
        }

        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }
}
