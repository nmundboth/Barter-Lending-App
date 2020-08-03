package phase1;

import java.util.ArrayList;

/**
 * <h1>User Catalogue</h1>
 * <p>UserCatalogue contains a userBase that contains all registered users, and their information.</p>
 */
public class UserCatalogue {
    public ArrayList<User> userBase;

    /**
     * @param list the list of users, corresponding to the userBase
     */
    public UserCatalogue(ArrayList<User> list){
        userBase = new ArrayList<User>(list);
    }

    /**
     * Gets a list of all items in the system, both confirmed and unconfirmed (all items from all users' inventories).
     * @return an ArrayList representing all items from all users' inventories.
     */
    public ArrayList<Item> findAllItems(){
        ArrayList<Item> items = new ArrayList<Item>();
        for(User trader: userBase){
            if(trader instanceof Trader) {
                for (Item inv : ((Trader) trader).getInventory().getInv()) {
                    if (!items.contains(inv)) {
                        items.add(inv);
                    }
                }
            }
        }
        return items;
    }

    //Prints the Catalogue for guest only
    public void printDetails(){
        for (User trader: userBase){
            if (trader instanceof Trader){
                System.out.println("Name: "+((Trader) trader).getName()+"\n");

                if (((Trader) trader).getTraderStatus().isFrozen()){
                    System.out.println(((Trader) trader).getName()+"'s account is frozen. You cannot arrange any transaction" +
                            " with this "+((Trader) trader).getName()+".\n");
                }

                int i = 0;
                //Print trader's inventory
                if (((Trader) trader).getInventory().getInv().size() == 0){
                    System.out.println(((Trader) trader).getName()+"'s inventory is empty.");
                }
                else{
                    System.out.println("Inventory:");
                    while (i < ((Trader) trader).getInventory().getInv().size()){
                        System.out.println(((Trader) trader).getInventory().getInv().get(i).getName());
                        i++;
                    }
                }
                System.out.println("\n");

                i = 0;
                if (((Trader) trader).getWishList().getInv().size() == 0){
                    System.out.println(((Trader) trader).getName()+"'s wishlist is empty.");
                }
                else{
                    //Print trader's wishlist
                    System.out.println("Wishlist:");
                    while (i < ((Trader) trader).getWishList().getInv().size()){
                        System.out.println(((Trader) trader).getWishList().getInv().get(i).getName());
                        i++;
                    }
                }
                System.out.println("\n");

                i = 0;
                if (((Trader) trader).frequentPartners().size() == 0){
                    System.out.println("'"+((Trader) trader).getName()+"' has no frequent partner at the moment.");
                }
                else{
                    //Print trader's frequent partners
                    System.out.println("Frequent partners:");
                    while (i < ((Trader) trader).frequentPartners().size()){
                        System.out.println(((Trader) trader).frequentPartners().get(i).getName());
                        i++;
                    }
                }
                System.out.println("\n");

                i = 0;
                if (((Trader) trader).getRecentItems().size() == 0){
                    System.out.println("'"+((Trader) trader).getName()+"' has no recent item at the moment.");
                }
                else{
                    //Print trader's frequent partners
                    System.out.println("Recent items:");
                    while (i < ((Trader) trader).getRecentItems().size()){
                        System.out.println(((Trader) trader).getRecentItems().get(i).getName());
                        i++;
                    }
                }
                System.out.println("\n");
            }
        }
    }

    //Filters location for guest only
    public void filterByLocation(String location){
        for (User trader: userBase){
            if (trader instanceof Trader && ((Trader) trader).getLocation().equals(location)){
                System.out.println("Traders found in "+location+":\n");
                System.out.println("Name: "+((Trader) trader).getName()+"\n");

                if (((Trader) trader).getTraderStatus().isFrozen()){
                    System.out.println(((Trader) trader).getName()+"'s account is frozen. You cannot arrange any transaction" +
                            " with this "+((Trader) trader).getName()+".\n");
                }

                int i = 0;
                //Print trader's inventory
                if (((Trader) trader).getInventory().getInv().size() == 0){
                    System.out.println(((Trader) trader).getName()+"'s inventory is empty.");
                }
                else{
                    System.out.println("Inventory:");
                    while (i < ((Trader) trader).getInventory().getInv().size()){
                        System.out.println(((Trader) trader).getInventory().getInv().get(i).getName());
                        i++;
                    }
                }
                System.out.println("\n");

                i = 0;
                if (((Trader) trader).getWishList().getInv().size() == 0){
                    System.out.println(((Trader) trader).getName()+"'s wishlist is empty.");
                }
                else{
                    //Print trader's wishlist
                    System.out.println("Wishlist:");
                    while (i < ((Trader) trader).getWishList().getInv().size()){
                        System.out.println(((Trader) trader).getWishList().getInv().get(i).getName());
                        i++;
                    }
                }
                System.out.println("\n");

                i = 0;
                if (((Trader) trader).frequentPartners().size() == 0){
                    System.out.println("'"+((Trader) trader).getName()+"' has no frequent partner at the moment.");
                }
                else{
                    //Print trader's frequent partners
                    System.out.println("Frequent partners:");
                    while (i < ((Trader) trader).frequentPartners().size()){
                        System.out.println(((Trader) trader).frequentPartners().get(i).getName());
                        i++;
                    }
                }
                System.out.println("\n");

                i = 0;
                if (((Trader) trader).getRecentItems().size() == 0){
                    System.out.println("'"+((Trader) trader).getName()+"' has no recent item at the moment.");
                }
                else{
                    //Print trader's frequent partners
                    System.out.println("Recent items:");
                    while (i < ((Trader) trader).getRecentItems().size()){
                        System.out.println(((Trader) trader).getRecentItems().get(i).getName());
                        i++;
                    }
                }
                System.out.println("\n");
            }
        }
    }

    //Checks if location exists
    public boolean validLocation(String location){
        for (User user: userBase){
            if (user instanceof Trader && ((Trader) user).getLocation().equals(location)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a list of all unconfirmed items in the system (used by admins when confirming items).
     * @return an ArrayList representing all items that have not been confirmed
     */
    public ArrayList<Item> findUnconfirmed(){
        ArrayList<Item> unconfirmed = new ArrayList<phase1.Item>();
        for (Item item: findAllItems()){
            if (!item.isConfirmed()){
                unconfirmed.add(item);
            }
        }
        return unconfirmed;
    }

    /**
     * Gets a list of all confirmed items in the system (used when a trader wants to add to their wishlist)
     * @return an ArrayList representing all items that have been confirmed.
     */
    public ArrayList<Item> findConfirmed(){
        ArrayList<Item> confirmed = new ArrayList<phase1.Item>();
        for (Item item: findAllItems()){
            if (item.isConfirmed()){
                confirmed.add(item);
            }
        }
        return confirmed;
    }

    /**
     * Finds a user with a specific item (used for trade functions).
     * @param item the Item that is being searched for
     * @return an ArrayList of User(s) that have the item being searched for in their inventory.
     */
    public ArrayList<User> findUserWithItem(Item item){
        ArrayList<User> users = new ArrayList<phase1.User>();
        for(User trader: userBase){
            if(trader instanceof Trader) {
                if (((Trader) trader).getInventory().getInv().contains(item)) {
                    users.add(trader);
                }
            }
        }
        return users;
    }

    /**
     * Finds a user that wants an item (has an item on their wishlist)
     * @param item The Item that some User wants
     * @return an ArrayList of User(s) that want the item being searched for.
     */
    public ArrayList<User> findUserWantsItem(Item item){
        ArrayList<phase1.User> users = new ArrayList<phase1.User>();
        for (User trader: userBase){
            if(trader instanceof Trader){
                if (((Trader) trader).getWishList().getInv().contains(item)){
                    users.add(trader);
                }
            }
        }
        return users;
    }

    /**
     * Determines whether the user corresponding to the username input is in the userBase.
     * @param Username a String corresponding to a User's username.
     * @return a boolean representing whether the user is in the userBase.
     */
    public boolean inUserBase(String Username){
        for (User user: userBase){
            if (user.getUsername().equals(Username)){
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a user in the userBase, given their username as a String input.
     * @param Username a String corresponding to a username.
     * @return a User object representing the user for whom was being searched for by their username.
     */
    //Should only be called once we know a user is in the userBase, otherwise returns empty User.
    public User getUserByName(String Username){
        User temp = new Trader();
        for (User user: userBase){
            if (user.getUsername().equals(Username)){
                temp = user;
            }
        }
        return temp;
    }

    /**
     * Gets all users that have been automatically flagged by the system.
     * @return an ArrayList representing all flagged users (too many outstanding incomplete transactions/weekly
     * transactions)
     */
    public ArrayList<User> findFlagged(){
        ArrayList<User> flagged = new ArrayList<User>();
        for (User user: userBase){
            if (user instanceof Trader){
                if (((Trader) user).getTraderStatus().isFlagged()){
                    flagged.add(user);
                }
            }
        }
        return flagged;
    }


}
