package dat.backend.model.entities;

import java.util.Objects;

public class Wood extends Material
{
    private int length;
    private int width;
    private int height;

    public Wood(int idWood, int length, int width, int height, String name, String unit, int price, String variant)
    {
        super.id = idWood;
        this.length = length;
        this.width = width;
        this.height = height;
        super.name = name;
        super.unit = unit;
        super.price = price;
        super.variant = variant;
    }

    public int getIdWood()
    {
        return id;
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
        return id == wood.id && length == wood.length && width == wood.width && height == wood.height &&
                price == wood.price && name.equals(wood.name) && unit.equals(wood.unit) && variant.equals(wood.variant);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, length, width, height, name, unit, price, variant);
    }

    @Override
    public String toString()
    {
        return "Wood{" +
                "idWood=" + id +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                ", variant='" + variant + '\'' +
                '}';
    }
}
