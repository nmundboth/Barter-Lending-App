package phase1;

import java.util.List;

public class MeetingManager {

    private List<Trader> incompleteFlagged;

    public MeetingManager(List<Trader> incompleteFlagged){
        this.incompleteFlagged = incompleteFlagged;
    }

    public List<Trader> getIncompleteFlagged(){
        return incompleteFlagged;
    }

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
        meeting.setAll(location, date, time, sender); //TODO: Change setAll if we decide to not use proposedBy variable
        trade.setOgEdits(trade.getOgEdits() - 1);
        this.sendMeet(sender, receiver, meeting, trade);
    }

    public void otherScheduleMeet(Trade trade, String location, String date, String time){
        Trader sender = trade.getOtherTrader();
        Trader receiver = trade.getOgTrader();
        Meeting meeting = trade.getMeeting();
        meeting.setAll(location, date, time, sender); //TODO: Change setAll if we decide to not use proposedBy variable
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
    // Or, we just rely on the sending user not being able to confirm meetings since they won't get a notification in their inbox
    // Then we can remove the requirement for the proposedBy variable

    public void confirmMeet(Trader confirmer, TwoWayTrade trade){
        this.helperConfirmMeet(confirmer, trade);
    }

    public void confirmMeet(Trader confirmer, OneWayTrade trade) {
        this.helperConfirmMeet(confirmer, trade);
        trade.getLender().setGreedyInt(trade.getLender().getGreedyInt() - 1);
        trade.getReceiver().setGreedyInt(trade.getReceiver().getGreedyInt() + 1);
    }

    // Helper for the above methods - wanted to overload methods, but avoid duplicate code.
    public void helperConfirmMeet(Trader confirmer, Trade trade) {
        this.checkIncompleteLimit(trade.getOgTrader());
        this.checkIncompleteLimit(trade.getOtherTrader());
        trade.setOpen(true);
        trade.setOgEdits(0);
        trade.setOtherEdits(0);
        Trader receiver;
        Meeting meeting = trade.getMeeting();
        if (confirmer == trade.getOgTrader()) {
            receiver = trade.getOtherTrader();
        } else {// confirmer == trade.getOtherTrader()
            receiver = trade.getOgTrader();
        }
        receiver.getInbox().setTradeUnread(receiver.getInbox().getTradeUnread() + 1);
        receiver.getInbox().addTraderNoti("Meeting with " + confirmer.getName() + " confirmed for "
                + meeting.getTime() + " on " + meeting.getDate() + " at " + meeting.getLocation() +
                " to conduct the following trade:\n" + trade);
    }

    public void checkIncompleteLimit(Trader trader){
        trader.addIncomplete();
        if (trader.overIncompleteLimit()){
            incompleteFlagged.add(trader); // Doesn't automatically freeze trader, just flags them
        }
    }
}
