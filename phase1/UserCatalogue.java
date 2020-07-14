package phase1;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserCatalogue {
    public ArrayList<User> userBase;

    public UserCatalogue(ArrayList<User> list){
        userBase = new ArrayList<User>(list);
    }

    public ArrayList<Item> findAllItems(){
        ArrayList<Item> items = new ArrayList<Item>();
        for(User trader: userBase){
            if(trader instanceof Trader) {
                for (Item inv : ((Trader) trader).getInventory()) {
                    if (!items.contains(inv)) {
                        items.add(inv);
                    }
                }
            }
        }
        return items;
    }

    public ArrayList<Item> findUnconfirmed(){
        ArrayList<Item> unconfirmed = new ArrayList<Item>();
        for (Item item: findAllItems()){
            if (!item.isConfirmed()){
                unconfirmed.add(item);
            }
        }
        return unconfirmed;
    }

    public ArrayList<Item> findConfirmed(){
        ArrayList<Item> confirmed = new ArrayList<Item>();
        for (Item item: findAllItems()){
            if (item.isConfirmed()){
                confirmed.add(item);
            }
        }
        return confirmed;
    }

    public ArrayList<User> findUserWithItem(Item item){
        ArrayList<User> users = new ArrayList<User>();
        for(User trader: userBase){
            if(trader instanceof Trader) {
                if (((Trader) trader).getInventory().contains(item)) {
                    users.add(trader);
                }
            }
        }
        return users;
    }

    public ArrayList<User> findUserWantsItem(Item item){
        ArrayList<User> users = new ArrayList<User>();
        for (User trader: userBase){
            if(trader instanceof Trader){
                if (((Trader) trader).getWishList().contains(item)){
                    users.add(trader);
                }
            }
        }
        return users;
    }

    public ArrayList<Item> findUserItem(User user){
        for(User trader: userBase) {
            if (trader instanceof Trader) {
                if (user == trader) {
                    return (ArrayList<Item>) ((Trader) trader).getInventory();
                }
            }
        }
        return new ArrayList<Item>();
    }

    public boolean inUserBase(String Username){
        for (User user: userBase){
            if (user.getUsername().equals(Username)){
                return true;
            }
        }
        return false;
    }

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

    public ArrayList<User> findFlagged(){
        ArrayList<User> flagged = new ArrayList<User>();
        for (User user: userBase){
            if (user instanceof Trader){
                if (((Trader) user).isFlagged()){
                    flagged.add(user);
                }
            }
        }
        return flagged;
    }


}
