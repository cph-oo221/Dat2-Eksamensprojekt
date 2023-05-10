package dat.backend.model.entities;

import java.util.Objects;

public class Wood
{
    private int idWood;
    private int length;
    private int width;
    private int height;
    private String name;
    private String unit;
    private int price;
    private String variant;

    public Wood(int idWood, int length, int width, int height, String name, String unit, int price, String variant)
    {
        this.idWood = idWood;
        this.length = length;
        this.width = width;
        this.height = height;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.variant = variant;
    }

    public int getIdWood()
    {
        return idWood;
    }

    public int getLength()
    {
        return length;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public String getName()
    {
        return name + " " + length + "x" + width + "x" + height;
    }

    public String getUnit()
    {
        return unit;
    }

    public int getPrice()
    {
        return price;
    }

    public String getVariant()
    {
        return variant;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wood wood = (Wood) o;
        return idWood == wood.idWood && length == wood.length && width == wood.width && height == wood.height &&
                price == wood.price && name.equals(wood.name) && unit.equals(wood.unit) && variant.equals(wood.variant);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(idWood, length, width, height, name, unit, price, variant);
    }
}
