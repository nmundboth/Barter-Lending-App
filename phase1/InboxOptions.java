package phase1;

//TODO: Options should include:
// 1. View accepted trades --> view open trades | view unscheduled trades
// 2. View trade notifications
// 3. View system (admin) notifications
// 4. View trade requests from other users --> Accept a trade | Reject a trade
// 5. Propose a meeting for an unscheduled trade
// 6. Request to cancel a trade
// 7. Confirm that a transaction (meeting) has happened

public class InboxOptions {

    private Inbox currInbox;

    public InboxOptions(Inbox currInbox){
        this.currInbox = currInbox;
    }
}
