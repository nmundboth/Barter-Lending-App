package phase1;

import java.io.Serializable;
import java.util.ArrayList;

/** <h1>Inventory</h1>
 * <p>Represents the inventory of a trader.</p>
 * @author Navnee Mundboth
 * @author James Veale
 */
public class Inventory implements Serializable {

    private ArrayList<Item> inventory;
    private String name;

    /**
     *
     * @param inventory The trader's list of items - wishlist or inventory.
     * @param name the trader's inventory's name.
     */
    public Inventory(ArrayList<Item> inventory, String name){
        this.inventory = inventory;
        this.name = name;
    }

    /** Gets the list of Items in a Trader's inventory.
     *
     * @return an Arraylist of Items.
     */
    public ArrayList<Item> getInv(){
        return inventory;
    }

    /** Gets the name of the inventory.
     *
     * @return a String representing the name of the inventory.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds an item to the trader's inventory
     *
     * @param item the Item object that the trader wants to add to their inventory.
     */
    public void addItem(Item item){
        this.inventory.add(item);
        System.out.println("Added item " + item.getName() + " to your " + name + "!");
    }

    /** Removes an Item from Trader's inventory.
     *
     * @param item An Item object containing the name and description of the item.
     */
    public void removeItem(Item item){
        this.inventory.remove(item);
    }

    /** Removes an Item that Trader is no longer interested in from Trader's wishlist.
     *
     * @param item An Item object containing the name and description of the item.
     */
    public void removeWish(Item item){
             //   if (this.name.equals("wishlist")){
        for(int i = 0; i < this.inventory.size(); i++){
            if(item == this.inventory.get(i)){
                this.inventory.remove(item);
                break;
            }
                  //  }
                    System.out.println("Item not found!");
                }
        //System.out.println("Removes items only from a wishlist!");
    }

}
