package phase1;

//TODO: Admin Options should include:
// 1. View flagged traders
// 2. View unconfirmed items
// 3. View inbox (right now is only unfreeze requests)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AdminOptions {

    private User curr;
    private UserCatalogue uc;
    private UserSerialization us;


    public AdminOptions(User curr, UserCatalogue uc, UserSerialization us){
        this.curr = curr;
        this.uc = uc;
        this.us = us;
    }

    public void run(){

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("To perform an action, type the corresponding number.\n1. View flagged traders\n" +
                "2. View unconfirmed items\n3. View inbox\n" + "To logout, type 'logout'.");
        try{
            String input = br.readLine();
            while(!input.equals("logout")){
                if (input.equals("1")){

                }
                else if (input.equals("2")){
                    viewUnconfirmed(br);
                    break;
                }
                else if (input.equals("3")){

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
                if (isNumeric(input) && (Integer.parseInt(input) >= 1 &&
                        Integer.parseInt(input) <= unconfirmed.size())){
                    unconfirmed.get((Integer.parseInt(input) - 1)).setConfirm(); // Could use a use case class for admin
                    System.out.println("Item confirmed!");
                    us.toSerialize(uc.userBase);
                    break;
                }
                input = br.readLine();
            }
            run();
        }
        catch (Exception e){
            System.out.println("Something went wrong.");
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
