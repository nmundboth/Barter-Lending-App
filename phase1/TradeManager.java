package phase1;

import java.util.List;

public class TradeManager implements Tradable{

    private List<Trader> transxnFlagged; // Users flagged to be frozen (but not yet frozen) - should be accessed when admin wants to view users to flag
    //This class relies on the input of two traders to the methods, so will need to have something like "current trader"
    //and "desired trader" (person they are interacting with) to be input from the controller - will be possible using
    //UserCatalogue and having the user select which user they want to trade with/borrow from, etc.

    public TradeManager(List<Trader> transxnFlagged){
        this.transxnFlagged = transxnFlagged;
    }

    public List<Trader> getTransxnFlagged(){
        return transxnFlagged;
    }

    //TODO: Gate all Trade Request functionality behind whether a trader is frozen (can do in controller)

    //TODO: Gate borrow functionality behind GreedyUser integer of receiver (can be done in controller level)
    public void sendBorrowRequest(Trader ogTrader, Trader otherTrader, Item item, boolean isPermanent){
        OneWayTrade trade = new OneWayTrade(ogTrader, otherTrader, item);
        trade.setReceiver(ogTrader);
        trade.setLender(otherTrader);
        trade.setPermanent(isPermanent);
        ogTrader.getInbox().addUnaccepted(trade, otherTrader);
        otherTrader.getInbox().addTraderNoti(ogTrader.getName() + " wants to borrow " + item + " from you.");
    }

    //TODO: Gate lend functionality behind GreedyUser integer of receiver (can be done in controller level)
    public void sendLendRequest(Trader ogTrader, Trader otherTrader, Item item, boolean isPermanent){
        OneWayTrade trade = new OneWayTrade(ogTrader, otherTrader, item);
        trade.setLender(ogTrader);
        trade.setReceiver(otherTrader);
        trade.setPermanent(isPermanent);
        ogTrader.getInbox().addUnaccepted(trade, otherTrader);
        otherTrader.getInbox().addTraderNoti(ogTrader.getName() + " wants to lend you " + item + ".");
    }

    //Two-way trade request
    public void sendTWTradeRequest(Trader ogTrader, Trader otherTrader, Item ogItem, Item otherItem,
                                   boolean isPermanent){
        TwoWayTrade trade = new TwoWayTrade(ogTrader, otherTrader, ogItem, otherItem);
        trade.setPermanent(isPermanent);
        ogTrader.getInbox().addUnaccepted(trade, otherTrader);
        otherTrader.getInbox().addTraderNoti(ogTrader.getName() + " wants to trade their " + ogItem +
                " for your " + otherItem + ".");
    }

    // Overloading this method depending on whether OneWay or TwoWay trade is input (see following two methods)
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

    // Overloading the next two similarly.
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

    public void checkTransxnLimit(Trader trader){
        trader.addWeeklyTransxn();
        if (trader.overWeeklyLimit()){
            transxnFlagged.add(trader); // Doesn't automatically freeze trader, just flags them
        }
    }

    // Can add an else block (outer layer) that prints "You have already confirmed the trade!" if the trader has already confirmed.
    public void confirmTrade(Trader confirming, Trader other, Trade trade){
        if (!trade.getConfirmations().contains(confirming)){
            trade.addConfirmation(confirming);
            if(trade.getConfirmations().size() == 2){
                this.confirm(confirming, other, trade);
            }
        }
    }

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

    public void completeTrade(Trade trade){
        Trader ogTrader = trade.getOgTrader();
        Trader otherTrader = trade.getOtherTrader();

        trade.setOpen(false);
        ogTrader.removeIncomplete(); // Note this is not removing them from being flagged completing the trade drops them into an acceptable range
        otherTrader.removeIncomplete();

        ogTrader.addTradingPartner(otherTrader);
        otherTrader.addTradingPartner(ogTrader);

        trade.addRecentItem();

        ogTrader.getInbox().completeTrade(otherTrader, trade, "The trade:\n" + trade + "\n has been completed.");
    }

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
