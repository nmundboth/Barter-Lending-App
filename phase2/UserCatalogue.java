package phase2;

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
            if(trader instanceof Trader && ((Trader) trader).getTraderStatus().isAvailable()) {
                for (Item inv : ((Trader) trader).getInventory().getInv()) {
                    if (!items.contains(inv)) {
                        items.add(inv);
                    }
                }
            }
        }
        return items;
    }

    /**
     * Prints the Trade Catalogue where all information is provided about Traders and what they want to buy and sell.
     */
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

                if (!((Trader) trader).getTraderStatus().isAvailable()){
                    System.out.println(((Trader) trader).getName()+" is unavailable at the moment. You cannot arrange any transaction" +
                            " with this "+((Trader) trader).getName()+".\n");
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

    /** Filters the Trade Catalogue by city.
     *
     * @param location which represents the city where Trader resides.
     */
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

    /** Checks if the city exists in the Trade Catalogue.
     *
     * @param location which represents the city where Trader resides.
     * @return a boolean representing if the city exists in the Trade Catalogue.
     */
    public boolean validLocation(String location){
        for (User user: userBase){
            if (user instanceof Trader && ((Trader) user).getLocation().equals(location)){
                return true;
            }
        }
        return false;
    }

    /** Filters the Trade Catalogue by name.
     *
     * @param name which represents the name of the Trader.
     */
    public void filterByName(String name){
        for (User trader: userBase){
            if (trader instanceof Trader && ((Trader) trader).getName().equals(name)){
                System.out.println("Viewing "+((Trader) trader).getName()+"'s information:\n");
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

    /** Checks if that name exists in the Trade Catalogue.
     *
     * @param name which represents the name of a Trader.
     * @return a boolean to check if that name exists in the Trade Catalogue.
     */
    public boolean validName(String name){
        if (!(userBase.size() == 0)) {
            for (User user : userBase) {
                if (user instanceof Trader && ((Trader) user).getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Checks if the item in a Trader's inventory exists.
     *
     * @param item which represents an item in a Trader's inventory.
     * @return a boolean to check if the item in a Trader's inventory exists.
     */
    public boolean validInventoryItem(String item){
        for (User user: userBase){
            if (user instanceof Trader){
                if (!(((Trader) user).getInventory().getInv().size() == 0)) {
                    for (Item i : ((Trader) user).getInventory().getInv()) {
                        if (i.getName().equals(item)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /** Checks if the item in a Trader's wishlist exists.
     *
     * @param item which represents an item in a Trader's wishlist.
     * @return a boolean to check if the item in a Trader's wishlist exists.
     */
    public boolean validWishlistItem(String item){
        for (User user: userBase){
            if (user instanceof Trader){
                if (!(((Trader) user).getWishList().getInv().size() == 0)) {
                    for (Item i : ((Trader) user).getWishList().getInv()) {
                        if (i.getName().equals(item)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /** Filters Trade Catalogue by inventory.
     *
     * @param item which represents an item in a Trader's inventory.
     */
    public void filterInventory(String item){
        for (User trader: userBase){
            if (trader instanceof Trader){
                for (Item it : ((Trader) trader).getInventory().getInv()) {
                    if (it.getName().equals(item)) {
                        System.out.println("Viewing " + ((Trader) trader).getName() + "'s information:\n");
                        System.out.println("Name: " + ((Trader) trader).getName() + "\n");

                        if (((Trader) trader).getTraderStatus().isFrozen()) {
                            System.out.println(((Trader) trader).getName() + "'s account is frozen. You cannot arrange any transaction" +
                                    " with this " + ((Trader) trader).getName() + ".\n");
                        }

                        int i = 0;
                        //Print trader's inventory
                        if (((Trader) trader).getInventory().getInv().size() == 0) {
                            System.out.println(((Trader) trader).getName() + "'s inventory is empty.");
                        } else {
                            System.out.println("Inventory:");
                            while (i < ((Trader) trader).getInventory().getInv().size()) {
                                System.out.println(((Trader) trader).getInventory().getInv().get(i).getName());
                                i++;
                            }
                        }
                        System.out.println("\n");

                        i = 0;
                        if (((Trader) trader).getWishList().getInv().size() == 0) {
                            System.out.println(((Trader) trader).getName() + "'s wishlist is empty.");
                        } else {
                            //Print trader's wishlist
                            System.out.println("Wishlist:");
                            while (i < ((Trader) trader).getWishList().getInv().size()) {
                                System.out.println(((Trader) trader).getWishList().getInv().get(i).getName());
                                i++;
                            }
                        }
                        System.out.println("\n");

                        i = 0;
                        if (((Trader) trader).frequentPartners().size() == 0) {
                            System.out.println("'" + ((Trader) trader).getName() + "' has no frequent partner at the moment.");
                        } else {
                            //Print trader's frequent partners
                            System.out.println("Frequent partners:");
                            while (i < ((Trader) trader).frequentPartners().size()) {
                                System.out.println(((Trader) trader).frequentPartners().get(i).getName());
                                i++;
                            }
                        }
                        System.out.println("\n");

                        i = 0;
                        if (((Trader) trader).getRecentItems().size() == 0) {
                            System.out.println("'" + ((Trader) trader).getName() + "' has no recent item at the moment.");
                        } else {
                            //Print trader's frequent partners
                            System.out.println("Recent items:");
                            while (i < ((Trader) trader).getRecentItems().size()) {
                                System.out.println(((Trader) trader).getRecentItems().get(i).getName());
                                i++;
                            }
                        }
                        System.out.println("\n");
                    }
                }
            }
        }
    }

    /** Filters Trade Catalogue by wishlist.
     *
     * @param item which represents an item in a Trader's wishlist.
     */
    public void filterWishlist(String item){
        for (User trader: userBase){
            if (trader instanceof Trader){
                for (Item it : ((Trader) trader).getWishList().getInv()) {
                    if (it.getName().equals(item)) {
                        System.out.println("Viewing " + ((Trader) trader).getName() + "'s information:\n");
                        System.out.println("Name: " + ((Trader) trader).getName() + "\n");

                        if (((Trader) trader).getTraderStatus().isFrozen()) {
                            System.out.println(((Trader) trader).getName() + "'s account is frozen. You cannot arrange any transaction" +
                                    " with this " + ((Trader) trader).getName() + ".\n");
                        }

                        int i = 0;
                        //Print trader's inventory
                        if (((Trader) trader).getInventory().getInv().size() == 0) {
                            System.out.println(((Trader) trader).getName() + "'s inventory is empty.");
                        } else {
                            System.out.println("Inventory:");
                            while (i < ((Trader) trader).getInventory().getInv().size()) {
                                System.out.println(((Trader) trader).getInventory().getInv().get(i).getName());
                                i++;
                            }
                        }
                        System.out.println("\n");

                        i = 0;
                        if (((Trader) trader).getWishList().getInv().size() == 0) {
                            System.out.println(((Trader) trader).getName() + "'s wishlist is empty.");
                        } else {
                            //Print trader's wishlist
                            System.out.println("Wishlist:");
                            while (i < ((Trader) trader).getWishList().getInv().size()) {
                                System.out.println(((Trader) trader).getWishList().getInv().get(i).getName());
                                i++;
                            }
                        }
                        System.out.println("\n");

                        i = 0;
                        if (((Trader) trader).frequentPartners().size() == 0) {
                            System.out.println("'" + ((Trader) trader).getName() + "' has no frequent partner at the moment.");
                        } else {
                            //Print trader's frequent partners
                            System.out.println("Frequent partners:");
                            while (i < ((Trader) trader).frequentPartners().size()) {
                                System.out.println(((Trader) trader).frequentPartners().get(i).getName());
                                i++;
                            }
                        }
                        System.out.println("\n");

                        i = 0;
                        if (((Trader) trader).getRecentItems().size() == 0) {
                            System.out.println("'" + ((Trader) trader).getName() + "' has no recent item at the moment.");
                        } else {
                            //Print trader's frequent partners
                            System.out.println("Recent items:");
                            while (i < ((Trader) trader).getRecentItems().size()) {
                                System.out.println(((Trader) trader).getRecentItems().get(i).getName());
                                i++;
                            }
                        }
                        System.out.println("\n");
                    }
                }
            }
        }
    }

    /**
     * Gets a list of all unconfirmed items in the system (used by admins when confirming items).
     * @return an ArrayList representing all items that have not been confirmed
     */
    public ArrayList<Item> findUnconfirmed(){
        ArrayList<Item> unconfirmed = new ArrayList<Item>();
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
        ArrayList<Item> confirmed = new ArrayList<Item>();
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
        ArrayList<User> users = new ArrayList<User>();
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
        ArrayList<User> users = new ArrayList<User>();
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
