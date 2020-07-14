package phase1;

import java.io.Serializable;

/**
 * <h1>Two-Way Trade</h1>
 * <p>The TwoWayTrade class represents a TwoWayTrade (exchange of items) between users.</p>
 */
public class TwoWayTrade extends Trade implements Serializable {

    private Item ogItem;
    private Item otherItem;

    /**
     * @param ogTrader trader that initially proposed the trade
     * @param otherTrader trader that initially received the trade
     * @param ogItem the original proposer's item
     * @param otherItem the original receiver's item
     */
    public TwoWayTrade(Trader ogTrader, Trader otherTrader, Item ogItem, Item otherItem){
        super(ogTrader, otherTrader);
        this.ogItem = ogItem;
        this.otherItem = otherItem;
    }

    /**
     * Gets the item that the trader who originally proposed this trade is offering in the trade.
     * @return an Item object representing the original trader's item
     */
    public Item getOgItem(){
        return ogItem;
    }

    /**
     * Gets the item that the trader who originally received this trade is offering in the trade.
     * @return an Item object representing the other trader's item
     */
    public Item getOtherItem(){
        return otherItem;
    }

    /**
     * @return a String representation of a Two-Way trade.
     */
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

    /**
     * Removes items from the inventories of the traders involved in the trade.
     */
    @Override
    public void removeItems(){
        ogTrader.removeFromInventory(ogItem);
        otherTrader.removeFromInventory(otherItem);
    }

    /**
     * Adds the items from the trade to the recently traded items lists of both traders involved in this trade.
     */
    @Override
    public void addRecentItem(){
        ogTrader.addRecentItemToList(ogItem);
        otherTrader.addRecentItemToList(otherItem);
    }
}
