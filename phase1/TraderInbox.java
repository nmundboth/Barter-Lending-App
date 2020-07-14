package phase1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Inbox of a Trader
 */
public class TraderInbox extends Inbox implements Serializable {

    public TraderInbox(List<Trade> trades, List<String> traderNotifs, List<String> adminNotifs){
        super(trades, traderNotifs, adminNotifs);
    }
}
