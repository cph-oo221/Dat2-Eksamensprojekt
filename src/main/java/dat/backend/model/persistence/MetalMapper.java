package dat.backend.model.persistence;

import dat.backend.model.entities.Metal;
import dat.backend.model.entities.MetalOrderItem;
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

public class MetalMapper
{
    public static List<MetalOrderItem> getMetalOrderItemsByReceiptId(int idReceipt, ConnectionPool connectionPool)
    {
        return null;
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
}
