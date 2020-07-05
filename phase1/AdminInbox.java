package phase1;

import java.util.List;

public class AdminInbox extends Inbox{

    public AdminInbox(List<String> traderNotifs, List<String> adminNotifs){
        super(traderNotifs, adminNotifs);

    }

    public int getTotalUnread(){
        return super.getTotalUnread();
    }
    //Returns the total number of unread messages

    // Returns messages from Traders
    public int getTradersUnread(){
        return super.getTradersUnread();
    }
}
