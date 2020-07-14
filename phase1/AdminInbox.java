package phase1;

import java.io.Serializable;
import java.util.List;

public class AdminInbox extends Inbox implements Serializable {

    /**
     * A list representing the undoFrozen sub-inbox for the Admin to view
     */
    static List<Trader> undoFrozen;

    /**
     * The number of unread messages form other traders
     */
    private int traderUnread ;

    /**
     * The number of unread messages form admins
     */
    private int admiNotiUnread;

    /**
     * The number of unread unfreezing requests from traders
     */
    private int undoFrozenUnread;


    /**
     * Constructor for the AdminInbox class
     * @param traderNoti is all text-based notifications for an Admin from Traders
     * @param adminNotifs is all text-based notifications for an Admin from another Admin/System
     */
    public AdminInbox(List<Trade> trades, List<String> traderNoti, List<String> adminNotifs){
        super(trades, traderNoti, adminNotifs);
        this.admiNotiUnread = 0;
        this.traderUnread = 0;
        this.undoFrozenUnread = 0;
    }

    /**
     * Getter for traderUnread
     * @return returns the number of unread trader notification
     */
    public int getTradersUnread(){return this.traderUnread;}

    /**
     * Getter for AdmiNotiUnread
     * @return returns the number of unread Admin/System notification
     */
    public int getAdmiNotiUnread(){return this.admiNotiUnread;}

    /**
     * getter for UndoFrozenUnread
     * @return returns the number of unread user unfreezing requests
     */
    public int getUndoFrozenUnread(){return this.undoFrozenUnread;}

    /**
     * Getter for all three unread sub inbox messages
     * @return returns the total number of unread messages
     */
    public int getTotalUnread(){return this.traderUnread + this.admiNotiUnread + this.undoFrozenUnread;}


    /**
     * Getter for undoFrozen list
     * @return a list of frozen users
     */
    public List<Trader> getUndoFrozen(){return undoFrozen; }

    /**
     * Method for accessing a frozen user from the request sub-inbox
     * Admin is represented with a list of these users in AdminOptions
     * Admin can then chose a user to unfreeze using this method
     * @param index is the position of a chosen user inside the undoFrozen inbox
     */
    public void showUndoFrozen(int index){
        Trader temp = undoFrozen.get(index);
        undoFrozen.remove(index);
        this.undoFrozenUnread -= 1;
        if(temp.isFrozen()){
            temp.frozen = false;
        }
    }

    /**
     * This method serves as a tool for an Admin to access a message within the AdminNoti sub-inbox
     * Admin is represented with a list of messages in AdminOptions from which Admin can chose one to open and read
     * @param index is the position of a specific message within the sub-inbox
     * @return returns a String that contains the message for Admin
     */
    public String showAdminNoti(int index){
        String temp = this.getAdmiNoti().get(index);
        this.getAdmiNoti().remove(index);
        this.admiNotiUnread -= 1;
        return temp;
    }

    /**
     * This method serves as a tool for an Admin to access a message within the TraderNoti sub-inbox
     * Admin is represented with a list of messages in AdminOptions from which Admin can choose one to open and read
     * @param index is the position of a specific message within the sub-inbox
     * @return returns the String that contains the message for Admin
     */
    public String showTraderNoti(int index){
        String temp = this.getTraderNoti().get(index);
        this.getTraderNoti().remove(index);
        this.traderUnread -= 1;
        return temp;
    }
    // method for accessing trader notification

}
