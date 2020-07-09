package phase1;

public class OneWayTrade extends Trade{

    private Item item;
    private Trader lender;
    private Trader receiver;

    //Note that lender and receiver are not set in constructor, but the constructor and setters for lender and receiver
    //will be within the same method in TradeManager (so won't  have to worry about NullReference).
    public OneWayTrade(Trader ogTrader, Trader otherTrader, Item item){
        super(ogTrader, otherTrader);
        this.item = item;
    }

    public Trader getLender(){
        return this.lender;
    }

    public Trader getReceiver(){
        return this.receiver;
    }

    public void setLender(Trader lender){
        this.lender = lender;
    }

    public void setReceiver(Trader receiver){
        this.receiver = receiver;
    }

    public Item getItem(){
        return this.item;
    }

    @Override
    public void removeItems(){
        lender.removeFromInventory(item);
    }

    @Override
    public void addRecentItem(){
        lender.addRecentItemToList(item);
    }

    public String toString(){
        if (permanent){
            return receiver.getName() + " receiving " + item + " from " + lender.getName() + ".";
        }
        else { // temporary trade
            return receiver.getName() + " borrowing " + item + " from " + lender.getName() + ".";
        }
    }


}
