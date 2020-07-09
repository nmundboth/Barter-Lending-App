package phase1;

import java.time.Period;
import java.time.LocalDate;
import java.util.ArrayList;

public class Trade {

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

    public Trader getOgTrader(){
        return ogTrader;
    }

    public Trader getOtherTrader(){
        return otherTrader;
    }

    public int getOgEdits() {
        return ogEdits;
    }

    public int getOtherEdits() {
        return otherEdits;
    }

    public ArrayList<Trader> getConfirmations(){
        return confirmations;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public Meeting getMeeting(){
        return meeting;
    }

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

    public void setOgEdits(int ogEdits) {
        this.ogEdits = ogEdits;
    }

    public void setOtherEdits(int otherEdits) {
        this.otherEdits = otherEdits;
    }

    public void addCancellation(Trader trader){
        cancellations.add(trader);
    }

    public ArrayList<Trader> getCancellations(){
        return cancellations;
    }

    public void addConfirmation(Trader trader){
        confirmations.add(trader);
    }

    public void clearConfirmations(){
        confirmations.clear();
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public void addRecentItem(){
    }

    public void removeItems(){
    }
}