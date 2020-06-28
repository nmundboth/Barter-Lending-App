package group_0073.phase1;

import java.util.ArrayList;
import java.util.List;

public class Inbox {

    private List<Trade> trades;
    //All the trades available to the user
    private List<String> admiNoti;
    //All notifications from the Admin
    private List<Trade> unacceptedTrades;
    //All unaccepted offers
    private int tradeUnread ;
    private int admiNotiUnread;
    private int unaccptedUnread;
    //The number of unread messages for each category

    public Inbox(List<Trade> trades, List<String> notifs){
        //Admin cannot trade so trades for admin is an empty list?
        this.trades = trades;
        this.admiNoti = notifs;
        this.unacceptedTrades = new ArrayList<Trade>();
        this.tradeUnread = 0;
        this.admiNotiUnread = 0;
        this.unaccptedUnread = 0;
    }

    public List<String> getAdmiNoti() { return admiNoti; }
        //getter for adminNoti

    public List<Trade> getTrades() {
        return trades;
    }
        //getter for trades

    public List<Trade> getUnacceptedTrades() {
        return unacceptedTrades;
    }
        //getter for unaccepted offers

    public int getTotalUnread(){return this.tradeUnread + this.unaccptedUnread + this.admiNotiUnread; }
        //Returns the total number of unread messeges

    public Trade getTrade(int index){
        Trade temp = this.trades.get(index);
        this.trades.remove(index);
        this.tradeUnread -= 1;
        return temp;
    }

    // Returns a certain Trade from the list. I'm not sure if we are keeping a message after being accessed
    // For now I'm assuming we are removing it instantly from the list, feel free to change

    public String getAdminNoti(int index){
        String temp = this.admiNoti.get(index);
        this.admiNoti.remove(index);
        this.admiNotiUnread -= 1;
        return temp;
    }
    // Same deal as the one before

    public Trade getUnacceptedTrades(int index){
        Trade temp = this.unacceptedTrades.get(index);
        this.unaccptedUnread.reemove(index);
        this.admiNotiUnread -= 1;
        return temp;
    }


    public void addTrade(Trade trade){ this.trades.add(trade); }

    public void addAdminNoti(String text){ this.admiNoti.add(text);}

    public void addUnaccepted(Trade trade){this.unacceptedTrades.add(trade);}

    // for adding to the lists, maybe for an incoming trade or admin notification

}
