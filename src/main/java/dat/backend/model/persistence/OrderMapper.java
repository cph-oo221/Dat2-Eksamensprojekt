package dat.backend.model.persistence;

import dat.backend.model.entities.Wood;
import dat.backend.model.entities.WoodOrderItem;
import dat.backend.model.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderMapper
{
    protected static int createOrder(int receiptId, List<WoodOrderItem> woodOrderItemList, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO,"Saving orderlist for receipt: " + receiptId);

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

    protected static List<WoodOrderItem> getWoodOrderItemsByReceiptId(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO,"Fetching items from receipt: " + idReceipt);

        String sql = "SELECT * FROM orderwood\n" +
                "JOIN wood w on orderwood.idwood = w.idwood\n" +
                "WHERE idreceipt = ?;";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idReceipt);

                ResultSet rs = ps.executeQuery();
                List<WoodOrderItem> orderItems = new ArrayList<>();

                while (rs.next())
                {
                    int idWood = rs.getInt("idwood");
                    int length = rs.getInt("length");
                    int width = rs.getInt("width");
                    int height = rs.getInt("height");
                    String name = rs.getString("name");
                    String unit = rs.getString("unit");
                    int price = rs.getInt("price");
                    String variant = rs.getString("variant");

                    Wood w = new Wood(idWood, length, width, height, name, unit, price, variant);

                    int amount = rs.getInt("amount");
                    String description = rs.getString("description");


                    WoodOrderItem wo = new WoodOrderItem(amount, w, description);
                    orderItems.add(wo);
                }
                return orderItems;
            }
        }

        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }

    protected static void deleteWoodOrderByReceiptId(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "DELETE FROM orderwood WHERE idreceipt = ?;";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idReceipt);

                ps.executeUpdate();
            }
        }

        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }
}
