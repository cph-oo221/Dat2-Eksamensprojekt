package dat.backend.model.persistence;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.exceptions.DatabaseException;

import java.sql.*;

public class ReceiptMapper
{

    public static int createReceipt(int idUser, int width, int length, String comment, ConnectionPool connectionPool) throws DatabaseException
    {
        // TODO: Create receipt entry in database, and return newly created receipt id

        String sql = "INSERT INTO receipt (idUser, width, length, comment) VALUES (?, ?, ?, ?);";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idUser);
                ps.setInt(2, width);
                ps.setInt(3, length);
                ps.setString(4, comment);

                ps.executeUpdate();
            }

            try (PreparedStatement ps = connection.prepareStatement("SELECT LAST_INSERT_ID();"))
            {
                ResultSet rs = ps.executeQuery();

                if (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }

        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }

        return 0;
    }
}
