package dat.backend.model.persistence;

import dat.backend.model.config.Env;
import dat.backend.model.entities.OrderState;
import dat.backend.model.entities.Receipt;
import dat.backend.model.entities.User;
import dat.backend.model.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptMapperTest
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
                stmt.execute("delete from fog_test.user");
                stmt.execute("ALTER TABLE fog_test.user DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.user AUTO_INCREMENT = 1");
                stmt.execute("ALTER TABLE fog_test.receipt DISABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.receipt AUTO_INCREMENT = 1");
                stmt.execute("insert into fog_test.user VALUES " +
                        "(1, 'user@user.com','user','user', 'uservej 1', 'Vice city', 12345678)," +
                        "(2, 'admin@admin.com','admin','admin', 'adminvej 2', 'San Andreas', 87654321)");
                stmt.execute("INSERT INTO receipt (idUser, width, length, comment) VALUES" +
                    "(1, 240, 420, 'hej')," +
                    "(2, 420, 240, 'hej1')");
                stmt.execute("ALTER TABLE fog_test.user ENABLE KEYS");
                stmt.execute("ALTER TABLE fog_test.receipt ENABLE KEYS");
            }
        } catch (SQLException throwables)
        {
            System.out.println(throwables.getMessage());
            fail("Database connection failed");
        }
    }


    @Test
    void getReceiptsByIdUser()
    {
        int idUser1 = 1;
        int width1 = 240;
        int length1 = 420;
        String comment1 = "hej";

        int idUser2 = 2;
        int width2 = 420;
        int length2 = 240;
        String comment2 = "hej2";

        ArrayList<Receipt> receiptList = null;
        try
        {
            receiptList = Facade.getReceiptsByIdUser(1, connectionPool);
        }
        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }

        assertEquals(receiptList.get(0).getIdUser(), idUser1);
    }

    @Test
    void createReceipt() throws DatabaseException
    {
        User user = Facade.createUser("joe", "2343242343", "lyngby vej 23", "Lyngby", 23432434, "user", connectionPool);

        assertEquals(3, Facade.createReceipt(user.getIdUser(), 300, 600, "Hurtigts mugligt", connectionPool));
    }

    @Test
    void acceptReceiptUser() throws DatabaseException
    {
        Facade.acceptReceipt(1, connectionPool);

        Receipt receipt = Facade.getReceiptById(1, connectionPool);

        assertEquals(OrderState.COMPLETE, receipt.getOrderState());
    }

    @Test
    void acceptReceiptAdmin() throws DatabaseException
    {
        Facade.acceptReceipt(1, connectionPool);

        Receipt receipt = Facade.getReceiptById(1, connectionPool);

        assertEquals(OrderState.COMPLETE, receipt.getOrderState());
    }

    @Test
    void deleteReceipt() throws DatabaseException
    {
        assertEquals(2, Facade.getAllReceipts(connectionPool).size());

        Facade.deleteReceipt(1, connectionPool);

        assertEquals(1, Facade.getAllReceipts(connectionPool).size());
    }


    @Test
    void getAllReceipts() throws DatabaseException
    {
        List<Receipt> receiptList = Facade.getAllReceipts(connectionPool);

        receiptList.forEach(System.out::println);
        int expectedSize = 2;
        assertEquals(expectedSize, receiptList.size());
    }
}