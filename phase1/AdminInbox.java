package phase1;

import java.util.List;

public class AdminInbox extends Inbox{

    public AdminInbox(List<Trade> trades, List<String> traderNotifs, List<String> adminNotifs){
        super(trades, traderNotifs, adminNotifs);
    }

    // Returns messages from Traders
    public int getTradersUnread(){
        return super.getTradersUnread();
    }
}
