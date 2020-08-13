package phase1;

import java.util.ArrayList;
import java.util.List;

/**
 * RUN THIS BEFORE RUNNING TradingMain FOR THE FIRST TIME
 */
public class Initialize {

    public static void main(String[] args) throws Exception {
        List<Message> traderNoti = new ArrayList<Message>();
        List<Message> adminNotifs = new ArrayList<Message>();
        List<ItemMessage> undoWishList = new ArrayList<ItemMessage>();
        List<ItemMessage> undoInventory = new ArrayList<ItemMessage>();
        List<TradeMessage> restartedTrades = new ArrayList<TradeMessage>();
        List<Trader> undoFrozen = new ArrayList<Trader>();
        AdminInbox inbox = new AdminInbox(traderNoti, adminNotifs, undoWishList,
                undoInventory, restartedTrades, undoFrozen);
        User admin = new Admin("admin", "admin", inbox);
        inbox.setOwner(admin);

        List<TradeMessage> tradeMessages1 = new ArrayList<TradeMessage>();
        List<Message> traderNoti1 = new ArrayList<Message>();
        List<Message> adminNotifs1 = new ArrayList<Message>();
        TraderInbox traderInbox = new TraderInbox(tradeMessages1, traderNoti1, adminNotifs1);
        ArrayList<Item> list1 = new ArrayList<Item>();
        Item game = new Item("game", "game");
        game.setConfirm();
        list1.add(game);
        list1.add(new Item("dvd", "dvd"));
        Inventory inventory = new Inventory(list1, "inventory");
        Inventory wishlist = new Inventory(new ArrayList<Item>(), "wishlist");
        TraderStatus traderStatus = new TraderStatus();
        Trader trader = new Trader("trader", "trader", traderInbox,
                inventory, wishlist, "trader", traderStatus, "trader");
        traderInbox.setOwner(trader);
        //trader.frozen = true;

        List<TradeMessage> tradeMessages2 = new ArrayList<TradeMessage>();
        List<Message> traderNoti2 = new ArrayList<Message>();
        List<Message> adminNotifs2 = new ArrayList<Message>();
        TraderInbox traderInbox2 = new TraderInbox(tradeMessages2, traderNoti2, adminNotifs2);
        ArrayList<Item> list2 = new ArrayList<Item>();
        Item cd = new Item("cd", "cd");
        cd.setConfirm();
        list2.add(cd);
        list2.add(new Item("book", "book"));
        Inventory inventory2 = new Inventory(list2, "inventory");
        Inventory wishlist2 = new Inventory(list1, "wishlist2");
        TraderStatus traderStatus2 = new TraderStatus();
        User trader2 = new Trader("trader2", "trader2", traderInbox2,
                inventory2, wishlist2, "trader2", traderStatus2, "trader2");
        traderInbox2.setOwner(trader2);

        UserSerialization us = new UserSerialization();
        ArrayList<User> users = new ArrayList<User>();
        users.add(admin);
        users.add(trader);
        users.add(trader2);
        us.toSerialize(users);
    }
}