package phase1;

import java.io.Serializable;
import java.util.List;

public class AdminInbox extends Inbox implements Serializable {

    private List<Trader> undoFrozen;
    // User can request to be unfrozen and then an instance of the user is received inside the sub-inbox undoFrozen
    // I figured this is the easiest way to do it
//    private List<String> adminNoti;
//    // Notification from other admins
//    private List<String> traderNoti;
//    // Notification from traders


    private int traderUnread ;
    //The number of unread messages form other traders
    private int admiNotiUnread;
    //The number of unread messages form admins
    private int undoFrozenUnread;
    //The number of unread unfreezing requests from traders

    public AdminInbox(List<Trade> trades, List<String> traderNoti, List<String> adminNotifs, List<Trader> undoFrozen){
        super(trades, traderNoti, adminNotifs);
        this.undoFrozen = undoFrozen;
        this.admiNotiUnread = 0;
        this.traderUnread = 0;
        this.undoFrozenUnread = 0;
    }
    // Returns messages from Traders


    public int getTradersUnread(){return this.traderUnread;}
    public int getAdmiNotiUnread(){return this.admiNotiUnread;}
    public int getUndoFrozenUnread(){return this.undoFrozenUnread;}
    public int getTotalUnread(){return this.traderUnread + this.admiNotiUnread + undoFrozenUnread;}

    // getters for unread messages

    public void getUndoFrozen(int index){
        Trader temp = this.undoFrozen.get(index);
        this.undoFrozen.remove(index);
        this.undoFrozenUnread -= 1;
        if(temp.isFrozen()){
            temp.setFrozen(true);
        }
    }
    // method for undoing frozen status for a user

    public String getAdminNoti(int index){
        String temp = this.getAdmiNoti().get(index);
        this.getAdmiNoti().remove(index);
        this.admiNotiUnread -= 1;
        return temp;
    }
    //method for accessing admin notifications

    public String getTraderNoti(int index){
        String temp = this.getTraderNoti().get(index);
        this.getTraderNoti().remove(index);
        this.traderUnread -= 1;
        return temp;
    }
    // method for accessing trader notification

}
