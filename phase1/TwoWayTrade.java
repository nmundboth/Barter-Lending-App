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

    public String toString(){
        if (permanent){
            return ogTrader.getName() + " giving " + ogItem + " to " + otherTrader.getName() + " for " +
                    otherItem + ".";
        }
        else {// temporary
            return ogTrader.getName() + " temporarily giving " + ogItem + " to " + otherTrader.getName() +
                    " for " + otherItem + ".";
        }
    }

    @Override
    public void removeItems(){
        ogTrader.removeFromInventory(ogItem);
        otherTrader.removeFromInventory(otherItem);
    }

    @Override
    public void addRecentItem(){
        ogTrader.addRecentItemToList(ogItem);
        otherTrader.addRecentItemToList(otherItem);
    }
}
