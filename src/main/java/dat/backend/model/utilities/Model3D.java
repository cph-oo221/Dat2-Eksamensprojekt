package dat.backend.model.utilities;

import dat.backend.model.entities.OrderItem;
import dat.backend.model.entities.Receipt;
import dat.backend.model.entities.Wood;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.persistence.Facade;
import org.abstractica.javacsg.Geometry3D;
import org.abstractica.javacsg.JavaCSG;
import org.abstractica.javacsg.JavaCSGFactory;

import java.util.ArrayList;
import java.util.List;

public class Model3D
{
    private ConnectionPool connectionPool;
    private JavaCSG csg = JavaCSGFactory.createDefault();
    private Receipt receipt;
    private double widthmm;
    private double lengthmm;
    private double offsetZ = 0;
    private int receiptID;


    public Model3D(int receiptID, ConnectionPool connectionPool)
    {
        this.receiptID = receiptID;
        this.connectionPool = connectionPool;
    }

/**
 * The final method of the class. Implements all the other methods
 * @throws DatabaseException
 * @see Facade#getWoodOrderItemsByReceiptId(int, ConnectionPool) for the mapper method
 * @see #getRoofModel(List, double, double, JavaCSG)  for the method generating roofs
 * @see #getSternModel(List, double, double, JavaCSG)  for the method generating sterns
 * @see #getRafterModel(List, double, double, JavaCSG)  for the method generating rafters
 * @see #getRemModel(List, double, double, JavaCSG) for the method generating rems
 * @see #getPoleModel(List, double, double, JavaCSG) for the method generating poles
 * @see JavaCSG#view(Geometry3D) for the JavaCSG method creating views
 * @see JavaCSG#union3D(Geometry3D...) for the JavaCSG method putting models together
 */
    public void generate3D() throws DatabaseException
    {
        receipt = Facade.getReceiptById(receiptID, connectionPool);
        lengthmm = receipt.getLength() * 10;
        widthmm = receipt.getWidth() * 10;

        List<Wood> woodItems = getWoods(Facade.getWoodOrderItemsByReceiptId(receipt.getIdReceipt(), connectionPool));

        boolean hasRoof = false;

        for(Wood w : woodItems)
        {
            if(w.getVariant().equals("Tag"))
            {
                hasRoof = true;
                break;
            }
        }

        Geometry3D roof = null;
        if(hasRoof)
        {
            roof = getRoofModel(woodItems, widthmm, lengthmm, csg);
        }

        Geometry3D stern = getSternModel(woodItems, widthmm, lengthmm, csg);

        Geometry3D rafters = getRafterModel(woodItems, widthmm, lengthmm, csg);

        Geometry3D rems = getRemModel(woodItems, widthmm, lengthmm, csg);

        Geometry3D poles = getPoleModel(woodItems, widthmm, lengthmm, csg);

        if(hasRoof)
        {
            csg.view(csg.union3D(roof, stern, rafters, rems, poles), receiptID);
        }
        else
        {
            csg.view(csg.union3D(stern, rafters, rems, poles), receiptID);
        }
    }

/**
 * Creates a Geometry3D model for sterns
 * @param  widthmm- the width of the carport in mm
 * @param lengthmm- the length of the carport in mm
 * @param  csg- the model so far
 * @throws DatabaseException
 * @return the sterns as a Geometry3D object
 * @see Facade#getWoodOrderItemsByReceiptId(int, ConnectionPool)  for the mapper method
 * @see JavaCSG#box3D(double, double, double, boolean) for the method generating a box
 * @see JavaCSG#translate3DY(double) for the method translating the box along the Y axis
 * @see JavaCSG#translate3DX(double) for the method translating the box along the X axis
 *@see JavaCSG#union3D(Geometry3D...) for the method joining the models together
 */

    private Geometry3D getSternModel(List<Wood> woodItems, double widthmm, double lengthmm, JavaCSG csg) throws DatabaseException
    {
        Wood stern = null;

        for (Wood w: woodItems)
        {
            if (w.getVariant().equals("Stern"))
            {
                stern = w;
                break;
            }
        }

        Geometry3D modelW = csg.box3D(widthmm + (stern.getHeight() * 10) * 2, stern.getHeight() * 10, stern.getWidth() * 10, false);
        Geometry3D modelL = csg.box3D(stern.getHeight() * 10, lengthmm + stern.getHeight() * 10, stern.getWidth() * 10, false);

        Geometry3D wpos0 = csg.translate3DY(-lengthmm / 2 - (stern.getHeight() * 10) / 2).transform(modelW);
        Geometry3D wpos1 = csg.translate3DY(lengthmm / 2 + (stern.getHeight() * 10) / 2).transform(modelW);

        Geometry3D lpos0 = csg.translate3DX(-widthmm / 2 - (stern.getHeight() * 10) / 2).transform(modelL);
        Geometry3D lpos1 = csg.translate3DX(widthmm / 2 + (stern.getHeight() * 10) / 2).transform(modelL);

        return csg.union3D(wpos0, wpos1, lpos0, lpos1);
    }

    /**
     * Creates a Geometry3D model for poles
     * @param  widthmm- the width of the carport in mm
     * @param lengthmm- the length of the carport in mm
     * @param  csg- the model so far
     * @throws DatabaseException
     * @return the poles as a Geometry3D object
     * @see Facade#getWoodOrderItemsByReceiptId(int, ConnectionPool)  for the mapper method
     * @see JavaCSG#box3D(double, double, double, boolean) for the method generating a box
     * @see JavaCSG#translate3D(double, double, double) for the translation method
     * @see JavaCSG#union3D(Geometry3D...) for the method joining the models together
     */
    private Geometry3D getPoleModel(List<Wood> woodItems, double widthmm, double lengthmm, JavaCSG csg) throws DatabaseException
    {
        Wood wood = null;

        for (Wood w: woodItems)
        {
            if (w.getVariant().equals("Stolpe"))
            {
                wood = w;
                break;
            }
        }

        Geometry3D model = csg.box3D(wood.getWidth()*10, wood.getHeight()*10, wood.getLength() * 10 - 900, false);

        int flyvl = 500;
        int flyvb = 350;

        List<Geometry3D> geometry3DS = new ArrayList<>();

        Geometry3D pos0 = csg.translate3D(-widthmm / 2 + flyvb,-lengthmm / 2 + flyvl, offsetZ).transform(model);
        Geometry3D pos1 = csg.translate3D(widthmm / 2 - flyvb ,-lengthmm / 2 + flyvl,  offsetZ).transform(model);
        Geometry3D pos2 = csg.translate3D( -widthmm / 2 + flyvb, lengthmm / 2 - flyvl, offsetZ).transform(model);
        Geometry3D pos3 = csg.translate3D(widthmm / 2 - flyvb, lengthmm / 2 - flyvl, offsetZ).transform(model);
        geometry3DS.add(pos0);
        geometry3DS.add(pos1);
        geometry3DS.add(pos2);
        geometry3DS.add(pos3);
        if(lengthmm > 3100+(flyvl*2) && lengthmm < 6201+(flyvb*2))
        {
            Geometry3D pos4 = csg.translate3D(-widthmm / 2 + flyvb,0 , offsetZ).transform(model);
            Geometry3D pos5 = csg.translate3D(widthmm / 2 - flyvb,0 , offsetZ).transform(model);
            geometry3DS.add(pos4);
            geometry3DS.add(pos5);
        }
        if(lengthmm > 6200 + flyvl)
        {
            double spaceX = -lengthmm/3;
            double firstExtra = lengthmm/2 + spaceX;
            double secondExtra = lengthmm/2 + spaceX*2;
            Geometry3D pos6 = csg.translate3D(widthmm / 2 - flyvb , firstExtra , offsetZ).transform(model);
            Geometry3D pos7 = csg.translate3D(widthmm / 2 - flyvb , secondExtra, offsetZ).transform(model);
            Geometry3D pos8 = csg.translate3D(-widthmm / 2 + flyvb , firstExtra, offsetZ).transform(model);
            Geometry3D pos9 = csg.translate3D(-widthmm / 2 + flyvb , secondExtra , offsetZ).transform(model);
            geometry3DS.add(pos6);
            geometry3DS.add(pos7);
            geometry3DS.add(pos8);
            geometry3DS.add(pos9);
        }
        return csg.union3D(geometry3DS);
    }


    /**
     * Creates a Geometry3D model for rems
     * @param  woodItems- the full list of wood in the given receipt
     * @param  widthmm- the width of the carport in mm
     * @param lengthmm- the length of the carport in mm
     * @param  csg- the model so far
     * @return the rems as a Geometry3D object
     * @see JavaCSG#box3D(double, double, double, boolean) for the method generating a box
     * @see JavaCSG#translate3D(double, double, double) for the translation method
     * @see JavaCSG#union3D(Geometry3D...) for the method joining the models together
     */
    private Geometry3D getRemModel(List<Wood> woodItems, double widthmm, double lengthmm, JavaCSG csg)
    {
        int remSpacingRoff = 350;


        Wood remItem = null;

        for (Wood w: woodItems)
        {
            if (w.getVariant().equals("Rem"))
            {
                remItem = w;
                break;
            }
        }

        Geometry3D model = csg.box3D(remItem.getHeight() * 10, lengthmm, remItem.getWidth() * 10, false);
        Geometry3D rem0 = csg.translate3D(- (widthmm / 2) + remSpacingRoff, 0, offsetZ).transform(model);
        Geometry3D rem1 = csg.translate3D(widthmm / 2 - remSpacingRoff, 0, offsetZ).transform(model);

        offsetZ += remItem.getWidth() * 10;
        return csg.union3D(rem0, rem1);
    }

    /**
     * Creates a Geometry3D model for rafters
     * @param  woodItems- the full list of wood in the given receipt
     * @param  widthmm- the width of the carport in mm
     * @param lengthmm- the length of the carport in mm
     * @param  csg- the model so far
     * @return the rafters as a Geometry3D object
     * @see JavaCSG#box3D(double, double, double, boolean) for the method generating a box
     * @see JavaCSG#translate3D(double, double, double) for the translation method
     * @see JavaCSG#union3D(Geometry3D...) for the method joining the models together
     */
    private Geometry3D getRafterModel(List<Wood> woodItems, double widthmm, double lengthmm, JavaCSG csg)
    {
        Wood rafterItem = null;

        for (Wood w: woodItems)
        {
            if (w.getVariant().equals("Spær"))
            {
                rafterItem = w;
                break;
            }
        }

        List<Geometry3D> rafters = new ArrayList<>();
        double amount = Math.floor(lengthmm / 550.0);
        double offset = 0;
        double initialPos = -(lengthmm / 2) + rafterItem.getWidth() + 250;

        Geometry3D model = csg.box3D(widthmm, rafterItem.getHeight() * 10, rafterItem.getWidth() * 10, false);

        for (int i = 0; i < amount; i++)
        {
            rafters.add(csg.translate3D(0, initialPos + offset, offsetZ).transform(model));
            offset = lengthmm / amount + offset;
        }

        offsetZ += rafterItem.getWidth() * 10;
        return csg.union3D(rafters);
    }

    /**
     * helping method fetching the List of Wood
     * @param items- a List of orderItems containing both metal and wood
     * @return a List of only Wood
     */
    private List<Wood> getWoods(List<OrderItem> items)
    {
        List<Wood> woods = new ArrayList<>();

        for (OrderItem i: items)
        {
            woods.add((Wood) i.getMaterial());
        }

        return woods;
    }

    /**
     * Creates a Geometry3D model for roofing
     * @param  woods- the full list of wood in the given receipt
     * @param  widthmm- the width of the carport in mm
     * @param lengthmm- the length of the carport in mm
     * @param  csg- the model so far
     * @return the roof as a Geometry3D object
     * @see JavaCSG#box3D(double, double, double, boolean) for the method generating a box
     */
    private Geometry3D getRoofModel(List<Wood> woods, double widthmm, double lengthmm, JavaCSG csg)
    {
        Wood roofItem = null;

        for (Wood w: woods)
        {
            if (w.getVariant().equals("Tag"))
            {
                roofItem = w;
                break;
            }
        }
        offsetZ += roofItem.getHeight() * 10;
        return csg.box3D(widthmm, lengthmm, roofItem.getHeight() * 10, false);
    }
}