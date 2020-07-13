package phase1;

import java.io.Serializable;
import java.time.Period;
import java.time.LocalDate;
import java.util.ArrayList;

/** Represents a Trade object.
 * @author Navnee Mundboth
 * @author James Veale
 */
public class Trade implements Serializable {

    // og is Original Trader
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

    /** Gets the number of all meetings completed between the two Traders.
     *
     * @return an integer which shows the number of completed meetings.
     */
    public int getCompletedMeetings(){
        return completedMeetings;
    }

    // Currently does not take time of day into account, just adds one month, and sets the meeting to be at the same
    // time of day as the first one.
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
     * @param ogEdits
     */
    public void setOgEdits(int ogEdits) {
        this.ogEdits = ogEdits;
    }

    /** Sets the number of edits available to the other Trader.
     *
     * @param otherEdits
     */
    public void setOtherEdits(int otherEdits) {
        this.otherEdits = otherEdits;
    }

    /**
     *
     * @param trader
     */
    public void addCancellation(Trader trader){
        cancellations.add(trader);
    }

    /**
     *
     * @return
     */
    public ArrayList<Trader> getCancellations(){
        return cancellations;
    }

    /**
     *
     * @param trader
     */
    public void addConfirmation(Trader trader){
        confirmations.add(trader);
    }

    /**
     *
     */
    public void clearConfirmations(){
        confirmations.clear();
    }

    /** Sets a Trade as open or closed. true is open and closed is false.
     *
     * @param open
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /** Sets a Trade as permanent or temporary. true is permanent and false is temporary.
     *
     * @param permanent
     */
    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public void addRecentItem(){
    }

    public void removeItems(){
    }
}