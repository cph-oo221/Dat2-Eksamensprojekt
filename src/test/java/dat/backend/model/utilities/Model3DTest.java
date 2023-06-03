package dat.backend.model.utilities;

import dat.backend.model.config.Env;
import dat.backend.model.entities.OrderItem;
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

class Model3DTest
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

                stmt.execute("DELETE FROM ordermetal");
                stmt.execute("DELETE FROM orderwood");
                stmt.execute("DELETE FROM wood");
                stmt.execute("DELETE FROM receipt");
                stmt.execute("ALTER TABLE fog_test.receipt DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.receipt AUTO_INCREMENT = 1");
                stmt.execute("ALTER TABLE fog_test.wood DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.wood AUTO_INCREMENT = 1");
                stmt.execute("ALTER TABLE fog_test.orderwood DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.orderwood AUTO_INCREMENT = 1");
                stmt.execute("ALTER TABLE fog_test.ordermetal DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.ordermetal AUTO_INCREMENT = 1");

                stmt.execute("INSERT INTO fog_test.wood VALUES " +
                        "(1, 410, 55, 20, 'Spærtræ', 'stk', 200, 'Rem')," +
                        "(2, 205, 55, 20, 'Spærtræ', 'stk', 100, 'Rem')," +
                        "(3, 300, 20, 20, 'Stolpe', 'stk', 100, 'Stolpe')," +
                        "(4, 410, 40, 20, 'Spærtræ', 'stk', 200, 'Spær')," +
                        "(5, 205, 40, 20, 'Spærtræ', 'stk', 100, 'Spær'), " +
                        "(6, 100, 100, 10, 'Trapezplade', 'stk', 30, 'Tag')," +
                        "(8, 205, 40, 10, 'Brædt', 'stk', 150, 'Stern')," +
                        "(9, 410, 40, 10, 'Brædt', 'stk', 200, 'Stern')");

                stmt.execute("ALTER TABLE fog_test.receipt ENABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.wood ENABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.orderwood ENABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.ordermetal ENABLE KEYS");

                int receiptId = Facade.createReceipt(1, 240, 600, "Testuser 1", connectionPool);

                List<OrderItem> orderItemList = PartsListCalculator.materialCalc(240, 600, 0, true, connectionPool);

                Facade.createOrder(receiptId, orderItemList, connectionPool);
            }
            catch (DatabaseException e)
            {
               fail(e.getMessage());
            }
        }
        catch (SQLException throwables)
        {
            System.out.println(throwables.getMessage());
            fail("Database connection failed");
        }
    }

    @Test
    void generate3D()
    {
        int idReceipt = 1;
        Model3D model3D = new Model3D(idReceipt, connectionPool);
        try
        {
            model3D.generate3D();
        }
        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    void generate3DFail()
    {
        // No receipt with id 4
        Model3D model3D = new Model3D(4, connectionPool);
        assertThrows(NullPointerException.class, model3D::generate3D);
    }
}