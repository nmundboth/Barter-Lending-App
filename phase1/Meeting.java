package phase1;

public class Meeting {

    private String location;
    private String date;
    private String time;

    public Meeting(String location, String date, String time){
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    // Setters are for traders to modify meeting
    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
