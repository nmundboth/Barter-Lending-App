package phase1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * UndoOptions
 * The UndoOptions class shows options to a trader after they have logged in, and allows them to
 * perform actions based on those options about undoing things they have done before
 */
public class UndoOptions {

    private Trader curr;
    private String menuOptions;
    public List<Message> cancelledTrades;
    private UserCatalogue uc;

    public UndoOptions(Trader curr, UserCatalogue uc) {
        this.uc = uc;
        this.curr = curr;
        this.menuOptions = "To perform an action, type the corresponding numbern\n" +
                "1. Remove an item from wishlist\n" +
                "2. Remove an item from inventory\n" +
                "3. Restart an already cancelled trade";
    }


    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(this.menuOptions);

        ArrayList<User> list = uc.userBase;
        Admin admin = null;
        for (User user : list) {
            if (user instanceof Admin) {
                admin = (Admin)user;
            }
        }
        assert admin != null;
        AdminInbox adminInbox = (AdminInbox)admin.getInbox();
        try {
            String input = br.readLine();
            while (!input.equals("exit")) {
                switch (input) {
                    case "1":
                        // Removing from wishlist
                        System.out.println("Choose an item to delete from your wish list:");
                        Inventory wishList = this.curr.getWishList();
                        //if wish list is empty
                        if (wishList.getInv().size() == 0) {
                            System.out.println("Wish list empty");
                            System.out.println(this.menuOptions);
                            break;
                        }
                        for (int i = 0; i < wishList.getInv().size(); i++) {
                            System.out.println(i + " " + wishList.getInv().get(i).getName());
                        }
                        String removeNum = br.readLine();
                        Item item = (wishList.getInv().get(Integer.parseInt(removeNum)));
                        ItemMessage message = new ItemMessage("Request for this item to be removed", curr,
                                curr, item);
                        adminInbox.undoWishList.add(message);
                        System.out.println("You have submitted your request to remove this item");
                        System.out.println(this.menuOptions);
                        break;

                    case "2":
                        // Removing from Inventory
                        System.out.println("Choose an item to delete from your inventory");
                        Inventory inventory = this.curr.getInventory();
                        if (inventory.getInv().size() == 0) {
                            System.out.println("Inventory empty");
                            System.out.println(this.menuOptions);
                            break;
                        }
                        for (int i = 0; i < inventory.getInv().size(); i++) {
                            System.out.println(i + " " + inventory.getInv().get(i).getName());
                        }
                        String removeNum1 = br.readLine();
                        Item item1 = (inventory.getInv().get(Integer.parseInt(removeNum1)));
                        ItemMessage message1 = new ItemMessage("Request for this item to be removed", curr, curr,
                                item1);
                        adminInbox.undoInventory.add(message1);
                        System.out.println("You have submitted your request to remove this item");
                        System.out.println(this.menuOptions);
                        break;


                    case "3":
                        // Restart a cancelled trade
                        System.out.println("Choose a trade to restart:");
                        List<TradeMessage> list1 = ((TraderInbox) curr.getInbox()).getCancelledTrades();
                        if (list.size() == 0) {
                            System.out.print("You have no cancelled trades");
                            System.out.println(this.menuOptions);
                            break;
                        }
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println(i + " " + list1.get(i).getTrade().getOgTrader().getName());
                        }
                        String removeNum2 = br.readLine();
                        TradeMessage trade = list1.get(Integer.parseInt(removeNum2));
                        adminInbox.restartedTrades.add(trade);
                        System.out.println("You have submitted your request to restart this trade");
                        System.out.println(this.menuOptions);
                        break;


                    default:
                        System.out.println("Invalid input");
                        System.out.println(menuOptions);
                }
                input = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }
    }


}
