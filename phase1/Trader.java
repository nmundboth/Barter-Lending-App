package phase1;

import java.util.ArrayList;
import java.util.List;

public class Trader extends User{

    //Might have to create an inventory class?
    private List<Item> inventory;
    private List<Item> wish_list;
    private String name;
    private boolean frozen;
    private int greedyInt; // Higher = greedier, so in order to borrow, must be <= -1 (or whatever the threshold is set to by the admin(s))
    private int incomplete;// # of outstanding incomplete trades currently associated with the trader
    private int weeklyTransxns; //TODO: Modify so that it is on a per week basis (probably use java.time methods)
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

    // Schedule a meeting between Traders. (Work-in-progress) method
    public void scheduleMeet(Trader trader, Meeting meet){
        trader.getInbox().setTradeUnread(trader.getInbox().getTradeUnread() + 1);
        trader.getInbox().addTraderNoti("Hey "+trader.name+"! Can you meet "+this.name+" on"+meet.getDate()+" at "+
                meet.getLocation()+" at "+meet.getTime()+"?");
    }

    // Change meeting time/place. Each Trader has up to 3 edits
    public void changeMeet(Trader trader, Trade trade, String text, String cancel, Meeting meet, String location, String date, String time){
        if (trade.getOgEdits() == 0 && trade.getOtherEdits() == 0){
            this.tradeCancelled(trader, trade, text, cancel);
        }
        else {
            meet.setDate(date);
            meet.setLocation(location);
            meet.setTime(time);
            if ((this.name.equals(trade.getOgTrader()))) {
                trade.setOgEdits(trade.getOgEdits() - 1);
            }
            else {
                trade.setOtherEdits(trade.getOtherEdits() - 1);
            }
            this.scheduleMeet(trader, meet);
        }
    }

    public String getName() {
        return name;
    }


    public void tradeCancelled(Trader trader, Trade trade, String text, String cancel){
        this.getInbox().cancelTrade(trade, trader, text, cancel);
    }
    //^ Moved this method to TradeManager, but keeping here for now since ChangeMeet uses it.
    //Can possibly create another UseCase that deals with the Meeting methods

    public void addWeeklyTransxn(){
        this.weeklyTransxns += 1;
    }

    public void addIncomplete(){
        this.incomplete += 1;
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
}
