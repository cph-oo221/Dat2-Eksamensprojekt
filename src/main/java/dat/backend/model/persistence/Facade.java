package dat.backend.model.persistence;

import dat.backend.model.entities.OrderState;
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
        OrderState open = OrderState.OPEN;
        OrderState offer = OrderState.OFFER;
        OrderState complete = OrderState.COMPLETE;
        receiptsList.add(new Receipt(1, 1, open));
        receiptsList.add(new Receipt(2, 1, offer));
        receiptsList.add(new Receipt(3, 1, complete));
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
}
