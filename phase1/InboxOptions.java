package phase1;

//TODO: Options should include:
// 1. Send trade/borrow/lend request
// 2. View accepted trades --> view open trades | view unscheduled trades --> Propose a meeting for an unscheduled trade
// 3. View trade notifications
// 4. View system (admin) notifications
// 5. View trade requests from other users --> Accept a trade | Reject a trade

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class InboxOptions {

    private User curr;
    private UserCatalogue uc;
    private UserSerialization us;
    private TradeManager tm;
    private String menuOptions;

    public InboxOptions(User curr, UserCatalogue uc, UserSerialization us){
        this.curr = curr;
        this.uc = uc;
        this.us = us;
        this.tm = new TradeManager();
        this.menuOptions = "To perform an action, type the corresponding number.\n1. Send transaction request\n" +
                "2. View accepted trades\n3. View trade notifications (" + curr.getInbox().getTradersUnread() + ")\n" +
                "4. View system notifications (" + curr.getInbox().getAdmiNotiUnread() +
                ")\n5. View trade requests (" + curr.getInbox().getUnaccptedUnread() +
                ")\nTo return to options menu, type 'exit'.";
    }

    public void run(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(menuOptions);

        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (input.equals("1") && !(((Trader) curr).isFrozen())){
                    sendTrnsxnRequest(br);
                    System.out.println(menuOptions);
                }
                else if (input.equals("1") && (((Trader) curr).isFrozen())){
                    System.out.println("Your account is frozen and can not currently send transaction requests. " +
                            "Please select 'request unfreeze' from this menu to ask an admin to review your account.");
                    System.out.println(menuOptions);
                }
                else if (input.equals("2")){
                    AcceptedTradeOptions ato = new AcceptedTradeOptions(curr, uc, us);
                    ato.run();
                    System.out.println(menuOptions);
                }
                else if(input.equals("3")){
                    Inbox inbox = curr.getInbox();
                    if(inbox.getTradersUnread() == 0){
                        System.out.println("Sub-inbox empty");
                        System.out.println(menuOptions);
                    }
                    else {
                        System.out.println("You have " + inbox.getTradersUnread() + " unread messages");
                        int index = 0;
                        for (String i : inbox.getTraderNoti()) {
                            System.out.println(index);
                            index += 1;
                        }
                        System.out.println("Choose a message to display:");
                        String messageNum = br.readLine();
                        inbox.getTradeNoti(Integer.parseInt(messageNum));
                        System.out.println(menuOptions);
                    }
                }
                else if (input.equals("4")){
                    Inbox inbox = curr.getInbox();
                    if(inbox.getAdmiNotiUnread() == 0){
                        System.out.println("Sub-inbox empty");
                        System.out.println(menuOptions);
                    }
                    else {
                        System.out.println("You have " + inbox.getAdmiNotiUnread() + " unread messages");
                        int index = 0;
                        for (String i : inbox.getAdmiNoti()) {
                            System.out.println(index);
                            index += 1;
                        }
                        System.out.println("Choose a message to display");
                        String messageNum = br.readLine();
                        inbox.getAdminNoti(Integer.parseInt(messageNum));
                        System.out.println(menuOptions);
                    }
                }
                else if (input.equals("5")){
                    viewTR(br);
                    System.out.println(menuOptions);
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    //WIP
    public void sendTrnsxnRequest(BufferedReader br){
        String trnsxnOptions = "To perform an action, type the corresponding number.\n1. Request to borrow " +
                "an item from a user\n2. Request to lend an item to a user\n3. Request to trade an item with a user." +
                "\nType 'exit' to go back to your inbox menu.";
        System.out.println(trnsxnOptions);
        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (input.equals("1") && !(((Trader) curr).isGreedy())){
                    borrowItem(br);
                    System.out.println(trnsxnOptions);
                }
                else if(input.equals("1") && ((Trader) curr).isGreedy()){
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

    public void borrowItem(BufferedReader br){
        for (int i = 0; i < ((Trader) curr).getWishList().size(); i++) {
            System.out.println("    " + (i + 1) + ". " + ((Trader) curr).getWishList().get(i));
        }
        String borrowPrompt = "Select the item from your wishlist that you would like to borrow from a user, " +
                "or type 'exit' to go back: ";
        System.out.println(borrowPrompt);
        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input) >= 1 &&
                        Integer.parseInt(input) <= ((Trader) curr).getWishList().size())){ // Item is in current trader's wishlist
                    Item item = ((Trader) curr).getWishList().get(Integer.parseInt(input) - 1);
                    ArrayList <User> userWithItem = uc.findUserWithItem(item);
                    for (int i = 0; i < userWithItem.size(); i++){
                        if (!((Trader) userWithItem.get(i)).isFrozen()){
                            System.out.println("    " + (i + 1) + ". " +
                                    uc.findUserWithItem(item).get(i).getUsername());
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
                            tm.sendBorrowRequest(((Trader) curr),
                                    ((Trader) userWithItem.get(index)), item, true);
                            System.out.println("Request sent!");
                            break;
                        }
                        else if(input.equals("N")){
                            tm.sendBorrowRequest(((Trader) curr),
                                    ((Trader) userWithItem.get(index)), item, false);
                            System.out.println("Request sent!");
                            break;
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

    public void lendItem(BufferedReader br){
        for (int i = 0; i < ((Trader) curr).getInventory().size(); i++) {
            System.out.println("    " + (i + 1) + ". " + ((Trader) curr).getInventory().get(i));
        }
        String lendPrompt = "Select the item from your inventory that" +
                " you would like to lend to a user, or type 'exit' to go back:";
        System.out.println(lendPrompt);

        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input) >= 1 &&
                        Integer.parseInt(input) <= ((Trader) curr).getInventory().size())){
                    Item item = ((Trader) curr).getInventory().get(Integer.parseInt(input) - 1);
                    ArrayList <User> userWantsItem = uc.findUserWantsItem(item);
                    for (int i = 0; i < userWantsItem.size(); i++){
                        if (!((Trader) userWantsItem.get(i)).isFrozen()){
                            System.out.println("    " + (i + 1) + ". " +
                                    uc.findUserWithItem(item).get(i).getUsername());
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
                            tm.sendLendRequest(((Trader) curr),
                                    ((Trader) userWantsItem.get(index)), item, true);
                            System.out.println("Request sent!");
                            break;
                        }
                        else if(input.equals("N")){
                            tm.sendLendRequest(((Trader) curr),
                                    ((Trader) userWantsItem.get(index)), item, false);
                            System.out.println("Request sent!");
                            break;
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

    public void tradeItem(BufferedReader br){
        for (int i = 0; i < ((Trader) curr).getWishList().size(); i++) {
            System.out.println("    " + (i + 1) + ". " + ((Trader) curr).getWishList().get(i));
        }
        String tradePrompt = "Select the item from your wishlist that you would like to trade with a user, " +
                "or type 'exit' to go back: ";
        System.out.println(tradePrompt);

        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input) >= 1 &&
                        Integer.parseInt(input) <= ((Trader) curr).getWishList().size())){
                    Item item = ((Trader) curr).getWishList().get(Integer.parseInt(input) - 1);
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

    public void selectTrader(Item item, ArrayList<User> userWithItem, BufferedReader br){
        for (int i = 0; i < userWithItem.size(); i++){
            if (!((Trader) userWithItem.get(i)).isFrozen()){
                System.out.println("    " + (i + 1) + ". " +
                        uc.findUserWithItem(item).get(i).getUsername());
            }
        }
        String selectPrompt = "Enter the number associated with the user you would like to trade with.\n" +
                "If no users are listed above, then no user currently has your item, or the user(s) that " +
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

    public boolean checkCompatibility(User other){
        for (int i = 0; i < ((Trader)curr).getInventory().size(); i++){
            if (((Trader) other).getWishList().contains(((Trader)curr).getInventory().get(i))){
                return true;
            }
        }
        return false;
    }

    public void selectItem(BufferedReader br, User other, Item theirItem){
        ArrayList<Item> tradableItems = new ArrayList<Item>();
        for (int i = 0; i < ((Trader) other).getWishList().size(); i++){
            if (((Trader) curr).getInventory().contains(((Trader) other).getWishList().get(i))){
                System.out.println("    " + (i + 1) + ". " + ((Trader) other).getWishList().get(i));
                tradableItems.add(((Trader) other).getWishList().get(i));
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
                        tm.sendTWTradeRequest(((Trader) curr), ((Trader) other), yourItem, theirItem, true);
                        System.out.println("Request sent!");
                        break;
                    }
                    else if(input.equals("N")){
                        tm.sendTWTradeRequest(((Trader) curr), ((Trader) other), yourItem, theirItem, false);
                        System.out.println("Request sent!");
                        break;
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

    public void viewTR(BufferedReader br){
        System.out.println("Unaccepted trade offers from other users: ");
        for (int i = 0; i < curr.getInbox().getUnacceptedTrades().size(); i++){
            System.out.println("    " + (i + 1) + ". " + curr.getInbox().getUnacceptedTrades().get(i));
        }
        System.out.println("Enter the number associated with the trade offer that you would like to accept/reject," +
                "or type 'exit' to go back:");
        try{
            String input = br.readLine();
            while (!input.equals("exit")){
                if (isInteger(input) && Integer.parseInt((input)) >= 1 &&
                        Integer.parseInt(input) <= curr.getInbox().getUnacceptedTrades().size()){
                    int index = Integer.parseInt(input) - 1;
                    Trade trade = curr.getInbox().getUnacceptedTrades().get(index);
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
    public boolean isInteger(String strNum) {
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
