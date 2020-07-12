package phase1;

import java.io.Serializable;

public class Item implements Serializable {

    private String name;
    private String descrip;
    private boolean status;

    public Item(String name, String descrip){
        this.name = name;
        this.descrip = descrip;
        this.status = false;
        //Every item starts as unconfirmed until confirmed by the admin
    }

    // Testing rebase on master branch

    public String getName() {
        return name;
    }

    public String getDescrip() {
        return descrip;
    }

    public boolean isConfirmed(){
        return status;
    }

    public String toString(){
        return this.name;
    }

    public void setConfirm(){this.status = true; }
}
