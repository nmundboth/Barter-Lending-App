package group_0073.phase1;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserCatalogue {
    public ArrayList<User> userBase;


    public ArrayList<Item> findItem(){
        ArrayList<Item> items = new ArrayList<Item>();
        for(User trader: userBase){
            for (Item inv: ((Trader)trader).getInventory()){
                if(!items.contains(inv)){
                    items.add(inv);
                }
            }
        }
        return items;
    }

    public ArrayList<User> findUserWithItem(Item item){
        ArrayList<User> users = new ArrayList<User>();
        for(User trader: userBase){
            if(((Trader)trader).getInventory().contains(item)){
                users.add(trader);
            }
        }
        return users;
    }
}
