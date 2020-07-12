package phase1;

//TODO: Options should include:
// 1. Send trade/borrow/lend request
// 2. View accepted trades --> view open trades | view unscheduled trades --> Propose a meeting for an unscheduled trade
// 3. View trade notifications
// 4. View system (admin) notifications
// 5. View trade requests from other users --> Accept a trade | Reject a trade
// 6. Request to cancel a trade
// 7. Confirm that a transaction (meeting) has happened

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InboxOptions {

    private User curr;
    private UserCatalogue uc;
    private UserSerialization us;

    public InboxOptions(User curr, UserCatalogue uc, UserSerialization us){
        this.curr = curr;
        this.uc = uc;
        this.us = us;
    }

    public void run(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("To perform an action, type the corresponding number.\n1. Send transaction request\n" +
                "2. View accepted trades\n3. View trade notifications (" + curr.getInbox().getTradersUnread() + ")\n" +
                "4. View system notifications (" + curr.getInbox().getAdmiNotiUnread() +
                ")\n5. View trade requests (" + curr.getInbox().getUnaccptedUnread() +
                ")\nTo return to options menu, type 'exit'.");

        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (input.equals("1")){
                    sendTrnsxnRequest(br);
                    break;
                }
                else if (input.equals("2")){

                }
                else if(input.equals("3")){

                }
                else if (input.equals("4")){

                }
                else if (input.equals("5")){

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
        System.out.println("To perform an action, type the corresponding number.");
        System.out.println("1. Request to borrow an item from a user\n2. Request to lend an item to a user\n" +
                "3. Request to trade an item with a user.\nType 'exit' to go back to your inbox menu.");
        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (input.equals("1")){
                    System.out.println("Select the item from your wishlist that" +
                            " you would like to borrow from a user, or type 'exit' to go back: ");
                    for (int i = 0; i < ((Trader) curr).getWishList().size(); i++){
                        System.out.println("    " + (i + 1) + ". " + ((Trader) curr).getWishList().get(i));
                    }
                    input = br.readLine();
                    while (!input.equals("exit")){
                        if (isNumeric(input) && (Integer.parseInt(input) >= 1 &&
                                Integer.parseInt(input) <= ((Trader) curr).getWishList().size())){
                            Item item = ((Trader) curr).getWishList().get(Integer.parseInt(input));
                        }
                    }

                }
            }
        }
        catch (IOException e){

        }
    }

    // Template taken from
    // https://www.baeldung.com/java-check-string-number
    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
