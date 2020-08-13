package phase1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>User Factory</h1>
 * <p>The UserFactory class creates instances of different types of users.</p>
 */
public class UserFactory {

    public UserFactory(){
    }

    /**
     * Creates a new instance of a Trader User.
     * @param br Trader sending the request.
     * @param username Trader receiving the request.
     * @param us Item offered by ogTrader in transaction.
     */
    public UserCatalogue newTrader(BufferedReader br, String username, UserSerialization us) throws Exception{
        UserCatalogue uc = new UserCatalogue(us.deserialize());
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
//                    String type = "trader"; // Can't register an admin this way, so must be a trader
                    System.out.println("Please enter your city: ");
                    input = br.readLine();
                    String location = input;
                    List<TradeMessage> trades = new ArrayList<TradeMessage>();
                    List<Message> traderNotifs = new ArrayList<Message>();
                    List<Message>adminNotifs = new ArrayList<Message>();
                    phase1.TraderInbox inbox = new phase1.TraderInbox(trades, traderNotifs, adminNotifs);
                    phase1.Inventory inventory = new Inventory(new ArrayList<Item>(), "inventory");
                    phase1.TraderStatus traderStatus = new TraderStatus();
                    User user = new Trader(username, password, inbox, inventory, inventory, name,
                            traderStatus, location);
                    inbox.setOwner(user);
                    uc.userBase.add(user);
                    us.toSerialize(uc.userBase);
                    System.out.println("Trader user created!");
                    us.toSerialize(uc.userBase);
                }
            }
        }
        catch (Exception e){
            System.out.println("Something went wrong");
        }
        return uc;
    }

    /**
     * Creates a new instance of an Admin User.
     * @param br Trader sending the request.
     * @param username Trader receiving the request.
     * @param us Item offered by ogTrader in transaction.
     */
    public UserCatalogue newAdmin(BufferedReader br, String username, UserSerialization us) throws Exception{
        UserCatalogue uc = new UserCatalogue(us.deserialize());
        //Need to get name and password for user
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
            System.out.println("Please enter a password: ");
            String input = br.readLine();
            if(!input.equals("exit")){
                String password = input;
                User user = new Admin(username, password, adminInbox);
                uc.userBase.add(user);
                us.toSerialize(uc.userBase);
                System.out.println("Admin user created!");
                us.toSerialize(uc.userBase);
            }
        }
        catch (Exception e){
            System.out.println("Something went wrong");
        }
        return uc;
    }
}
