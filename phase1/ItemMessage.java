package phase1;

public class ItemMessage extends Message {

    private Item item;

    public boolean read = false;

    public ItemMessage(String content, User sender, User recipient, Item item) {
        super(content, sender, recipient);
        this.item = item;
    }

    public Trader getSender(){return (Trader)this.sender;}

    public Item getItem(){
        this.read = true;
        return this.item;
    }
}
