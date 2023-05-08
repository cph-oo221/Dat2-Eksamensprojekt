package dat.backend.model.persistence;

import dat.backend.model.entities.Wood;
import dat.backend.model.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
