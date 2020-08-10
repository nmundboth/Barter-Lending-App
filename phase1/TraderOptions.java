package phase1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * <h1>Trader Options</h1>
 * <p>The TraderOptions class shows options to a Trader after they have logged in, and allows them to perform
 * actions based on those options.</p>
 */

public class TraderOptions {

    private Trader curr;
    private UserCatalogue uc;
    private UserSerialization us;
    private String menuOptions;
    private Boolean setToAvailable;

    /**
     * @param curr The User currently logged in.
     * @param uc The UserCatalogue associated with all currently registered Users.
     * @param us UserSerialization, for saving information.
     */
    public TraderOptions(Trader curr, UserCatalogue uc, UserSerialization us){
        this.curr = curr;
        this.uc = uc;
        this.us = us;
        this.menuOptions = "To perform an action, type the corresponding number.\n" +
                "1. View Inbox\n" +
                "2. View your profile\n" +
                "3. Edit your profile\n" +
                "4. View Trading History\n" +
                "To logout, type 'logout'.";
        this.setToAvailable = false;
    }

    /**
     * Method which sets up the TraderOptions menu, and presents the trader with a list of available actions (which are
     * listed in the menuOptions variable).
     */
    public void run(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        if (!curr.getTraderStatus().isAvailable()) {
            System.out.println("Last time you logged in you set yourself unavailable to trades. If you proceed to log in, you will now be available to make trades.\n" +
                "1. Proceed and log in\n" +
                "2. Remain unavailable and log out\n");
            try {
                String input = br.readLine();
                switch (input) {
                    case "1":
                        System.out.println("You are not available for new trades. You are now logged in.");
                        curr.getTraderStatus().setAvailable();
                        break;
                    case "2":
                        System.out.println("No action taken. You are still unavailable for new trades. Logging out.");
                        this.setToAvailable = true;
                        break;
                    default:
                        System.out.println("Invalid input.");
                        this.setToAvailable = true;
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!this.setToAvailable) {
            System.out.println(menuOptions);
        }

        try {
            System.out.println(this.setToAvailable);
            String input = "logout";

            if (!this.setToAvailable) {
                input = br.readLine();
            }

            while (!input.equals("logout")) {
                switch (input) {
                    //Inbox Options
                    case "1":
                        InboxOptions iopt = new InboxOptions(curr, uc, us);
                        iopt.run();
                        System.out.println(menuOptions);
                        break;
                    //View Profile Options
                    case "2":
                        System.out.println("Please select what you would like to View:\n" +
                                "1. Wishlist\n2. Inventory\n3. To return to the last menu");
                        String viewNum = br.readLine();
                        switch (viewNum) {
                            //Viewing Wishlist
                            case "1":
                                System.out.println("Wishlist: ");
                                for (int i = 0; i < ((Trader) curr).getWishList().getInv().size(); i++) {
                                    System.out.println("    " + (i + 1) + ". " + ((Trader) curr).getWishList().getInv().get(i));
                                }
                                System.out.println();
                                System.out.println(menuOptions);
                                break;
                            //Viewing Inventory
                            case "2":
                                System.out.println("Inventory (C = Confirmed item, U = Unconfirmed item):");
                                for (int i = 0; i < ((Trader) curr).getInventory().getInv().size(); i++) {
                                    if (((Trader) curr).getInventory().getInv().get(i).isConfirmed()) {
                                        System.out.println("    " + (i + 1) + ". " + ((Trader) curr).getInventory().getInv().get(i) + " (C)");
                                    } else { // Item is unconfirmed
                                        System.out.println("    " + (i + 1) + ". " + ((Trader) curr).getInventory().getInv().get(i) + " (U)");
                                    }
                                }
                                System.out.println();
                                System.out.println(menuOptions);
                                break;
                            //Returning to Last Menu
                            case "3":
                                System.out.println(menuOptions);
                                break;
                        }
                        break;
                    //Editing Profile Options
                    case "3":
                        System.out.println("Please select what you would like to Edit:\n" +
                                "1. Wishlist\n2. Inventory\n3. Location\n4. To return to the last menu\n5. Make unavailable");
                        String editNum = br.readLine();
                        switch (editNum) {
                            //Editing WishList
                            case "1":
                                addtoWishlist(br);
                                System.out.println(menuOptions);
                                break;
                            //Editing Inventory
                            case "2":
                                addToInventory(br);
                                System.out.println(menuOptions);
                                break;
                            //Editing Location
                            case "3":
                                changeLocation(br);
                                System.out.println(menuOptions);
                                break;
                            //Returning to last Menu
                            case "4":
                                System.out.println(menuOptions);
                                break;
                            case "5":
                                this.setToAvailable = makeUnavailable(br);
                                if (!this.setToAvailable) {
                                    System.out.println(menuOptions);
                                }
                                break;
                        }
                        break;
                    //Trading History Options
                    case "4":
                        System.out.println("Please select what you would like to do:\n" +
                                "1. View recently Traded Items \n2. View frequently trading partners " +
                                "\n3. To return to the last menu");
                        String tradeNum = br.readLine();
                        switch (tradeNum) {
                            //Viewing Last traded items
                            case "1":
                                for (int i = 0; i < ((Trader) curr).getRecentItems().size(); i++) {
                                    System.out.println("    " + (i + 1) + ((Trader) curr).getRecentItems().get(i));
                                }
                                System.out.println("\n" + menuOptions);
                                break;
                            //Viewing frequently traded partners
                            case "2":
                                ArrayList<Trader> frequent = ((Trader) curr).frequentPartners();
                                for (int i = 0; i < frequent.size(); i++) {
                                    System.out.println("    " + (i + 1) + frequent.get(i));
                                }
                                System.out.println("\n" + menuOptions);
                                break;
                            //Return to last menu
                            case "3":
                                System.out.println(menuOptions);
                                break;
                        }
                        break;

                    default:
                        System.out.println("Invalid input");
                        System.out.println(menuOptions);
                }
                System.out.println(this.setToAvailable);
                input = "logout";

                if (!this.setToAvailable) {
                    input = br.readLine();
                }
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    // Allows a trader to create a new item, and add it to their inventory.
    private void addToInventory(BufferedReader br){
        System.out.println("To go back to the options menu at any point, type 'exit'.");
        System.out.println("Enter the name of the item you would like to add to your inventory: ");
        try{
            String itemName = br.readLine();
            if(!itemName.equals("exit")){
                System.out.println("Enter a description of the item: ");
                String descrip = br.readLine();
                if (!descrip.equals("exit")){
                    Item item = new Item(itemName, descrip);
                    ((Trader) curr).getInventory().addItem(item);
                    System.out.println("Updated Inventory:\n" + ((Trader)curr).getInventory() + "\n");
                    System.out.println("Item submitted for confirmation by admin.\n" +
                            "Returning to options menu...\n");


                }
            }
        }
        catch(IOException e){
            System.out.println("Something went wrong.");
        }
    }

    // Allows a trader to add an existing, confirmed item to their wishlist.
    // A trader can not add an item to their wishlist that is in their inventory.
    private void addtoWishlist(BufferedReader br){
        System.out.println("To go back to the options menu at any point, type 'exit'.");
        ArrayList<Item> confirmed = uc.findConfirmed();
        // Commented out for now because unsure if works as intended.
//        for (int i = 0; i < confirmed.size(); i++){
//            if (!((Trader) curr).getInventory().getInv().contains(confirmed.get(i)) &&
//                    !((Trader) curr).getWishList().getInv().contains(confirmed.get(i))){
//                System.out.println((i + 1) + ". " + confirmed.get(i) + " - " + confirmed.get(i).getDescrip() + "\n");
//            }
//        }
        ArrayList<Item> itemCatalogue = uc.findAllItems();
        int total = 1;
        for (Item item : itemCatalogue) {
            System.out.println(total + ". " + item.getName());
            total = total + 1;
        }
        System.out.println("To add an item to your wishlist, enter the number associated with that item from above." +
                "\nTo go back, type 'exit'.");
        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input) >= 1 &&
                        Integer.parseInt(input) <= confirmed.size())){
                    ((Trader) curr).getWishList().addItem(confirmed.get(Integer.parseInt(input) - 1));
                    System.out.println("Item added to wishlist!");
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

    // Allow User to change their location
    private void changeLocation(BufferedReader br){
        System.out.println("Your current location: " + curr.getLocation());
        System.out.println("Do you want to change your current location? (Y/N)");
        System.out.println("To go back to the options menu at any point, type 'exit'.");
        try{
            String check = br.readLine();
            while(!check.equals("exit")) {
                if (check.toUpperCase().equals("Y")) {
                    System.out.println("Enter your new location: ");
                    check = br.readLine();
                    curr.setLocation(check);
                    System.out.println("All set! returning to main menu.");
                    break;
                }
                else if (check.toUpperCase().equals("N")){
                    System.out.println("Returning to main menu.");
                    break;
                }else{
                    System.out.println("Please enter a valid command.");
                }
                check = br.readLine();
            }
        }
        catch(Exception e){
            System.out.println("Something went wrong.");
        }
    }

    private Boolean makeUnavailable(BufferedReader br) {
        System.out.println("Do you want to make yourself unavailable for new trades? You will be logged out and unavailable to make additional trades until you log in again.\n" +
        "1. Yes\n" +
        "2. No");
        try {
            String input = br.readLine();
            switch (input) {
                case "1":
                    curr.getTraderStatus().setUnavailable();
                    System.out.println("Logging out.");
                    return true;
                case "2":
                    System.out.println("No action taken.");
                    return false;
                default:
                    System.out.println("Invalid input.");
                    return false;
            }
        } catch (IOException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
        return false;
    }



    // Template taken from
    // https://www.baeldung.com/java-check-string-number
    // Checks whether a given string is an integer.
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
