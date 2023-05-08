package dat.backend.model.persistence;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.config.Env;
import dat.backend.model.entities.Receipt;
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
                stmt.execute("insert into fog_test.user VALUES " +
                        "(1, 'user@user.com','user','user', 'uservej 1', 'Vice city', 12345678)," +
                        "(2, 'admin@admin.com','admin','admin', 'adminvej 2', 'San Andreas', 87654321)");
                stmt.execute("INSERT INTO receipt (idUser, width, length, comment) VALUES" +
                    "(1, 240, 420, 'hej')," +
                    "(2, 420, 240, 'hej1')");
                stmt.execute("ALTER TABLE fog_test.user ENABLE KEYS");
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

        ArrayList<Receipt> receiptList = Facade.getReceiptsByIdUser(1, connectionPool);

        assertEquals(receiptList.get(0).getIdUser(), idUser1);



    }

    @Test
    void createReceipt()
    {
    }

    @Test
    void acceptReceipt()
    {
    }

    @Test
    void deleteReceipt()
    {
    }


    @Test
    void getAllReceipts() throws DatabaseException
    {
        List<Receipt> receiptList = Facade.getAllReceipts(connectionPool);

        receiptList.forEach(System.out::println);

        assertEquals(2, receiptList.size());
    }
}