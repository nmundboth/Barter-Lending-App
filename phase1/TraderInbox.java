package phase1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TraderInbox extends Inbox implements Serializable {

    public TraderInbox(List<Trade> trades, List<String> traderNotifs, List<String> adminNotifs){
        super(trades, traderNotifs, adminNotifs);
    }
}
