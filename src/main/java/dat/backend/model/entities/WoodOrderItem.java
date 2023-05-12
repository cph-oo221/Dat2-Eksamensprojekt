package dat.backend.model.entities;

import java.util.Objects;

public class WoodOrderItem
{
    private int amount;
    private Wood wood;
    private String description;

    public WoodOrderItem(int amount, Wood wood, String description)
    {
        this.amount = amount;
        this.wood = wood;
        this.description = description;
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
        return description;
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
        return amount == that.amount && Objects.equals(wood, that.wood) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(amount, wood, description);
    }
}
