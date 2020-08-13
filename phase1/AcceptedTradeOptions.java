//package phase1;
//
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * <h1>Options for Accepted Trades</h1>
// * <p>AcceptedTradeOptions contains options that traders should be able to perform only on trades that have been
// * accepted.</p>
// */
//public class AcceptedTradeOptions {
//
//    private Trader curr;
//    private UserCatalogue uc;
//    private UserSerialization us;
//    private MeetingManager mm;
//    private String menuOptions;
//    private TradeManager tm;
//
//    /**
//     * @param curr the trader that is currently logged in
//     * @param uc the UserCatalogue that contains all of the currently registered users
//     * @param us UserSerialization used for saving
//     */
//    protected AcceptedTradeOptions(Trader curr, UserCatalogue uc, UserSerialization us){
//        this.curr = curr;
//        this.uc = uc;
//        this.us = us;
//        menuOptions = "To perform an action, type the corresponding number.\n1. Propose a meeting " +
//                "for an unscheduled trade\n2. View and edit/accept proposed meetings\n" +
//                "3. Confirm that a meeting has taken place\n4. Request to cancel a trade\n" +
//                "To return to inbox, type 'exit'.";
//        mm = new MeetingManager();
//        tm = new TradeManager();
//    }
//
//    /**
//     * Presents the user with a list of actions to choose from to perform on a trade which they are involved in that has
//     * been accepted. Includes proposing meetings, viewing/editing/accepting meetings, confirming that meeting(s) have
//     * taken place, and requesting to cancel a trade.
//     */
//    public void run(){
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//        TraderInbox traderInbox = (TraderInbox) curr.getInbox();
//        List<TradeMessage> acceptedTrades = traderInbox.getTrades();
//        //List<TradeMessage> acceptedTrades = curr.getInbox().getTrades();
//        System.out.println("Accepted Trades: ");
//        for (int i = 0; i < acceptedTrades.size(); i++){
//            System.out.println("    " + (i + 1) + ". " + acceptedTrades.get(i).getTrade().getOgTrader().getName());
//        }
//
//        System.out.println("\n" + menuOptions);
//
//        try{
//            String input = br.readLine();
//            while(!input.equals("exit")){
//                if (input.equals("1")){
//                    proposeMeeting(br, acceptedTrades);
//                    System.out.println(menuOptions);
//                }
//                else if (input.equals("2")){
//                    viewMeetings(br, acceptedTrades);
//                    System.out.println(menuOptions);
//                }
//                else if (input.equals("3")){
//                    confirmMeeting(br, acceptedTrades);
//                    System.out.println(menuOptions);
//                }
//                else if (input.equals("4")){
//                    cancelTrade(br, acceptedTrades);
//                    System.out.println(menuOptions);
//                }
//
//                input = br.readLine();
//            }
//        }
//        catch (IOException e){
//            System.out.println("Something went wrong.");
//        }
//    }
//
//    // Allows a trader to propose a meeting for a trade that they have accepted, which does not currently contain a
//    // meeting (meeting is empty)
//    private void proposeMeeting(BufferedReader br, List<TradeMessage> acceptedTrades){
//        ArrayList<TradeMessage> unproposed = new ArrayList<TradeMessage>();
//        for (TradeMessage trade: acceptedTrades){
//            if (trade.getTrade().getMeeting().isEmpty() && !(trade.getTrade().isOpen()) &&
//                    !trade.getTrade().isNoMeet()){//If a meeting is required
//                unproposed.add(trade);
//            }
//        }
//        if (unproposed.size() == 0){
//            System.out.println("You have no unproposed meetings. To go back, type 'exit'.");
//        }
//        else {
//            for (int i = 0; i < unproposed.size(); i++){
//                System.out.println("    " + (i + 1) + ". " + unproposed.get(i).getContent());
//            }
//            System.out.println("Enter the number corresponding to the trade you would like to propose a meeting for.");
//        }
//
//
//        try{
//            String input = br.readLine();
//            while(!input.equals("exit")){
//                if (isInteger(input) && (Integer.parseInt(input)) >= 1 && Integer.parseInt(input) <= unproposed.size()){
//                    int index = Integer.parseInt(input) - 1;
//                    TradeMessage trade = unproposed.get(index);
//                    System.out.println("Enter the location you would like to meet: ");
//                    input = br.readLine();
//                    if (!input.equals("exit")){
//                        String location = input;
//                        System.out.println("Enter the date you would like to meet (YYYY-MM-DD): ");
//                        input = br.readLine();
//                        if(!input.equals("exit")){
//                            String date = input;
//                            System.out.println("Enter the time you would like to meet (24h clock, xx:xx)");
//                            input = br.readLine();
//                            if(!input.equals("exit")){
//                                String time = input;
//                                mm.proposeMeeting(((Trader) curr), trade.getTrade(), location, date, time);
//                                unproposed.clear();
//                            }
//                        }
//                    }
//                }
//                for (TradeMessage trade: acceptedTrades){
//                    if (trade.getTrade().getMeeting().isEmpty() && !(trade.getTrade().isOpen())){
//                        unproposed.add(trade);
//                    }
//                }
//                if (unproposed.size() == 0){
//                    System.out.println("You have no unproposed meetings. To go back, type 'exit'.");
//                }
//                else {
//                    for (int i = 0; i < unproposed.size(); i++){
//                        System.out.println("    " + (i + 1) + ". " + unproposed.get(i));
//                    }
//                    System.out.println("Enter the number corresponding to the trade you would like to propose a meeting for.");
//                }
//                input = br.readLine();
//            }
//        }
//        catch (IOException e){
//            System.out.println("Something went wrong.");
//        }
//    }
//
//    // Allows a trader to view the meetings for their trades that they have not yet accepted, and then edit a meeting
//    // or accept a meeting (can only accept a meeting if they are not the ones that proposed it). They also only have
//    // 3 edits each to a meeting, and proposing a meeting counts as an edit.
//    // Accepting a meeting sets the trade to open (incomplete).
//    private void viewMeetings(BufferedReader br, List<TradeMessage> acceptedTrades){
//        ArrayList<TradeMessage> proposed = new ArrayList<TradeMessage>();
//        for (TradeMessage trade: acceptedTrades){
//            if(!trade.getTrade().getMeeting().isEmpty() && !(trade.getTrade().isOpen())){
//                proposed.add(trade);
//            }
//        }
//        for (int i = 0; i < proposed.size(); i++){
//            System.out.println("    " + (i + 1) + ". Trade - " + proposed.get(i).getTrade());
//            System.out.println("       Meeting - " + proposed.get(i).getTrade().getMeeting());
//        }
//
//        System.out.println("Enter the number of the trade for which you would like to edit/accept the meeting.");
//        try{
//            String input = br.readLine();
//            while (!input.equals("exit")){
//                if (isInteger(input) && (Integer.parseInt(input)) >= 1 && Integer.parseInt(input) <= proposed.size()){
//                    int index = Integer.parseInt(input) - 1;
//                    TradeMessage trade = proposed.get(index);
//                    System.out.println("Would you like to 'edit' or 'accept' this meeting?");
//                    input = br.readLine();
//                    if (!input.equals("exit")){
//                        if (input.equals("edit")){
//                            System.out.println("Enter the location you would like to meet: ");
//                            input = br.readLine();
//                            if (!input.equals("exit")){
//                                String location = input;
//                                System.out.println("Enter the date you would like to meet (YYYY-MM-DD): ");
//                                input = br.readLine();
//                                if(!input.equals("exit")){
//                                    String date = input;
//                                    System.out.println("Enter the time you would like to meet (24h clock, xx:xx)");
//                                    input = br.readLine();
//                                    if(!input.equals("exit")){
//                                        String time = input;
//                                        mm.proposeMeeting(((Trader) curr), trade.getTrade(), location, date, time);
//                                    }
//                                }
//                            }
//                        }
//                        else if (input.equals("accept") &&
//                                !(((Trader) curr).equals(trade.getTrade().getMeeting().getProposedBy()))){
//                            if (trade.getTrade() instanceof TwoWayTrade){
//                                mm.confirmMeet(((Trader) curr), ((TwoWayTrade) trade.getTrade()));
//                            }
//                            else if (trade.getTrade() instanceof OneWayTrade){
//                                mm.confirmMeet(((Trader) curr), ((OneWayTrade) trade.getTrade()));
//                            }
//                        }
//                        else if (input.equals("accept") && ((Trader) curr).equals(trade.getTrade().getMeeting().getProposedBy())){
//                            System.out.println("You can not confirm a meeting that you proposed!");
//                        }
//                    }
//                }
//                System.out.println("Enter the number of the trade for which you would " +
//                        "like to edit/accept the meeting.");
//                input = br.readLine();
//            }
//        }
//        catch (IOException e){
//            System.out.println("Something went wrong.");
//        }
//    }
//
//    // Allows a user to confirm that a meeting (transaction) has happened in real life.
//    private void confirmMeeting(BufferedReader br, List<TradeMessage> acceptedTrades){
//        ArrayList<TradeMessage> openTrades = new ArrayList<TradeMessage>();
//        for (TradeMessage trade: acceptedTrades){
//            if (trade.getTrade().isOpen()){
//                openTrades.add(trade);
//            }
//        }
//        for (int i = 0; i < openTrades.size(); i++){
//            System.out.println("    " + (i + 1) + ". Trade - " + openTrades.get(i));
//            System.out.println("       Meeting - " + openTrades.get(i).getTrade().getMeeting());
//        }
//        System.out.println("Enter the number of the trade for which you would like to confirm that the meeting has" +
//                " happened.");
//        try {
//            String input = br.readLine();
//            while (!input.equals("exit")) {
//                if (isInteger(input) && (Integer.parseInt(input)) >= 1 &&
//                        Integer.parseInt(input) <= openTrades.size()) {
//                    int index = Integer.parseInt(input) - 1;
//                    TradeMessage trade = openTrades.get(index);
//                    tm.confirmTrade(((Trader) curr), trade.getTrade());
//                    System.out.println("You have confirmed the trade!");
//                }
//                input = br.readLine();
//            }
//        }
//        catch (IOException e){
//            System.out.println("Something went wrong.");
//        }
//    }
//
//    // Allows a trader to request to cancel a trade
//    private void cancelTrade(BufferedReader br, List<TradeMessage> acceptedTrades){
//        ArrayList<TradeMessage> unscheduled = new ArrayList<TradeMessage>();
//        for (TradeMessage trade: acceptedTrades){
//            if (!trade.getTrade().isOpen()){
//                unscheduled.add(trade);
//            }
//        }
//        for (int i = 0; i < acceptedTrades.size(); i++){
//            System.out.println("    " + (i + 1) + ". Trade - " + unscheduled.get(i));
//            System.out.println("       Meeting - " + unscheduled.get(i).getTrade().getMeeting());
//        }
//        System.out.println("Enter the number of the trade that you would like to request to cancel." +
//                " If both traders have requested to cancel, the trade will be canceled.");
//
//        try{
//            String input = br.readLine();
//            while (!input.equals("exit")){
//                if (isInteger(input) && (Integer.parseInt(input)) >= 1 &&
//                        Integer.parseInt(input) <= unscheduled.size()) {
//                    int index = Integer.parseInt(input) - 1;
//                    TradeMessage trade = unscheduled.get(index);
//                    tm.cancelTrade(((Trader) curr), trade.getTrade());
//                    System.out.println("You have requested to cancel the trade!");
//                }
//                input = br.readLine();
//            }
//
//        }
//        catch (IOException e){
//            System.out.println("Something went wrong.");
//        }
//
//    }
//
//    // Template taken from
//    // https://www.baeldung.com/java-check-string-number
//    // Checks whether a String is an integer.
//    private boolean isInteger(String strNum) {
//        if (strNum == null) {
//            return false;
//        }
//        try {
//            int i = Integer.parseInt(strNum);
//        } catch (NumberFormatException nfe) {
//            return false;
//        }
//        return true;
//    }
//}
