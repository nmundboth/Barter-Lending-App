package group_0073.phase1;

import java.util.List;

public class Inbox {

    private List<Trade> trades;
    private List<String> notifs;

    public Inbox(List<Trade> trades, List<String> notifs){
        this.trades = trades;
        this.notifs = notifs;
    }

}
