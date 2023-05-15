package dat.backend.model.entities;

public class OrderItem
{
    public OrderItem(int amount, Material item, String desc)
    {
        this.amount = amount;
        this.item = item;
        this.desc = desc;
    }

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
