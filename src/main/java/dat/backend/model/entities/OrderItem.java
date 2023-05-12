package dat.backend.model.entities;

public abstract class OrderItem
{
    private int idReceipt, amount;
    private String desc;

    public int getIdReceipt()
    {
        return idReceipt;
    }

    public void setIdReceipt(int idReceipt)
    {
        this.idReceipt = idReceipt;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}
