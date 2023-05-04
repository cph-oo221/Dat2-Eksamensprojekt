package dat.backend.persistence;

import dat.backend.model.config.Env;
import dat.backend.model.entities.Receipt;
import dat.backend.model.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest
{
    private static String USER;
    private static String PASSWORD;
    private static String TESTURL;

    private static ConnectionPool connectionPool;

    @BeforeAll
    public static void setUpClass()
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
    }

   /* @BeforeEach
    void setUp()
    {
        try (Connection testConnection = connectionPool.getConnection())
        {

        }
        catch (SQLException throwables)
        {
            System.out.println(throwables.getMessage());
            fail("Database connection failed");
        }
    }*/

    @Test
    void testConnection() throws SQLException
    {
        Receipt r = new Receipt(1, 1, false);

        try (Connection testConnection = connectionPool.getConnection())
        {
            assertNotNull(testConnection);

            try (Statement stmt = testConnection.createStatement())
            {
                // Create test database - if not exist
                stmt.execute("CREATE DATABASE  IF NOT EXISTS fog_test;");

                if (testConnection != null)
                {
                    testConnection.close();
                }
            }
        }
        catch (SQLException throwables)
        {
            System.out.println(throwables.getMessage());
            fail("Database connection failed");
        }

    }


}