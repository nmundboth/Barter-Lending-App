package phase1;

import UserSerialization;

public class TradingSystem {
    public static void main(String[] args) {
        userAccountOptions();
    }

    public static void userAccountOptions() {
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
        else if (signInOption == 2) {
            // add to list of accounts in userserialization
        }
    }
}