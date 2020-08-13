//package phase1;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * <h1>Main Menu</h1>
// * <p>The first menu that a user sees upon starting the program.</p>
// */
//public class TradingSystem {
//
//    private UserSerialization us;
//    private UserCatalogue uc;
//    private String menuOptions;
//    private UserFactory uf;
//
//    /**
//     * Has an instance of UserSerialization (for saving) and UserCatalogue (for tracking all user data).
//     */
//    public TradingSystem() throws Exception {
//        us = new UserSerialization();
//        uc = new UserCatalogue(us.deserialize());
//        menuOptions = "To perform an action, type the corresponding number.\n1. Login\n2. Register\n3. Guest\n" +
//                "To exit, type 'exit'.";
//        uf = new UserFactory();
//    }
//
//    /**
//     * Presents the user with the option to either login or register an account. User can type 'exit' toe xit the
//     * program.
//     */
//    public void run() {
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//
//        System.out.println(menuOptions);
//        try {
//            String input = br.readLine();
//            while (!input.equals("exit")){
//                if (input.equals("1")){
//                    login(br);
//                    System.out.println(menuOptions);
//                }
//                else if (input.equals("2")){
//                    register(br);
//                    System.out.println(menuOptions);
//                }
//                else if (input.equals("3")){
//                    guestMode(br);
//                    System.out.println(menuOptions);
//                }
//                input = br.readLine();
//            }
//            us.toSerialize(uc.userBase);
//        } catch (Exception e) {
//            System.out.println("Something went wrong.");
//            e.printStackTrace();
//        }
//    }
//
//    //Enters guest mode
//    private void guestMode(BufferedReader br){
//
//        try{
//            System.out.println("Press any key to access the Trade Catalogue.\n"+"To go back, type 'exit'.");
//            String input = br.readLine();
//            if (!input.equals("exit")){
//                GuestOptions guest = new GuestOptions(uc, us);
//                guest.run();
////                input = br.readLine();
//            }
//        }
//        catch (IOException e){
//            System.out.println("Something went wrong.");
//            e.printStackTrace();
//        }
//    }
//
//    // Takes user input, and checks it against the user catalogue, then logs the user in if the user exists and the pw
//    // is correct.
//    private void login(BufferedReader br){
//        try{
//            System.out.println("Please enter your Username: ");
//            String input = br.readLine();
//            while(!input.equals("exit")) {
//                if (uc.inUserBase(input)) {
//                    User currUser = uc.getUserByName(input);
//                    System.out.println("Please enter your password: ");
//                    input = br.readLine();
//                    if (!input.equals("exit")) {
//                        checkPW(currUser, input, br);
//                        break;
//                    }
//                    else {// input.equals("exit")
//                        System.out.println("Please enter your Username: ");
//                    }
//                }
//                else if (!uc.inUserBase(input)){
//                    System.out.println("User not found, please try again.");
//                }
//                input = br.readLine();
//            }
//        }
//        catch (IOException e){
//            System.out.println("Something went wrong.");
//            e.printStackTrace();
//        }
//    }
//
//    // Checks the user's password, and if it is correct, directs the user to the next menu (TraderOptions).
//    private void checkPW(User currUser, String input, BufferedReader br){
//        try{
//            while ((!input.equals(currUser.getPassword())) && (!input.equals("exit"))){
//                System.out.println("Incorrect password, please try again or type 'exit' to return to the main menu.");
//                input = br.readLine();
//            }
//            if (input.equals(currUser.getPassword())){ // Password entered correctly
//                if (currUser.getType().equals("trader")) {
//                    TraderOptions opt = new TraderOptions((Trader)currUser, uc, us);
//                    opt.run();
//                }
//                else { //currUser.getType().equals("admin")
//                    AdminOptions opa = new AdminOptions(currUser, uc, us);
//                    opa.run();
//                }
//            }
//            // else input.equals("exit"), so do nothing which ends method and returns to main menu
//        }
//        catch (IOException e){
//            System.out.println("Something went wrong");
//        }
//    }
//
//    // Prompts the user for a username, and checks whether that user already exists.
//    // If the user does not exist, then creates a user via NewUser
//    private void register(BufferedReader br){
//        try{
//            System.out.println("What would you like your username to be?");
//            String input = br.readLine();
//            while (!input.equals("exit")) {
//                if (!uc.inUserBase(input)) {
//                    try {
//                        uc = uf.newTrader(br, input, us);
//                    } catch (Exception e) {
//                        System.out.println("Something went wrong");
//                        e.printStackTrace();
//                    }
//                    break;
//                }
//                else if (uc.inUserBase(input)){
//                    System.out.println("A user with that username is already registered.\nPlease enter a different" +
//                            "username");
//                    input = br.readLine();
//                }
//            }
//        }
//        catch (IOException e){
//            System.out.println("Something went wrong");
//        }
//    }
//
//    // Called once verified that user with username doesn't already exist.
//    // Creates a new user with the specified username, and the first name & password that the user inputs.
////    public void newUser(BufferedReader br, String username){
////        //Need to get name and password for user
////        try{
////            System.out.println("Please enter your first name: ");
////            String input = br.readLine();
////            if (!input.equals("exit")){
////                String name = input;
////                System.out.println("Please enter a password: ");
////                input = br.readLine();
////                if(!input.equals("exit")){
////                    String password = input;
//////                    String type = "trader"; // Can't register an admin this way, so must be a trader
////                    System.out.println("Please enter your city: ");
////                    input = br.readLine();
////                    String location = input;
////                    List<TradeMessage> trades = new ArrayList<TradeMessage>();
////                    List<Message> traderNotifs = new ArrayList<Message>();
////                    List<Message>adminNotifs = new ArrayList<Message>();
////                    phase1.TraderInbox inbox = new phase1.TraderInbox(trades, traderNotifs, adminNotifs);
////                    phase1.Inventory inventory = new Inventory(new ArrayList<Item>(), "inventory");
////                    phase1.TraderStatus traderStatus = new TraderStatus();
////                    User user = new Trader(username, password, inbox, inventory, inventory, name,
////                            traderStatus, location);
////                    inbox.setOwner(user);
////                    uc.userBase.add(user);
////                    us.toSerialize(uc.userBase);
////                    System.out.println("User created!");
////                    us.toSerialize(uc.userBase);
////                }
////            }
////        }
////        catch (Exception e){
////            System.out.println("Something went wrong");
////        }
////    }
//}