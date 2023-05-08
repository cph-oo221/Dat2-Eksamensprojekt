package dat.backend.model.persistence;

import dat.backend.model.entities.OrderState;
import dat.backend.model.entities.Receipt;
import dat.backend.model.entities.User;
import dat.backend.model.exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.ArrayList;

public class Facade
{
    public static ArrayList<Receipt> getReceiptsByIdUser(int idUser, ConnectionPool connectionPool)
    {
        /*dummy
        ArrayList<Receipt> receiptsList = new ArrayList<>();
        receiptsList.add(new Receipt(1, 1, OrderState.OPEN));
        receiptsList.add(new Receipt(2, 1, OrderState.OFFER));
        receiptsList.add(new Receipt(3, 1, OrderState.COMPLETE));
        return receiptsList;
         */
        return ReceiptMapper.getReceiptsByIdUser(idUser, connectionPool);
    }

    public static User login(String username, String password, ConnectionPool connectionPool) throws DatabaseException
    {
        //dummy
        return new User(2, "test@test.com", "test", "user", "testvej", "testby", 12341234);
        /*
        try
        {
            return UserMapper.login(username, password, connectionPool);
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }
        return null;

         */
    }

    public static int createReceipt(int idUser, int width, int length, String comment) throws DatabaseException
    {
        return ReceiptMapper.createReceipt(idUser, width, length, comment);
    }


    public static User createUser(String email, String password, String address, String city, int phoneNumber, String role) throws SQLException, DatabaseException
    {
        return UserMapper.createUser(email, password, address, city, phoneNumber, role);
    }

    public static User getUserByEmail(String email) throws DatabaseException
    {
        return UserMapper.getUserByEmail(email);
    }

    public static void acceptReceipt(int idReceipt, ConnectionPool connectionPool)
    {
        //dummy
        //return new Receipt(2, 1, OrderState.COMPLETE);

        ReceiptMapper.acceptReceipt(idReceipt, connectionPool);
    }

    public static void deleteReceipt(int idReceipt, ConnectionPool connectionPool)
    {
        ReceiptMapper.deleteReceipt(idReceipt, connectionPool);
    }
}
