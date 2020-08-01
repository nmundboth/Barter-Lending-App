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

    private List<Item> inventory;
    private List<Item> wish_list;
    private String name;
    private boolean flagged;
    protected boolean frozen;
    private int greedyInt; // Higher = greedier, so in order to borrow, must be <= -1 (or whatever the threshold is set to by the admin(s))
    private int incomplete;// # of outstanding incomplete trades currently associated with the trader
    private int weeklyTransxns;
    private LocalDateTime weeklyEnd;
    private HashMap<Trader, Integer> tradingPartners;
    private ArrayList<Item> recentItems;
    private int incompleteLimit;
    private int weeklyTransxnLimit;
    private TraderInbox inbox;

    //Need empty user as placeholder in UserCatalogue findUserByName method
    public Trader(){

    }

    /**
     * @param username The trader's username
     * @param password The trader's password
     * @param type The type of User ("trader")
     * @param inbox The trader's inbox
     * @param inventory The trader's inventory
     * @param name The trader's first name
     */
    public Trader(String username, String password, String type, TraderInbox inbox, List<Item> inventory, String name) {
        super(username, password, type, inbox);
        this.inventory = inventory;
        this.wish_list = new ArrayList<Item>();
        this.name = name;
        this.flagged = false;
        this.frozen = false;
        this.greedyInt = 0;
        this.incomplete = 0;
        this.weeklyTransxns = 0;
        this.weeklyEnd = LocalDateTime.now().plus(Period.ofWeeks(1)); // Initial period is one week after creation of account
        this.tradingPartners = new HashMap<Trader, Integer>();
        this.recentItems = new ArrayList<Item>();
        this.incompleteLimit = 3; // Change this to change the limit on incomplete transxns a trader can have
        this.weeklyTransxnLimit = 10; // Change this to change the weekly transxn limit
        this.inbox = inbox;
    }

    /**
     * Checks whether this user has borrowed more than they have lent.
     * @return a boolean indicating whether the trader has borrowed more than they lent.
     */
    public boolean isGreedy(){
        return this.greedyInt > greedLimit;
    }

    /** Returns all Trades from the Trader's Inbox.
     *
     * @return a list of all Trades.
     */
    public List<TradeMessage> readTrades(){
        TraderInbox traderInbox = (TraderInbox) this.getInbox();
        return traderInbox.getTrades();
    }

    /** Returns all unaccepted Trades from the Trader's Inbox.
     *
     * @return a list of all Trades.
     */
    public List<TradeMessage> readUnacceptedTrades(){
        TraderInbox traderInbox = (TraderInbox) this.getInbox();
        return traderInbox.getUnacceptedTrades();
    }

    /** Prints all Admin's notifications from Trader's Inbox.
     *
     */
    public void readAdminNotifs(){
        this.getInbox().setAdmiNotiUnread(0);

        //Reads notifications line by line
        int i = 0;
        while (i < this.getInbox().getAdmiNoti().size()){
            System.out.println(this.getInbox().getAdmiNoti().get(i));
            i++;
        }

    }

    /**
     * Prints all other Traders' notifications from Trader's Inbox.
     */
    public void readTraderNotifs(){
        this.getInbox().setUnaccptedUnread(0);
        this.getInbox().setTradeUnread(0);

        //Reads notifications line by line
        int i = 0;
        while (i < this.getInbox().getTraderNoti().size()){
            System.out.println(this.getInbox().getTraderNoti().get(i));
            i++;
        }

    }

    /** Tells how many unread notifications the Trader has from other Traders.
     *
     * @return an integer which says how many unread notifications the Trader has from other Traders.
     */
    public int unreadTraderNotifs(){
        return this.getInbox().getTraderNotiUnread();
    }

    /** Tells how many unread notifications the Trader has from Admin/s.
     *
     * @return an integer which says how many unread notifications the Trader has from Admin/s.
     */
    public int unreadAdminNotifs(){
        return this.getInbox().getAdmiNotiUnread();
    }

    /** Tells how many unread notifications the Trader has.
     *
     * @return an integer which gives the total number of unread notifications.
     */
    public int totalUnreadNotifs(){
        return this.getInbox().getTotalUnread();
    }

    /** Returns the list of Items that Trader has.
     *
     * @return a list of Items.
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /** Returns the list of Items that Trader is interested in getting.
     *
     * @return a list of Items.
     */
    public List<Item> getWishList() {
        return wish_list;
    }

    /** Adds an Item that Trader is interested in to Trader's wishlist.
     *
     * @param item An Item object containing the name and description of the item.
     */
    public void add_wish(Item item){
        this.wish_list.add(item);
        System.out.println("Added item " + item.getName() + " to your wish list!");
    }

    /** Removes an Item that Trader is no longer interested in from Trader's wishlist.
     *
     * @param item An Item object containing the name and description of the item.
     */
    public void remove_wish(Item item){
        for(int i = 0; i < this.wish_list.size(); i++){
            if(item == this.wish_list.get(i)){
                this.wish_list.remove(item);
                break;
            }
        }
        System.out.println("Item not found!");
    }

    /**
     * Adds an item to the trader's inventory
     *
     * @param item the Item object that the trader wants to add to their inventory.
     */
    public void addToInventory(Item item){
        inventory.add(item);
    }

    /** Removes an Item from Trader's inventory.
     *
     * @param item An Item object containing the name and description of the item.
     */
    public void removeFromInventory(Item item){
        inventory.remove(item);
    }

    /** Gets Trader's name.
     *
     * @return a String which is the name of the Trader.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a weekly transaction to the trader's number of weekly transactions conducted.
     * Every time a transaction is added, checks to see if the current week has ended. If it has, then the
     * weekly transaction period resets, and so the added trade would be the trader's first transaction of the week.
     */
    public void addWeeklyTransxn(){
        if (LocalDateTime.now().isAfter(weeklyEnd) || LocalDateTime.now().isEqual(weeklyEnd)){
            weeklyTransxns = 1;
            weeklyEnd = LocalDateTime.now().plus(Period.ofWeeks(1));
        }
        else{ // Weekly period hasn't ended
            weeklyTransxns += 1;
        }
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

    // Removes the trader from a trader's three most frequent if they are the least frequently traded with in that list.
    // Called when deciding which trader to remove from the trader's most frequent trading partner list.
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
    public void addRecentItemToList(Item item){
        if (recentItems.size() < 3){
            recentItems.add(item);
        }
        else{ // recentItems.size == 3 ---> recentItems will always have a size <= 3
            recentItems.add(item);
            recentItems.remove(0);
        }
    }

    /**
     * Keeps count of incomplete Trades. Records number of incomplete Trades.
     */
    public void addIncomplete(){
        incomplete += 1;
    }

    /**
     * Once a Trade is completed, number of incomplete Trades decreases.
     */
    public void removeIncomplete(){
        incomplete -= 1;
    }

    /** Checks if limit of Trades for a Trader has been exceeded in a week.
     *
     * @return true if limit of Trades has been exceeded.
     */
    public boolean overWeeklyLimit(){
        return this.weeklyTransxns > weeklyTransxnLimit;
    }

    /** Checks if Trader has too many incomplete Trades.
     *
     * @return true if limit of incomplete Trades has been exceeded.
     */
    public boolean overIncompleteLimit(){
        return this.incomplete > incompleteLimit;
    }

    /**
     * Checks whether the trader has been automatically flagged for having too many incomplete transactions, or too
     * many weekly transactions. Flagged traders are sent to admins, who can then decide whether or not to freeze a
     * trader.
     *
     * @return a boolean representing whether the user has been flagged.
     */
    public boolean isFlagged(){
        return flagged;
    }

    /**
     * Flags a trader - indicates too many outstanding incomplete transactions, or too many weekly transactions.
     */
    public void flag(){
        this.flagged = true;
    }

    /** Checks if Trader's account is frozen. true means the account is frozen.
     *
     * @return state of Trader's account.
     */
    public boolean isFrozen(){return frozen;}

    /** Checks if the Trader is within the limits of borrowing from other Traders.
     *
     * @return an integer which checks if Trader has exceeded limit in borrowing.
     */
    public int getGreedyInt(){
        return greedyInt;
    }

    /**
     * Used to track the lend/borrow ratio of users (higher = more borrows than lends = greedier)
     * @param greedyInt The integer tracking how greedy a user is (how much more they have borrowed than lent)
     */
    public void setGreedyInt(int greedyInt){
        this.greedyInt = greedyInt;
    }

    /** Gets the number of incomplete Trades for a Trader.
     *
     * @return an integer telling the number of incomplete Trades.
     */
    public int getIncomplete(){
        return this.incomplete;
    }

    /**
     * Gets the number of transactions that a trader has conducted this week.
     *
     * @return an integer representing number of weekly transactions.
     */
    public int getWeeklyTransxns(){
        return this.weeklyTransxns;
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

    @Override
    public TraderInbox getInbox() { return this.inbox; }
}
