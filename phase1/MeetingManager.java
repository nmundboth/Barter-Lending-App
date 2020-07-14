package phase1;

/**
 * <h1>Meeting Functions</h1>
 * <p>The MeetingManager class contains methods that allow traders to propose meetings for specific trades to each
 * other, and confirm or edit proposed meetings from other traders.</p>
 */

public class MeetingManager {

    public MeetingManager(){
    }

    /**
     * Checks whether a Trader is allowed to propose/edit a meeting (each trader gets 3 edits in total).
     * If both users have used up all of their meeting proposals, then the only options are to confirm or cancel a
     * trade (no more meeting proposals allowed). Similarly, if one user has used all of their edits, then they can
     * no longer propose a meeting, and must either confirm a meeting set by the other user, or request to cancel the
     * trade.
     * @param setter The trader proposing the meeting.
     * @param trade The trade that the meeting is being proposed for.
     * @param location The location of the meeting.
     * @param date The date of the meeting. <b>Format: "YYYY-MM-DD"</b>
     * @param time The time of the meeting.
     */
    public void proposeMeeting(Trader setter, Trade trade, String location, String date, String time){
        if (trade.getOgEdits() == 0 && trade.getOtherEdits() == 0){
            System.out.println("Both users involved in this trade have used all of their available edits.\n" +
                    "Please accept the meeting or cancel the trade.");
        }
        else if (setter == trade.getOgTrader()){
            if (trade.getOgEdits() == 0){
                System.out.println("You have no available edits for this trade.\n" +
                        "Please accept the meeting or cancel the trade.");
            }
            else {
                this.ogScheduleMeet(trade, location, date, time);
                System.out.println("Meeting successfully proposed to " + trade.getOtherTrader().getName() + ".\n" +
                        "Remaining edits: " + trade.getOgEdits());
            }
        }
        else { // setter == trade.getOtherTrader
            if (trade.getOtherEdits() == 0){
                System.out.println("You have no available edits for this trade.\nPlease accept or cancel the trade.");
            }
            else {
                this.otherScheduleMeet(trade, location, date, time);
                System.out.println("Meeting successfully proposed to " + trade.getOgTrader().getName() + ".\n" +
                        "Remaining edits: " + trade.getOtherEdits());
            }
        }
    }

    private void ogScheduleMeet(Trade trade, String location, String date, String time){
        Trader sender = trade.getOgTrader();
        Trader receiver = trade.getOtherTrader();
        Meeting meeting = trade.getMeeting();
        meeting.setAll(location, date, time, sender);
        trade.setOgEdits(trade.getOgEdits() - 1);
        this.sendMeet(sender, receiver, meeting, trade);
    }

    private void otherScheduleMeet(Trade trade, String location, String date, String time){
        Trader sender = trade.getOtherTrader();
        Trader receiver = trade.getOgTrader();
        Meeting meeting = trade.getMeeting();
        meeting.setAll(location, date, time, sender);
        trade.setOtherEdits(trade.getOtherEdits() - 1);
        this.sendMeet(sender, receiver, meeting, trade);
    }

    private void sendMeet(Trader sender, Trader receiver, Meeting meeting, Trade trade){
        receiver.getInbox().setTradeUnread(receiver.getInbox().getTradeUnread() + 1);
        receiver.getInbox().addTraderNoti("Hey " + receiver.getName() + "! Can you meet " + sender.getName() +
                " at " + meeting.getTime() + " on " + meeting.getDate() + " at " + meeting.getLocation() +
                " to conduct the following trade?\n" + trade);
    }

    /**
     * Allows a trader that has received a meeting proposal to confirm a meeting time.
     * Sets available edits to 0, so that neither user can edit the meeting after it is confirmed.
     * Sets the trade to open, adjusting amount of incomplete trades for each user, since this trade is now
     * considered incomplete (open).
     * Adjust greedy user integers if the meeting is for a One-Way Trade.
     * @param trade The trade for which the meeting is being confirmed.
     */

    public void confirmMeet(Trader confirmer, TwoWayTrade trade){
        this.helperConfirmMeet(confirmer, trade);
    }

    public void confirmMeet(Trader confirmer, OneWayTrade trade) {
        this.helperConfirmMeet(confirmer, trade);
        trade.getLender().setGreedyInt(trade.getLender().getGreedyInt() - 1);
        trade.getReceiver().setGreedyInt(trade.getReceiver().getGreedyInt() + 1);
    }


    private void helperConfirmMeet(Trader confirmer, Trade trade) {
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

    private void checkIncompleteLimit(Trader trader){
        trader.addIncomplete();
        if (trader.overIncompleteLimit()){
            trader.flag(); // Doesn't automatically freeze trader, just flags them
        }
    }
}
