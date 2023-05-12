package dat.backend.model.entities;

import java.util.Objects;

public class WoodOrderItem extends OrderItem
{
    private int amount;
    private Wood wood;
    private String desc;

    public WoodOrderItem(int amount, Wood wood, String desc)
    {
        this.amount = amount;
        this.wood = wood;
        this.desc = desc;
    }

    public int getAmount()
    {
        return amount;
    }

    public Wood getWood()
    {
        return wood;
    }

    public String getDescription()
    {
        return desc;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WoodOrderItem that = (WoodOrderItem) o;
        return amount == that.amount && Objects.equals(wood, that.wood) && Objects.equals(desc, that.desc);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(amount, wood, desc);
    }
}
