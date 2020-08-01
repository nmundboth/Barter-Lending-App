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

    /**
     * @param curr The User currently logged in.
     * @param uc The UserCatalogue associated with all currently registered Users.
     * @param us UserSerialization, for saving information.
     */
    public TraderOptions(Trader curr, UserCatalogue uc, UserSerialization us){
        this.curr = curr;
        this.uc = uc;
        this.us = us;
        this.menuOptions = "To perform an action, type the corresponding number.\n1. View Inbox\n" +
                "2. Add an item to your inventory\n3. Add an item to your wishlist (browse all items)\n" +
                "4. View recently traded items\n5. View frequent trading partners\n" +
                "To logout, type 'logout'.";
    }

    /**
     * Method which sets up the TraderOptions menu, and presents the trader with a list of available actions (which are
     * listed in the menuOptions variable).
     */
    public void run(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //Print trader's inventory
        System.out.println("Inventory (C = Confirmed item, U = Unconfirmed item):");
        for (int i = 0; i < ((Trader) curr).getInventory().size(); i++){
            if (((Trader) curr).getInventory().get(i).isConfirmed()){
                System.out.println("    " + (i + 1) + ". " + ((Trader) curr).getInventory().get(i) + " (C)");
            }
            else { // Item is unconfirmed
                System.out.println("    " + (i + 1) + ". " + ((Trader) curr).getInventory().get(i) + " (U)");
            }
        }
        System.out.println();

        //Print trader's wishlist
        System.out.println("Wishlist: ");
        for (int i = 0; i < ((Trader) curr).getWishList().size(); i++){
            System.out.println("    " + (i + 1) + ". " + ((Trader) curr).getWishList().get(i));
        }
        System.out.println();

        System.out.println(menuOptions);
        try{
            String input = br.readLine();
            while(!input.equals("logout")){
                if (input.equals("1")){
                    InboxOptions iopt = new InboxOptions(curr, uc, us);
                    iopt.run();
                    System.out.println(menuOptions);
                }
                else if (input.equals("2")){
                    addToInventory(br);
                    System.out.println(menuOptions);
                }
                else if (input.equals("3")){
                    addtoWishlist(br);
                    System.out.println(menuOptions);
                }
                else if (input.equals("4")){
                    for (int i = 0; i < ((Trader) curr).getRecentItems().size(); i++){
                        System.out.println("    " + (i + 1) + ((Trader) curr).getRecentItems().get(i));
                    }
                    System.out.println("\n" + menuOptions);
                }
                else if (input.equals("5")){
                    ArrayList<Trader> frequent = ((Trader) curr).frequentPartners();
                    for (int i =0; i < frequent.size(); i++){
                        System.out.println("    " + (i + 1) + frequent.get(i));
                    }
                    System.out.println("\n" + menuOptions);
                }
                input = br.readLine();
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
                    ((Trader) curr).addToInventory(item);
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
        for (int i = 0; i < confirmed.size(); i++){
            if (!((Trader) curr).getInventory().contains(confirmed.get(i))){
                System.out.println((i + 1) + ". " + confirmed.get(i) + " - " + confirmed.get(i).getDescrip() + "\n");
            }
        }
        System.out.println("To add an item to your wishlist, enter the number associated with that item from above." +
                "\nTo go back, type 'exit'.");
        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input) >= 1 &&
                        Integer.parseInt(input) <= confirmed.size())){
                    ((Trader) curr).add_wish(confirmed.get((Integer.parseInt(input) - 1)));
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
