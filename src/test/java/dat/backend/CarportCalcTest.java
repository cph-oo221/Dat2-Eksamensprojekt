package dat.backend;

import dat.backend.model.config.Env;
import dat.backend.model.entities.Wood;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarportCalcTest
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
    }

    @Test
    void calcRemTest()
    {
        int width = 240;
        int length = 600;
        int remAmount = 2;

        try
        {
            Wood expected = new Wood(1, 410, 55, 20, "Spærtræ 410x55x20", "stk", 200, "Rem");

            List<Wood> woods = Facade.getWoodByVariant("Rem", connectionPool);

            woods.sort(new Comparator<Wood>()
            {
                @Override
                public int compare(Wood s, Wood t1)
                {
                    return t1.getLength() - s.getLength();
                }
            });

            Wood rem = selectRem(woods, length);

            while (rem == null)
            {
                length = length / 2;
                rem = selectRem(woods, length);
                remAmount += 2;
            }


            assertNotNull(rem);
            assertEquals(4, remAmount);
            assertEquals(expected, rem);

        }

        catch (DatabaseException e)
        {
            fail(e.getMessage());
        }
    }

    private Wood selectRem(List<Wood> woods, int length)
    {
        Wood buffer = null;
        for (Wood w: woods)
        {
            if (w.getLength() >= length)
            {
                buffer = w;
            }

            else
            {
                return buffer;
            }
        }
        return null;
    }
}
