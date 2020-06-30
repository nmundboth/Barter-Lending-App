package group_0073.phase1;

import java.util.List;

public class Admin extends User{

    private int notifs_count;

    public Admin(String username, String password, String type, Inbox inbox) {

        super(username, password, type, inbox);
        this.notifs_count = 0;
    }

    public void readNotifs(){
        this.notifs_count = 0;

        //Reads notifications line by line
        int i = 0;
        while (i < this.getInbox().getAdmiNoti().size()){
            System.out.println(this.getInbox().getAdmiNoti().get(i));
            i++;
        }
    }

}
