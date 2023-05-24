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
    private double widthmm = 0;
    private double lengthmm = 0;
    private double offsetZ = 0;
    private int receiptID;


    public Model3D(int receiptID, ConnectionPool connectionPool)
    {
        this.receiptID = receiptID;
        this.connectionPool = connectionPool;
    }

    public void generate3D() throws DatabaseException
    {
        receipt = Facade.getReceiptById(receiptID, connectionPool);
        lengthmm = receipt.getLength() * 10;
        widthmm = receipt.getWidth() * 10;

        List<Wood> woodItems = getWoods(Facade.getWoodOrderItemsByRecieptId(receipt.getIdReceipt(), connectionPool));

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



    private Geometry3D getSternModel(List<Wood> woodItems, double widthmm, double lengthmm, JavaCSG csg) throws DatabaseException
    {
        List<OrderItem> orderItemList = Facade.getWoodOrderItemsByRecieptId(receipt.getIdReceipt(), connectionPool);
        OrderItem sternItem = null;

        for (OrderItem oi: orderItemList)
        {
            if (oi.getMaterial().getVariant().equals("Stern"))
            {
                sternItem = oi;
                break;
            }
        }

        Wood stern = (Wood) sternItem.getMaterial();

        Geometry3D modelW = csg.box3D(widthmm + stern.getHeight(), stern.getHeight(), stern.getWidth(), false);
        Geometry3D modelL = csg.box3D(stern.getHeight(), lengthmm + stern.getHeight(), stern.getWidth() , false);

        Geometry3D wpos0 = csg.translate3DY(-lengthmm / 2).transform(modelW);
        Geometry3D wpos1 = csg.translate3DY(lengthmm / 2).transform(modelW);

        Geometry3D lpos0 = csg.translate3DX(-widthmm / 2).transform(modelL);
        Geometry3D lpos1 = csg.translate3DX(widthmm / 2).transform(modelL);



        return csg.union3D(wpos0, wpos1, lpos0, lpos1);
    }

    private Geometry3D getPoleModel(List<Wood> woodItems, double widthmm, double lengthmm, JavaCSG csg) throws DatabaseException
    {

        List<OrderItem> orderItemList = Facade.getWoodOrderItemsByRecieptId(receipt.getIdReceipt(), connectionPool);
        OrderItem poleItem = null;

        for (OrderItem oi: orderItemList)
        {
            if (oi.getMaterial().getVariant().equals("Stolpe"))
            {
                poleItem = oi;
                break;
            }
        }

        Wood wood = (Wood) poleItem.getMaterial();

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

    private List<Wood> getWoods(List<OrderItem> items)
    {
        List<Wood> woods = new ArrayList<>();

        for (OrderItem i: items)
        {
            woods.add((Wood) i.getMaterial());
        }

        return woods;
    }

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
