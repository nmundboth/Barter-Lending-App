package phase1;

/**
 * <h1>Trade Functions</h1>
 * <p>The TradeManager class contains methods that allow traders to send/receive trades, accept/reject trade requests,
 * confirm that a transaction has taken place, or cancel a trade. It also relays trade information to the involved
 * traders' inboxes. TradeManager and MeetingManager work to manage all of the components of trades.
 * TradeManager also tracks users that have too many accepted trades, flagging them for admin users to decide if they
 * should be frozen.</p>
 * <p>All methods in this class require that the traders are not frozen.</p>
 */
public class TradeManager {

    public TradeManager(){
    }

    /**
     * Sends a transaction request from ogTrader to borrow an item from otherTrader.
     * The transaction can be either temporary or permanent.
     * The trader requesting the item (ogTrader) must have previously lent more items than they have received.
     * @param ogTrader Trader who proposes the borrow request (item receiver).
     * @param otherTrader Trader the borrow request is being sent to (item sender).
     * @param item The item being requested.
     * @param isPermanent Whether the borrow transaction is permanent.
     */
    public void sendBorrowRequest(Trader ogTrader, Trader otherTrader, Item item, boolean isPermanent,
                                  boolean digital){
        item.setDigital(digital);
        OneWayTrade trade = new OneWayTrade(ogTrader, otherTrader, item);
        trade.setReceiver(ogTrader);
        trade.setLender(otherTrader);
        trade.setPermanent(isPermanent);
        trade.setNoMeet(item.isDigital());
        TradeMessage message = new TradeMessage("", ogTrader, otherTrader, trade);
        TraderInbox traderInbox = (TraderInbox) ogTrader.getInbox();
        traderInbox.addUnaccepted(message);//ogTrader.getInbox().addUnaccepted(message);
        Message text = new Message(ogTrader.getName() + " wants to borrow " + item + " from you.",
                ogTrader, otherTrader);
        otherTrader.getInbox().addTraderNoti(text);
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
    public void sendLendRequest(Trader ogTrader, Trader otherTrader, Item item, boolean isPermanent,
                                boolean digital){
        item.setDigital(digital);
        OneWayTrade trade = new OneWayTrade(ogTrader, otherTrader, item);
        trade.setLender(ogTrader);
        trade.setReceiver(otherTrader);
        trade.setPermanent(isPermanent);
        trade.setNoMeet(item.isDigital());
        TradeMessage message = new TradeMessage("", ogTrader, otherTrader, trade);
        TraderInbox traderInbox = (TraderInbox) ogTrader.getInbox();
        traderInbox.addUnaccepted(message);//ogTrader.getInbox().addUnaccepted(message);
        Message text = new Message(ogTrader.getName() + " wants to lend you " + item + ".", ogTrader, otherTrader);
        otherTrader.getInbox().addTraderNoti(text);
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
                                   boolean isPermanent, boolean ogDigital, boolean otherDigital){
        ogItem.setDigital(ogDigital);
        otherItem.setDigital(otherDigital);
        TwoWayTrade trade = new TwoWayTrade(ogTrader, otherTrader, ogItem, otherItem);
        trade.setPermanent(isPermanent);
        trade.setNoMeet(ogDigital && otherDigital);
        TradeMessage message = new TradeMessage("", ogTrader, otherTrader, trade);
        TraderInbox traderInbox = (TraderInbox) ogTrader.getInbox();
        traderInbox.addUnaccepted(message);//ogTrader.getInbox().addUnaccepted(message);
        Message text = new Message(ogTrader.getName() + " wants to trade their " + ogItem +
                " for your " + otherItem + ".", ogTrader, otherTrader);
        otherTrader.getInbox().addTraderNoti(text);
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
        TraderInbox traderInbox = (TraderInbox) rejecting.getInbox();
        traderInbox.refuseUnaccepted(new TradeMessage("", rejecting, rejected, trade));
        //rejecting.getInbox().refuseUnaccepted(new TradeMessage("", rejecting, rejected, trade));
        if (rejecting == trade.getLender()){
            rejected.getInbox().addTraderNoti(new Message(rejecting.getName() + " can't lend " +
                    trade.getItem() + " to you.", rejecting, rejected));
        }
        else { // (rejecting == trade.getReceiver())
            rejected.getInbox().addTraderNoti(new Message(rejecting.getName() + "doesn't want to borrow " +
                    trade.getItem() + "from you.", rejecting, rejected));
        }
    }

    public void rejectUnaccepted(Trader rejecting, Trader rejected, TwoWayTrade trade){
        TraderInbox traderInbox = (TraderInbox) rejecting.getInbox();
        traderInbox.refuseUnaccepted(new TradeMessage("", rejecting, rejected, trade));
        //rejecting.getInbox().refuseUnaccepted(new TradeMessage("", rejecting, rejected, trade));
        if (rejecting == trade.getOgTrader()) {
            rejected.getInbox().addTraderNoti(new Message(rejecting.getName() + " doesn't want to trade their " +
                    trade.getOgItem() + " for your " + trade.getOtherItem() + ".", rejecting, rejected));
        } else { // (rejecting == trade.getOtherTrader())
            rejected.getInbox().addTraderNoti(new Message(rejecting.getName() + " doesn't want to trade their " +
                    trade.getOtherItem() + " for your " + trade.getOgItem() + ".", rejecting, rejected));
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
        TraderInbox traderInbox = (TraderInbox) accepting.getInbox();
        traderInbox.addTrade(new TradeMessage("", accepted, accepting, trade));
        //accepting.getInbox().addTrade(new TradeMessage("", accepted, accepting, trade));
        this.checkTransxnLimit(accepting);
        this.checkTransxnLimit(accepted);
        if (accepting == trade.getLender()){
            accepted.getInbox().addTraderNoti(new Message(accepting.getName() + " will lend you their " +
                    trade.getItem() + ".", accepting, accepted));
        }
        else { // (accepting == trade.getReceiver())
            accepted.getInbox().addTraderNoti(new Message(accepting.getName() + " will borrow your " +
                    trade.getItem() + ".", accepting, accepted));
        }
    }

    public void acceptTrade(Trader accepting, Trader accepted, TwoWayTrade trade){
        TraderInbox traderInbox = (TraderInbox) accepting.getInbox();
        traderInbox.addTrade(new TradeMessage("", accepted, accepting, trade));
        //accepting.getInbox().addTrade(new TradeMessage("", accepted, accepting, trade));
        this.checkTransxnLimit(accepting);
        this.checkTransxnLimit(accepted);
        if (accepting == trade.getOgTrader()){
            accepted.getInbox().addTraderNoti(new Message(accepting.getName() + " accepts the trade for their " +
                    trade.getOgItem() + " and your " + trade.getOtherItem() + ".", accepting, accepted));
        }
        else { // (accepting == trade.getOtherTrader())
            accepted.getInbox().addTraderNoti(new Message(accepting.getName() + " accepts the trade for their " +
                    trade.getOtherItem() + " and your" + trade.getOgItem() + ".", accepting, accepted));
        }
    }

    private void checkTransxnLimit(Trader trader){
        trader.getTraderStatus().addWeeklyTransxn();
        if (trader.getTraderStatus().overWeeklyLimit()){
            trader.getTraderStatus().flag();// Doesn't automatically freeze trader, just flags them
        }
    }

    /**
     * Allows a user to confirm that a transaction has taken place, that is, the items have been exchanged in real life.
     * If both users have confirmed the trade, then it confirms the trade.
     * @param confirming Trader that is confirming the transaction took place.
     * @param trade The trade that is being confirmed.
     */
    public void confirmTrade(Trader confirming, Trade trade){
        if (!trade.getConfirmations().contains(confirming)){
            trade.addConfirmation(confirming);
            if(trade.getConfirmations().size() == 2){
                this.confirm(trade);
            }
        }
    }

    private void confirm(Trade trade){
        if (trade.isPermanent() || trade.isNoMeet()){
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

    private void completeTrade(Trade trade){
        Trader ogTrader = trade.getOgTrader();
        Trader otherTrader = trade.getOtherTrader();

        trade.setOpen(false);
        ogTrader.getTraderStatus().removeIncomplete(); // Unflags the user if this puts them in an acceptable range
        otherTrader.getTraderStatus().removeIncomplete();

        ogTrader.addTradingPartner(otherTrader);
        otherTrader.addTradingPartner(ogTrader);

        trade.addRecentItem();

        TraderInbox traderInbox = (TraderInbox) ogTrader.getInbox();
        traderInbox.completeTrade(otherTrader, trade, "The trade:\n" + trade + "\n has been completed.");
        //ogTrader.getInbox().completeTrade(otherTrader, trade, "The trade:\n" + trade + "\n has been completed.");
    }

    /**
     * Cancels a trade between two traders, if both agree to its cancellation. Traders can not cancel an open trade
     * (the trade must be cancelled before setting a meeting). Once the trade is cancelled, removes it from the traders'
     * lists of active trades, and sends an appropriate notification to both traders' inboxes.
     * @param cancelling The trader who is requesting to cancel the trade.
     * @param trade The trade that is requesting to be cancelled.
     */
    public void cancelTrade(Trader cancelling, Trade trade){
        Trader other;
        if (cancelling == trade.getOgTrader()){
            other = trade.getOtherTrader();
        }
        else {
            other = trade.getOgTrader();
        }
        if ((!trade.getCancellations().contains(cancelling)) && !trade.isOpen()){
            trade.addCancellation(cancelling);
            if(trade.getCancellations().size() == 2){
                TraderInbox traderInbox = (TraderInbox) cancelling.getInbox();
                traderInbox.cancelTrade(trade, other, "The trade:\n" + trade + "\nhas been cancelled.");
                //cancelling.getInbox().cancelTrade(trade, other, "The trade:\n" + trade + "\nhas been cancelled.");
            }
        }
    }
}
