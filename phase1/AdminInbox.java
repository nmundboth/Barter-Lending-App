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

    static List<Trader> undoFrozen;
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

    public AdminInbox(List<Trade> trades, List<String> traderNoti, List<String> adminNotifs){
        super(trades, traderNoti, adminNotifs);
        this.admiNotiUnread = 0;
        this.traderUnread = 0;
        this.undoFrozenUnread = 0;
    }
    // Returns messages from Traders

    /** Gets the amount of unread notifications from other Traders to Admin.
     *
     * @return an integer which shows the amount of unread notifications from other Traders.
     */
    public int getTradersUnread(){return this.traderUnread;}

    /** Gets the amount of unread notifications from other Admins to Admin.
     *
     * @return an integer which shows the amount of unread notifications from other Admins.
     */
    public int getAdmiNotiUnread(){return this.admiNotiUnread;}

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
     * @param index
     */
    public void showUndoFrozen(int index){
        Trader temp = undoFrozen.get(index);
        undoFrozen.remove(index);
        this.undoFrozenUnread -= 1;
        if(temp.isFrozen()){
            temp.frozen = false;
        }
    }

    /** Shows Admins' notifications.
     *
     * @param index
     * @return a String which shows Admins' notifications.
     */
    public String showAdminNoti(int index){
        String temp = this.getAdmiNoti().get(index);
        this.getAdmiNoti().remove(index);
        this.admiNotiUnread -= 1;
        return temp;
    }

    /** Shows Traders' notifications.
     *
     * @param index
     * @return a String which shows Traders' notifications.
     */
    public String showTraderNoti(int index){
        String temp = this.getTraderNoti().get(index);
        this.getTraderNoti().remove(index);
        this.traderUnread -= 1;
        return temp;
    }

}
