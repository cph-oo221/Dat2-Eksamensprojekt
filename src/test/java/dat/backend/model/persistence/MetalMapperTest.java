package dat.backend.model.persistence;

import dat.backend.model.config.Env;
import dat.backend.model.entities.Metal;
import dat.backend.model.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class MetalMapperTest
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
            }
            else
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
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.ordermetal LIKE `Dat2-Eksamensopgave`.ordermetal;");
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.orderwood LIKE `Dat2-Eksamensopgave`.orderwood;");
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

                stmt.execute("delete from fog_test.metal");


                stmt.execute("ALTER TABLE fog_test.metal DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.metal AUTO_INCREMENT = 1");
                stmt.execute("INSERT INTO fog_test.metal (idmetal, name, price, unit, variant) VALUES " +
                        "(1, '100mm skruer', 2, 'Stk', 'Skrue')," +
                        "(2, '50mm skruer', 1, 'Stk', 'Skrue')," +
                        "(3,'Hulbånd' , 20 , 'Rulle', 'Hulbånd')," +
                        "(4, 'Bræddebolt', 500 , 'Stk', 'Bræddebolt')," +
                        "(5, 'Firkantskiver' , 20 , 'Stk', 'Firkantskiver')," +
                        "(6, 'Stalddørsgreb' , 80, 'Sæt', 'Lås')," +
                        "(7, 'T hængsel' , 50, 'Stk', 'Hængsel')," +
                        "(8, 'Vinkelbeslag' , 23, 'Stk', 'Vinkelbeslag')," +
                        "(9, 'Universalbeslag højre' ,15 , 'Stk' , 'Beslag Højre')," +
                        "(10, 'Universalbeslag venstre' , 15 , 'Stk', 'Beslag Venstre');");
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
    void getAllMetal()
    {
        try
        {
            assertEquals(10, Facade.getAllMetal(connectionPool).size());
        }
        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    void updateMetalPrice()
    {
        int expected = 80;
        int idMetal = 1;
        try
        {
            Facade.updateMetalPrice(idMetal, 80, connectionPool);
            assertEquals(expected, Facade.getAllMetal(connectionPool).get(0).getPrice());
        }
        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    void deleteMetal()
    {
        int expected = 9;
        try
        {
            Facade.deleteMetal(2, connectionPool);
            assertEquals(expected, Facade.getAllMetal(connectionPool).size());
        }
        catch (DatabaseException e)
        {
           fail(e.getMessage());
        }
    }

    @Test
    void createMetal() throws DatabaseException
    {
        int idMetal = 11;
        String name = "Bolt";
        int price = 10;
        String unit = "Stk";
        String variant = "Bolt";

        Metal expectedMetal = new Metal(idMetal, name, price, unit, variant);

        int expectedSize = 11;

        Metal actual = Facade.createMetal(name, price, unit, variant, connectionPool);

        assertEquals(expectedMetal, actual);
        assertEquals(expectedSize, Facade.getAllMetal(connectionPool).size());
    }

    @Test
    void getMetalById() throws DatabaseException
    {
        int idMetal = 1;
        String name = "100mm skruer";
        int price = 2;
        String unit = "Stk";
        String variant = "Skrue";

        Metal expected = new Metal(idMetal, name, price, unit, variant);

        Facade.getMetalById(1, connectionPool);

        assertEquals(expected, Facade.getMetalById(1, connectionPool));
        assertTrue(Facade.getMetalById(12, connectionPool) == null);
    }

    @Test
    void createMetalInvaildInputs()
    {

        // Price is less than zero
        assertThrows(DatabaseException.class, () -> Facade.createMetal("Test", -54, "stk",
                "Bolt", connectionPool));

        // Name is empty
        assertThrows(DatabaseException.class, () -> Facade.createMetal("", 54, "stk",
                "Bolt", connectionPool));

         // Variant is empty
        assertThrows(DatabaseException.class, () -> Facade.createMetal("Test1", 54, "stk",
                "", connectionPool));
    }
}