package phase1;

import java.io.Serializable;
import java.util.List;

public class Admin extends User implements Serializable {
    private int notifs_count;
    private AdminInbox inbox;

    public Admin(String username, String password, String type, AdminInbox inbox) {
        super(username, password, type, inbox);
        this.notifs_count = 0;
    }

    public int getNotifs_count(){return this.notifs_count;}

    public void readNotifs(){
        this.notifs_count = 0;
        //Reads notifications line by line
        int i = 0;
        while (i < this.getInbox().getAdmiNoti().size()){
            System.out.println(this.getInbox().getAdmiNoti().get(i));
            i++;
        }
    }
    public void freezeTrader(Trader trader){trader.setFrozen(true);}
    // Setting the frozen status after isGreedy is set to true

    public void unFreezeTrader(Trader trader){trader.setFrozen(false); }
    // Lifting the frozen status for a user upon request

    public void confirmItem(Item item){item.setConfirm();}
    // Confirms a certain item

    public String toString(){
        return getUsername();
    }
}
