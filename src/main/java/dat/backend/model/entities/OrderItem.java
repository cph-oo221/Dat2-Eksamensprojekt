package dat.backend.model.entities;

public class OrderItem
{
    protected int amount;
    protected String desc;
    protected Material item;

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

    public Material getMaterial()
    {
        return item;
    }
}
