package phase2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Inbox of a Trader
 */
public class TraderInbox extends Inbox implements Serializable {

    static List<TradeMessage> cancelledTrades = new ArrayList<>();
    //All the trades available to the user
    private List<TradeMessage> trades;
    //All unaccepted offers
    private List<TradeMessage> unacceptedTrades;

    public int unaccptedUnread = 0;
    //The number of unread trade requests
    public int tradeUnread = 0;
    //The number of unread messages form other traders
    public int traderNotiUnread = 0;
    // The number of Traders notification unread
    public int admiNotiUnread = 0;
    //The number of unread messages form admins
    //<trades> for admin user should be empty

    public TraderInbox(List<TradeMessage> trades, List<Message> traderNotifs, List<Message> adminNotifs){
        super(traderNotifs, adminNotifs);
        this.trades = trades;
        this.tradeUnread = 0;
        this.unaccptedUnread = 0;
        this.unacceptedTrades = new ArrayList<TradeMessage>();
    }
    //Getters for unread messages

    /**
     * Returns the number of unaccepted Trades
     * @return an integer representing the number of unread messages
     */

    protected int getUnaccptedUnread(){ return unaccptedUnread; }

    /**
     * gets the number of unread messages in the trade sub-inbox
     * @return the number of unread messages
     */
    protected int getTradeUnread() { return tradeUnread; }

    /**
     * Returns the number of unread messages from other traders
     * @return an integer representing the number of unread trader messages
     */

    public int getTotalUnread(){
        return this.tradeUnread + this.admiNotiUnread + this.unaccptedUnread + this.traderNotiUnread;
    }

    /**
     * Setter for the number of unread messages in the tradeUnread sub-inbox
     * @param tradeUnread number of unread messages
     */
    protected void setTradeUnread(int tradeUnread) { this.tradeUnread = tradeUnread; }

    /**
     * Setter for the number of unread messages in the unacceptedTrade sub-inbox
     * @param unaccptedUnread number of unread messages
     */
    protected void setUnaccptedUnread(int unaccptedUnread) { this.unaccptedUnread = unaccptedUnread;}

    /**
     * A getter for the Trades sub-inbox
     * @return the sub-inbox
     */
    protected List<TradeMessage> getTrades() { return this.trades; }

    /**
     * method for getting the unacceptedTrade sub-inbox
     * @return the requested sub-inbox
     */
    protected List<TradeMessage> getUnacceptedTrades() { return unacceptedTrades; }

    protected List<TradeMessage> getCancelledTrades() { return cancelledTrades; }

    /**
     * A method for the user to be able to access a message from the Trade inbox
     * User is represented with a list of trades they can choose from
     * @param index represents the position of a desired message within the inbox
     * @return a Trade at a specific position
     */
    protected Trade getTrade(int index){
        TradeMessage message = this.trades.get(index);
        this.trades.remove(index);
        this.admiNotiUnread -=1;
        return message.getTrade();
    }

    /**
     * A getter for the user to be able to access a specific Trade in the sub-inbox
     * User is represented with a list of Unaccepted trades to chose from
     * @param index the position of a specific message within the inbox
     * @return returns a Trade
     */
    protected Trade getUnacceptedTrades(int index){
        TradeMessage message = this.unacceptedTrades.get(index);
        this.unacceptedTrades.remove(index);
        this.admiNotiUnread -=1;
        return message.getTrade();
    }

    /**
     * Method to confirm a proposed trade and send message back to the trader proposing the inital trade
     * @param trader the other client who originally proposed a trade
     * @param text A text form message to the client to inform them of the acceptance
     */
    protected void tradeConfirmed(Trader trader, String text){
        this.tradeUnread += 1;
        TraderInbox traderInbox = (TraderInbox) trader.getInbox();
        traderInbox.tradeUnread += 1;
        this.addTraderNoti(new Message(text, OG, trader));
        trader.getInbox().addTraderNoti(new Message(text, OG, trader));
    }



    /**
     * Carries out trade between Trader Users.
     * @param trader the other client who originally proposed a trade
     * @param text A text form message to the client to inform them of the confirmation
     */
    public void completeTrade(Trader trader, Trade trade, String text){
        //Handled in GUI, removing a TradeMessage that was just created would cause problems
        //this.trades.remove(new TradeMessage("", trade.getOgTrader(), trade.getOtherTrader(), trade));

        TraderInbox traderInbox = (TraderInbox) trader.getInbox();

        //Same with this
        //traderInbox.trades.remove(new TradeMessage("", trade.getOgTrader(), trade.getOtherTrader(), trade));

        this.tradeUnread += 1;
        traderInbox.tradeUnread += 1;
        this.addTraderNoti(new Message(text, trader, this.OG));
        trader.getInbox().addTraderNoti(new Message(text, this.OG, trader));
    }

    /**
     * Adds a trade to a trader's inbox.
     * @param message message detailing trade has been added.
     */
    public void addTrade(TradeMessage message){
        Trader trader = findCorrectRecipient(message);
        this.trades.add(message);
        this.unaccptedUnread -= 1;
        TraderInbox traderInbox = (TraderInbox) trader.getInbox();
        traderInbox.trades.add(message);
        traderInbox.tradeUnread += 1;
    }

    /**
     * Messages a trader telling them their trade was unaccepted.
     * @param message a message displaying an unaccepted trade.
     */
    public void refuseUnaccepted(TradeMessage message){
        TraderInbox traderInbox = (TraderInbox) message.getSender().getInbox();
        traderInbox.tradeUnread += 1;
    }

    /**
     * Takes a current trade, the other trader, and cancels the trade.
     * @param trade trade being cancelled.
     * @param trader other trader who's trade has been cancelled.
     * @param cancel the message string variable.
     */
    public void cancelTrade(Trade trade, Trader trader, String cancel) {

        TraderInbox traderInbox = (TraderInbox) trader.getInbox();
        this.addTraderNoti(new Message(cancel, trade.getOgTrader(), trade.getOtherTrader()));
        trader.getInbox().addTraderNoti(new Message(cancel, trade.getOgTrader(), trade.getOtherTrader()));
        this.tradeUnread += 1;
        traderInbox.tradeUnread += 1;
        cancelledTrades.add(new TradeMessage("", trade.ogTrader, trade.otherTrader, trade));
        traderInbox.getCancelledTrades().add(new TradeMessage("", trade.ogTrader, trade.otherTrader,
                trade));
    }

    /**
     * Messages a trader telling them an unaccepted trade has been added.
     * @param message a message displaying unaccepted trade has been added.
     */
    public void addUnaccepted(TradeMessage message){
        Trader trader = findCorrectRecipient(message);
        TraderInbox traderInbox = (TraderInbox) trader.getInbox();
        traderInbox.unacceptedTrades.add(message);
        traderInbox.unaccptedUnread += 1;
    }

    /**
     * Finds the trader that is a recipient of a Message.
     * @param message message contents.
     * @return The trader to send a message to.
     */
    public Trader findCorrectRecipient(TradeMessage message){
        if(message.getSender() == (this.OG)){
            return message.getRecipient();
        }
        else{
            return message.getSender();
        }
    }

}
