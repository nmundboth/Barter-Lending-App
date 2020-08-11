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
    static List<ItemMessage> undoWishList;

    /**
     * A list representing the requests for removing an item from the Trader's inventory
     */
    static List<ItemMessage> undoInventory;

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
    protected AdminInbox(List<Message> traderNoti, List<Message> adminNotifs){
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
    protected int getUndoFrozenUnread(){return this.undoFrozenUnread;}

    /** Gets the total amount of unread notifications to Admin.
     *
     * @return an integer which shows the total amount of unread notifications.
     */
    protected int getTotalUnread(){return this.traderUnread + this.admiNotiUnread + this.undoFrozenUnread;}

    /** Gets a list of Traders who requested to have their accounts unfrozen.
     *
     * @return a list of Traders who requested to have their accounts unfrozen.
     */
    protected List<Trader> getUndoFrozen(){return undoFrozen; }

    protected List<ItemMessage> getUndoWishList(){return undoWishList;}

    protected List<ItemMessage> getUndoInventory(){return  undoInventory;}

    protected List<TradeMessage> getRestartedTrades(){ return restartedTrades;}

    /** Unfreezes all Traders who requested to have their accounts unfrozen.
     *
     * @param index is the position of a specific message within the sub-inbox
     */
    protected void showUndoFrozen(int index){
        Trader temp = undoFrozen.get(index);
        undoFrozen.remove(index);
        this.undoFrozenUnread -= 1;
        if(temp.getTraderStatus().isFrozen()){
            temp.frozen = false;
        }
    }

    /** Method for accessing a wishlist removing requests and removing that item
     * from the wishlist
     *
     * @param index the position of that said message inside the sub-inbox
     */
    public void showUndoWishList(int index){
        ItemMessage message = undoWishList.get(index);
        undoWishList.remove(message);
        this.unreadWishListNoti -= 1;
        Item item = message.getItem();
        Trader user = message.getSender();
        user.getWishList().removeItem(item);
    }

    /** Method for accessing an Inventory removing requests and removing that item
     * from the inventory
     *
     * @param index the position of that said message inside the sub-inbox
     */
    public void showUndoInv(int index){
        ItemMessage message = undoInventory.get(index);
        undoInventory.remove(message);
        this.unreadInventUnread -= 1;
        Item item = message.getItem();
        Trader user = message.getSender();
        user.getInventory().removeItem(item);
    }

    /** Method for accessing a trade reset request and resetting the said trade
     *
     * @param index the position of a trade request within the sub-inbox
     */
    public void showTradeReset(int index){
        TradeMessage message = restartedTrades.get(index);
        // A one way trade
        if (message.getTrade() instanceof OneWayTrade){
            OneWayTrade trade = (OneWayTrade)message.getTrade();
            // If the item exists in the correct inventories and wishlists
            if (trade.getOgTrader().getWishList().getInv().contains(trade.getItem()) &&
                    trade.getOtherTrader().getInventory().getInv().contains(trade.getItem())){
                OneWayTrade newTrade = new OneWayTrade(trade.getOgTrader(), trade.getOtherTrader(), trade.getItem());
                TradeMessage newMessage = new TradeMessage("Trade restarted", trade.getOgTrader(),
                        trade.getOtherTrader(), newTrade);
                TraderInbox ogInbox = (TraderInbox) trade.getOgTrader().getInbox();
                TraderInbox otherInbox = (TraderInbox) trade.getOtherTrader().getInbox();
                ogInbox.addUnaccepted(newMessage);
                otherInbox.addUnaccepted(newMessage);
            }
            // If trade conditions are no longer true
            else{
                Message newMessage = new Message("Could not reset trade as the trade conditions no longer holf",
                        message.sender, message.recipient);
                message.sender.getInbox().addAdminNoti(newMessage);
                message.recipient.getInbox().addAdminNoti(newMessage);
            }
        }
        // A two way trade
        else{
            TwoWayTrade trade = (TwoWayTrade)message.getTrade();
            // If the item exists in the correct inventories and wishlists
            if (trade.getOgTrader().getInventory().getInv().contains(trade.getOgItem()) &&
                    trade.getOtherTrader().getInventory().getInv().contains(trade.getOtherItem()) &&
                    trade.getOgTrader().getWishList().getInv().contains(trade.getOtherItem()) &&
                    trade.getOtherTrader().getWishList().getInv().contains(trade.getOgItem())) {

                TwoWayTrade newTrade = new TwoWayTrade(trade.getOgTrader(), trade.getOtherTrader(), trade.getOgItem(), trade.getOtherItem());
                TradeMessage newMessage = new TradeMessage("Trade restarted", trade.getOgTrader(),
                        trade.getOtherTrader(), newTrade);
                TraderInbox ogInbox = (TraderInbox) trade.getOgTrader().getInbox();
                TraderInbox otherInbox = (TraderInbox) trade.getOtherTrader().getInbox();
                ogInbox.addUnaccepted(newMessage);
                otherInbox.addUnaccepted(newMessage);
            }
            // If trade conditions are no longer true
            else{
                Message newMessage = new Message("Could not reset trade as the trade conditions no longer holf",
                        message.sender, message.recipient);
                message.sender.getInbox().addAdminNoti(newMessage);
                message.recipient.getInbox().addAdminNoti(newMessage);
            }
        }



    }



}
