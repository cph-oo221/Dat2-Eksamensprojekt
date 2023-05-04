package dat.backend.model.entities;

import java.sql.Timestamp;

public class Receipt
{
    private int idReceipt;
    private int idUser;
    private Timestamp timeOfOrder;
    private boolean isComplete;

    public Receipt(int idReceipt, int idUser, Timestamp timeOfOrder, boolean isComplete)
    {
        this.idReceipt = idReceipt;
        this.idUser = idUser;
        this.timeOfOrder = timeOfOrder;
        this.isComplete = isComplete;
    }

    //dummy
    public Receipt(int idReceipt, int idUser, boolean isComplete)
    {
        this.idReceipt = idReceipt;
        this.idUser = idUser;
        this.isComplete = isComplete;
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

    public boolean isComplete()
    {
        return isComplete;
    }

    public void setComplete(boolean complete)
    {
        isComplete = complete;
    }

    @Override
    public String toString()
    {
        return "Receipt{" +
                "idReceipt=" + idReceipt +
                ", idUser=" + idUser +
                ", timeOfOrder=" + timeOfOrder +
                ", isComplete=" + isComplete +
                '}';
    }
}
