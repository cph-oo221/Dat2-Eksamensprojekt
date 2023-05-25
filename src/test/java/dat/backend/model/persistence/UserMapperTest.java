package dat.backend.model.persistence;

import dat.backend.model.config.Env;
import dat.backend.model.entities.User;
import dat.backend.model.exceptions.DatabaseException;
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
                stmt.execute("delete from fog_test.receipt");
                stmt.execute("delete from fog_test.ordermetal");
                stmt.execute("delete from fog_test.orderwood");
                stmt.execute("delete from fog_test.wood");
                stmt.execute("delete from fog_test.metal");
                stmt.execute("delete from fog_test.user");

                stmt.execute("ALTER TABLE fog_test.user DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.user AUTO_INCREMENT = 1");
                stmt.execute("insert into fog_test.user VALUES " +
                        "(1, 'user@user.com','user','user', 'uservej 1', 'Vice city', 12345678)," +
                        "(2, 'admin@admin.com','admin','admin', 'adminvej 2', 'San Andreas', 87654321)," +
                        "(3, 'test@124.com', 'test124', 'user', 'testvej 124', 'testing city', 13233334)");
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
        System.out.println("Actual: " + Facade.getUserByEmail("user@user.com", connectionPool));

        assertEquals(user, Facade.getUserByEmail("user@user.com", connectionPool));
    }

    @Test
    void getUserByEmailFail() throws SQLException, DatabaseException
    {
        assertNull(Facade.getUserByEmail("testemailfail.com", connectionPool));
        assertFalse(Facade.getUserByEmail("testfail@gmail.com", connectionPool) != null);
        assertEquals(null, Facade.getUserByEmail("oleolsen@gmail.com,", connectionPool));
    }

    @Test
    void createUser() throws SQLException, DatabaseException
    {
        int iduser = 3;
        String email = "test@test34.com";
        String password = "test124";
        String role = "user";
        String address = "testvej 124";
        String city = "testing city";
        int phone = 13233334;

        assertEquals(new User(iduser, email, password, role, address, city, phone), Facade.createUser("test@test34.com", "test124",
                "testvej 124", "testing city", 13233334, "user", connectionPool));
    }

    @Test
    void invalidCreateUser()
    {
        // empty password has to be 5 characters or more
        assertThrows(IllegalArgumentException.class, () -> Facade.createUser("test@125.com", "123", "testvej 126", "test city", 23456234, "user", connectionPool));
        // space in email
        assertThrows(IllegalArgumentException.class, () -> Facade.createUser("test @125.com", "123456", "testvej 126", "test city", 23456234, "user", connectionPool));
        // email already exists
        assertThrows(IllegalArgumentException.class, () -> Facade.createUser("user@user.com","user", "uservej 1", "Vice city", 12345678, "user", connectionPool));
    }

    @Test
    void succesfulLogin()
    {
        try
        {
            User expected = new User(3, "test@124.com", "test124", "user", "testvej 124",
                    "testing city", 13233334);

            /* User expected = new User(1, "user@user.com", "user", "user", "uservej 1" ,"Vice city", 12345678);*/

            User actual = Facade.login("test@124.com", "test124", connectionPool);

            assertEquals(expected, actual);
        }

        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    void invalidEmailLogin() throws DatabaseException
    {
        assertNull(Facade.login("usr@user.com", "user", connectionPool));
    }

    @Test
    void invalidPassLogin() throws DatabaseException
    {
        assertNull(Facade.login("user@user.com", "usr", connectionPool));
    }
}