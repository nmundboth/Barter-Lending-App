package phase1;

public class Item {

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

    public String toString(){
        return this.name;
    }

    public void setConfirm(){this.status = true; }
}
