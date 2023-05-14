package dat.backend.persistence;

import dat.backend.model.config.Env;
import dat.backend.model.entities.Metal;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;
import org.junit.jupiter.api.AfterEach;
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
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.order LIKE `Dat2-Eksamensopgave`.order;");
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.ordermetal LIKE `Dat2-Eksamensopgave`.ordermetal;");
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.orderwood LIKE `Dat2-Eksamensopgave`.orderwood;");
                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.metalstuff LIKE `Dat2-Eksamensopgave`.metalstuff;");
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
                stmt.execute("delete from fog_test.receipt");
                stmt.execute("delete from fog_test.order");
                stmt.execute("delete from fog_test.ordermetal");
                stmt.execute("delete from fog_test.orderwood");
                stmt.execute("delete from fog_test.wood");
                stmt.execute("delete from fog_test.metalstuff");
                stmt.execute("delete from fog_test.user");


                stmt.execute("ALTER TABLE fog_test.user DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.user AUTO_INCREMENT = 1");
                stmt.execute("INSERT INTO fog_test.metalstuff (idmetalstuff, name, price, unit, variant) VALUES " +
                        "(1, '4,5x60 mm. skruer 200 stk.', 10, 'Pakke', 'Skruer');" +
                        "(2, '5,6x80 mm. skruer 400 stk.', 60, 'Pakke', 'Skruer')");
                stmt.execute("ALTER TABLE fog_test.metalstuff ENABLE KEYS");
            }
        } catch (SQLException throwables)
        {
            System.out.println(throwables.getMessage());
            fail("Database connection failed");
        }
    }


    @Test
    void getAllMetal() throws DatabaseException
    {
        assertEquals(2, Facade.getAllMetal(connectionPool).size());
    }

    @Test
    void updateMetalPrice() throws DatabaseException
    {
        int expected = 80;
        int idMetal = 1;
        Facade.updateMetalPrice(idMetal, 80, connectionPool);
        assertEquals(expected, Facade.getAllMetal(connectionPool).get(0).getPrice());
    }

    @Test
    void deleteMetal() throws DatabaseException
    {
        int expected = 1;
        Facade.deleteMetal(2, connectionPool);
        assertEquals(expected, Facade.getAllMetal(connectionPool).size());
    }

    @Test
    void createMetal() throws DatabaseException
    {
        int idMetal = 3;
        String name = "2,4x60 mm. skruer 400 stk.";
        int price = 100;
        String unit = "Pakke";
        String variant = "Skruer";

        Metal expectedMetal = new Metal(idMetal, name, price, unit, variant);

        int expectedSize = 3;

        Metal actual = Facade.createMetal("2,4x60 mm. skruer 400 stk.", 100, "Pakke", "Skruer", connectionPool);

        assertEquals(expectedMetal, actual);
        assertEquals(3, Facade.getAllMetal(connectionPool).size());
    }
}