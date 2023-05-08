package dat.backend.model.entities;

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
        return name;
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
}
