package group_0073.phase1;

import java.util.ArrayList;
import java.util.List;

public class Inbox {

    private List<Trade> trades;
    private List<String> notifs;
    private List<Trade> unacceptedTrades;

    public Inbox(List<Trade> trades, List<String> notifs){
        //Admin cannot trade so trades for admin is an empty list?
        this.trades = trades;
        this.notifs = notifs;
        this.unacceptedTrades = new ArrayList<Trade>();
    }

    public List<String> getNotifs() {
        return notifs;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public List<Trade> getUnacceptedTrades() {
        return unacceptedTrades;
    }
}
