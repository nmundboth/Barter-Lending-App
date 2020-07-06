package phase1;

public class TradingSystem {
    public static void main(String[] args) {
        userAccountOptions();
    }

    public static void userAccountInfo() {
        String loginInfo = "src/accounts.ser";
        UserManager userManager = new UserManager(loginInfo);

    }
}