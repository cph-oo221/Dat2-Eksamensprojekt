package dat.backend.model.entities;

import dat.backend.model.entities.OrderItem;

import java.util.Objects;

public class MetalOrderItem extends OrderItem
{
    int amount;
    Metal metal;
    String desc;

    @Override
    public String toString()
    {
        return "MetalOrderItem{" +
                "amount=" + amount +
                ", metal=" + metal +
                ", desc='" + desc + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetalOrderItem that = (MetalOrderItem) o;
        return amount == that.amount && Objects.equals(metal, that.metal) && Objects.equals(desc, that.desc);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(amount, metal, desc);
    }

    @Override
    public int getAmount()
    {
        return amount;
    }

    @Override
    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public Metal getMetal()
    {
        return metal;
    }

    public void setMetal(Metal metal)
    {
        this.metal = metal;
    }

    @Override
    public String getDesc()
    {
        return desc;
    }

    @Override
    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public MetalOrderItem(int amount, Metal metal, String desc)
    {
        this.amount = amount;
        this.metal = metal;
        this.desc = desc;
    }
}
