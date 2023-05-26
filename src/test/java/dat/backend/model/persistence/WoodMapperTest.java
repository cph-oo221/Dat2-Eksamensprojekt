package dat.backend.model.persistence;

import dat.backend.model.config.Env;
import dat.backend.model.entities.Wood;
import dat.backend.model.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WoodMapperTest
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
        } else
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
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.order LIKE `Dat2-Eksamensopgave`.order;");
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.ordermetal LIKE `Dat2-Eksamensopgave`.ordermetal;");
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.orderwood LIKE `Dat2-Eksamensopgave`.orderwood;");
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.metalstuff LIKE `Dat2-Eksamensopgave`.metal;");
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
                stmt.execute("delete from fog_test.wood");

                stmt.execute("ALTER TABLE fog_test.wood DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.wood AUTO_INCREMENT = 1");
                stmt.execute("insert into fog_test.wood VALUES " +
                        "(1, 410, 55, 20, 'Spærtræ', 'stk', 200, 'Rem')," +
                        "(2, 205, 55, 20, 'Spærtræ', 'stk', 100, 'Rem')," +
                        "(3, 300, 97, 97, 'Stolpe', 'stk', 100, 'Stolpe')," +
                        "(4, 410, 55, 20, 'Spærtræ', 'stk', 200, 'Spær')," +
                        "(5, 205, 55, 20, 'Spærtræ', 'stk', 100, 'Spær')");
                stmt.execute("ALTER TABLE fog_test.wood ENABLE KEYS");
            }
        } catch (SQLException throwables)
        {
            System.out.println(throwables.getMessage());
            fail("Database connection failed");
        }
    }

    @Test
    void getRafters()
    {
        try
        {
            List<Wood> list = Facade.getWoodByVariant("Rem", connectionPool);
            assertEquals(2, list.size());
        }

        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }


    @Test
    void updatePriceOfWood() throws DatabaseException
    {
        Facade.updateWoodPrice(1, 300, connectionPool);
        assertEquals(300, Facade.getWoodById(1, connectionPool).getPrice());
        System.out.println("Price: " + Facade.getWoodById(1, connectionPool).getPrice());
    }


    @Test
    void deleteWood() throws DatabaseException
    {
        int idWood = 5;
        int expected = 4;

        Facade.deleteWood(idWood, connectionPool);
        assertEquals(expected, Facade.getAllWood(connectionPool).size());
    }

    @Test
    void createWood() throws DatabaseException
    {
        Wood expected = new Wood(6, 160, 54, 23, "Spærtræ", "stk", 120, "Rem");

        Wood actual = Facade.createWood(160, 54, 23, "Spærtræ", "stk", 120, "Rem", connectionPool);

        System.out.println(expected);
        System.out.println(actual);

        assertEquals(expected, actual);
    }


    @Test
    void createWoodInvaildInputs() throws DatabaseException
    {

        // Length is less than zero
        assertThrows(DatabaseException.class, () -> Facade.createWood(-12, 54, 54,
                "Spærtræ", "stk", 120, "Rem", connectionPool));
        // Width is less than zero
        assertThrows(DatabaseException.class, () -> Facade.createWood(120, -10, 54,
                "Spærtræ", "stk", 120, "Rem", connectionPool));
        // Height is less than zero
        assertThrows(DatabaseException.class, () -> Facade.createWood(120, 54, -12,
                "Spærtræ", "stk", 120, "Rem", connectionPool));

        // price is less than zero
        assertThrows(DatabaseException.class, () -> Facade.createWood(120, 54, 54,
                "Spærtræ", "stk", -100, "Rem", connectionPool));

        // Name is empty
        assertThrows(DatabaseException.class, () -> Facade.createWood(120, 54, 54,
                "", "stk", 100, "Rem", connectionPool));

        // Unit is empty
        assertThrows(DatabaseException.class, () -> Facade.createWood(120, 54, 54,
                "Spærtræ", "", 100, "Rem", connectionPool));

        // Variant is empty
        assertThrows(DatabaseException.class, () -> Facade.createWood(120, 54, 54,
                "Spærtræ", "stk", 100, "", connectionPool));
    }

}