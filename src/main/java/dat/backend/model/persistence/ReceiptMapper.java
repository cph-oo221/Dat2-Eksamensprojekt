package dat.backend.model.persistence;
import dat.backend.model.entities.OrderState;
import dat.backend.model.entities.Receipt;
import dat.backend.model.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiptMapper
{
    static ArrayList<Receipt> getReceiptsByIdUser(int idUser, ConnectionPool connectionPool) throws DatabaseException
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
                    Timestamp time = rs.getTimestamp("timeOfOrder");
                    int intOrder = rs.getInt("orderstate");
                    OrderState orderstate =  OrderState.intToOrder(intOrder);
                    String comment = rs.getString("comment");
                    int width = rs.getInt("width");
                    int length = rs.getInt("length");
                    receipt = new Receipt(idReceipt, idUser, time, orderstate, width, length, price, comment);
                    receiptList.add(receipt);
                }
                Collections.reverse(receiptList);
                return receiptList;
            }
        }
        catch (SQLException throwables)
        {
            throw new DatabaseException(throwables.getMessage());
        }
    }

    static int createReceipt(int idUser, double width, double length, String comment, ConnectionPool connectionPool) throws DatabaseException
    {
        // TODO: Create receipt entry in database, and return newly created receipt id

        String sql = "INSERT INTO receipt (idUser, width, length, comment) VALUES (?, ?, ?, ?);";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idUser);
                ps.setInt(2, (int) width);
                ps.setInt(3, (int) length);
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

    static void acceptReceipt(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
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
            throw new DatabaseException(throwables.getMessage());
        }
    }

    static void acceptReceiptAdmin(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");

        String sql = "UPDATE receipt SET orderstate = 1 WHERE idReceipt = ?";

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
            throw new DatabaseException(throwables.getMessage());
        }
    }

    static void deleteReceipt(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
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
            throw new DatabaseException(throwables.getMessage());
        }
    }


    static List<Receipt> getAllReceipts(ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "Trying to get all receipts from database...");
        List<Receipt> receiptList = new ArrayList<>();

        String sql = "SELECT * FROM receipt";

        try(Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                {
                    int idReceipt = rs.getInt("idReceipt");
                    int idUser = rs.getInt("idUser");
                    int width = rs.getInt("width");
                    int length = rs.getInt("length");
                    int price = rs.getInt("price");
                    Timestamp time = rs.getTimestamp("timeOfOrder");
                    OrderState orderstate = OrderState.intToOrder(rs.getInt("orderstate"));
                    String comment = rs.getString("comment");

                    receiptList.add(new Receipt(idReceipt, idUser, time, orderstate, width, length, price, comment));
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
        Collections.reverse(receiptList);
        return receiptList;
    }

    static int updateReceiptPrice(int newPrice, int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "Trying to update price on receipt");

        String sql = "UPDATE receipt SET price = ? WHERE idreceipt = ?";
        try(Connection connection = connectionPool.getConnection())
        {
            try(PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, newPrice);
                ps.setInt(2, idReceipt);

                int rowsAffected = ps.executeUpdate();
                return rowsAffected;
            }
        }
        catch (SQLException throwables)
        {
            throw new DatabaseException(throwables.getMessage());
        }
    }

    static Receipt getReceiptById(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "SELECT * FROM receipt WHERE idreceipt = ?;";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, idReceipt);

                ResultSet rs = ps.executeQuery();

                if (rs.next())
                {
                    int idUser = rs.getInt("iduser");
                    Timestamp time = rs.getTimestamp("timeoforder");
                    OrderState orderstate = OrderState.intToOrder(rs.getInt("orderstate"));
                    int width = rs.getInt("width");
                    int length = rs.getInt("length");
                    int price = rs.getInt("price");
                    String comment = rs.getString("comment");

                    return new Receipt(idReceipt, idUser, time, orderstate, width, length, price, comment);
                }
            }
        }

        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
        return null;
    }
}
