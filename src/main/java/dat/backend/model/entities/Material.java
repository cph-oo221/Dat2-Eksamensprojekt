package dat.backend.model.entities;

import java.util.Objects;

public abstract class Material
{
    protected int id;
    protected String name;
    protected int price;
    protected String unit;
    protected String variant;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getVariant()
    {
        return variant;
    }

    public void setVariant(String variant)
    {
        this.variant = variant;
    }

    public abstract boolean isInstance(Object o);

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return id == material.id && price == material.price && name.equals(material.name) && unit.equals(material.unit) && variant.equals(material.variant);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, price, unit, variant);
    }
}
