package phase1;

public class Message {

    // The String part of this message that contains the user's actual message
    private String content;

    // The user sending this message
    private User sender;

    // The user receiving this message
    private User recipient;

    public boolean read = false;


    public Message(String content, User sender, User recipient){
        this.content =  content;
        this.sender = sender;
        this.recipient = recipient;
    }

    public String getContent(){return this.content;}

    public User getSender(){return this.sender;}

    public User getRecipient(){return this.recipient;}

    public void read(){
        System.out.println(this.content);
        this.read = true;
    }



}
