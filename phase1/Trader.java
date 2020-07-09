package phase1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.Period;
import java.time.LocalDateTime;

public class Trader extends User{

    //Might have to create an inventory class?
    private List<Item> inventory;
    private List<Item> wish_list;
    private String name;
    private boolean frozen;
    private int greedyInt; // Higher = greedier, so in order to borrow, must be <= -1 (or whatever the threshold is set to by the admin(s))
    private int incomplete;// # of outstanding incomplete trades currently associated with the trader
    private int weeklyTransxns;
    private LocalDateTime weeklyEnd;
    private HashMap<Trader, Integer> tradingPartners;
    private ArrayList<Item> recentItems;
    private int incompleteLimit;
    private int weeklyTransxnLimit;

    public Trader(String username, String password, String type, TraderInbox inbox, List<Item> inventory, String name) {
        super(username, password, type, inbox);
        this.inventory = inventory;
        this.wish_list = new ArrayList<Item>();
        this.name = name;
        this.frozen = false;
        this.greedyInt = 0;
        this.incomplete = 0;
        this.weeklyTransxns = 0;
        this.weeklyEnd = LocalDateTime.now().plus(Period.ofWeeks(1));
        this.tradingPartners = new HashMap<Trader, Integer>();
        this.recentItems = new ArrayList<Item>();
        this.incompleteLimit = 3; // Change this to change the limit on incomplete transxns a trader can have
        this.weeklyTransxnLimit = 10; // Change this to change the weekly transxn limit
    }

    // Reads Trades
    public List<Trade> readTrades(){
        TraderInbox traderInbox = (TraderInbox) this.getInbox();
        return traderInbox.getTrades();
    }

    // Reads pending Trade requests
    public List<Trade> readUnacceptedTrades(){
        TraderInbox traderInbox = (TraderInbox) this.getInbox();
        return traderInbox.getUnacceptedTrades();
    }

    public void readAdminNotifs(){
        this.getInbox().setAdmiNotiUnread(0);

        //Reads notifications line by line
        int i = 0;
        while (i < this.getInbox().getAdmiNoti().size()){
            System.out.println(this.getInbox().getAdmiNoti().get(i));
            i++;
        }

    }

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

    public int unreadTraderNotifs(){
        return this.getInbox().getTradersUnread();
    }

    public int unreadAdminNotifs(){
        return this.getInbox().getAdmiNotiUnread();
    }

    //Keeps track of new notifications
    public int totalUnreadNotifs(){
        return this.getInbox().getTotalUnread();
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public List<Item> getWishList() {
        return wish_list;
    }

    public void add_wish(Item item){
        this.wish_list.add(item);
        System.out.println("Added item " + item.getName() + " to your wish list!");
    }

    public void remove_wish(Item item){
        for(int i = 0; i < this.wish_list.size(); i++){
            if(item == this.wish_list.get(i)){
                this.wish_list.remove(item);
                break;
            }
        }
        System.out.println("Item not found!");
    }

    public void removeFromInventory(Item item){
        inventory.remove(item);
    }

    public String getName() {
        return name;
    }

    //^ Moved this method to TradeManager, but keeping here for now since ChangeMeet uses it.
    //Can possibly create another UseCase that deals with the Meeting methods


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

    public void addIncomplete(){
        this.incomplete += 1;
    }

    public void removeIncomplete(){
        this.incomplete -= 1;
    }

    public boolean overWeeklyLimit(){
        return this.weeklyTransxns >= weeklyTransxnLimit;
    }

    public boolean overIncompleteLimit(){
        return this.incomplete >= incompleteLimit;
    }

    public boolean isFrozen(boolean b) {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
        //Set to true by Admin freezeTrader and set to false automatically when user returns overdue book.
    }

    public int getGreedyInt(){
        return greedyInt;
    }

    public void setGreedyInt(int greedyInt){
        this.greedyInt = greedyInt;
    }

    public int getIncomplete(){
        return this.incomplete;
    }

    public String toString(){
        return this.name;
    }
}
