package phase1;

import java.util.ArrayList;
import java.util.List;

public class Initialize {

    public static void main(String[] args) throws Exception {
        List<Trade> trades = new ArrayList<Trade>();
        List<String> traderNoti = new ArrayList<String>();
        List<String> adminNotifs = new ArrayList<String>();
        List<Trader> undoFrozen = new ArrayList<Trader>();
        AdminInbox inbox = new AdminInbox(trades, traderNoti, adminNotifs, undoFrozen);
        User admin = new Admin("admin", "admin", "admin", inbox);
        UserSerialization us = new UserSerialization();
        ArrayList<User> users = new ArrayList<User>();
        users.add(admin);
        us.toSerialize(users);
    }
}