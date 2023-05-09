package dat.backend.model.persistence;

import dat.backend.model.entities.User;
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
    protected static List<Wood> getWoodByVariant(String variant, ConnectionPool connectionPool) throws DatabaseException
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

    public static List<Wood> getAllWood(ConnectionPool connectionPool) throws DatabaseException
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

    public static void deleteWood(int idWood, ConnectionPool connectionPool)
    {
        Logger.getLogger("web").log(Level.INFO, "");
        // first delete all orderwood containing the wood

        String sql = "DELETE FROM orderwood WHERE idWood = ?";

        try(Connection connection = connectionPool.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idWood);
                ps.executeUpdate();
                connection.close();

                sql = "DELETE FROM wood WHERE idWood = ?";
                try(PreparedStatement ps2 = connection.prepareStatement(sql))
                {
                    ps2.setInt(1, idWood);
                    ps2.executeUpdate();
                }
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }
}
