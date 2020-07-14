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
    private TradeManager tm;

    public AcceptedTradeOptions(User curr, UserCatalogue uc, UserSerialization us){
        this.curr = curr;
        this.uc = uc;
        this.us = us;
        menuOptions = "To perform an action, type the corresponding number.\n1. Propose a meeting " +
                "for an unscheduled trade\n2. View and edit/accept proposed meetings\n" +
                "3. Confirm that a meeting has taken place\n4. Request to cancel a trade\n" +
                "To return to inbox, type 'exit'.";
        mm = new MeetingManager();
        tm = new TradeManager();
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
                    viewMeetings(br, acceptedTrades);
                    System.out.println(menuOptions);
                }
                else if (input.equals("3")){
                    confirmMeeting(br, acceptedTrades);
                    System.out.println(menuOptions);
                }
                else if (input.equals("4")){
                    cancelTrade(br, acceptedTrades);
                    System.out.println(menuOptions);
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

    public void viewMeetings(BufferedReader br, List<Trade> acceptedTrades){
        ArrayList<Trade> proposed = new ArrayList<Trade>();
        for (Trade trade: acceptedTrades){
            if(!trade.getMeeting().isEmpty() && !(trade.isOpen())){
                proposed.add(trade);
            }
        }
        for (int i = 0; i < proposed.size(); i++){
            System.out.println("    " + (i + 1) + ". Trade - " + proposed.get(i));
            System.out.println("       Meeting - " + proposed.get(i).getMeeting());
        }

        System.out.println("Enter the number of the trade for which you would like to edit/accept the meeting.");
        try{
            String input = br.readLine();
            while (!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input)) >= 1 && Integer.parseInt(input) <= proposed.size()){
                    int index = Integer.parseInt(input) - 1;
                    Trade trade = proposed.get(index);
                    System.out.println("Would you like to 'edit' or 'accept' this meeting?");
                    input = br.readLine();
                    if (!input.equals("exit")){
                        if (input.equals("edit")){
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
                        else if (input.equals("accept") &&
                                !(((Trader) curr).equals(trade.getMeeting().getProposedBy()))){
                            if (trade instanceof TwoWayTrade){
                                mm.confirmMeet(((Trader) curr), ((TwoWayTrade) trade));
                            }
                            else if (trade instanceof OneWayTrade){
                                mm.confirmMeet(((Trader) curr), ((OneWayTrade) trade));
                            }
                        }
                        else if (input.equals("accept") && ((Trader) curr).equals(trade.getMeeting().getProposedBy())){
                            System.out.println("You can not confirm a meeting that you proposed!");
                        }
                    }
                }
                System.out.println("Enter the number of the trade for which you would " +
                        "like to edit/accept the meeting.");
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    public void confirmMeeting(BufferedReader br, List<Trade> acceptedTrades){
        ArrayList<Trade> openTrades = new ArrayList<Trade>();
        for (Trade trade: acceptedTrades){
            if (trade.isOpen()){
                openTrades.add(trade);
            }
        }
        for (int i = 0; i < openTrades.size(); i++){
            System.out.println("    " + (i + 1) + ". Trade - " + openTrades.get(i));
            System.out.println("       Meeting - " + openTrades.get(i).getMeeting());
        }
        System.out.println("Enter the number of the trade for which you would like to confirm that the meeting has" +
                " happened.");
        try {
            String input = br.readLine();
            while (!input.equals("exit")) {
                if (isInteger(input) && (Integer.parseInt(input)) >= 1 &&
                        Integer.parseInt(input) <= openTrades.size()) {
                    int index = Integer.parseInt(input) - 1;
                    Trade trade = openTrades.get(index);
                    tm.confirmTrade(((Trader) curr), trade);
                    System.out.println("You have confirmed the trade!");
                }
                input = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    public void cancelTrade(BufferedReader br, List<Trade> acceptedTrades){
        ArrayList<Trade> unscheduled = new ArrayList<Trade>();
        for (Trade trade: acceptedTrades){
            if (!trade.isOpen()){
                unscheduled.add(trade);
            }
        }
        for (int i = 0; i < acceptedTrades.size(); i++){
            System.out.println("    " + (i + 1) + ". Trade - " + unscheduled.get(i));
            System.out.println("       Meeting - " + unscheduled.get(i).getMeeting());
        }
        System.out.println("Enter the number of the trade that you would like to request to cancel." +
                " If both traders have requested to cancel, the trade will be canceled.");

        try{
            String input = br.readLine();
            while (!input.equals("exit")){
                if (isInteger(input) && (Integer.parseInt(input)) >= 1 &&
                        Integer.parseInt(input) <= unscheduled.size()) {
                    int index = Integer.parseInt(input) - 1;
                    Trade trade = unscheduled.get(index);
                    tm.cancelTrade(((Trader) curr), trade);
                    System.out.println("You have requested to cancel the trade!");
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
