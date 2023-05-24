package dat.backend.model.persistence;

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

public class WoodMapper
{
    static List<Wood> getWoodByVariant(String variant, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "SELECT * FROM wood WHERE variant = ?;";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, variant);

                ResultSet rs = ps.executeQuery();
                List<Wood> woods = new ArrayList<>();

                while (rs.next())
                {
                    int idWood = rs.getInt("idWood");
                    int length = rs.getInt("length");
                    int width = rs.getInt("width");
                    int height = rs.getInt("height");
                    String name = rs.getString("name");
                    String unit = rs.getString("unit");
                    int price = rs.getInt("price");

                    woods.add(new Wood(idWood, length, width, height, name, unit, price, variant));
                }

                if (woods.size() < 1)
                {
                    throw new DatabaseException("Nothing found for specified wood variant!");
                }
                return woods;
            }
        }

        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }

    static List<Wood> getAllWood(ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");
        String sql = "SELECT * FROM wood";

        List<Wood> woodList = new ArrayList<>();

        try(Connection connection = connectionPool.getConnection())
        {
            try(PreparedStatement pr = connection.prepareStatement(sql))
            {
                ResultSet rs = pr.executeQuery();
                while(rs.next())
                {
                    int idWood = rs.getInt("idWood");
                    int length = rs.getInt("length");
                    int width = rs.getInt("width");
                    int height = rs.getInt("height");
                    String name = rs.getString("name");
                    String unit = rs.getString("unit");
                    int price = rs.getInt("price");
                    String variant = rs.getString("variant");

                    woodList.add(new Wood(idWood, length, width, height, name, unit, price, variant));
                }
            }
            return woodList;
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }

    static void deleteWood(int idWood, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");

        String sql = "DELETE FROM wood WHERE idWood = ?";

        try(Connection connection = connectionPool.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idWood);
                ps.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }

    static void updateWoodPrice(int idWood, int price, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");

        String sql = "UPDATE wood SET price = ? WHERE idWood = ?";

        try(Connection connection = connectionPool.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, price);
                ps.setInt(2, idWood);
                ps.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }

    static Wood getWoodById(int idWood, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");

        String sql = "SELECT * FROM wood WHERE idWood = ?";

        try(Connection connection = connectionPool.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idWood);
                ResultSet rs = ps.executeQuery();
                if(rs.next())
                {
                    int length = rs.getInt("length");
                    int width = rs.getInt("width");
                    int height = rs.getInt("height");
                    String name = rs.getString("name");
                    String unit = rs.getString("unit");
                    int price = rs.getInt("price");
                    String variant = rs.getString("variant");

                    return new Wood(idWood, length, width, height, name, unit, price, variant);
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
        return null;
    }

    static Wood createWood(int length, int width, int height, String name, String unit, int price, String variant, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "Trying to create new product wood: " + name);


        if(length <= 0 || width <= 0 || height <= 0 || name.isEmpty() || unit.isEmpty() || price <= 0 || variant.isEmpty())
        {
            throw new DatabaseException("One or more parameters are empty or null!");
        }


        String sql = "insert into wood (length, width, height, name, unit, price, variant) values (?, ?, ?, ?, ?, ?, ?)";
        Wood wood;

        try(Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, length);
                ps.setInt(2, width);
                ps.setInt(3, height);
                ps.setString(4, name);
                ps.setString(5, unit);
                ps.setInt(6, price);
                ps.setString(7, variant);

                int rowsAffected = ps.executeUpdate();

                if(rowsAffected == 1)
                {
                    ps.close();
                    sql = "SELECT LAST_INSERT_ID()";
                    try(PreparedStatement ps2 = connection.prepareStatement(sql))
                    {
                        ResultSet rs = ps2.executeQuery();
                        if(rs.next())
                        {
                            int idWood = rs.getInt("LAST_INSERT_ID()");
                            wood = new Wood(idWood, length, width, height, name, unit, price, variant);
                        }
                        else
                        {
                            throw new DatabaseException("No ID was found!");
                        }
                    }
                }
                else
                {
                    throw new DatabaseException("This wood: " + name + ", could not be insert in to the database");
                }
            }
        }
        catch (SQLException | DatabaseException e)
        {
            throw new DatabaseException(e.getMessage());
        }
        return wood;
    }
}
