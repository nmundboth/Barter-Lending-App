package phase2;

import java.io.Serializable;

public class Message implements Serializable {

    // The String part of this message that contains the user's actual message
    private String content;

    // The user sending this message
    public User sender;

    // The user receiving this message
    public User recipient;

    public boolean read = false;


    public Message(String content, User sender, User recipient){
        this.content =  content;
        this.sender = sender;
        this.recipient = recipient;
    }

    public User getSender(){return this.sender;}

    public User getRecipient(){return this.recipient;}

    public String getContent() {
        this.read = true;
        return content;
    }
}
