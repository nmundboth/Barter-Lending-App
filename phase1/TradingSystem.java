package phase1;

import UserSerialization;
import User;
import Inbox;
import Trade;

public class TradingSystem {
    public static void main(String[] args) {

        // serialization and catalogue classes
        UserSerialization userSerialization;
        UserCatalogue userCatalogue;

        // log in menu
        Scanner userInfo = new Scanner(System.in);
        System.out.println("Enter username");
        String username = userInfo.nextLine();
        System.out.println("Enter password");
        String password = userInfo.nextLine();
        System.out.println("1: Log in 2: Register");
        int signInOption = userInfo.nextLine();

        // what happens after user logs in (could be put in use case class)
        // if they are registering
        if (signInOption == 2) {
            System.out.println("Account type: 'trader' or 'admin'");
            int type = userInfo.nextLine();
            ArrayList<Trade> trades = new ArrayList<Trade>();
            ArrayList<String> strings = new ArrayList<String>();
            Inbox inbox = new Inbox(trades, strings, strings);
            User tempUser = new User(username, password, type, inbox);
            userSerialization.userList.append();
        // if they are logging in
        } else if (signInOption == 1) {
            userSerialization.deSerialize();
        }
    }
}