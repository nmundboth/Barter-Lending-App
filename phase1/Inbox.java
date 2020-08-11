package phase1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

abstract class Inbox implements Serializable {

    //Notifications from other Traders and Admins
    private List<Message> traderNoti;
    private List<Message> admiNoti;

    private int traderNotiUnread = 0;
    // The number of Traders notification unread
    private int admiNotiUnread = 0;
    //The number of unread messages form admins
    //<trades> for admin user should be empty

    public User OG;

    /**
     * Constructor for the Inbox class
     * @param traderNotifs A List representing the other Trader notification sub-inbox
     * @param adminNotifs A list representing the Admin/System notifications sub-inbox
     */
    public Inbox(List<Message> traderNotifs, List<Message> adminNotifs){
        this.traderNoti = traderNotifs;
        this.admiNoti = adminNotifs;
        this.OG = null;
    }

    //Getters for sub-inboxes
    /**
     * Getter for the AdminNoti sub-inbox
     * @return AdminNoti List
     */
    protected List<Message> getAdmiNoti() { return admiNoti; }

    /**
     * a getter for the TraderNotification sub-inbox
     * @return the TraderNotification sub-inbox
     */
    protected List<Message> getTraderNoti() { return traderNoti; }


    // Getters for the un-read messages
    /**
     * A getter for the Trades sub-inbox
     * @return the sub-inbox
     */
    protected int getTraderNotiUnread(){return traderNotiUnread; }

    /**
     * returns the number of messages from the admin that are left unread
     * @return the number of unread messages
     */
    protected int getAdmiNotiUnread() { return admiNotiUnread; }

    /**
     * Getter for the total number of unread messages
     * @return an integer representing the number of unread messages
     */
    protected int getTotalUnread(){ return this.traderNotiUnread + this.admiNotiUnread; }

    /**
     * The setter for the number of unread admin messages
     * @param admiNotiUnread the number of unread messages
     */
    protected void setAdmiNotiUnread(int admiNotiUnread) { this.admiNotiUnread = admiNotiUnread; }

    /**
     * A getter for the user to be able to access a message from the Trade inbox
     * User is represented with a list of messages they can chose from
     * @param index represents the position of a desired message from the inbox
     */
    protected void getTradeNoti(int index){
        Message message = this.traderNoti.get(index);
        this.traderNoti.remove(index);
        this.traderNotiUnread -= 1;
        message.read();
    }

    /**
     * A getter for the user to be able to access a message from the Admin inbox
     * User is represented with the position of a desired message from the inbox
     * @param index represents the position of a desired message from the inbox
     */
    protected void getAdminNoti(int index){
        Message message = this.admiNoti.get(index);
        this.admiNoti.remove(index);
        this.admiNotiUnread -= 1;
        message.read();
    }

    protected void addAdminNoti(Message message){ this.admiNoti.add(message);}

    protected void addTraderNoti(Message message){ this.traderNoti.add(message);}

    protected void setOwner(User user){this.OG = user;}

}
