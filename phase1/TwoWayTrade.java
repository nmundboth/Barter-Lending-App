package phase1;

public class TwoWayTrade extends Trade{

    private Item ogItem;
    private Item otherItem;

    public TwoWayTrade(Trader ogTrader, Trader otherTrader, Item ogItem, Item otherItem){
        super(ogTrader, otherTrader);
        this.ogItem = ogItem;
        this.otherItem = otherItem;
    }

    public Item getOgItem(){
        return ogItem;
    }

    public Item getOtherItem(){
        return otherItem;
    }
}
