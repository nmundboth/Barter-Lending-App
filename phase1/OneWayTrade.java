package phase1;

import java.io.Serializable;

/**
 * <h1>One-Way Trade</h1>
 * <p>The OneWayTrade class represents a OneWayTrade (borrow or lend) between users.</p>
 */
public class OneWayTrade extends Trade implements Serializable {

    private Item item;
    private Trader lender;
    private Trader receiver;

    /**
     * @param ogTrader the trader that originally proposed the trade
     * @param otherTrader the trader that originally received the trade request
     * @param item the item involved in the trade
     */
    public OneWayTrade(Trader ogTrader, Trader otherTrader, Item item){
        super(ogTrader, otherTrader);
        this.item = item;
    }

    /**
     * Gets the user that is lending the item in this trade.
     *
     * @return a Trader object representing the trader lending an item.
     */
    public Trader getLender(){
        return this.lender;
    }

    /**
     * Gets the user that is receiving the item in this trade
     *
     * @return a Trader object representing the trader receiving the item.
     */
    public Trader getReceiver(){
        return this.receiver;
    }

    /**
     * Sets the trader who is lending the item in the trade.
     *
     * @param lender Trader object that represents the trader lending the item in this trade.
     */
    public void setLender(Trader lender){
        this.lender = lender;
    }

    /**
     * Sets the trader who is receiving the item in the trade.
     *
     * @param receiver Trader object that represents the trader receiving the item in this trade.
     */
    public void setReceiver(Trader receiver){
        this.receiver = receiver;
    }

    /**
     * Gets the item in this trade.
     *
     * @return an Item object representing the item in this trade.
     */
    public Item getItem(){
        return this.item;
    }

    /**
     * Removes the item from the lender's inventory (called upon trade confirmation).
     */
    @Override
    public void removeItems(){
        lender.removeFromInventory(item);
    }

    /**
     * Adds the item from this trade to the most recently lent items of the receiver.
     */
    @Override
    public void addRecentItem(){
        lender.addRecentItemToList(item);
    }

    /**
     * @return a String representation of a OneWayTrade.
     */
    public String toString(){
        if (permanent){
            return receiver.getName() + " receiving " + item + " from " + lender.getName() + ".";
        }
        else { // temporary trade
            return receiver.getName() + " borrowing " + item + " from " + lender.getName() + ".";
        }
    }


}
