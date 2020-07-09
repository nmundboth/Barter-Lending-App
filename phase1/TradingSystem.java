package phase1;

import UserSerialization;
import User;
import Inbox;
import Trade;

public class TradingSystem {
    public static void main(String[] args) {
        UserSerialization userSerialization = new UserSerialization();
        Scanner userInfo = new Scanner(System.in);
        System.out.println("Enter username")
        String username = userInfo.nextLine();
        System.out.println("Enter password")
        String password = userInfo.nextLine();
        System.out.println("1: Log in 2: Register");
        int signInOption = userInfo.nextLine();

        if (signInOption == 1) {
            System.out.println("Account type: 'trader' or 'admin'");
            int type = userInfo.nextLine();
            ArrayList<Trade> trades = new ArrayList<Trade>();
            ArrayList<String> strings = new ArrayList<String>();
            Inbox inbox = new Inbox(trades, strings, strings);
            User tempUser = new User(username, password, type, inbox);
            userSerialization.userList.append();
        }
        else if (signInOption == 2) {
            userSerialization.deSerialize();
        }
    }

    /*public static void TradingSystem() {
        Scanner userInfo = new Scanner(System.in);
        System.out.println("Enter username")
        String username = userInfo.nextLine();
        System.out.println("Enter password")
        String password = userInfo.nextLine();
        System.out.println("1: Log in 2: Register");
        int signInOption = 0;
        UserManager userManager = new UserManager(username, password, signInOption);
    }

    private void entryOptions(int signInOption) {
        while (signInOption == 0) {
            System.out.println("Input must be 1 or 2");
            System.out.println("1: Log in 2: Register");
            int signInOption = 0;
        }
        if (signInOption == 1) {
            // check if exists in userserialization and add to usermanager as current user
        }
        else if (signInOption) == 2) {
            // add to list of accounts in userserialization
        }
    }*/
}