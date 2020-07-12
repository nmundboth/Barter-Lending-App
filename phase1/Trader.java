package phase1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.Period;
import java.time.LocalDateTime;

/** Represents a Trader.
 * @author Navnee Mundboth
 * @author James Veale
 * @author TingRui Zhang
 */
public class Trader extends User{
public class Trader extends User implements Serializable {

    //Might have to create an inventory class?
    private List<Item> inventory;
    private List<Item> wish_list;
    private String name;
    private boolean flagged;
    private boolean frozen;
    private int greedyInt; // Higher = greedier, so in order to borrow, must be <= -1 (or whatever the threshold is set to by the admin(s))
    private int incomplete;// # of outstanding incomplete trades currently associated with the trader
    private int weeklyTransxns;
    private LocalDateTime weeklyEnd;
    private HashMap<Trader, Integer> tradingPartners;
    private ArrayList<Item> recentItems;
    private int incompleteLimit;
    private int weeklyTransxnLimit;

    //Need empty user as placeholder in UserCatalogue findUserByName method
    public Trader(){

    }

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
    }

    /** Returns all Trades from the Trader's Inbox.
     *
     * @return a list of all Trades.
     */
    public List<Trade> readTrades(){
        TraderInbox traderInbox = (TraderInbox) this.getInbox();
        return traderInbox.getTrades();
    }

    /** Returns all unaccepted Trades from the Trader's Inbox.
     *
     * @return a list of all Trades.
     */
    public List<Trade> readUnacceptedTrades(){
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
        return this.getInbox().getTradersUnread();
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

    // If we want to account for the very small time difference in LocalDateTime.now() between each use, can instead
    // create a temporary variable; LocalDateTime currTime = LocalDateTime.now() and use throughout method
    public void addWeeklyTransxn(){
        if (LocalDateTime.now().isAfter(weeklyEnd) || LocalDateTime.now().isEqual(weeklyEnd)){
            weeklyTransxns = 1;
            weeklyEnd = LocalDateTime.now().plus(Period.ofWeeks(1));
        }
        else{ // Weekly period hasn't ended
            weeklyTransxns += 1;
        }
    }

    /** Adds other Traders to Trade with.
     *
     * @param partner
     */
    public void addTradingPartner(Trader partner){
        if (tradingPartners.containsKey(partner)){
            tradingPartners.put(partner, tradingPartners.get(partner) + 1);
        }
        else{
            tradingPartners.put(partner, 1);
        }
    }

    public ArrayList<Trader> frequentPartners(){
        HashMap<Trader, Integer> threeMostFrequent = new HashMap<Trader, Integer>();
        for (Trader t : tradingPartners.keySet()){
            if (threeMostFrequent.size() < 3){
                threeMostFrequent.put(t, tradingPartners.get(t));
            }
            else{ // Size of threeMostFrequent >= 3
                for (Trader ft : threeMostFrequent.keySet()){
                    if (tradingPartners.get(t) > threeMostFrequent.get(ft)){
                        threeMostFrequent.remove(ft);
                        threeMostFrequent.put(t, tradingPartners.get(t));
                    }
                }
            }
        }
        ArrayList<Trader> frequentPartners = new ArrayList<Trader>(threeMostFrequent.keySet());
        return frequentPartners;
    }

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

    public boolean isFlagged(){
        return flagged;
    }

    public void flag(){
        this.flagged = true;
    }

    /** Checks if Trader's account is frozen. true means the account is frozen.
     *
     * @return state of Trader's account.
     */
    public boolean isFrozen(){return frozen;}

    // this method should not be here. this means that the Trader can freeze/unfreeze their account.
    public void setFrozen(boolean b){this.frozen = b;}

    /** Checks if the Trader is within the limits of borrowing from other Traders.
     *
     * @return an integer which checks if Trader has exceeded limit in borrowing.
     */
    public int getGreedyInt(){
        return greedyInt;
    }

    // this method should not be here. this means that the Trader can set limit in borrowing.
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

    // method already written up. Choose which one to use and delete the other.
    public String toString(){
        return this.name;
    }
}
