package phase1;

import java.io.Serializable;

/**
 * Represents a User.
 * Reference: https://www.dummies.com/programming/java/how-to-use-javadoc-to-document-your-classes/
 */
abstract class User implements Serializable{

    protected String username;
    private String password;
    private String type;
    private Inbox inbox;

    //Need empty user as placeholder in UserCatalogue findUserByName method
    public User(){

    }

    /**
     * @param username the user's username
     * @param password the user's password
     * @param type the type of the user
     * @param inbox the user's inbox
     */
    public User(String username, String password, String type, Inbox inbox){
        this.username = username;
        this.password = password;
        // User type is either "admin" or "trader"
        this.type = type;
        this.inbox = inbox;
    }

    /** Gets the User's username.
     *
     * @return a String which has the username of the User.
     */
    public String getUsername(){
        return this.username;
    }

    /** Gets the User's password.
     *
     * @return a String containing the User's password.
     */
    public String getPassword(){
        return this.password;
    }

    /** Says if User is a Trader or Admin.
     *
     * @return a String which states the type of User.
     */
    public String getType() {
        return type;
    }

    /** Gets the User's Inbox.
     *
     * @return an Inbox object which is the User's Inbox.
     */
    public Inbox getInbox() {
        return inbox;
    }

    @Override
    public String toString() {
        return username;
    }
}
