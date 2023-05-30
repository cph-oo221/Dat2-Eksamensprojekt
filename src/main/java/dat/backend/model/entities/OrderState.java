package dat.backend.model.entities;

public enum OrderState
{
    OPEN,
    OFFER,
    COMPLETE;

    public static OrderState intToOrder(int i)
    {
        OrderState orderState;
        switch (i)
        {
            case 0: orderState = OrderState.OPEN;
            break;

            case 1: orderState = OrderState.OFFER;
            break;

            case 2: orderState = OrderState.COMPLETE;
            break;

            default: throw new IllegalStateException("ENUM-state not allowed");
        }
        return orderState;
    }
}
