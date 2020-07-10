package phase1;

import java.util.List;

/**
 * <h1>Trade Functions</h1>
 * <p>The TradeManager class contains method that allow traders to send/receive trades, accept/reject trade requests,
 * confirm that a transaction has taken place, or cancel a trade. It also relays trade information to the involved
 * traders' inboxes. TradeManager and MeetingManager work to manage all of the components of trades.
 * TradeManager also tracks users that have too many accepted trades, flagging them for admin users to decide if they
 * should be frozen.</p>
 * <p>All methods in this class require that the traders are not frozen.</p>
 */

public class TradeManager implements Tradable{

    private List<Trader> transxnFlagged; // Users flagged to be frozen (but not yet frozen) - should be accessed when admin wants to view users to flag
    //This class relies on the input of two traders to the methods, so will need to have something like "current trader"
    //and "desired trader" (person they are interacting with) to be input from the controller - will be possible using
    //UserCatalogue and having the user select which user they want to trade with/borrow from, etc.

    /**
     * On instantiation, TradeManager contains a list of all Traders that have been flagged for having too many weekly
     * transactions outstanding (empty if no users are flagged).
     * @param transxnFlagged List of all users that have too many weekly transactions (used by admin to determine which
     *                       users should be frozen).
     */
    public TradeManager(List<Trader> transxnFlagged){
        this.transxnFlagged = transxnFlagged;
    }

    /**
     * Returns the list of traders flagged for too many weekly transactions.
     * @return List of flagged traders.
     */
    public List<Trader> getTransxnFlagged(){
        return transxnFlagged;
    }

    //TODO: Gate all Trade Request functionality behind whether a trader is frozen (can do in controller)

    /**
     * Sends a transaction request from ogTrader to borrow an item from otherTrader.
     * The transaction can be either temporary or permanent.
     * The trader requesting the item (ogTrader) must have previously lent more items than they have received.
     * @param ogTrader Trader who proposes the borrow request (item receiver).
     * @param otherTrader Trader the borrow request is being sent to (item sender).
     * @param item The item being requested.
     * @param isPermanent Whether the borrow transaction is permanent.
     */
    //TODO: Gate borrow functionality behind GreedyUser integer of receiver (can be done in controller level)
    public void sendBorrowRequest(Trader ogTrader, Trader otherTrader, Item item, boolean isPermanent){
        OneWayTrade trade = new OneWayTrade(ogTrader, otherTrader, item);
        trade.setReceiver(ogTrader);
        trade.setLender(otherTrader);
        trade.setPermanent(isPermanent);
        ogTrader.getInbox().addUnaccepted(trade, otherTrader);
        otherTrader.getInbox().addTraderNoti(ogTrader.getName() + " wants to borrow " + item + " from you.");
    }

    /**
     * Sends a transaction request from ogTrader to lend an item to otherTrader.
     * The transaction can be either temporary or permanent.
     * The trader who is being lent the item must have previously lent more items than they have received.
     * @param ogTrader Trader who proposes the lend request (item sender).
     * @param otherTrader Trader who receives the lend request (item receiver).
     * @param item The item that is being lent.
     * @param isPermanent Whether the lend transaction is permanent.
     */
    //TODO: Gate lend functionality behind GreedyUser integer of receiver (can be done in controller level)
    public void sendLendRequest(Trader ogTrader, Trader otherTrader, Item item, boolean isPermanent){
        OneWayTrade trade = new OneWayTrade(ogTrader, otherTrader, item);
        trade.setLender(ogTrader);
        trade.setReceiver(otherTrader);
        trade.setPermanent(isPermanent);
        ogTrader.getInbox().addUnaccepted(trade, otherTrader);
        otherTrader.getInbox().addTraderNoti(ogTrader.getName() + " wants to lend you " + item + ".");
    }

    /**
     * Sends a request from ogTrader to trade their ogItem for otherTrader's otherItem.
     * The transaction can be either temporary or permanent.
     * @param ogTrader Trader sending the request.
     * @param otherTrader Trader receiving the request.
     * @param ogItem Item offered by ogTrader in transaction.
     * @param otherItem Item offered by otherTrader in transaction.
     * @param isPermanent Whether the transaction is permanent.
     */
    public void sendTWTradeRequest(Trader ogTrader, Trader otherTrader, Item ogItem, Item otherItem,
                                   boolean isPermanent){
        TwoWayTrade trade = new TwoWayTrade(ogTrader, otherTrader, ogItem, otherItem);
        trade.setPermanent(isPermanent);
        ogTrader.getInbox().addUnaccepted(trade, otherTrader);
        otherTrader.getInbox().addTraderNoti(ogTrader.getName() + " wants to trade their " + ogItem +
                " for your " + otherItem + ".");
    }

    /**
     * Rejects a transaction offer from another trader, and removes the request from both traders' inboxes.
     * Notification that the rejected user receives is different depending on whether the transaction was a one-way
     * trade or two-way trade.
     * @param rejecting The trader rejecting the transaction.
     * @param rejected The trader whose transaction request is being rejected.
     * @param trade The trade that is being rejected.
     */
    public void rejectUnaccepted(Trader rejecting, Trader rejected, OneWayTrade trade){
        rejecting.getInbox().refuseUnaccepted(trade, rejected);
        if (rejecting == trade.getLender()){
            rejected.getInbox().addTraderNoti(rejecting.getName() + " can't lend " +
                    trade.getItem() + " to you.");
        }
        else { // (rejecting == trade.getReceiver())
            rejected.getInbox().addTraderNoti(rejecting.getName() + "doesn't want to borrow " +
                    trade.getItem() + "from you.");
        }
    }

    public void rejectUnaccepted(Trader rejecting, Trader rejected, TwoWayTrade trade){
        rejecting.getInbox().refuseUnaccepted(trade, rejected);
        if (rejecting == trade.getOgTrader()) {
            rejected.getInbox().addTraderNoti(rejecting.getName() + " doesn't want to trade their " +
                    trade.getOgItem() + " for your " + trade.getOtherItem() + ".");
        } else { // (rejecting == trade.getOtherTrader())
            rejected.getInbox().addTraderNoti(rejecting.getName() + " doesn't want to trade their " +
                    trade.getOtherItem() + " for your " + trade.getOgItem() + ".");
        }
    }

    /**
     * Accepts a transaction request from another user, which initially has no meeting associated with it.
     * Adjusts the traders' inboxes accordingly (removes trade from their unaccepted trades list, and adds to their
     * accepted trades list.
     * Sends an appropriate notification to the trader whose request is being accepted, based on whether this is a
     * one-way or two-way trade.
     * The transaction is not yet open (opens after a meeting is agreed upon), but at this stage, a trade now counts
     * towards the traders' weekly transaction limits, so this method adjusts those values accordingly.
     * @param accepting The trader accepting the transaction.
     * @param accepted The trader whose transaction request has been accepted.
     * @param trade The trade that is being accepted.
     */
    public void acceptTrade(Trader accepting, Trader accepted, OneWayTrade trade){
        accepting.getInbox().addTrade(trade, accepted);
        this.checkTransxnLimit(accepting);
        this.checkTransxnLimit(accepted);
        if (accepting == trade.getLender()){
            accepted.getInbox().addTraderNoti(accepting.getName() + " will lend you their " +
                    trade.getItem() + ".");
        }
        else { // (accepting == trade.getReceiver())
            accepted.getInbox().addTraderNoti(accepting.getName() + " will borrow your " +
                    trade.getItem() + ".");
        }
    }

    public void acceptTrade(Trader accepting, Trader accepted, TwoWayTrade trade){
        accepting.getInbox().addTrade(trade, accepted);
        this.checkTransxnLimit(accepting);
        this.checkTransxnLimit(accepted);
        if (accepting == trade.getOgTrader()){
            accepted.getInbox().addTraderNoti(accepting.getName() + " accepts the trade for their " +
                    trade.getOgItem() + " and your " + trade.getOtherItem() + ".");
        }
        else { // (accepting == trade.getOtherTrader())
            accepted.getInbox().addTraderNoti(accepting.getName() + " accepts the trade for their " +
                    trade.getOtherItem() + " and your" + trade.getOgItem() + ".");
        }
    }

    /**
     * Helper method for acceptTrade that adds a transaction to the involved traders' weekly transactions, and flags
     * the user if this puts them at or over the weekly limit.
     * @param trader Trader that is being checked.
     */
    public void checkTransxnLimit(Trader trader){
        trader.addWeeklyTransxn();
        if (trader.overWeeklyLimit()){
            transxnFlagged.add(trader); // Doesn't automatically freeze trader, just flags them
        }
    }

    /**
     * Allows a user to confirm that a transaction has taken place, that is, the items have been exchanged in real life.
     * If both users have confirmed the trade, then it confirms the trade.
     * @see #confirm(Trader, Trader, Trade) .
     * @param confirming Trader that is confirming the transaction took place.
     * @param other Other trader with whom the transaction took place.
     * @param trade The trade that is being confirmed.
     */
    // Can add an else block (outer layer) that prints "You have already confirmed the trade!" if the trader has already confirmed.
    public void confirmTrade(Trader confirming, Trader other, Trade trade){
        if (!trade.getConfirmations().contains(confirming)){
            trade.addConfirmation(confirming);
            if(trade.getConfirmations().size() == 2){
                this.confirm(confirming, other, trade);
            }
        }
    }

    /**
     * Helper method for confirmTrade method, that executes upon both users confirming that a transaction has taken
     * place in real life. If the trade is permanent, or if this was the first meeting of a temporary transaction, then
     * the items are removed from the traders' inventories. If it is a permanent trade, or the second meeting of a temporary trade,
     * then the trade is marked as complete.
     * @see #completeTrade(Trade).
     * If it is the first meeting of a temporary trade, the second meeting is set and the confirmations are cleared so
     * that the second meeting can be confirmed after it has happened.
     * @param confirming Trader confirming the trade.
     * @param other Other trader with whom the transaction took place.
     * @param trade The trade that is being confirmed.
     */
    public void confirm(Trader confirming, Trader other, Trade trade){
        if (trade.isPermanent()){
            trade.removeItems();
            this.completeTrade(trade);
        }
        else if ((!trade.isPermanent()) && trade.getCompletedMeetings() == 0){
            trade.removeItems();
            trade.completeFirstMeeting();
            trade.clearConfirmations();
        }
        else{// Temporary trade that has had one meeting completed already
            this.completeTrade(trade);
        }

    }

    /**
     * Completes a trade, which marks it as closed (removes it from a trader's list of incomplete transactions),
     * and removes the trade from the list of active trades in the traders' inboxes. Also sends appropriate
     * notifications to both traders' inboxes.
     * For each trader, adds their trading partner for this trade to their list of trading partners, so that their most
     * frequent trading partners can be retrieved.
     * Also adds the item that trader(s) gave to their list of the most recent three items they have offered in trades.
     * @param trade The trade that is being completed.
     */
    public void completeTrade(Trade trade){
        Trader ogTrader = trade.getOgTrader();
        Trader otherTrader = trade.getOtherTrader();

        trade.setOpen(false);
        ogTrader.removeIncomplete(); // Note this is not removing them from being flagged if completing the trade drops them into an acceptable range
        otherTrader.removeIncomplete();

        ogTrader.addTradingPartner(otherTrader);
        otherTrader.addTradingPartner(ogTrader);

        trade.addRecentItem();

        ogTrader.getInbox().completeTrade(otherTrader, trade, "The trade:\n" + trade + "\n has been completed.");
    }

    /**
     * Cancels a trade between two traders, if both agree to its cancellation. Traders can not cancel an open trade
     * (the trade must be cancelled before setting a meeting). Once the trade is cancelled, removes it from the traders'
     * lists of active trades, and sends an appropriate notification to both traders' inboxes.
     * @param cancelling The trader who is requesting to cancel the trade.
     * @param other The other trader involved in the trade.
     * @param trade The trade that is requesting to be cancelled.
     */
    // Similar to confirmTrade, but else block would read: "You have already requested to cancel the trade, or are trying to cancel an open trade!"
    public void cancelTrade(Trader cancelling, Trader other, Trade trade){
        if ((!trade.getCancellations().contains(cancelling)) && !trade.isOpen()){
            trade.addCancellation(cancelling);
            if(trade.getCancellations().size() == 2){
                cancelling.getInbox().cancelTrade(trade, other, "The trade:\n" + trade + "\nhas been cancelled.");
            }
        }
    }
}
