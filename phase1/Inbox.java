package phase1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

abstract class Inbox implements Serializable {
    //All the trades available to the user
    private List<Trade> trades;
    //Notifications from other Traders
    private List<String> traderNoti;
    private List<String> admiNoti;
    //All notifications from the Admin
    private List<Trade> unacceptedTrades;
    //All unaccepted offers

    private int unaccptedUnread;
    //The number of unread trade requests
    private int tradeUnread ;
    //The number of unread messages form other traders
    private int traderNotiUnread;
    // The number of Traders notification unread
    private int admiNotiUnread;
    //The number of unread messages form admins
    //<trades> for admin user should be empty

    /**
     * Constructor for the Inbox class
     * @param trades A List representing the Trades sub-inbox
     * @param traderNotifs A List representing the other Trader notification sub-inbox
     * @param adminNotifs A list representing the Admin/System notifications sub-inbox
     */
    public Inbox(List<Trade> trades, List<String> traderNotifs, List<String> adminNotifs){
        this.traderNoti = traderNotifs;
        this.admiNoti = adminNotifs;
        this.tradeUnread = 0;
        this.admiNotiUnread = 0;
        this.trades = trades;
        this.unaccptedUnread = 0;
        this.traderNotiUnread = 0;
        this.unacceptedTrades = new ArrayList<Trade>();
    }

    //Getters for unread messages
    /**
     * Getter for the AdminNoti sub-inbox
     * @return AdminNoti List
     */
    public List<String> getAdmiNoti() { return admiNoti; }

    /**
     * a getter for the TraderNotification sub-inbox
     * @return the TraderNotification sub-inbox
     */
    public List<String> getTraderNoti() { return traderNoti; }

    /**
     * A getter for the Trades sub-inbox
     * @return the sub-inbox
     */
    public List<Trade> getTrades() { return this.trades; }

    /**
     * method for getting the unacceptedTrade sub-inbox
     * @return the requested sub-inbox
     */
    public List<Trade> getUnacceptedTrades() { return unacceptedTrades; }

    /**
     * Returns the number of unaccepted Trades
     * @return an integer representing the number of unread messages
     */

    public int getUnaccptedUnread(){ return unaccptedUnread; }

    /**
     * gets the number of unread messages in the trade sub-inbox
     * @return the number of unread messages
     */
    public int getTradeUnread() { return tradeUnread; }

    /**
     * Returns the number of unread messages from other traders
     * @return an integer representing the number of unread trader messages
     */
    public int getTradersUnread(){return this.tradeUnread; }

    /**
     * Getter for the number of unread admin notifications
     * @return the integer representing this number
     */
    public int getAdmiNotiUnread() { return admiNotiUnread; }

    /**
     * Getter for the total number of unread messages
     * @return an integer representing the number of unread messages
     */
    public int getTotalUnread(){
        return this.tradeUnread + this.admiNotiUnread + this.unaccptedUnread;
    }

    /**
     * The setter for the number of unread admin messages
     * @param admiNotiUnread the number of unread messages
     */
    public void setAdmiNotiUnread(int admiNotiUnread) { this.admiNotiUnread = admiNotiUnread; }

    /**
     * Setter for the number of unread messages in the tradeUnread sub-inbox
     * @param tradeUnread number of unread messages
     */
    public void setTradeUnread(int tradeUnread) { this.tradeUnread = tradeUnread; }

    /**
     * Setter for the number of unread messages in the unacceptedTrade sub-inbox
     * @param unaccptedUnread number of unread messages
     */
    public void setUnaccptedUnread(int unaccptedUnread) { this.unaccptedUnread = unaccptedUnread;}

    /**
     * A method for the user to be able to access a message from the Trade inbox
     * User is represented with a list of trades they can choose from
     * @param index represents the position of a desired message within the inbox
     * @return a Trade at a specific position
     */
    public Trade getTrade(int index){
        Trade temp = this.trades.get(index);
        this.trades.remove(index);
        this.admiNotiUnread -=1;
        return temp;
    }

    /**
     * A getter for the user to be able to access a message from the Trade inbox
     * User is represented with a list of messages they can chose from
     * @param index represents the position of a desired message from the inbox
     * @return The contents of a message in the form of a String
     */
    public String getTradeNoti(int index){
        String temp = this.traderNoti.get(index);
        this.traderNoti.remove(index);
        this.traderNotiUnread -= 1;
        return temp;
    }

    /**
     * A getter for the user to be able to access a message from the Admin inbox
     * User is represented with the position of a desired message from the inbox
     * @param index represents the position of a desired message from the inbox
     * @return The contents of a message in the form a String
     */
    public String getAdminNoti(int index){
        String temp = this.admiNoti.get(index);
        this.admiNoti.remove(index);
        this.admiNotiUnread -= 1;
        return temp;
    }

    /**
     * A getter for the user to be able to access a specific Trade in the sub-inbox
     * User is represented with a list of Unaccepted trades to chose from
     * @param index the position of a specific message within the inbox
     * @return returns a Trade
     */
    public Trade getUnacceptedTrades(int index){
        Trade temp = this.unacceptedTrades.get(index);
        this.unacceptedTrades.remove(index);
        this.admiNotiUnread -=1;
        return temp;
    }

    /**
     * Method to confirm a proposed trade and send message back to the trader proposing the inital trade
     * @param trader the other client who originally proposed a trade
     * @param text A text form message to the client to inform them of the acceptance
     */
    public void tradeConfirmed(Trader trader, String text){
        this.tradeUnread += 1;
        trader.getInbox().tradeUnread += 1;
        this.addTraderNoti(text);
        trader.getInbox().addTraderNoti(text);
    }

    /**
     * Method to mark a trade as complete
     * @param trader the other Trader in this Trade
     * @param trade the ongoing trade
     * @param text the text notification for this trade
     */
    public void completeTrade(Trader trader, Trade trade, String text){
        this.trades.remove(trade);
        trader.getInbox().trades.remove(trade);
        this.tradeUnread += 1;
        trader.getInbox().tradeUnread += 1;
        this.addTraderNoti(text);
        trader.getInbox().addTraderNoti(text);
    }


    /**
     * A method for a user to accept a date and add it to the ongoing trades
     * @param trade the ongoing trade
     * @param trader the other client on this trade
     */
    public void addTrade(Trade trade, Trader trader){
        this.trades.add(trade);
        this.unaccptedUnread -= 1;
        trader.getInbox().trades.add(trade);
        trader.getInbox().tradeUnread += 1;
        this.unacceptedTrades.remove(trade);
    }

    /**
     * A method for a trader to refuse a proposed trade
     * @param trade the ongoing trade
     * @param trader the other client on this trade
     */
    public void refuseUnaccepted(Trade trade, Trader trader){
        this.unacceptedTrades.remove(trade);
        trader.getInbox().tradeUnread += 1;
    }

    /**
     * A method for the user to cancel an ongoing trade that has already been proposed
     * @param trade the ongoing trade
     * @param trader the other client on this trade
     * @param cancel the text notification for the other user to view
     */
    public void cancelTrade(Trade trade, Trader trader, String cancel){
            this.trades.remove(trade);
            trader.getInbox().trades.remove(trade);
            this.addTraderNoti(cancel);
            trader.getInbox().addTraderNoti(cancel);
            this.tradeUnread += 1;
            trader.getInbox().tradeUnread += 1;
    }

    /**
     *  method for adding a message to the Admin Notification sub-inbox
     * @param text the text that is to be added
     */
    public void addAdminNoti(String text){ this.admiNoti.add(text);}

    /**
     *  method for adding a message to the Trader notification sub-inbox
     * @param text the text that is to be added
     */
    public void addTraderNoti(String text){ this.traderNoti.add(text);}

    /**
     * method for adding a Trade to the unaccptedTrade sub-inbox
     * @param trade the ongoing tarde
     * @param trader the other user for this ongoing trade
     */
    public void addUnaccepted(Trade trade, Trader trader){
        trader.getInbox().unacceptedTrades.add(trade);
        trader.getInbox().unaccptedUnread += 1;
    }
}
