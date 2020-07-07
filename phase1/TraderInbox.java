package phase1;

import java.util.ArrayList;
import java.util.List;

public class TraderInbox extends Inbox{

    public TraderInbox(List<Trade> trades, List<String> traderNotifs, List<String> adminNotifs){
        super(trades, traderNotifs, adminNotifs);
    }
}
