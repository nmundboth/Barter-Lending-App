package phase2;

import java.io.Serializable;

/**
 * <h1>Meeting</h1>
 * <p>The meeting class contains information pertaining to a meeting associated with a particular trade.</p>
 */
public class Meeting implements Serializable {

    private String location = "";
    private String date = ""; // Format should be "YYYY-MM-DD" -> NEED TO HAVE TRY/CATCH BLOCK IN CONTROLLER
    private String time = "";
    private Trader proposedBy;

    public Meeting(){
    }

    /**
     * @param location location of the meeting
     * @param date date of the meeting
     * @param time time of the meeting
     */
    public Meeting(String location, String date, String time){
        this.location = location;
        this.date = date;
        this.time = time;
    }

    /**
     * Checks if a meeting has been proposed (if it is empty, no meeting has been proposed).
     *
     * @return Whether the meeting is empty (hasn't been proposed)
     */
    public boolean isEmpty(){
        return (location.equals("") && date.equals("") && time.equals(""));
    }

    /**
     * Gets the date of the meeting.
     *
     * @return a string representing the date of the meeting.
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the time of the meeting.
     *
     * @return a string representing the time of the meeting
     */
    public String getTime() {
        return time;
    }

    /**
     * Gets the location of the meeting.
     *
     * @return a string representing the location of the meeting.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the trader that most recently proposed the meeting.
     *
     * @return a Trader object which represents who has most recently proposed this meeting
     */
    public Trader getProposedBy(){
        return proposedBy;
    }

    /**
     * Sets all meeting values (location, date, time, and trader that proposed the trade.
     *
     * @param location The location of the meeting
     * @param date The date of the meeting (YYYY-MM-DD)
     * @param time The time of the meeting
     * @param proposedBy Trader that most recently proposed the meeting
     */
    public void setAll(String location, String date, String time, Trader proposedBy){
        this.location = location;
        this.date = date;
        this.time = time;
        this.proposedBy = proposedBy;
    }

    /**
     * Sets the date of a meeting. Used when automatically setting the second meeting for a temporary trade.
     *
     * @param date the date of the meeting (YYYY-MM-DD)
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns a string representation of a meeting (doesn't include proposed by variable).
     *
     * @return a string with meeting details
     */
    public String toString(){
        return "Location: " + this.location + ", " + "Date: " + this.date + ", " + "Time: " + this.time;
    }
}
