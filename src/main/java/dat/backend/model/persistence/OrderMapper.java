package dat.backend.model.persistence;

import dat.backend.model.entities.Metal;
import dat.backend.model.entities.OrderItem;
import dat.backend.model.entities.Wood;
import dat.backend.model.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class OrderMapper
{
    static int createOrder(int receiptId, List<OrderItem> orderItemList, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "Saving orderlist for receipt: " + receiptId);

        String sqlw = "INSERT INTO orderwood (idreceipt, idwood, amount, description) VALUES (?, ?, ?, ?);";
        String sqlm = "INSERT INTO ordermetal (idreceipt, idmetal, amount, description) VALUES (?, ?, ?, ?);";
        int rowsAffected = 0;

        try (Connection connection = connectionPool.getConnection())
        {
            for (OrderItem i : orderItemList)
            {
                PreparedStatement ps;

                if (i.getMaterial() instanceof Wood)
                {
                    ps = connection.prepareStatement(sqlw);
                }
                else
                {
                    ps = connection.prepareStatement(sqlm);
                }

                ps.setInt(1, receiptId);
                ps.setInt(2, i.getMaterial().getId());
                ps.setInt(3, i.getAmount());
                ps.setString(4, i.getDesc());

                rowsAffected += ps.executeUpdate();
            }
            if (rowsAffected < 1)
            {
                Logger.getLogger("web").log(Level.SEVERE, "No DB rows were updated for " + receiptId);
                throw new DatabaseException("Nothing was written to ordermetal/orderwood with receiptId " + receiptId);
            }
        }
        catch (SQLException throwables)
        {
            throw new DatabaseException(throwables.getMessage());
        }
        return rowsAffected; //Fejlhåndtering
    }

    static List<OrderItem> getWoodOrderItemsByReceiptId(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "Fetching items from receipt: " + idReceipt);

        String sql = "SELECT * FROM orderwood\n" +
                "JOIN wood w on orderwood.idwood = w.idwood\n" +
                "WHERE idreceipt = ?;";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idReceipt);

                ResultSet rs = ps.executeQuery();
                List<OrderItem> orderItems = new ArrayList<>();

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


                    OrderItem wo = new OrderItem(amount, w, description);
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

    static void deleteWoodOrderByReceiptId(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
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

    static void deleteMetalOrderByReceiptID(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "DELETE FROM ordermetal WHERE idreceipt = ?;";

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

    static List<OrderItem> getMetalOrderItemsByReceiptId(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO,"Fetching items from receipt: " + idReceipt);

        String sql = "SELECT * FROM ordermetal\n" +
                "JOIN metal m on ordermetal.idmetal = m.idmetal\n" +
                "WHERE idreceipt = ?;";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idReceipt);

                ResultSet rs = ps.executeQuery();
                List<OrderItem> orderItems = new ArrayList<>();

                while (rs.next())
                {
                    int idMetal = rs.getInt("idmetal");
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    String unit = rs.getString("unit");
                    String variant = rs.getString("variant");

                    Metal metal = new Metal(idMetal, name, price, unit, variant);

                    int amount = rs.getInt("amount");
                    String description = rs.getString("description");

                    OrderItem metalOrderItem = new OrderItem(amount, metal, description);
                    orderItems.add(metalOrderItem);
                }
                return orderItems;
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }
}
