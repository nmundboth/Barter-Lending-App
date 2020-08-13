package phase2;

import java.io.Serializable;

/**
 * <h1>Item</h1>
 * <p>An item that has been added to the system by a user. Can either be unconfirmed (how it starts) or confirmed (once
 * confirmed by an admin as an appropriate object).</p>
 */
public class Item implements Serializable {

    private String name;
    private String descrip;
    private boolean status;
    private boolean digital;

    /**
     * @param name The name of the item
     * @param descrip The description of the item
     */
    public Item(String name, String descrip){
        this.name = name;
        this.descrip = descrip;
        this.status = false; // Whether or not the item has been confirmed
        this.digital = false; // Whether item is physical or digital
    }

    /**
     * Gets the name of the item.
     *
     * @return a string of the item name.
     */
    public String getName() {
        return name;
    }

    /**
     *Gets the description of the item.
     *
     * @return a String of the item description.
     */
    public String getDescrip() {
        return descrip;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    /**
     * States whether or not the item is confirmed.
     *
     * @return a boolean corresponding to whether or not the item has been confirmed.
     */
    public boolean isConfirmed(){
        return status;
    }

    /**
     * Returns a string representation of the item.
     *
     * @return a string of the item name.
     */
    public String toString(){
        return this.name;
    }

    /**
     * Confirms an item.
     */
    public void setConfirm(){
        this.status = true;
    }

    public boolean isDigital() {
        return digital;
    }

    public void setDigital(boolean digital) {
        this.digital = digital;
    }
}
