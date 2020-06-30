package group_0073.phase1;

import java.util.List;

public class Trader extends User{

    //Might have to create an inventory class?
    private List<Item> inventory;
    private int notifs_count;
    private String name;

    public Trader(String username, String password, String type, Inbox inbox, List<Item> inventory, String name) {
        super(username, password, type, inbox);
        this.inventory = inventory;
        this.notifs_count = 0;
        this.name = name;
    }

    // Reads Trades
    public List<Trade> readTrades(){
        return this.getInbox().getTrades();
    }

    // Reads pending Trade requests
    public List<Trade> readUnacceptedTrades(){
        return this.getInbox().getUnacceptedTrades();
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

    //OG Trader requests a Trade
    public void sendRequest(Trader trader, Trade trade){
        this.getInbox().addUnaccepted(trade, trader);
        trader.getInbox().addTraderNoti(this.name+" wants to trade "+trade.getObj()+" with "+trader.name);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void rejectUnaccepted(Trader trader, Trade trade){
        this.getInbox().refuseUnaccepted(trade, trader);
        trader.getInbox().addTraderNoti(this.name+" can't trade "+trade.getObj()+" with "+trader.name);
    }

    public void acceptTrade(Trader trader, Trade trade){
        this.getInbox().addTrade(trade, trader);
        trader.getInbox().addTraderNoti(this.name+" accepts to trade "+trade.getObj()+" with "+trader.name);
    }

    // Schedule a meeting between Traders. (Work-in-progress) method
    public void scheduleMeet(Trader trader, Meeting meet){
        trader.getInbox().setTradeUnread(trader.getInbox().getTradeUnread() + 1);
        trader.getInbox().addTraderNoti("Hey "+trader.name+"! Can you meet "+this.name+" on"+meet.getDate()+" at "+
                meet.getLocation()+" at "+meet.getTime()+"?");
    }

    // Change meeting time/place. Each Trader has up to 3 edits
    public void changeMeet(Trader trader, Trade trade, String text, String cancel, Meeting meet, String location, String date, String time){
        if (trade.getOg_edits() == 0 && trade.getOther_edits() == 0){
            this.tradeCancelled(trader, trade, text, cancel);
        }
        else {
            meet.setDate(date);
            meet.setLocation(location);
            meet.setTime(time);
            if ((this.name.equals(trade.getOg_trader()))) {
                trade.setOg_edits(trade.getOg_edits() - 1);
            }
            else {
                trade.setOther_edits(trade.getOther_edits() - 1);
            }
            this.scheduleMeet(trader, meet);
        }
    }

    public String getName() {
        return name;
    }

    public void confirmTrade(Trader trader, Trade trade, String text){
        this.getInbox().tradeConfirmed(trader, trade, text);
    }

    public void tradeComplete(Trader trader, Trade trade, String text){
        this.getInbox().completeTrade(trader, trade, text);
    }

    public void tradeCancelled(Trader trader, Trade trade, String text, String cancel){
        this.getInbox().cancelTrade(trade, trader, text, cancel);
    }

}
