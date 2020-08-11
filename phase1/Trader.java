package phase1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.Period;
import java.time.LocalDateTime;

/** <h1>Trader</h1>
 * <p>Represents a trader.</p>
 * @author Navnee Mundboth
 * @author James Veale
 * @author TingRui Zhang
 */

public class Trader extends User implements Serializable {

    static int greedLimit = -1; // The lower this limit, the more a trader must lend before they can borrow.

    private String name;
    private String location;
    private HashMap<Trader, Integer> tradingPartners;
    private ArrayList<Item> recentItems;

    private TraderStatus status;
    private Inventory inventory;
    private Inventory wishlist;

    //Need empty user as placeholder in UserCatalogue findUserByName method
    public Trader(){

    }

    /**
     * @param username The trader's username
     * @param password The trader's password
     * @param inbox The trader's inbox
     * @param inventory The trader's inventory
     * @param name The trader's first name
     * @param location The trader's city location
     */
    public Trader(String username, String password, TraderInbox inbox, Inventory inventory,
                  Inventory wishlist, String name, TraderStatus status, String location) {
        super(username, password, "trader", inbox);
        this.inventory = inventory;
        this.wishlist = wishlist;
        this.name = name;
        this.location = location;
        this.tradingPartners = new HashMap<Trader, Integer>();
        this.recentItems = new ArrayList<Item>();

        this.status = status;
    }

    /** Returns an object containing booleans to show statuses and integers which define some limits for the Trader.
     *
     * @return the object TraderStatus of the Trader.
     */
    public TraderStatus getTraderStatus(){
        return status;
    }

    /** Returns the list of Items that Trader has.
     *
     * @return a list of Items.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /** Returns the list of Items that Trader is interested in getting.
     *
     * @return a list of Items.
     */
    public Inventory getWishList() {
        return wishlist;
    }

    /** Gets Trader's name.
     *
     * @return a String which is the name of the Trader.
     */
    public String getName() {
        return name;
    }

    /** Tracks which other traders this trader has traded with, and how many times they have traded with them.
     *
     * @param partner the other trader with whom this trader conducted a transaction
     */
    public void addTradingPartner(Trader partner){
        if (tradingPartners.containsKey(partner)){
            tradingPartners.put(partner, tradingPartners.get(partner) + 1);
        }
        else{
            tradingPartners.put(partner, 1);
        }
    }

    /**
     * Gets the trader's three most frequent trading partners.
     *
     * @return an ArrayList representing the trader's three most frequent trading partners.
     */
    public ArrayList<Trader> frequentPartners(){
        HashMap<Trader, Integer> threeMostFrequent = new HashMap<Trader, Integer>();
        for (Trader t : tradingPartners.keySet()){
            if (threeMostFrequent.size() < 3){
                threeMostFrequent.put(t, tradingPartners.get(t));
            }
            else{ // Size of threeMostFrequent >= 3
                for (Trader ft : threeMostFrequent.keySet()){
                    if (tradingPartners.get(t) > threeMostFrequent.get(ft)){
                        removeLeastFrequent(threeMostFrequent);
                        threeMostFrequent.put(t, tradingPartners.get(t));
                        break;
                    }
                }
            }
        }
        ArrayList<Trader> frequentPartners = new ArrayList<Trader>(threeMostFrequent.keySet());
        return frequentPartners;
    }

    /** Removes the trader from a trader's three most frequent if they are the least frequently traded with in that list.
     *  Called when deciding which trader to remove from the trader's most frequent trading partner list.
     *
     * @param frequentPartners which represents the recent Traders that Trader traded with.
     */
    private void removeLeastFrequent(HashMap<Trader, Integer> frequentPartners){
        int leastTrades = Integer.MAX_VALUE;
        Trader leastFrequent = new Trader();
        for (Trader t: frequentPartners.keySet()){
            if (frequentPartners.get(t) < leastTrades){
                leastTrades = frequentPartners.get(t);
                leastFrequent = t;
            }
        }
        frequentPartners.remove(leastFrequent);

    }

    /**
     * Adds an item to the list of this trader's most recently traded items (items they have most recently offered in
     * trades).
     *
     * @param item an Item object representing the item that this trader has recently traded.
     */
    public void addRecentItemToList(phase1.Item item){
        if (recentItems.size() < 3){
            recentItems.add(item);
        }
        else{ // recentItems.size == 3 ---> recentItems will always have a size <= 3
            recentItems.add(item);
            recentItems.remove(0);
        }
    }

    /** Gets the location of the Trader.
     *
     * @return a String representing the Trader's location.
     */
    public String getLocation(){
        return this.location;
    }

    /** Sets the location of the Trader.
     *
     * @param location which represents the Trader's location.
     */
    public void setLocation(String location){
        this.location = location;
    }

    /**
     * Gets the items that this trader has most recently traded to others (max. 3 items)
     *
     * @return a list of the trader's most recently traded items
     */
    public ArrayList<Item> getRecentItems(){
        return this.recentItems;
    }

    /**
     * String representation of a trader will be their username
     *
     * @return a string of the trader's username
     */
    public String toString(){
        return this.username;
    }
}
