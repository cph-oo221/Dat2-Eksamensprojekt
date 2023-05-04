package dat.backend.persistence;


import dat.backend.model.config.ApplicationStart;
import dat.backend.model.config.Env;
import dat.backend.model.entities.User;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

        ApplicationStart.setConnectionPool(connectionPool);


        try (Connection testConnection = connectionPool.getConnection())
        {
            try (Statement stmt = testConnection.createStatement())
            {
                // Create test database - if not exist
                stmt.execute("CREATE DATABASE IF NOT EXISTS fog_test;");

                stmt.execute("CREATE TABLE IF NOT EXISTS fog_test.user LIKE `Dat2-Eksamensopgave`.user;");



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
                stmt.execute("delete from fog_test.user");

                stmt.execute("ALTER TABLE fog_test.user DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.user AUTO_INCREMENT = 1");
                stmt.execute("insert into fog_test.user VALUES " +
                        "(1, 'user@user.com','user','user', 'uservej 1', 'Vice city', 12345678)," +
                        "(2, 'admin@admin.com','admin','admin', 'adminvej 2', 'San Andreas', 87654321)");
                stmt.execute("ALTER TABLE fog_test.user ENABLE KEYS");
            }
        } catch (SQLException throwables)
        {
            System.out.println(throwables.getMessage());
            fail("Database connection failed");
        }
    }

    @Test
    void testConnection() throws SQLException
    {
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
        } catch (SQLException throwables)
        {
            System.out.println(throwables.getMessage());
            fail("Database connection failed");
        }

    }


    @Test
    void getUserByEmail() throws SQLException, DatabaseException
    {
        int iduser = 1;
        String email = "user@user.com";
        String password = "user";
        String role = "user";
        String address = "uservej 1";
        String city = "Vice city";
        int phone = 12345678;

        User user = new User(iduser, email, password, role, address, city, phone);

        System.out.println("Expected: " + user);
        System.out.println("Actual: " + Facade.getUserByEmail("user@user.com"));

        assertEquals(user, Facade.getUserByEmail("user@user.com"));
    }

    @Test
    void createUser() throws SQLException, DatabaseException
    {
        int iduser = 3;
        String email = "test@124.com";
        String password = "test124";
        String role = "user";
        String address = "testvej 124";
        String city = "testing city";
        int phone = 13233334;

        assertEquals(new User(iduser, email, password, role, address, city, phone), Facade.createUser("test@124.com", "test124", "testvej 124", "testing city", 13233334, "user"));
    }
}