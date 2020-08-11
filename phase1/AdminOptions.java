package phase1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AdminOptions {

    private User curr;
    private UserCatalogue uc;
    private UserSerialization us;
    private String menuOptions;


    public AdminOptions(User curr, UserCatalogue uc, UserSerialization us){
        this.curr = curr;
        this.uc = uc;
        this.us = us;
        menuOptions = "To perform an action, type the corresponding number.\n1. Perform Administrative Actions\n" +
                "2. View inbox\nTo logout, type 'logout'.";
    }

    public void run(){

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(menuOptions);
        try{
            String input = br.readLine();
            while(!input.equals("logout")){
                switch (input) {
                    //Admin Actions
                    case "1":
                        System.out.println("Select the administrative action that you would like to perform: \n" +
                                "1. Freeze flagged users\n2. Review unconfirmed items\n3. Change how many times" +
                                " a user must lend before they can borrow\n4. Create a new admin\n5. Return to main" +
                                " menu");

                        String adminNum = br.readLine();
                        switch (adminNum) {
                            //Freeze flagged users
                            case "1":
                                System.out.println("Flagged Users:\n");
                                for (int i = 0; i < uc.findFlagged().size(); i++){
                                    System.out.println("    " + (i + 1) + uc.findFlagged().get(i).getUsername() + " - " +
                                            "Incomplete Transactions: " + ((Trader) uc.findFlagged().get(i)).getTraderStatus().getIncomplete() +
                                            ", Weekly Transactions: " + ((Trader) uc.findFlagged().get(i)).getTraderStatus().getWeeklyTransxns());
                                }
                                System.out.println("Enter the number associated with the trader you would like to freeze:");
                                String toFreeze = br.readLine();
                                if (isInteger(toFreeze) && Integer.parseInt(toFreeze) >= 1 &&
                                        Integer.parseInt(toFreeze) <= uc.findFlagged().size()){
                                    ((Admin) curr).freezeTrader((Trader) uc.findFlagged().get(Integer.parseInt(toFreeze) - 1));
                                    System.out.println("User has been frozen.");
                                }
                                else {
                                    System.out.println("Invalid Input\nReturning to Options Menu...\n");
                                }
                                System.out.println(menuOptions);
                                break;
                            //View Unconfirmed items
                            case "2":
                                viewUnconfirmed(br);
                                System.out.println(menuOptions);
                                break;
                            //Change how many times a user must lend before they can borrow
                            case "3":
                                System.out.println("Enter the new value: ");
                                input = br.readLine();
                                while (!input.equals("exit")) {
                                    if (isInteger(input)) {
                                        Trader.greedLimit = -(Integer.parseInt(input));
                                        System.out.println("Limit set!\n");
                                        break;
                                    }
                                    input = br.readLine();
                                }
                                System.out.println(menuOptions);
                                break;
                            //admin creation
                            case "4":
                                System.out.println("To create an Admin, start by typing the user name:");
                                String userName = br.readLine();
                                System.out.println("Create a password:");
                                String password = br.readLine();
                                List<Message> adminNoti = new ArrayList<Message>();
                                List<Message> traderNoti = new ArrayList<Message>();
                                AdminInbox newAdminInbox = new AdminInbox(traderNoti, adminNoti);
                                Admin newAdmin = new Admin(userName, password, newAdminInbox);
                                newAdminInbox.setOwner(newAdmin);
                                uc.userBase.add(newAdmin);
                                System.out.println("New Admin created successfully");
                                System.out.println(menuOptions);
                                break;
                            //back to menu
                            case "5":
                                System.out.println(menuOptions);
                                break;
                        }

                        break;

                    //inbox actions
                    case "2":
                        System.out.println("To open a sub-inbox, please type in the corresponding number. \n" +
                                "1. Unfreeze requests\n2. Admin/System notification\n3. Trader notifications \n" +
                                "4. Undo requests\n"+
                                "5. Return to main menu");
                        String inboxNum = br.readLine();

                        switch (inboxNum) {
                            //Handling unfreeze requests
                            case "1":
                                AdminInbox inbox = ((Admin) curr).getAdminInbox();
                                if(inbox.getUndoFrozenUnread() == 0){
                                    System.out.println("Sub-inbox empty");
                                    System.out.println(menuOptions);
                                    break;
                                }
                                else {
                                    int index = 0;
                                    System.out.println("You have " + inbox.getUndoFrozenUnread() + " unread messages");
                                    for (Trader i : inbox.getUndoFrozen()) {
                                        System.out.println(index + " " + i.getUsername());
                                        index += 1;
                                    }
                                    System.out.println("Enter the corresponding number for a user to unfreeze:");
                                    String messageNum = br.readLine();
                                    int i = 0;
                                    try{
                                        i = Integer.parseInt(messageNum);
                                    } catch (NumberFormatException e){
                                        System.out.println("Invalid input");
                                        System.out.println(menuOptions);
                                        break;
                                    }
                                    inbox.showUndoFrozen(i);
                                    System.out.println("User has been unfrozen");
                                    System.out.println(menuOptions);
                                    break;
                                }

                            //Admin notifications
                            case "2":
                                AdminInbox inbox1 = ((Admin)curr).getAdminInbox();
                                if(inbox1.getAdmiNotiUnread() == 0){
                                    System.out.println("Sub-inbox empty");
                                    System.out.println(menuOptions);
                                    break;
                                }
                                else {
                                    int index1 = 0;
                                    System.out.println("You have " + inbox1.getAdmiNotiUnread() + " unread messages");
                                        for (Message i : inbox1.getAdmiNoti()) {
                                        System.out.println(index1);
                                        index1 += 1;
                                    }
                                    System.out.println("Enter the corresponding number for a message to display:");
                                    String messageNum1 = br.readLine();
                                    int i = 0;
                                    try{
                                        i = Integer.parseInt(messageNum1);
                                    } catch (NumberFormatException | IndexOutOfBoundsException e){
                                        System.out.println("Invalid input");
                                        System.out.println(menuOptions);
                                        break;
                                    }
                                    inbox1.getAdminNoti(i);
                                    System.out.println(menuOptions);
                                    break;
                                }

                            //trader notifications
                            case "3":
                                AdminInbox inbox2 = ((Admin)curr).getAdminInbox();
                                if(inbox2.getTraderNotiUnread() == 0){
                                    System.out.println("Sub-inbox empty");
                                    System.out.println(menuOptions);
                                    break;
                                }
                                else {
                                    int index2 = 0;
                                    System.out.println("You have " + inbox2.getTraderNotiUnread() + " unread messages");
                                    for (Message i : inbox2.getTraderNoti()) {
                                        System.out.println(index2);
                                        index2 += 1;
                                    }
                                    System.out.println("Enter the corresponding number for a message to display:");
                                    String messageNum2 = br.readLine();
                                    int i = 0;
                                    try{
                                        i = Integer.parseInt(messageNum2);
                                    } catch (NumberFormatException | IndexOutOfBoundsException e){
                                        System.out.println("Invalid input");
                                        System.out.println(menuOptions);
                                        break;
                                    }
                                    inbox2.getTradeNoti(i);
                                    System.out.println(menuOptions);
                                    break;
                                }

                            case "4":
                                System.out.println("Chose a category of undo requests:\n" +
                                        "1. Wishlist removal\n"+
                                        "2. Inventory removal\n"+
                                        "3. Trade reset requests");
                                String input1 = br.readLine();
                                try {
                                    while (!input.equals("exit")) {
                                        switch (input) {
                                            case "1":
                                                System.out.println("Choose an item to delete from the user's wishlist");
                                                for(int i = 0;
                                                    i < ((AdminInbox)(curr.getInbox())).getUndoWishList().size(); i++){
                                                    System.out.println(i + ((AdminInbox)(curr.getInbox())).getUndoWishList().get(i).getItem().getName());
                                                }
                                                String removeNum = br.readLine();
                                                AdminInbox in = (AdminInbox)(curr.getInbox());
                                                in.showUndoWishList(Integer.parseInt(removeNum));
                                                System.out.println("Item removed");
                                                System.out.println(menuOptions);
                                                break;
                                            case "2":
                                                System.out.println("Choose an item to delete from the user's inventory");
                                                for(int i = 0;
                                                    i < ((AdminInbox)(curr.getInbox())).getUndoInventory().size(); i++){
                                                    System.out.println(i + ((AdminInbox)(curr.getInbox())).getUndoInventory().get(i).getItem().getName());
                                                }
                                                String removeNum1 = br.readLine();
                                                AdminInbox in1 = (AdminInbox)(curr.getInbox());
                                                in1.showUndoWishList(Integer.parseInt(removeNum1));
                                                System.out.println("Item removed");
                                                System.out.println(menuOptions);
                                                break;

                                            case "3":
                                                System.out.println("Choose a trade to restart");
                                                for( int i = 0;
                                                i < ((AdminInbox)(curr.getInbox())).getRestartedTrades().size(); i++){
                                                    System.out.println(i + ((AdminInbox)(curr.getInbox())).getRestartedTrades().get(i).getSender().getName());
                                                }
                                                String removeNum2 = br.readLine();
                                                AdminInbox in2 = (AdminInbox)(curr.getInbox());
                                                in2.showTradeReset(Integer.parseInt(removeNum2));
                                                System.out.println("Trade reset");
                                                System.out.println(menuOptions);
                                                break;
                                        }
                                        input = br.readLine();
                                    }
                                }
                                catch (IOException e){
                                    System.out.println("Something went wrong");
                                }
                                break;

                            //back to menu  
                            case "5":
                                System.out.println(menuOptions);
                                break;

                            default:
                                System.out.println("Invalid input");
                                System.out.println(menuOptions);

                        }

                        break;

                    default:
                        System.out.println("Invalid input");
                        System.out.println(menuOptions);
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    private void viewUnconfirmed(BufferedReader br){
        ArrayList<Item> unconfirmed = uc.findUnconfirmed();
        for (int i = 0; i < unconfirmed.size(); i++){
            System.out.println((i + 1) + ". " + unconfirmed.get(i) + " - " + unconfirmed.get(i).getDescrip() + "\n");
        }
        System.out.println("If you would like to confirm an item, enter the number associated with that item.\n" +
                "To go back, type 'exit'.");
        try{
            String input = br.readLine();
            while (!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input) >= 1 &&
                        Integer.parseInt(input) <= unconfirmed.size())){
                    ((Admin) curr).confirmItem(unconfirmed.get((Integer.parseInt(input) - 1))); // Could use a use case class for admin
                    System.out.println("Item confirmed!");
                    us.toSerialize(uc.userBase);
                    break;
                }
                input = br.readLine();
            }
        }
        catch (Exception e){
            System.out.println("Something went wrong.");
        }

    }

    // Template taken from
    // https://www.baeldung.com/java-check-string-number
    private boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
