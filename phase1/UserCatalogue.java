package phase1;

import java.util.ArrayList;

/**
 * <h1>User Catalogue</h1>
 * <p>UserCatalogue contains a userBase that contains all registered users, and their information.</p>
 */
public class UserCatalogue {
    public ArrayList<phase1.User> userBase;

    /**
     * @param list the list of users, corresponding to the userBase
     */
    public UserCatalogue(ArrayList<phase1.User> list){
        userBase = new ArrayList<phase1.User>(list);
    }

    /**
     * Gets a list of all items in the system, both confirmed and unconfirmed (all items from all users' inventories).
     * @return an ArrayList representing all items from all users' inventories.
     */
    public ArrayList<phase1.Item> findAllItems(){
        ArrayList<phase1.Item> items = new ArrayList<phase1.Item>();
        for(phase1.User trader: userBase){
            if(trader instanceof phase1.Trader) {
                for (phase1.Item inv : ((phase1.Trader) trader).getInventory().getInv()) {
                    if (!items.contains(inv)) {
                        items.add(inv);
                    }
                }
            }
        }
        return items;
    }

    public void printDetails(){
        for (phase1.User trader: userBase){
            if (trader instanceof phase1.Trader){
                System.out.println("Name: "+((phase1.Trader) trader).getName()+"\n");

                if (((phase1.Trader) trader).getTraderStatus().isFrozen()){
                    System.out.println(((phase1.Trader) trader).getName()+"'s account is frozen. You cannot arrange any transaction" +
                            " with this "+((phase1.Trader) trader).getName()+".\n");
                }

                int i = 0;
                //Print trader's inventory
                if (((phase1.Trader) trader).getInventory().getInv().size() == 0){
                    System.out.println(((phase1.Trader) trader).getName()+"'s inventory is empty.");
                }
                else{
                    System.out.println("Inventory:");
                    while (i < ((phase1.Trader) trader).getInventory().getInv().size()){
                        System.out.println(((phase1.Trader) trader).getInventory().getInv().get(i).getName());
                        i++;
                    }
                }
                System.out.println("\n");

                i = 0;
                if (((phase1.Trader) trader).getWishList().getInv().size() == 0){
                    System.out.println(((phase1.Trader) trader).getName()+"'s wishlist is empty.");
                }
                else{
                    //Print trader's wishlist
                    System.out.println("Wishlist:");
                    while (i < ((phase1.Trader) trader).getWishList().getInv().size()){
                        System.out.println(((phase1.Trader) trader).getWishList().getInv().get(i).getName());
                        i++;
                    }
                }
                System.out.println("\n");

                i = 0;
                if (((phase1.Trader) trader).frequentPartners().size() == 0){
                    System.out.println("'"+((phase1.Trader) trader).getName()+"' has no frequent partner at the moment.");
                }
                else{
                    //Print trader's frequent partners
                    System.out.println("Frequent partners:");
                    while (i < ((phase1.Trader) trader).frequentPartners().size()){
                        System.out.println(((phase1.Trader) trader).frequentPartners().get(i).getName());
                        i++;
                    }
                }
                System.out.println("\n");

                i = 0;
                if (((phase1.Trader) trader).getRecentItems().size() == 0){
                    System.out.println("'"+((phase1.Trader) trader).getName()+"' has no recent item at the moment.");
                }
                else{
                    //Print trader's frequent partners
                    System.out.println("Recent items:");
                    while (i < ((phase1.Trader) trader).getRecentItems().size()){
                        System.out.println(((phase1.Trader) trader).getRecentItems().get(i).getName());
                        i++;
                    }
                }
                System.out.println("\n");
            }
        }
    }

    /**
     * Gets a list of all unconfirmed items in the system (used by admins when confirming items).
     * @return an ArrayList representing all items that have not been confirmed
     */
    public ArrayList<phase1.Item> findUnconfirmed(){
        ArrayList<phase1.Item> unconfirmed = new ArrayList<phase1.Item>();
        for (phase1.Item item: findAllItems()){
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
    public ArrayList<phase1.Item> findConfirmed(){
        ArrayList<phase1.Item> confirmed = new ArrayList<phase1.Item>();
        for (phase1.Item item: findAllItems()){
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
    public ArrayList<phase1.User> findUserWithItem(phase1.Item item){
        ArrayList<phase1.User> users = new ArrayList<phase1.User>();
        for(phase1.User trader: userBase){
            if(trader instanceof phase1.Trader) {
                if (((phase1.Trader) trader).getInventory().getInv().contains(item)) {
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
    public ArrayList<phase1.User> findUserWantsItem(phase1.Item item){
        ArrayList<phase1.User> users = new ArrayList<phase1.User>();
        for (phase1.User trader: userBase){
            if(trader instanceof phase1.Trader){
                if (((phase1.Trader) trader).getWishList().getInv().contains(item)){
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
        for (phase1.User user: userBase){
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
    public phase1.User getUserByName(String Username){
        phase1.User temp = new phase1.Trader();
        for (phase1.User user: userBase){
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
    public ArrayList<phase1.User> findFlagged(){
        ArrayList<phase1.User> flagged = new ArrayList<phase1.User>();
        for (phase1.User user: userBase){
            if (user instanceof phase1.Trader){
                if (((phase1.Trader) user).getTraderStatus().isFlagged()){
                    flagged.add(user);
                }
            }
        }
        return flagged;
    }


}
