package phase1;

import java.util.ArrayList;
import java.util.List;

/**
 * RUN THIS BEFORE RUNNING TradingMain FOR THE FIRST TIME
 */
public class Initialize {

    public static void main(String[] args) throws Exception {
        List<Message> traderNoti = new ArrayList<Message>();
        List<Message> adminNotifs = new ArrayList<Message>();
        AdminInbox inbox = new AdminInbox(traderNoti, adminNotifs);
        User admin = new Admin("admin", "admin", "admin", inbox);
        inbox.setOwner(admin);
        UserSerialization us = new UserSerialization();
        ArrayList<User> users = new ArrayList<User>();
        users.add(admin);
        us.toSerialize(users);
    }
}