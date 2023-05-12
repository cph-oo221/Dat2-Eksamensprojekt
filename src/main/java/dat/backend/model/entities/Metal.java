package dat.backend.model.entities;

import java.util.Objects;

public class Metal
{
    private int idMetal;
    private String name;
    private int price;
    private String unit;
    private String variant;

    public Metal(int idMetal, String name, int price, String unit, String variant)
    {
        this.idMetal = idMetal;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.variant = variant;
    }

    public int getIdMetal()
    {
        return idMetal;
    }

    public void setIdMetal(int idMetal)
    {
        this.idMetal = idMetal;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metal metal = (Metal) o;
        return idMetal == metal.idMetal && price == metal.price && name.equals(metal.name) && unit.equals(metal.unit) && variant.equals(metal.variant);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(idMetal, name, price, unit, variant);
    }

    @Override
    public String toString()
    {
        return "Metal{" + "idMetal=" + idMetal + ", name=" + name + ", price=" + price + ", unit=" + unit + ", variant=" + variant + '}';
    }
}
