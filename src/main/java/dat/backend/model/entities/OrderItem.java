package dat.backend.model.entities;

public class OrderItem
{
    private int amount;
    private String desc;
    private Material item;

    public OrderItem(int amount, Material item, String desc)
    {
        this.amount = amount;
        this.item = item;
        this.desc = desc;
    }

    public int getAmount()
    {
        return amount;
    }

    public String getDesc()
    {
        return desc;
    }

    public Material getMaterial()
    {
        return item;
    }
}
