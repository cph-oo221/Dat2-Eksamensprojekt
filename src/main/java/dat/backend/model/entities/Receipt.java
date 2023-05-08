package dat.backend.model.entities;

import java.sql.Timestamp;

public class Receipt
{
    private int width, length, idReceipt, idUser, price;
    private Timestamp timeOfOrder;
    private OrderState orderstate;
    private String comment;


    public Receipt(int idReceipt, int idUser, int width, int length, int price, Timestamp timeOfOrder, OrderState orderstate, String comment)
    {
        this.idReceipt = idReceipt;
        this.idUser = idUser;
        this.width = width;
        this.length = length;
        this.price = price;
        this.timeOfOrder = timeOfOrder;
        this.orderstate = orderstate;
        this.comment = comment;
    }

    //dummy
    public Receipt(int idReceipt, int idUser, OrderState orderstate)
    {
        this.idReceipt = idReceipt;
        this.idUser = idUser;
        this.orderstate = orderstate;
    }

    public int getIdReceipt()
    {
        return idReceipt;
    }

    public void setIdReceipt(int idReceipt)
    {
        this.idReceipt = idReceipt;
    }

    public int getIdUser()
    {
        return idUser;
    }

    public void setIdUser(int idUser)
    {
        this.idUser = idUser;
    }

    public Timestamp getTimeOfOrder()
    {
        return timeOfOrder;
    }

    public void setTimeOfOrder(Timestamp timeOfOrder)
    {
        this.timeOfOrder = timeOfOrder;
    }

    public OrderState getOrderState() { return orderstate; }

    public void setOrderState(OrderState orderstate) { this.orderstate = orderstate; }

    public int getPrice()
    {
        return price;
    }

    @Override
    public String toString()
    {
        return "Receipt{" +
                "idReceipt=" + idReceipt +
                ", idUser=" + idUser +
                ", timeOfOrder=" + timeOfOrder +
                ", orderState=" + orderstate +
                '}';
    }


}
