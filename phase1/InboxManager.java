package phase1;

import java.util.Scanner;

public class InboxManager {
    public static void main(String[] args, Inbox inbox) {
        if (inbox instanceof AdminInbox) {
            Scanner inboxInfo = new Scanner(System.in);
            System.out.println("Choose one of the following sub-inboxes:\n 1) Unfreeze Requests\n " +
                    "2) Admin Notification\n 3) Trader Notification");
            String subInboxNum = inboxInfo.nextLine();
            switch (subInboxNum) {
                case "1": {
                    int index = 0;
                    for (Trader i : ((AdminInbox) inbox).getUndoFrozen()) {
                        System.out.println(index + ")." + i.getName() + "\n");
                        index += 1;
                    }
                    System.out.println("Choose a Trader to Unfreeze:");
                    String messageNum = inboxInfo.nextLine();
                    ((AdminInbox) inbox).showUndoFrozen(Integer.parseInt(messageNum));
                    break;
                }
                case "2": {
                    int index = 0;
                    for (String i : inbox.getAdmiNoti()) {
                        System.out.println(index);
                        index += 1;
                    }
                    System.out.println("Choose an message to read:");
                    String messageNum = inboxInfo.nextLine();
                    ((AdminInbox) inbox).showAdminNoti(Integer.parseInt(messageNum));
                    break;
                }
                case "3": {
                    int index = 0;
                    for (String i : inbox.getTraderNoti()) {
                        System.out.println(index);
                        index += 1;
                    }
                    System.out.println("Choose an message to read:");
                    String messageNum = inboxInfo.nextLine();
                    ((AdminInbox) inbox).showTraderNoti(Integer.parseInt(messageNum));

                    break;
                }
                default:
                    System.out.println("Invalid Response");
                    break;
            }

        }
        else{
            Scanner inboxInfo = new Scanner(System.in);
            System.out.println("Choose one of the following sub-inboxes\n 1) Available Trades\n " +
                    "2) Trader Notifications\n 3) Admin Notification\n 4) Unaccepted Offers" );
            String subInboxNum = inboxInfo.nextLine();
            switch (subInboxNum){
                case "1":{
                    System.out.println("You have " + inbox.getTradersUnread() +" messages");
                    int index = 0;
                    for (Trade i : inbox.getTrades()){
                        System.out.println(index);
                        index += 1;
                    }
                    System.out.println("Choose a message to read:");
                    String messageNum = inboxInfo.nextLine();
                    inbox.getTrade(index);
                }
                //incomplete
            }
        }
    }
}
