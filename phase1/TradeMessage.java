package phase1;

public class TradeMessage extends Message {

    private Trade trade;

    public boolean read = false;

    public TradeMessage(String content, User sender, User recipient, Trade trade) {
        super(content, sender, recipient);
        this.trade = trade;
    }

    public Trader getSender(){return (Trader)this.sender;}

    public Trader getRecipient(){return (Trader)this.recipient;}

    public Trade getTrade(){
        this.read = true;
        return this.trade;
    }
}
