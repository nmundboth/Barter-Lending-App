package phase1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * <h1>Inbox Options</h1>
 * <p>The InboxOptions class shows options to a Trader after they have requested to view their inbox. This is where
 * all trade functionality (sending/receiving/proposing meetings/confirming, etc.) is based out of.</p>
 */
public class
InboxOptions {

    private Trader curr;
    private UserCatalogue uc;
    private UserSerialization us;
    private TradeManager tm;
    private String menuOptions;

    /**
     * @param curr The user currently logged in.
     * @param uc The UserCatalogue associated with all currently registered Users.
     * @param us UserSerialization, for saving information.
     */
    public InboxOptions(Trader curr, UserCatalogue uc, UserSerialization us){
        this.curr = curr;
        this.uc = uc;
        this.us = us;
        this.tm = new TradeManager();
        this.menuOptions = "To perform an action, type the corresponding number.\n1. Send transaction request\n" +
                "2. View accepted trades\n3. View trade notifications\n" +
                "4. View system notifications\n5. View trade requests\n6. Request to be unfrozen by admin\n" +
                "To return to options menu, type 'exit'.";
    }

    /**
     * Method which sets up the InboxOptions menu, and presents the trader with a list of available actions (which are
     * listed in the menuOptions variable). Checks that a trader is not frozen before allowing them to send a
     * transaction request.
     */
    public void run(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(menuOptions);

        ArrayList<User> list = uc.userBase;
        Admin admin = null;
        for (User user : list) {
            if (user instanceof Admin) {
                admin = (Admin)user;
            }
        }
        assert admin != null;
        AdminInbox adminInbox = (AdminInbox)admin.getInbox();

        try{
            String input = br.readLine();
            while (!input.equals("exit")) {
                switch (input) {
                    case "1":
                        if (!(((Trader) curr).getTraderStatus().isFrozen())) {
                            sendTrnsxnRequest(br);
                            System.out.println(menuOptions);
                        } else if ((((Trader) curr).getTraderStatus().isFrozen())) {
                            System.out.println("Your account is frozen and can not currently send transaction requests. " +
                                    "Please select 'request unfreeze' from this menu to ask an admin to review your account.");
                            System.out.println(menuOptions);
                        }
                        break;
                    case "2":
                        AcceptedTradeOptions ato = new AcceptedTradeOptions(curr, uc, us);
                        ato.run();
                        System.out.println(menuOptions);
                        break;
                    case "3":
                        Inbox inbox = curr.getInbox();
                        if (inbox.getTraderNotiUnread() == 0) {
                            System.out.println("Sub-inbox empty");
                            System.out.println(menuOptions);
                            break;
                        } else {
                            System.out.println("You have " + inbox.getTraderNotiUnread() + " unread messages");
                            int index = 0;
                            for (Message i : inbox.getTraderNoti()) {
                                System.out.println(index);
                                index += 1;
                            }
                            System.out.println("Choose a message to display:");
                            String messageNum = br.readLine();
                            int i = 0;
                            try {
                                i = Integer.parseInt(messageNum);
                            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                                System.out.println("Invalid input");
                                System.out.println(menuOptions);
                                break;
                            }
                            inbox.getTradeNoti(i);
                            System.out.println(menuOptions);
                            break;
                        }
                    case "4":
                        Inbox inbox1 = curr.getInbox();
                        if (inbox1.getAdmiNotiUnread() == 0) {
                            System.out.println("Sub-inbox empty");
                            System.out.println(menuOptions);
                            break;
                        } else {
                            System.out.println("You have " + inbox1.getAdmiNotiUnread() + " unread messages");
                            int index = 0;
                            for (Message i : inbox1.getAdmiNoti()) {
                                System.out.println(index);
                                index += 1;
                            }
                            System.out.println("Choose a message to display");
                            String messageNum = br.readLine();
                            int j = 0;
                            try {
                                j = Integer.parseInt(messageNum);
                            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                                System.out.println("Invalid input");
                                System.out.println(menuOptions);
                                break;
                            }
                            inbox1.getAdminNoti(j);
                            System.out.println(menuOptions);
                            break;
                        }
                    case "5":
                        viewTR(br);
                        System.out.println(menuOptions);
                        break;

                    case "6":
                        if (((Trader) curr).f){
                            if (!(adminInbox.undoFrozen.contains((Trader) curr))){
                                adminInbox.undoFrozen.add((Trader) curr);
                                System.out.println("You have requested to be unfrozen.");
                            }
                            else{ // undoFrozen.contains(Trader)
                                System.out.println("You have already requested that you be unfrozen.");
                            }
                        }
                        else{
                            System.out.println("Your account is not frozen!");
                        }
                        System.out.println(menuOptions);
                        break;

                    default:
                        System.out.println("Invalid Response");
                        System.out.println(menuOptions);
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    // Presents a trader with the different kinds of transactions that they can send, and upon option selection, makes
    // sure that the trader's account is eligible for that type of trade
    private void sendTrnsxnRequest(BufferedReader br){
        String trnsxnOptions = "To perform an action, type the corresponding number.\n1. Request to borrow " +
                "an item from a user\n2. Request to lend an item to a user\n3. Request to trade an item with a user." +
                "\nType 'exit' to go back to your inbox menu.";
        System.out.println(trnsxnOptions);
        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (input.equals("1") && !(((Trader) curr).getTraderStatus().isGreedy())){
                    borrowItem(br);
                    System.out.println(trnsxnOptions);
                }
                else if(input.equals("1") && ((Trader) curr).getTraderStatus().isGreedy()){
                    System.out.println("Please lend more items before you attempt to borrow!\n");
                    System.out.println(trnsxnOptions);
                }
                else if (input.equals("2")){
                    lendItem(br);
                    System.out.println(trnsxnOptions);
                }
                else if (input.equals("3")){
                    tradeItem(br);
                    System.out.println(trnsxnOptions);
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    // Allows a trader to borrow an item from another trader.
    // In order to borrow an item, it must be on the current trader's wishlist, and in the other trader's inventory.
    private void borrowItem(BufferedReader br){
        for (int i = 0; i < ((Trader) curr).getWishList().getInv().size(); i++) {
            System.out.println("    " + (i + 1) + ". " + ((Trader) curr).getWishList().getInv().get(i));
        }
        String borrowPrompt = "Select the item from your wishlist that you would like to borrow from a user, " +
                "or type 'exit' to go back: ";
        System.out.println(borrowPrompt);
        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input) >= 1 &&
                        Integer.parseInt(input) <= ((Trader) curr).getWishList().getInv().size())){ // Item is in current trader's wishlist
                    phase1.Item item = ((Trader) curr).getWishList().getInv().get(Integer.parseInt(input) - 1);
                    ArrayList <User> userWithItem = uc.findUserWithItem(item);
                    for (int i = 0; i < userWithItem.size(); i++){
                        if (!((Trader) userWithItem.get(i)).getTraderStatus().isFrozen()){
                            System.out.println("    " + (i + 1) + ". " +
                                    uc.findUserWithItem(item).get(i).getUsername() +
                                    ". (" + ((Trader)uc.findUserWithItem(item).get(i)).getLocation() +")");
                        }
                    }
                    System.out.println("Enter the number associated with the user you would like to borrow from.\n" +
                            "If no users are listed above, then no user currently has your item, or the user(s) that " +
                            "do is frozen and can not currently lend items.");
                    input = br.readLine();
                    if (input.equals("exit")){
                        break;
                    }
                    else if (isInteger(input) && Integer.parseInt(input) >= 1 &&
                            Integer.parseInt(input) <= userWithItem.size()){
                        int index = Integer.parseInt(input) - 1;
                        System.out.println("Would you like the transaction to be permanent? (Y/N)");
                        input = br.readLine();

                        if (input.equals("exit")){
                            break;
                        }
                        else if (input.equals("Y")){
                            System.out.println("Is the item digital? (Y/N)");
                            input = br.readLine();
                            if (input.equals("exit")){
                                break;
                            }
                            else if (input.equals("Y")){
                                tm.sendBorrowRequest(((Trader) curr),
                                        ((Trader) userWithItem.get(index)), item, true, true);
                                System.out.println("Request sent!");
                                break;
                            }
                            else if (input.equals("N")){
                                tm.sendBorrowRequest(((Trader) curr),
                                        ((Trader) userWithItem.get(index)), item, true, false);
                                System.out.println("Request sent!");
                                break;
                            }
                            else {
                                System.out.println(borrowPrompt);
                            }
                        }
                        else if(input.equals("N")){
                            System.out.println("Is the item digital? (Y/N)");
                            input = br.readLine();
                            if (input.equals("exit")){
                                break;
                            }
                            else if (input.equals("Y")){
                                tm.sendBorrowRequest(((Trader) curr),
                                        ((Trader) userWithItem.get(index)), item, false, true);
                                System.out.println("Request sent!");
                                break;
                            }
                            else if (input.equals("N")){
                                tm.sendBorrowRequest(((Trader) curr),
                                        ((Trader) userWithItem.get(index)), item, false, false);
                                System.out.println("Request sent!");
                                break;
                            }
                            else {
                                System.out.println(borrowPrompt);
                            }
                        }
                        else{
                            System.out.println(borrowPrompt);
                        }
                    }
                    else{
                        System.out.println(borrowPrompt);
                    }
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }

    }

    // Allows a trader to lend an item to another user.
    // In order to lend an item, it must be in the current trader's inventory and on the other trader's wishlist.
    private void lendItem(BufferedReader br){
        for (int i = 0; i < ((Trader) curr).getInventory().getInv().size(); i++) {
            System.out.println("    " + (i + 1) + ". " + ((Trader) curr).getInventory().getInv().get(i));
        }
        String lendPrompt = "Select the item from your inventory that" +
                " you would like to lend to a user, or type 'exit' to go back:";
        System.out.println(lendPrompt);

        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input) >= 1 &&
                        Integer.parseInt(input) <= ((Trader) curr).getInventory().getInv().size())){
                    phase1.Item item = ((Trader) curr).getInventory().getInv().get(Integer.parseInt(input) - 1);
                    ArrayList <User> userWantsItem = uc.findUserWantsItem(item);
                    for (int i = 0; i < userWantsItem.size(); i++){
                        if (!((Trader) userWantsItem.get(i)).getTraderStatus().isFrozen()){
                            System.out.println("    " + (i + 1) + ". " +
                                    uc.findUserWithItem(item).get(i).getUsername() +
                                    ". (" + ((Trader)uc.findUserWithItem(item).get(i)).getLocation() +")");
                        }
                    }
                    System.out.println("Enter the number associated with the user you would like to lend to.\n" +
                            "If no users are listed above, then no user currently wants your item, or the user(s) " +
                            "that do is frozen and can not currently receive items.");
                    input = br.readLine();
                    if (input.equals("exit")){
                        break;
                    }
                    else if (isInteger(input) && Integer.parseInt(input) >= 1 &&
                            Integer.parseInt(input) <= userWantsItem.size()){
                        int index = Integer.parseInt(input) - 1;
                        System.out.println("Would you like the transaction to be permanent? (Y/N)");
                        input = br.readLine();
                        if (input.equals("exit")){
                            break;
                        }
                        else if (input.equals("Y")){
                            System.out.println("Is the item digital? (Y/N)");
                            input = br.readLine();
                            if (input.equals("exit")){
                                break;
                            }
                            else if (input.equals("Y")){
                                tm.sendLendRequest(((Trader) curr),
                                        ((Trader) userWantsItem.get(index)), item, true, true);
                                System.out.println("Request sent!");
                                break;
                            }
                            else if (input.equals("N")){
                                tm.sendLendRequest(((Trader) curr),
                                        ((Trader) userWantsItem.get(index)), item, true, false);
                                System.out.println("Request sent!");
                                break;
                            }
                            else {
                                System.out.println(lendPrompt);
                            }
                        }
                        else if(input.equals("N")){
                            System.out.println("Is the item digital? (Y/N)");
                            input = br.readLine();
                            if (input.equals("exit")){
                                break;
                            }
                            else if (input.equals("Y")){
                                tm.sendLendRequest(((Trader) curr),
                                        ((Trader) userWantsItem.get(index)), item, false, true);
                                System.out.println("Request sent!");
                                break;
                            }
                            else if (input.equals("N")){
                                tm.sendLendRequest(((Trader) curr),
                                        ((Trader) userWantsItem.get(index)), item, false, false);
                                System.out.println("Request sent!");
                                break;
                            }
                            else {
                                System.out.println(lendPrompt);
                            }
                        }
                        else{
                            System.out.println(lendPrompt);
                        }
                    }
                    else{
                        System.out.println(lendPrompt);
                    }
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    // Allows a trader to trade an item with another trader.
    // This method initiates that process, by asking the trader which item from their wishlist that they would like
    // to receive in a trade.
    private void tradeItem(BufferedReader br){
        for (int i = 0; i < ((Trader) curr).getWishList().getInv().size(); i++) {
            System.out.println("    " + (i + 1) + ". " + ((Trader) curr).getWishList().getInv().get(i));
        }
        String tradePrompt = "Select the item from your wishlist that you would like to trade for, " +
                "or type 'exit' to go back: ";
        System.out.println(tradePrompt);

        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input) >= 1 &&
                        Integer.parseInt(input) <= ((Trader) curr).getWishList().getInv().size())){
                    phase1.Item item = ((Trader) curr).getWishList().getInv().get(Integer.parseInt(input) - 1);
                    ArrayList <User> userWithItem = uc.findUserWithItem(item);
                    selectTrader(item, userWithItem, br);
                    System.out.println(tradePrompt);
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    // Finds trader(s) that has the selected item (see tradeItem method) in their inventory that the initial trader
    // wanted to trade for (if any), and lists them to the user
    private void selectTrader(Item item, ArrayList<User> userWithItem, BufferedReader br){
        for (int i = 0; i < userWithItem.size(); i++){
            if (!((Trader) userWithItem.get(i)).getTraderStatus().isFrozen()){
                System.out.println("    " + (i + 1) + ". " +
                        uc.findUserWithItem(item).get(i).getUsername() +
                        ". (" + ((Trader)uc.findUserWithItem(item).get(i)).getLocation() +")");
            }
        }
        String selectPrompt = "Enter the number associated with the user you would like to trade with.\n" +
                "If no users are listed above, then no user currently has your desired item, or the user(s) that " +
                "do is frozen and can not currently lend items.";
        System.out.println(selectPrompt);
        try{
            String input = br.readLine();
            while(!input.equals("exit")) {
                if (isInteger(input) && Integer.parseInt(input) >= 1 &&
                        Integer.parseInt(input) <= userWithItem.size()) {
                    int index = Integer.parseInt(input) - 1;
                    if (checkCompatibility(userWithItem.get(index))){
                        selectItem(br, userWithItem.get(index), item);
                        break;
                    }
                    else{
                        System.out.println("You do not have anything that the user wants! Type 'exit' to go back, " +
                                "or select a different user to trade with.");
                    }
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    // Checks whether a user that has an item wants anything that the other user has.
    private boolean checkCompatibility(User other){
        for (int i = 0; i < ((Trader)curr).getInventory().getInv().size(); i++){
            if (((Trader) other).getWishList().getInv().contains(((Trader)curr).getInventory().getInv().get(i))){
                return true;
            }
        }
        return false;
    }

    // Once confirmed that the users are eligible to trade with one another, initiates the 'making' of a trade.
    // Asks users for whether the trade should be permanent, then proposes a trade.
    private void selectItem(BufferedReader br, User other, Item theirItem){
        ArrayList<Item> tradableItems = new ArrayList<Item>();
        for (int i = 0; i < ((Trader) other).getWishList().getInv().size(); i++){
            if (((Trader) curr).getInventory().getInv().contains(((Trader) other).getWishList().getInv().get(i))){
                System.out.println("    " + (i + 1) + ". " + ((Trader) other).getWishList().getInv().get(i));
                tradableItems.add(((Trader) other).getWishList().getInv().get(i));
            }
        }
        System.out.println("Select an item you wish to offer in the trade.");
        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input) >= 1 &&
                        Integer.parseInt(input) <= tradableItems.size())){
                    int index = Integer.parseInt(input) - 1;
                    Item yourItem = tradableItems.get(index);
                    System.out.println("Would you like the transaction to be permanent? (Y/N)");
                    input = br.readLine();
                    if(input.equals("exit")){
                        break;
                    }
                    else if(input.equals("Y")){
                        System.out.println("Is your item digital? (Y/N)");
                        input = br.readLine();
                        if (input.equals("exit")){
                            break;
                        }
                        else if (input.equals("Y")){
                            System.out.println("Is "+((Trader) other).getName()+"'s item digital? (Y/N)");
                            input = br.readLine();
                            if (input.equals("exit")){
                                break;
                            }
                            else if (input.equals("Y")){
                                tm.sendTWTradeRequest(((Trader) curr), ((Trader) other), yourItem, theirItem,
                                        true, true, true);
                                System.out.println("Request sent!");
                                break;
                            }
                            else if (input.equals("N")){
                                tm.sendTWTradeRequest(((Trader) curr), ((Trader) other), yourItem, theirItem,
                                        true, true, false);
                                System.out.println("Request sent!");
                                break;
                            }
                            else {
                                System.out.println("Select an item you wish to offer in the trade.");
                            }
                        }
                        else if (input.equals("N")){
                            System.out.println("Is "+((Trader) other).getName()+"'s item digital? (Y/N)");
                            input = br.readLine();
                            if (input.equals("exit")){
                                break;
                            }
                            else if (input.equals("Y")){
                                tm.sendTWTradeRequest(((Trader) curr), ((Trader) other), yourItem, theirItem,
                                        true, true, true);
                                System.out.println("Request sent!");
                                break;
                            }
                            else if (input.equals("N")){
                                tm.sendTWTradeRequest(((Trader) curr), ((Trader) other), yourItem, theirItem,
                                        true, true, false);
                                System.out.println("Request sent!");
                                break;
                            }
                            else {
                                System.out.println("Select an item you wish to offer in the trade.");
                            }
                        }
                    }
                    else if(input.equals("N")){
                        System.out.println("Is your item digital? (Y/N)");
                        input = br.readLine();
                        if (input.equals("exit")){
                            break;
                        }
                        else if (input.equals("Y")){
                            System.out.println("Is "+((Trader) other).getName()+"'s item digital? (Y/N)");
                            input = br.readLine();
                            if (input.equals("exit")){
                                break;
                            }
                            else if (input.equals("Y")){
                                tm.sendTWTradeRequest(((Trader) curr), ((Trader) other), yourItem, theirItem,
                                        false, true, true);
                                System.out.println("Request sent!");
                                break;
                            }
                            else if (input.equals("N")){
                                tm.sendTWTradeRequest(((Trader) curr), ((Trader) other), yourItem, theirItem,
                                        false, true, false);
                                System.out.println("Request sent!");
                                break;
                            }
                            else {
                                System.out.println("Select an item you wish to offer in the trade.");
                            }
                        }
                        else if (input.equals("N")){
                            System.out.println("Is "+((Trader) other).getName()+"'s item digital? (Y/N)");
                            input = br.readLine();
                            if (input.equals("exit")){
                                break;
                            }
                            else if (input.equals("Y")){
                                tm.sendTWTradeRequest(((Trader) curr), ((Trader) other), yourItem, theirItem,
                                        false, false, true);
                                System.out.println("Request sent!");
                                break;
                            }
                            else if (input.equals("N")){
                                tm.sendTWTradeRequest(((Trader) curr), ((Trader) other), yourItem, theirItem,
                                        false, false, false);
                                System.out.println("Request sent!");
                                break;
                            }
                            else {
                                System.out.println("Select an item you wish to offer in the trade.");
                            }
                        }
                    }
                    else {
                        System.out.println("Select an item you wish to offer in the trade.");
                    }
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }

    }

    // Allows trader to view unaccepted trade requests from other users, and then accept or reject those trade offers.
    private void viewTR(BufferedReader br){
        System.out.println("Unaccepted trade offers from other users: ");
        TraderInbox traderInbox = (TraderInbox) curr.getInbox();
        for (int i = 0; i < traderInbox.getUnacceptedTrades().size(); i++){
            System.out.println("    " + (i + 1) + ". " + traderInbox.getUnacceptedTrades().get(i));
        }
        System.out.println("Enter the number associated with the trade offer that you would like to accept/reject," +
                "or type 'exit' to go back:");
        try{
            String input = br.readLine();
            while (!input.equals("exit")){
                if (isInteger(input) && Integer.parseInt((input)) >= 1 &&
                        Integer.parseInt(input) <= traderInbox.getUnacceptedTrades().size()){
                    int index = Integer.parseInt(input) - 1;
                    Trade trade = traderInbox.getUnacceptedTrades().get(index).getTrade();
                    System.out.println("Type 'accept' to accept this offer, or 'reject' to reject this offer.");
                    input = br.readLine();
                    if (input.equals("exit")){
                        break;
                    }
                    else if (input.equals("accept")){
                        if (trade instanceof OneWayTrade){
                            tm.acceptTrade(((Trader) curr), trade.getOgTrader(), (OneWayTrade) trade);
                            System.out.println("Trade accepted!\n");
                            break;
                        }
                        else if (trade instanceof TwoWayTrade){
                            tm.acceptTrade(((Trader) curr), trade.getOgTrader(), (TwoWayTrade) trade);
                            System.out.println("Trade accepted!\n");
                            break;
                        }
                    }
                    else if (input.equals("reject")){
                        if (trade instanceof OneWayTrade){
                            tm.rejectUnaccepted(((Trader)curr), trade.getOgTrader(), (OneWayTrade) trade);
                            System.out.println("Trade rejected!\n");
                            break;
                        }
                        else if (trade instanceof TwoWayTrade){
                            tm.rejectUnaccepted(((Trader)curr), trade.getOgTrader(), (TwoWayTrade) trade);
                            System.out.println("Trade rejected!\n");
                            break;
                        }
                    }
                    else {
                        System.out.println("Type 'accept' to accept this offer, or 'reject' to reject this offer.");
                    }
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    // Template taken from
    // https://www.baeldung.com/java-check-string-number
    // Checks whether a given string is an integer.
    private boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
