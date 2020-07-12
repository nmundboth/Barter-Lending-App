package phase1;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TraderOptions {

    private User curr;
    private UserCatalogue uc;

    public TraderOptions(User curr, UserCatalogue uc){
        this.curr = curr;
        this.uc = uc;
    }

    public void run(){
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//        System.out.println("To perform an action, type the corresponding number.\n1. View Inbox\n" +
//                "2. Add an item to your inventory\n3. Add an item to your wishlist (browse inventory)\n" +
//                "4. Propose a Transaction\n5. View recently traded items\n6. View frequent trading partners\n" +
//                "To logout, type 'logout'.");
//        try{
//            String input = br.readLine();
//            while(!input.equals("logout")){
//                if (input.equals("1")){
//                    InboxOptions iopt = new InboxOptions(curr.getInbox());
//                    iopt.run();
//                }
//            }
//        }
    }
}
