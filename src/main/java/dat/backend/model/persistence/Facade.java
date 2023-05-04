package dat.backend.model.persistence;

import dat.backend.model.entities.Receipt;
import dat.backend.model.entities.User;
import dat.backend.model.exceptions.DatabaseException;

import java.util.ArrayList;

public class Facade
{
    public static ArrayList<Receipt> getReceiptsByIdUser(int idUser, ConnectionPool connectionPool)
    {
        //dummy
        ArrayList<Receipt> receiptsList = new ArrayList<>();
        receiptsList.add(new Receipt(1, 1, false));
        receiptsList.add(new Receipt(2, 1, true));
        return receiptsList;
    }

    public static User login(String username, String password, ConnectionPool connectionPool) throws DatabaseException
    {
        //dummy
        return new User(1, "test@test.com", "test", "user", "testvej", "testby", 12341234, 4130);
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

    public static User createUser(String email, String password, String address, String city, int zipCode, int phoneNumber, String role)
    {
        return UserMapper.createUser(email, password, address, city, zipCode, phoneNumber, role);
    }
}
