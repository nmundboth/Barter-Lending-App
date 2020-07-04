package phase1;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserCatalogue {
    public ArrayList<User> userBase;

    public UserCatalogue(ArrayList<User> list){
        userBase = new ArrayList<User>(list);
    }

    public ArrayList<Item> findItem(){
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


}
