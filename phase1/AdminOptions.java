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
        menuOptions = "To perform an action, type the corresponding number.\n 1. View inbox\n 2. Perform an admin action" +
                "\n" + "To logout, type 'logout'.";
    }

    public void run() {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        mainMenu(br);
    }

    public void mainMenu(BufferedReader br) {
        /*
         * Main Menu of this method
         */
        System.out.println(menuOptions);
        try {
            String input = br.readLine();
            while (!input.equals("logout")) {
                switch (input) {
                    case "1":
                        inboxOptions(br);
                        break;
                    case "2":
                        adminOptions(br);
                        break;
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    public void inboxOptions(BufferedReader br){
        /*
        Options for inbox
         */
        System.out.println("To open a sub-inbox, please type in the corresponding number. \n" +
                "1. Unfreeze requests\n2. Admin/System notification\n3. Trader notifications \n4. Go Back to " +
                "Main Menu" );
        try {
            String inboxNum = br.readLine();
            switch (inboxNum) {
                case "1":
                    AdminInbox inbox = ((Admin) curr).getAdminInbox();
                    if (inbox.getUndoFrozenUnread() == 0) {
                        System.out.println("Sub-inbox empty");
                    } else {
                        int index = 0;
                        System.out.println("You have " + inbox.getUndoFrozenUnread() + " unread messages");
                        for (Trader i : inbox.getUndoFrozen()) {
                            System.out.println(index + " " + i.getUsername());
                            index += 1;
                        }
                        System.out.println("Enter the corresponding number for a user to unfreeze:");
                        String messageNum = br.readLine();
                        int i = 0;
                        try {
                            i = Integer.parseInt(messageNum);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input");
                            System.out.println(menuOptions);
                            break;
                        }
                        inbox.showUndoFrozen(i);
                        System.out.println("User has been unfrozen");
                    }
                    System.out.println(menuOptions);
                    break;
                case "2":
                    AdminInbox inbox1 = ((Admin) curr).getAdminInbox();
                    if (inbox1.getAdmiNotiUnread() == 0) {
                        System.out.println("Sub-inbox empty");
                        System.out.println(menuOptions);
                        break;
                    } else {
                        int index1 = 0;
                        System.out.println("You have " + inbox1.getAdmiNotiUnread() + " unread messages");
                        for (Message i : inbox1.getAdmiNoti()) {
                            System.out.println(index1);
                            index1 += 1;
                        }
                        System.out.println("Enter the corresponding number for a message to display:");
                        String messageNum1 = br.readLine();
                        int i = 0;
                        try {
                            i = Integer.parseInt(messageNum1);
                        } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            System.out.println("Invalid input");
                            System.out.println(menuOptions);
                            break;
                        }
                        inbox1.getAdminNoti(i);
                        System.out.println(menuOptions);
                        break;
                    }
                case "3":
                    AdminInbox inbox2 = ((Admin) curr).getAdminInbox();
                    if (inbox2.getTraderNotiUnread() == 0) {
                        System.out.println("Sub-inbox empty");
                        System.out.println(menuOptions);
                        break;
                    } else {
                        int index2 = 0;
                        System.out.println("You have " + inbox2.getTraderNotiUnread() + " unread messages");
                        for (Message i : inbox2.getTraderNoti()) {
                            System.out.println(index2);
                            index2 += 1;
                        }
                        System.out.println("Enter the corresponding number for a message to display:");
                        String messageNum2 = br.readLine();
                        int i = 0;
                        try {
                            i = Integer.parseInt(messageNum2);
                        } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            System.out.println("Invalid input");
                            System.out.println(menuOptions);
                            break;
                        }
                        inbox2.getTradeNoti(i);
                        System.out.println(menuOptions);
                    }
                    break;
                case "4":
                    mainMenu(br);
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    public void adminOptions(BufferedReader br){
        /*
        Options for admin
         */
        System.out.println("Please type the number corresponding to the action you wish to perform:\n" +
                "1. View Flagged Users\n2. View Unconfirmed Items \n3. Change the trading limit \n4. Add a new Admin" +
                "\n5. Go Back to Main Menu" );
        try {
            String adminNum = br.readLine();
            switch (adminNum) {
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
                case "2":
                    viewUnconfirmed(br);
                    System.out.println(menuOptions);
                    break;
                case "3":
                    System.out.println("Enter the new value: ");
                    adminNum = br.readLine();
                    while (!adminNum.equals("exit")) {
                        if (isInteger(adminNum)) {
                            Trader.greedLimit = -(Integer.parseInt(adminNum));
                            System.out.println("Limit set!\n");
                            break;
                        }
                        adminNum = br.readLine();
                    }
                    System.out.println(menuOptions);
                    break;
                case "4":
                    System.out.println("To create an Admin, start by typing the user name:");
                    String userName = br.readLine();
                    System.out.println("Create a password:");
                    String password = br.readLine();
                    List<Message> adminNoti = new ArrayList<Message>();
                    List<Message> traderNoti = new ArrayList<Message>();
                    AdminInbox newAdminInbox = new AdminInbox(traderNoti, adminNoti);
                    Admin newAdmin = new Admin(userName, password, "Admin", newAdminInbox);
                    newAdminInbox.setOwner(newAdmin);
                    uc.userBase.add(newAdmin);
                    System.out.println("New Admin created successfully");
                    System.out.println(menuOptions);
                    break;
                case "5":
                    mainMenu(br);
            }
        }
        catch (IOException e) {
            System.out.println("Something went wrong.");
        }
    }

    public void viewUnconfirmed(BufferedReader br){
        /*
          Method to view unconfirmed items that traders have tried to add
          @param br: a reader class that helps read what the user tries to type
         */
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
    public boolean isInteger(String strNum) {
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
