package phase1;

//TODO: Admin Options should include:
// 1. View flagged traders
// 2. View unconfirmed items
// 3. View inbox (right now is only unfreeze requests)
// 4. Change borrow limit

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AdminOptions {

    private User curr;
    private UserCatalogue uc;
    private UserSerialization us;
    private String menuOptions;


    public AdminOptions(User curr, UserCatalogue uc, UserSerialization us){
        this.curr = curr;
        this.uc = uc;
        this.us = us;
        menuOptions = "To perform an action, type the corresponding number.\n1. View flagged traders\n" +
                "2. View unconfirmed items\n3. View inbox\n4. Change how many times a user must lend " +
                "before they can borrow\n" + "To logout, type 'logout'.";
    }

    public void run(){

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("To perform an action, type the corresponding number.\n1. View flagged traders\n" +
                "2. View unconfirmed items\n3. View inbox\n" +
                "4. Change how many times a user must lend before they can borrow\n" + "To logout, type 'logout'.");
        try{
            String input = br.readLine();
            while(!input.equals("logout")){
                if (input.equals("1")){

                }
                else if (input.equals("2")){
                    viewUnconfirmed(br);
                    System.out.println(menuOptions);
                }
                else if (input.equals("3")){

                }
                else if (input.equals ("4")){
                    System.out.println("Enter the new value: ");
                    input = br.readLine();
                    while (!input.equals("exit")){
                        if (isInteger(input)){
                            Trader.greedLimit = -(Integer.parseInt(input));
                            System.out.println("Limit set!\n");
                            break;
                        }
                        input = br.readLine();
                    }
                    System.out.println(menuOptions);
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    public void viewUnconfirmed(BufferedReader br){
        ArrayList<Item> unconfirmed = uc.findUnconfirmed();
        for (int i = 0; i < unconfirmed.size(); i++){
            System.out.println((i + 1) + ". " + unconfirmed.get(i) + " - " + unconfirmed.get(i).getDescrip() + "\n");
        }
        System.out.println("If you would like to confirm an item, enter the number associated with that item.\n" +
                "To go back, type 'exit'.");
        try{
            String input = br.readLine();
            while (!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input) >= 1 &&
                        Integer.parseInt(input) <= unconfirmed.size())){
                    ((Admin) curr).confirmItem(unconfirmed.get((Integer.parseInt(input) - 1))); // Could use a use case class for admin
                    System.out.println("Item confirmed!");
                    us.toSerialize(uc.userBase);
                    break;
                }
                input = br.readLine();
            }
        }
        catch (Exception e){
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
