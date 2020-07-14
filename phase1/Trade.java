package phase1;

import java.io.Serializable;
import java.time.Period;
import java.time.LocalDate;
import java.util.ArrayList;

/** <h1>Trade</h1>
 * <p>Represents a trade object.</p>
 * @author Navnee Mundboth
 * @author James Veale
 */
public class Trade implements Serializable {

    protected Trader ogTrader;
    protected Trader otherTrader;
    private int ogEdits;
    private int otherEdits;
    private boolean open;
    protected boolean permanent;
    private ArrayList<Trader> confirmations;
    private ArrayList<Trader> cancellations;
    private Meeting meeting;
    private int completedMeetings; // For temporary trades only -> tracks whether this is the first or second meeting.
    private Period tempTradePeriod;

    /**
     * @param ogTrader the trader that originally proposed this trade
     * @param otherTrader the trader that originally received this trade request
     */
    public Trade(Trader ogTrader, Trader otherTrader){
        this.ogTrader = ogTrader;
        this.otherTrader = otherTrader;
        this.ogEdits = 3;
        this.otherEdits = 3;
        this.open = false;
        this.permanent = false;
        this.confirmations = new ArrayList<Trader>();
        this.cancellations = new ArrayList<Trader>();
        this.meeting = new Meeting();
        this.tempTradePeriod = Period.ofMonths(1); // Change this to change the length of a temporary trade
    }

    /** Gets the original Trader
     *
     * @return the object Trader.
     */
    public Trader getOgTrader(){
        return ogTrader;
    }

    /** Gets the Trader that the original Trader is trading with.
     *
     * @return the object Trader.
     */
    public Trader getOtherTrader(){
        return otherTrader;
    }

    /** The number of available edits an original Trader has for a meeting with another Trader.
     *
     * @return an integer which calculate how many edits the original Trader is left with when arranging a meeting
     * with another Trader.
     */
    public int getOgEdits() {
        return ogEdits;
    }

    /** The number of available edits the other Trader has for a meeting with the original Trader.
     *
     * @return an integer which calculate how many edits the other Trader is left with when arranging a meeting
     * with the original Trader.
     */
    public int getOtherEdits() {
        return otherEdits;
    }

    /**
     * Gets the users that have confirmed that a trade has taken place.
     *
     * @return an ArrayList representing the users that have confirmed the trade.
     */
    public ArrayList<Trader> getConfirmations(){
        return confirmations;
    }

    /** Says if the Trade is open or not. If isOpen() returns true, then the Trade is open. Otherwise, no.
     *
     * @return a boolean to show the status of a Trade (transaction).
     */
    public boolean isOpen() {
        return open;
    }

    /** Says if the Trade is permanent. A boolean returning true means the trade is permanent. Otherwise no.
     *
     * @return a boolean to show if the Trade is permanent.
     */
    public boolean isPermanent() {
        return permanent;
    }

    /** Gets the Meeting between the two Traders.
     *
     * @return the Meeting object.
     */
    public Meeting getMeeting(){
        return meeting;
    }

    /** Gets whether 0 or 1 meetings have happened for the current trade (only used by temporary trades).
     *
     * @return an integer which shows the number of completed meetings.
     */
    public int getCompletedMeetings(){
        return completedMeetings;
    }

    /**
     * Completes the first meeting of the trade, and automatically sets the second one according to tempTradePeriod.
     */
    public void completeFirstMeeting(){
        completedMeetings = 1;
        LocalDate currMeeting = LocalDate.parse(meeting.getDate());
        LocalDate secondMeeting = currMeeting.plus(tempTradePeriod);
        meeting.setDate(secondMeeting.toString());

        ogTrader.getInbox().tradeConfirmed(otherTrader, "First meeting for the trade:\n" + this +
                "\nhas been completed. Second meeting set for " + secondMeeting.toString());
    }

    /** Sets the number of edits available to the original Trader.
     *
     * @param ogEdits integer representation of the number of edits available to the original trader.
     */
    public void setOgEdits(int ogEdits) {
        this.ogEdits = ogEdits;
    }

    /** Sets the number of edits available to the other Trader.
     *
     * @param otherEdits integer representation of the number of edits available to the other trader.
     */
    public void setOtherEdits(int otherEdits) {
        this.otherEdits = otherEdits;
    }

    /**
     * Adds a trader to the ArrayList of traders that have requested to cancel the trade.
     *
     * @param trader the trader that has requested to cancel this trade.
     */
    public void addCancellation(Trader trader){
        cancellations.add(trader);
    }

    /**
     * Gets the traders that have requested to cancel this trade.
     *
     * @return an ArrayList representing the Trader(s) that have requested to cancel this trade.
     */
    public ArrayList<Trader> getCancellations(){
        return cancellations;
    }

    /**
     * Adds a user to the list of traders that have confirmed that this trade has happened (user must be involved in
     * trade to be added to this list).
     *
     * @param trader a Trader object representing the trader that confirmed that the trade has happened.
     */
    public void addConfirmation(Trader trader){
        confirmations.add(trader);
    }

    /**
     * Removes the traders (if any) from the list of traders that have confirmed the trader.
     */
    public void clearConfirmations(){
        confirmations.clear();
    }

    /** Sets a Trade as open or closed. true is open and closed is false.
     *
     * @param open a boolean representing whether the trade is open.
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /** Sets a Trade as permanent or temporary. true is permanent and false is temporary.
     *
     * @param permanent a boolean representing whether the trade is open.
     */
    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    /**
     * Method that has varying implementation depending on whether the trade is a OneWayTrade or TwoWayTrade.
     */
    public void addRecentItem(){
    }

    /**
     * Method that has varying implementation depending on whether the trade is a OneWayTrade or TwoWayTrade.
     */
    public void removeItems(){
    }
}