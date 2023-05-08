package dat.backend.model.persistence;
import dat.backend.model.config.ApplicationStart;
import dat.backend.model.entities.OrderState;
import dat.backend.model.entities.Receipt;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiptMapper
{
    protected static ArrayList<Receipt> getReceiptsByIdUser(int idUser, ConnectionPool connectionPool)
    {
        Logger.getLogger("web").log(Level.INFO,"");
        Receipt receipt;

        String sql = "SELECT * FROM receipt WHERE iduser = ?";

        try(Connection connection = connectionPool.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idUser);
                ResultSet rs = ps.executeQuery();
                ArrayList<Receipt> receiptList = new ArrayList<>();
                while(rs.next())
                {
                    int idReceipt = rs.getInt("idReceipt");
                    int price = rs.getInt("price");
                    Timestamp timeOfOrder = rs.getTimestamp("timeOfOrder");
                    int intOrder = rs.getInt("orderstate");
                    OrderState orderState =  OrderState.intToOrder(intOrder);
                    String comment = rs.getString("comment");
                    int width = rs.getInt("width");
                    int length = rs.getInt("length");
                    receipt = new Receipt(idReceipt, idUser, width, length, price, timeOfOrder, orderState, comment);
                    receiptList.add(receipt);
                }
                return receiptList;
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        //Todo fix here so error is handled.
        return null;
    }

    protected static void acceptReceipt(int idReceipt, ConnectionPool connectionPool)
    {
        int idUser;
        Logger.getLogger("web").log(Level.INFO, "");

        String sql = "UPDATE receipt SET orderstate = 2 WHERE idReceipt = ?";

        try(Connection connection = connectionPool.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idReceipt);
                ps.executeUpdate();
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public static void deleteReceipt(int idReceipt, ConnectionPool connectionPool)
    {
        Logger.getLogger("web").log(Level.INFO, "");

        String sql = "DELETE FROM receipt WHERE idreceipt = ?";

        try(Connection connection = connectionPool.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idReceipt);
                ps.executeUpdate();
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }
}
