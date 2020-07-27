package phase1;

public class TradeMessage extends Message {

    private Trade trade;

    private String content;

    // The user sending this message
    private User sender;

    private User recipient;

    public boolean read = false;

    public TradeMessage(String content, User sender, User recipient, Trade trade) {
        super(content, sender, recipient);
        this.trade = trade;
    }

    public Trade getTrade(){
        this.read = true;
        return this.trade;
    }
}
