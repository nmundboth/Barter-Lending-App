package phase1;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AcceptedTradeOptions {

    private User curr;
    private UserCatalogue uc;
    private UserSerialization us;
    private MeetingManager mm;
    private String menuOptions;

    public AcceptedTradeOptions(User curr, UserCatalogue uc, UserSerialization us){
        this.curr = curr;
        this.uc = uc;
        this.us = us;
        menuOptions = "To perform an action, type the corresponding number.\n1. Propose a meeting " +
                "for an unscheduled trade\n2. Edit/confirm proposed meetings\n3. Confirm that a meeting has taken" +
                " place\n4. Request to cancel a trade\nTo return to inbox, type 'exit'.";
        mm = new MeetingManager();
    }

    public void run(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        List<Trade> acceptedTrades = curr.getInbox().getTrades();
        System.out.println("Accepted Trades: ");
        for (int i = 0; i < acceptedTrades.size(); i++){
            System.out.println("    " + (i + 1) + ". " + acceptedTrades.get(i));
        }

        System.out.println("\n" + menuOptions);

        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (input.equals("1")){
                    proposeMeeting(br, acceptedTrades);
                    System.out.println(menuOptions);
                }
                else if (input.equals("2")){

                }
                else if (input.equals("3")){

                }
                else if (input.equals("4")){

                }

                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    public void proposeMeeting(BufferedReader br, List<Trade> acceptedTrades){
        ArrayList<Trade> unproposed = new ArrayList<Trade>();
        for (Trade trade: acceptedTrades){
            if (trade.getMeeting().isEmpty() && !(trade.isOpen())){
                unproposed.add(trade);
            }
        }
        for (int i = 0; i < unproposed.size(); i++){
            System.out.println("    " + (i + 1) + ". " + unproposed.get(i));
        }
        System.out.println("Enter the number corresponding to the trade you would like to propose a meeting for.");

        try{
            String input = br.readLine();
            while(!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input)) >= 1 && Integer.parseInt(input) <= unproposed.size()){
                    int index = Integer.parseInt(input) - 1;
                    Trade trade = unproposed.get(index);
                    System.out.println("Enter the location you would like to meet: ");
                    input = br.readLine();
                    if (!input.equals("exit")){
                        String location = input;
                        System.out.println("Enter the date you would like to meet (YYYY-MM-DD): ");
                        input = br.readLine();
                        if(!input.equals("exit")){
                            String date = input;
                            System.out.println("Enter the time you would like to meet (24h clock, xx:xx)");
                            input = br.readLine();
                            if(!input.equals("exit")){
                                String time = input;
                                mm.proposeMeeting(((Trader) curr), trade, location, date, time);
                            }
                        }
                    }
                }
                System.out.println("Enter the number corresponding to the trade you would like to " +
                        "propose a meeting for.");
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
