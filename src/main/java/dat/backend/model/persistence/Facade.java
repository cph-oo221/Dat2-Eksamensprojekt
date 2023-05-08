package dat.backend.model.persistence;

import dat.backend.model.entities.OrderState;
import dat.backend.model.entities.Receipt;
import dat.backend.model.entities.User;
import dat.backend.model.entities.Wood;
import dat.backend.model.exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Facade
{
    // USER ************************************************************************************************************
    public static User login(String username, String password, ConnectionPool connectionPool) throws DatabaseException
    {
        return UserMapper.login(username, password, connectionPool);
    }

    public static User createUser(String email, String password, String address, String city, int phoneNumber, String role, ConnectionPool connectionPool) throws SQLException, DatabaseException
    {
        return UserMapper.createUser(email, password, address, city, phoneNumber, role, connectionPool);
    }

    public static User getUserByEmail(String email, ConnectionPool connectionPool) throws DatabaseException
    {
        return UserMapper.getUserByEmail(email, connectionPool);
    }
    // /USER ***********************************************************************************************************

    // RECEIPT *********************************************************************************************************
    public static ArrayList<Receipt> getReceiptsByIdUser(int idUser, ConnectionPool connectionPool)
    {
        return ReceiptMapper.getReceiptsByIdUser(idUser, connectionPool);
    }

    public static int createReceipt(int idUser, int width, int length, String comment, ConnectionPool connectionPool) throws DatabaseException
    {
        return ReceiptMapper.createReceipt(idUser, width, length, comment, connectionPool);
    }


    public static void acceptReceipt(int idReceipt, ConnectionPool connectionPool)
    {
        ReceiptMapper.acceptReceipt(idReceipt, connectionPool);
    }

    public static void deleteReceipt(int idReceipt, ConnectionPool connectionPool)
    {
        ReceiptMapper.deleteReceipt(idReceipt, connectionPool);
    }

    // /RECEIPT ********************************************************************************************************

    public static List<Wood> getWoodByVariant(String variant, ConnectionPool connectionPool) throws DatabaseException
    {
        return WoodMapper.getWoodByVariant(variant, connectionPool);
    }
}
