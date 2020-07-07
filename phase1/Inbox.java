package phase1;

import java.util.ArrayList;
import java.util.List;

abstract class Inbox {
    //All the trades available to the user
    private List<Trade> trades;
    //Notifications from other Traders
    private List<String> traderNoti;
    private List<String> admiNoti;
    //All notifications from the Admin
    private List<Trade> unacceptedTrades;
    //All unaccepted offers

    private int unaccptedUnread;
    //The number of unread messages for each category
    private int tradeUnread ;
    //The number of unread messages form other traders
    private int admiNotiUnread;
    //The number of unread messages form admins
    //<trades> for admin user should be empty
    public Inbox(List<Trade> trades, List<String> traderNotifs, List<String> adminNotifs){
        this.traderNoti = traderNotifs;
        this.admiNoti = adminNotifs;
        this.tradeUnread = 0;
        this.admiNotiUnread = 0;
        this.trades = trades;
        this.unaccptedUnread = 0;
        this.unacceptedTrades = new ArrayList<Trade>();
    }

    public List<String> getAdmiNoti() { return admiNoti; }
        //getter for adminNoti

    public int getTotalUnread(){
        return this.tradeUnread + this.admiNotiUnread + this.unaccptedUnread;
    }
    //Returns the total number of unread messages

    // Returns messages from Traders
    public int getTradersUnread(){return this.tradeUnread; }

    public void tradeUnreadMinusOne(){
        this.tradeUnread -= 1;
    }

    public Trade getTrade(int index){
        Trade temp = this.trades.get(index);
        this.trades.remove(index);
        this.admiNotiUnread -=1;
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

    public void tradeConfirmed(Trader trader, Trade trade, String text){
        this.tradeUnread += 1;
        trader.getInbox().tradeUnread += 1;
        this.addTraderNoti(text);
        trader.getInbox().addTraderNoti(text);
        trade.setOpen(true);
    }

    // Mark the Trade as complete
    public void completeTrade(Trader trader, Trade trade, String text){
        this.tradeUnread += 1;
        trader.getInbox().tradeUnread += 1;
        this.addTraderNoti(text);
        trader.getInbox().addTraderNoti(text);
        if (trade.isPermanent()){
            trade.setOpen(false);
        }
    }

    public Trade getUnacceptedTrades(int index){
        Trade temp = this.unacceptedTrades.get(index);
        this.unacceptedTrades.remove(index);
        this.admiNotiUnread -=1;
        return temp;
    }

    // Traders accepting a Trade
    public void addTrade(Trade trade, Trader trader){
        this.trades.add(trade);
        // The Trader receiving the acceptance will be notified
        trader.getInbox().trades.add(trade);
        trader.getInbox().tradeUnread += 1;
        // Issue is how to remove that Trade from both Traders' unacceptedTrades. Need to be sure that we are
        // extracting the Trade correctly. Maybe need more details in the objects to extract Trade correctly from
        // both unacceptedTrades?
        this.unacceptedTrades.remove(trade);
        trader.getInbox().unacceptedTrades.remove(trade);
        // Would this work? ^
    }

    // Decline an unaccepted trade
    public void refuseUnaccepted(Trade trade, Trader trader){
        this.unacceptedTrades.remove(trade);
        trader.getInbox().unacceptedTrades.remove(trade);
        trader.getInbox().unaccptedUnread += 1;
    }

    // Both Traders reach a compromise to cancel a Trade. (Work-in-progress) method
    public boolean cancelTradeRequest(Trade trade, Trader trader, String request){
        // Case where editing Meeting by both Traders exceeded: automatically returns true
        return true;
    }

    // Traders successfully cancel the Trade
    public void cancelTrade(Trade trade, Trader trader, String text, String cancel){
        if (cancelTradeRequest(trade, trader, text)){
            this.trades.remove(trade);
            trader.getInbox().trades.remove(trade);
            this.addTraderNoti(cancel);
            trader.getInbox().addTraderNoti(cancel);
            this.tradeUnread += 1;
            trader.getInbox().tradeUnread += 1;
            trade.setOpen(false);
        }
    }

    public void addAdminNoti(String text){ this.admiNoti.add(text);}

    public void addTraderNoti(String text){ this.traderNoti.add(text);}

    public void addUnaccepted(Trade trade, Trader trader){
        this.unacceptedTrades.add(trade);
        // Trader gets a notification about a pending Trade request
        trader.getInbox().unacceptedTrades.add(trade);
        trader.getInbox().unaccptedUnread += 1;
    }
    // for adding to the lists, maybe for an incoming trade or admin notification

    public List<Trade> getTrades() {
        return this.trades;
    }
    //getter for trades

    public List<Trade> getUnacceptedTrades() {
        return unacceptedTrades;
    }
    //getter for unaccepted offers

    public int getAdmiNotiUnread() {
        return admiNotiUnread;
    }

    public void setAdmiNotiUnread(int admiNotiUnread) {
        this.admiNotiUnread = admiNotiUnread;
    }

    public List<String> getTraderNoti() {
        return traderNoti;
    }

    public void setTradeUnread(int tradeUnread) {
        this.tradeUnread = tradeUnread;
    }

    public void setUnaccptedUnread(int unaccptedUnread) {
        this.unaccptedUnread = unaccptedUnread;
    }

    public int getTradeUnread() {
        return tradeUnread;
    }
}
