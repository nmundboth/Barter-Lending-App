package phase1;

import java.io.Serializable;
import java.util.List;

/** Reference from: https://www.dummies.com/programming/java/how-to-use-javadoc-to-document-your-classes/
 * Represents the Inbox of an Admin
 * @author Navnee Mundboth
 * @author James Veale
 * @author Sasan Makvandi
 */
public class AdminInbox extends Inbox implements Serializable {
    /**
     * A list representing the requests for removing an item from the Trader's wish list
     */
    static List<Message> undoWishList;

    /**
     * A list representing the requests for removing an item from the Trader's inventory
     */
    static List<Message> undoInventory;

    /**
     * A list representing the requests for users to restart a trade
     */
    static List<TradeMessage> restartedTrades;

    /**
     * A list representing the undoFrozen sub-inbox for the Admin to view
     */
    static List<Trader> undoFrozen;

    /**
     * The number of unread messages form other traders
     */
    private int traderUnread;

    /**
     * The number of unread messages form admins
     */
    private int admiNotiUnread;

    /**
     * The number of unread unfreezing requests from traders
     */
    private int undoFrozenUnread;

    /**
     * The number of unread wishlist deletes available
     */
    private int unreadWishListNoti;

    /**
     * The number of unread inventory deletes available
     */
    private int unreadInventUnread;

    /**
     *  The number of unread trade reset requests
     */
    private int unreadResetTrade;

    /**
     * Constructor for the AdminInbox class
     * @param traderNoti is all text-based notifications for an Admin from Traders
     * @param adminNotifs is all text-based notifications for an Admin from another Admin/System
     */
    public AdminInbox(List<Message> traderNoti, List<Message> adminNotifs){
        super(traderNoti, adminNotifs);
        this.admiNotiUnread = 0;
        this.traderUnread = 0;
        this.undoFrozenUnread = 0;
        this.unreadWishListNoti = 0;
        this.unreadInventUnread = 0;
        this.unreadResetTrade = 0;
    }
    // Returns messages from Traders

    /** Gets the amount of unread wishlist delete requests from users
     *
     * @return an integer which shows the amount of unread wishlist delete requests from users
     */
    public int getUnreadWishListNoti(){return this.unreadWishListNoti; }

    /** Gets the amount of unread inventory delete requests from users
     *
     * @return an integer which shows the amount of unread inventory delete requests from users
     */
    public int getUnreadInventUnread(){return this.unreadInventUnread; }

    /** Gets the amount of unread trade reset requests from users
     *
     * @return an iteger representing the number of trade reset requests
     */
    public int getUnreadResetTrade(){return this.unreadResetTrade; }

    /** Gets the amount of unread unfreezing notifications from other Traders to Admin.
     *
     * @return an integer which shows the amount of unread unfreezing notifications from other Traders.
     */
    public int getUndoFrozenUnread(){return this.undoFrozenUnread;}

    /** Gets the total amount of unread notifications to Admin.
     *
     * @return an integer which shows the total amount of unread notifications.
     */
    public int getTotalUnread(){return this.traderUnread + this.admiNotiUnread + this.undoFrozenUnread;}

    /** Gets a list of Traders who requested to have their accounts unfrozen.
     *
     * @return a list of Traders who requested to have their accounts unfrozen.
     */
    public List<Trader> getUndoFrozen(){return undoFrozen; }

    /** Unfreezes all Traders who requested to have their accounts unfrozen.
     *
     * @param index is the position of a specific message within the sub-inbox
     */
    public void showUndoFrozen(int index){
        Trader temp = undoFrozen.get(index);
        undoFrozen.remove(index);
        this.undoFrozenUnread -= 1;
        if(temp.getTraderStatus().isFrozen()){
            temp.frozen = false;
        }
    }

    public void showUndoWishList(int index){

    }



}
