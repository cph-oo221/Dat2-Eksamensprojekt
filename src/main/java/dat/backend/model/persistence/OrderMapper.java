package dat.backend.model.persistence;

import dat.backend.model.entities.WoodOrderItem;
import dat.backend.model.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderMapper
{
    protected static int createOrder(int receiptId, List<WoodOrderItem> woodOrderItemList, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "INSERT INTO orderwood (idreceipt, idwood, amount, description) VALUES (?, ?, ?, ?);";

        try (Connection connection = connectionPool.getConnection())
        {
            for (WoodOrderItem w : woodOrderItemList)
            {
                try (PreparedStatement ps = connection.prepareStatement(sql))
                {
                    ps.setInt(1, receiptId);
                    ps.setInt(2, w.getWood().getIdWood());
                    ps.setInt(3, w.getAmount());
                    ps.setString(4, w.getDescription());

                    ps.executeUpdate();
                }
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
        catch (SQLException throwables)
        {
            throw new DatabaseException(throwables.getMessage());
        }
        return 0; //Fejlhåndtering
    }

   /* protected static List<WoodOrderItem> getWoodOrderItemsByReceiptId(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "SELECT * FROM orderwood WHERE idreceipt = ?;";

        try (Connection connection = connectionPool.getConnection())
        {
            try ()
        }

        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }*/
}
