package dat.backend.model.persistence;

import dat.backend.model.entities.*;
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

    public static List<User> getAllUsers(ConnectionPool connectionPool) throws DatabaseException
    {
        return UserMapper.getAllUsers(connectionPool);
    }
    // /USER ***********************************************************************************************************

    // RECEIPT *********************************************************************************************************
    public static ArrayList<Receipt> getReceiptsByIdUser(int idUser, ConnectionPool connectionPool)
    {
        return ReceiptMapper.getReceiptsByIdUser(idUser, connectionPool);
    }

    public static int createReceipt(int idUser, double width, double length, String comment, ConnectionPool connectionPool) throws DatabaseException
    {
        return ReceiptMapper.createReceipt(idUser, width, length, comment, connectionPool);
    }


    public static void acceptReceipt(int idReceipt, ConnectionPool connectionPool)
    {
        ReceiptMapper.acceptReceipt(idReceipt, connectionPool);
    }

    public static void acceptReceiptAdmin(int idReceipt, ConnectionPool connectionPool)
    {
        ReceiptMapper.acceptReceiptAdmin(idReceipt, connectionPool);
    }

    public static void deleteReceipt(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        ReceiptMapper.deleteReceipt(idReceipt, connectionPool);
    }

    public static List<Receipt> getAllReceipts(ConnectionPool connectionPool) throws DatabaseException
    {
        return ReceiptMapper.getAllReceipts(connectionPool);
    }

    public static Receipt getReceiptById(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        return ReceiptMapper.getReceiptById(idReceipt, connectionPool);
    }
    // /RECEIPT ********************************************************************************************************

    // WOOD ************************************************************************************************************
    public static List<Wood> getWoodByVariant(String variant, ConnectionPool connectionPool) throws DatabaseException
    {
        return WoodMapper.getWoodByVariant(variant, connectionPool);
    }

    public static List<Wood> getAllWood(ConnectionPool connectionPool) throws DatabaseException
    {
        return WoodMapper.getAllWood(connectionPool);
    }

    public static void deleteWood(int idWood, ConnectionPool connectionPool) throws DatabaseException
    {
        WoodMapper.deleteWood(idWood, connectionPool);
    }

    public static void updateWoodPrice(int idWood, int price, ConnectionPool connectionPool) throws DatabaseException
    {
        WoodMapper.updateWoodPrice(idWood, price, connectionPool);
    }

    public static Wood getWoodById(int idWood, ConnectionPool connectionPool) throws DatabaseException
    {
        return WoodMapper.getWoodById(idWood, connectionPool);
    }

    public static Wood createWood(int length, int width, int height, String name, String unit, int price, String variant, ConnectionPool connectionPool) throws DatabaseException
    {
        return WoodMapper.createWood(length, width, height, name, unit, price, variant, connectionPool);
    }

    // /WOOD ***********************************************************************************************************

    public static int createOrder(int receiptId, List<WoodOrderItem> woodOrderItemList, ConnectionPool connectionPool) throws DatabaseException
    {
        return OrderMapper.createOrder(receiptId, woodOrderItemList, connectionPool);
    }

    public static List<WoodOrderItem> getWoodOrderItemsByRecieptId(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        return OrderMapper.getWoodOrderItemsByReceiptId(idReceipt, connectionPool);
    }

    public static int updateReceiptPrice(int newPrice, int idReceipt, ConnectionPool connectionPool)
    {
        return ReceiptMapper.updateReceiptPrice(newPrice, idReceipt, connectionPool);
    }

    public static void deleteOrderByReceiptId(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        OrderMapper.deleteWoodOrderByReceiptId(idReceipt, connectionPool);
    }

    public static List<MetalOrderItem> getMetalOrderItemsByReceiptId(int idReceipt, ConnectionPool connectionPool) throws DatabaseException
    {
        return MetalMapper.getMetalOrderItemsByReceiptId(idReceipt, connectionPool);
    }

    public static List<Metal> getAllMetal(ConnectionPool connectionPool) throws DatabaseException
    {
        return MetalMapper.getAllMetal(connectionPool);
    }
}
