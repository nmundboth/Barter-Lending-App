package phase1;

public class TradeManager implements TradeScreen{

    //This class relies on the input of two traders to the methods, so will need to have something like "current trader"
    //and "desired trader" (person they are interacting with) to be input from the controller - will be possible using
    //UserCatalogue and having the user select which user they want to trade with/borrow from, etc.

    public TradeManager(){
    // Could just use default constructor but I like having a constructor even if its empty - feel free to remove.
    }

    //TODO: Gate all Trade Request functionality behind whether a trader is frozen (can do in controller)

    //TODO: Gate borrow functionality behind GreedyUser integer
    public void sendBorrowRequest(Trader ogTrader, Trader otherTrader, Item item, boolean isPermanent){
        OneWayTrade trade = new OneWayTrade(ogTrader, otherTrader, item);
        trade.setReceiver(ogTrader);
        trade.setLender(otherTrader);
        trade.setPermanent(isPermanent);
        ogTrader.getInbox().addUnaccepted(trade, otherTrader);
        otherTrader.getInbox().addTraderNoti(ogTrader.getName() + " wants to borrow " + item + " from you.");
        // Can either do "from you" or "from" + otherTrader.getName()
        // Note that I added a toString method for the Item class that is just the name of the item.
    }

    public void sendLendRequest(Trader ogTrader, Trader otherTrader, Item item, boolean isPermanent){
        OneWayTrade trade = new OneWayTrade(ogTrader, otherTrader, item);
        trade.setLender(ogTrader);
        trade.setReceiver(otherTrader);
        trade.setPermanent(isPermanent);
        ogTrader.getInbox().addUnaccepted(trade, otherTrader);
        otherTrader.getInbox().addTraderNoti(ogTrader.getName() + " wants to lend you " + item + ".");
        // Same thing here, can either do "wants to lend you" or "wants to lend " + otherTrader.getName()
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
        if (accepting == trade.getOgTrader()){
            accepted.getInbox().addTraderNoti(accepting.getName() + " accepts the trade for their " +
                    trade.getOgItem() + " and your " + trade.getOtherItem() + ".");
        }
        else { // (accepting == trade.getOtherTrader())
            accepted.getInbox().addTraderNoti(accepting.getName() + " accepts the trade for their " +
                    trade.getOtherItem() + " and your" + trade.getOgItem() + ".");
        }
    }

    // I'm using booleans here to handle the case that a User is trying to confirm a trade that they have already
    // confirmed. The booleans will be received by the presenter and output the appropriate text to the screen.
    //TODO: Related to above, need to track unconfirmed transactions per user per week.
    //TODO: Once transaction has been confirmed by both users, should remove items involved in the transaction from the
    // appropriate item lists — likely need to overload another method here depending on OneWay or TwoWayTrade.
    public boolean confirmTrade(Trader confirming, Trader other, Trade trade){
        String confirmationText = "Transaction between " + confirming.getName() + " and " + other.getName() +
                " has been confirmed.";
        if (!trade.getConfirmations().contains(confirming)){
            trade.addConfirmation(confirming);
            if(trade.getConfirmations().size() == 2){
                confirming.getInbox().tradeConfirmed(other, trade, confirmationText);
                trade.clearConfirmations(); //Clearing the confirmations so the second meeting for a temp trade can also be confirmed.
            }
            else{

            }
            return true;
        }
        else { // Trader has already confirmed the trade
            return false;
        }
    }

    //TODO: Implementation can vary, but should remove a trade once it is completed.
    public void completeTrade(Trade trade){
        Trader ogTrader = trade.getOgTrader();
        Trader otherTrader = trade.getOtherTrader();
        String completionText = "Transaction between " + ogTrader.getName() + " and " + otherTrader.getName() +
                " has been completed.";
        ogTrader.getInbox().completeTrade(otherTrader, trade, completionText);
    }

    public void cancelTrade(){
        //TODO: Could implement in a similar way to confirming a trade (i.e. have a List of Users that have requested
        // to cancel, and once the list contains both users, cancel the trade.
    }

    // ===============POSSIBLY MOVE THE FOLLOWING TO MeetingManager===============================

    // Output of this method will be passed to the controller/presenter upon a user attempting to set/edit a meeting.
    public String proposeMeeting(Trader setter, Trade trade, String location, String date, String time){
        if (trade.getOgEdits() == 0 && trade.getOtherEdits() == 0){
            return "Both users involved in this trade have used all of their available edits.\n" +
            "Would you like to cancel the trade? (Y/N)";
        }
        else if (setter == trade.getOgTrader()){
            if (trade.getOgEdits() == 0){
                return "You have no available edits for this trade.\nPlease confirm or cancel the trade.";
            }
            else {
                this.ogScheduleMeet(trade, location, date, time);
                return "Meeting successfully proposed to " + trade.getOtherTrader().getName() + ".\n" +
                        "Remaining edits: " + trade.getOgEdits();
            }
        }
        else { // setter == trade.getOtherTrader
            if (trade.getOtherEdits() == 0){
                return "You have no available edits for this trade.\nPlease confirm or cancel the trade.";
            }
            else {
                this.otherScheduleMeet(trade, location, date, time);
                return "Meeting successfully proposed to " + trade.getOgTrader().getName() + ".\n" +
                        "Remaining edits: " + trade.getOtherEdits();
            }
        }
    }

    public void ogScheduleMeet(Trade trade, String location, String date, String time){
        Trader sender = trade.getOgTrader();
        Trader receiver = trade.getOtherTrader();
        Meeting meeting = trade.getMeeting();
        meeting.setAll(location, date, time, sender);
        trade.setOgEdits(trade.getOgEdits() - 1);
        this.sendMeet(sender, receiver, meeting, trade);
    }

    public void otherScheduleMeet(Trade trade, String location, String date, String time){
        Trader sender = trade.getOtherTrader();
        Trader receiver = trade.getOgTrader();
        Meeting meeting = trade.getMeeting();
        meeting.setAll(location, date, time, sender);
        trade.setOtherEdits(trade.getOtherEdits() - 1);
        this.sendMeet(sender, receiver, meeting, trade);
    }

    /**
     * Helper method for ogScheduleMeet and otherScheduleMeet that sends a meeting request to the inbox of the receiver.
     * @param sender Trader proposing the meeting proposal.
     * @param receiver Trader receiving the meeting proposal.
     * @param meeting The meeting that is being proposed.
     */
    public void sendMeet(Trader sender, Trader receiver, Meeting meeting, Trade trade){
        receiver.getInbox().setTradeUnread(receiver.getInbox().getTradeUnread() + 1);
        receiver.getInbox().addTraderNoti("Hey " + receiver.getName() + "! Can you meet " + sender.getName() +
                " at " + meeting.getTime() + " on " + meeting.getDate() + " at " + meeting.getLocation() +
                " to conduct the following trade?\n" + trade);
    }

    /**
     * Allows a trader that has received a meeting proposal to confirm a trade.
     * Sets available edits to 0, so that neither user can edit the meeting after it is confirmed.
     * Sets the trade to open, adjusting amount of incomplete trades for each user, since this trade is now
     * considered incomplete (open).
     * Adjust greedy user integers if the meeting is for a One-Way Trade.
     * @param trade The trade for which the meeting is being confirmed.
     */
    // In controller method that interacts with this, need to make sure that the meeting is not being confirmed by the
    // person that proposed it (confirmer != meeting.getProposedBy())

    public void confirmMeet(TwoWayTrade trade){
        this.helperConfirmMeet(trade);
    }

    public void confirmMeet(OneWayTrade trade) {
        this.helperConfirmMeet(trade);
        trade.getLender().setGreedyInt(trade.getLender().getGreedyInt() - 1);
        trade.getReceiver().setGreedyInt(trade.getReceiver().getGreedyInt() + 1);
    }

    // Helper for the above methods - wanted to overload methods, but avoid duplicate code.
    public void helperConfirmMeet(Trade trade){
        trade.getOgTrader().setIncomplete(trade.getOgTrader().getIncomplete() + 1);
        trade.getOtherTrader().setIncomplete(trade.getOtherTrader().getIncomplete() + 1);
        trade.setOpen(true);
        trade.setOgEdits(0);
        trade.setOtherEdits(0);
    }
}
