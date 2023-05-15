package dat.backend.model.entities;

public class Metal extends Material
{

    public Metal(int idMetal, String name, int price, String unit, String variant)
    {
        super.id = idMetal;
        super.name = name;
        super.price = price;
        super.unit = unit;
        super.variant = variant;
    }

    public int getIdMetal()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }

    public int getPrice()
    {
        return price;
    }

    public String getUnit()
    {
        return unit;
    }

    public String getVariant()
    {
        return variant;
    }



    @Override
    public String toString()
    {
        return "Metal{" + "idMetal=" + id + ", name=" + name + ", price=" + price + ", unit=" + unit + ", variant=" + variant + '}';
    }

    @Override
    public boolean isInstance(Object o)
    {
        return o instanceof Metal;
    }
}
