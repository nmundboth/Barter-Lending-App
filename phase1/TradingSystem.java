package phase1;

//import UserSerialization;
//import User;
//import Inbox;
//import Trade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TradingSystem {

    private UserSerialization us;
    private UserCatalogue uc;
    private String menuOptions;

    public TradingSystem() throws Exception {
        us = new UserSerialization();
        uc = new UserCatalogue(us.deserialize());
        menuOptions = "To perform an action, type the corresponding number.\n1. Login\n2. Register\n" +
                "To exit, type 'exit'.";
    }

    public void run() {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        System.out.println(menuOptions);
        try {
            String input = br.readLine();
            while (!input.equals("exit")){
                if (input.equals("1")){
                    login(br);
                    System.out.println(menuOptions);
                }
                else if (input.equals("2")){
                    register(br);
                    System.out.println(menuOptions);
                }
                input = br.readLine();
            }
            us.toSerialize(uc.userBase);
        } catch (Exception e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }

    public void login(BufferedReader br){
        try{
            System.out.println("Please enter your Username: ");
            String input = br.readLine();
            while(!input.equals("exit")) {
                if (uc.inUserBase(input)) {
                    User currUser = uc.getUserByName(input);
                    System.out.println("Please enter your password: ");
                    input = br.readLine();
                    if (!input.equals("exit")) {
                        checkPW(currUser, input, br);
                        break;
                    }
                    else {// input.equals("exit")
                        System.out.println("Please enter your Username: ");
                    }
                }
                else if (!uc.inUserBase(input)){
                    System.out.println("User not found, please try again.");
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }

    public void checkPW(User currUser, String input, BufferedReader br){
        try{
            while ((!input.equals(currUser.getPassword())) && (!input.equals("exit"))){
                System.out.println("Incorrect password, please try again or type 'exit' to return to the main menu.");
                input = br.readLine();
            }
            if (input.equals(currUser.getPassword())){ // Password entered correctly
                if (currUser.getType().equals("trader")) {
                    TraderOptions opt = new TraderOptions(currUser, uc, us);
                    opt.run();
                }
                else { //currUser.getType().equals("admin")
                    AdminOptions opa = new AdminOptions(currUser, uc, us);
                    opa.run();
                }
            }
            // else input.equals("exit"), so do nothing which ends method and returns to main menu
        }
        catch (IOException e){
            System.out.println("Something went wrong");
        }
    }

    public void register(BufferedReader br){
        try{
            System.out.println("What would you like your username to be?");
            String input = br.readLine();
            while (!input.equals("exit")) {
                if (!uc.inUserBase(input)) {
                    newUser(br, input);
                    break;
                }
                else if (uc.inUserBase(input)){
                    System.out.println("A user with that username is already registered.\nPlease enter a different" +
                            "username");
                    input = br.readLine();
                }
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong");
        }
    }

    // Called once verified that user with username doesn't already exist
    public void newUser(BufferedReader br, String username){
        //Need to get name and password for user
        try{
            System.out.println("Please enter your first name: ");
            String input = br.readLine();
            if (!input.equals("exit")){
                String name = input;
                System.out.println("Please enter a password: ");
                input = br.readLine();
                if(!input.equals("exit")){
                    String password = input;
                    String type = "trader"; // Can't register an admin this way, so must be a trader
                    List<Trade> trades = new ArrayList<Trade>();
                    List<String> traderNotifs = new ArrayList<String>();
                    List<String>adminNotifs = new ArrayList<String>();
                    TraderInbox inbox = new TraderInbox(trades, traderNotifs, adminNotifs);
                    List<Item> inventory = new ArrayList<Item>();
                    User user = new Trader(username, password, type, inbox, inventory, name);
                    uc.userBase.add(user);
                    us.toSerialize(uc.userBase);
                    System.out.println("User created!");
                    us.toSerialize(uc.userBase);
                }
            }
        }
        catch (Exception e){
            System.out.println("Something went wrong");
        }
    }
}