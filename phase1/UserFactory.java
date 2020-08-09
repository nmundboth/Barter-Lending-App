package phase1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserFactory {

    public UserFactory(){
    }

    public UserSerialization newTrader(BufferedReader br, String username, UserSerialization us) throws Exception{
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
        return us;
    }

    public UserSerialization newAdmin(BufferedReader br, String username, UserSerialization us) throws Exception{
        UserCatalogue uc = new UserCatalogue(us.deserialize());
        //Need to get name and password for user
        try{
            System.out.println("Please enter a password: ");
            String input = br.readLine();
            if(!input.equals("exit")){
                String password = input;
                List<Message> traderNotifs = new ArrayList<Message>();
                List<Message>adminNotifs = new ArrayList<Message>();
                phase1.AdminInbox inbox = new phase1.AdminInbox(traderNotifs, adminNotifs);
                User user = new Admin(username, password, inbox);
                inbox.setOwner(user);
                uc.userBase.add(user);
                us.toSerialize(uc.userBase);
                System.out.println("Admin user created!");
                us.toSerialize(uc.userBase);
            }
        }
        catch (Exception e){
            System.out.println("Something went wrong");
        }
        return us;
    }
}
