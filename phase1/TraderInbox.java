package phase1;

import java.util.ArrayList;
import java.util.List;

public class TraderInbox extends Inbox{

    private List<Trade> trades;
    //All the trades available to the user
    private List<Trade> unacceptedTrades;
    //All unaccepted offers


    private int unaccptedUnread;
    //The number of unread messages for each category

    public TraderInbox(List<String> traderNotifs, List<String> adminNotifs, List<Trade> trades){
        super(traderNotifs, adminNotifs);
        this.trades = trades;
        this.unacceptedTrades = new ArrayList<Trade>();
        this.unaccptedUnread = 0;
    }

    public List<Trade> getTrades() {
        return trades;
    }
    //getter for trades

    public List<Trade> getUnacceptedTrades() {
        return unacceptedTrades;
    }
    //getter for unaccepted offers

    public int getTotalUnread(){
        return super.getTotalUnread() + this.unaccptedUnread;
    }
    //Returns the total number of unread messages

    // Returns messages from Traders
    public int getTradersUnread(){
        return super.getTradersUnread() + this.unaccptedUnread;
    }

    public Trade getTrade(int index){
        Trade temp = this.trades.get(index);
        this.trades.remove(index);
        super.tradeUnreadMinusOne();
        return temp;
    }
    // Returns a certain Trade from the list. I'm not sure if we are keeping a message after being accessed
    // For now I'm assuming we are removing it instantly from the list, feel free to change

    public Trade getUnacceptedTrades(int index){
        Trade temp = this.unacceptedTrades.get(index);
        this.unacceptedTrades.remove(index);
        super.adminNotiUnreadMinusOne();
        return temp;
    }
}
