package dat.backend.model.persistence;

import dat.backend.model.entities.Metal;
import dat.backend.model.entities.MetalOrderItem;
import dat.backend.model.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MetalMapper
{
    public static List<MetalOrderItem> getMetalOrderItemsByReceiptId(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO,"Fetching items from receipt: " + idReceipt);

        String sql = "SELECT * FROM ordermetal\n" +
                "JOIN metalstuff m on ordermetal.idmetalstuff = m.idmetalstuff\n" +
                "WHERE idreceipt = ?;";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idReceipt);

                ResultSet rs = ps.executeQuery();
                List<MetalOrderItem> orderItems = new ArrayList<>();

                while (rs.next())
                {
                    int idMetal = rs.getInt("idmetalstuff");
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    String unit = rs.getString("unit");
                    String variant = rs.getString("variant");

                    Metal metal = new Metal(idMetal, name, price, unit, variant);

                    int amount = rs.getInt("amount");
                    String description = rs.getString("description");

                    MetalOrderItem metalOrderItem = new MetalOrderItem(amount, metal, description);
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

    public static List<Metal> getAllMetal(ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");
        String sql = "SELECT * FROM metalstuff";

        List<Metal> metalList = new ArrayList<>();

        try(Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement pr = connection.prepareStatement(sql))
            {
                ResultSet rs = pr.executeQuery();
                while(rs.next())
                {
                    int idMetal = rs.getInt("idmetalstuff");
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    String unit = rs.getString("unit");
                    String variant = rs.getString("variant");

                    metalList.add(new Metal(idMetal, name, price, unit, variant));
                }
            }
            return metalList;
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static void updateMetalPrice(int idMetal, int price, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");

        String sql = "UPDATE metalstuff SET price = ? WHERE idmetalstuff = ?";

        try(Connection connection = connectionPool.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, price);
                ps.setInt(2, idMetal);
                ps.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static void deleteMetal(int idMetal, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");

        String sql = "DELETE FROM metalstuff WHERE idmetalstuff = ?";

        try(Connection connection = connectionPool.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idMetal);
                ps.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static Metal createMetal(String name, int price, String unit, String variant, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "Trying to create new metal: " + name + " and insert it to the database...");

        if(price <= 0 || name.isEmpty() || unit.isEmpty() || variant.isEmpty())
        {
            throw new DatabaseException("One or more parameters are empty or null!");
        }

        String sql = "INSERT INTO metalstuff (name, price, unit, variant) VALUES (?, ?, ?, ?)";
        Metal metal;

        try(Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, name);
                ps.setInt(2, price);
                ps.setString(3, unit);
                ps.setString(4, variant);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected == 1)
                {
                    ps.close();
                    sql = "SELECT LAST_INSERT_ID()";
                    try(PreparedStatement ps2 = connection.prepareStatement(sql))
                    {
                        ResultSet rs = ps2.executeQuery();
                        if(rs.next())
                        {
                            int idMetal = rs.getInt("LAST_INSERT_ID()");
                            metal = new Metal(idMetal, name, price, unit, variant);
                        }
                        else
                        {
                            throw new DatabaseException("No ID was found!");
                        }
                    }
                }
                else
                {
                    throw new DatabaseException("This metal: " + name + ", could not be insert in to the database");
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
        return metal;
    }
}
