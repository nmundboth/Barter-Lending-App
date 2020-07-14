package phase1;

import java.util.List;
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

    /** Sets a username for the User.
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /** Says if User is a Trader or Admin.
     *
     * @return a String which states the type of User.
     */
    public String getType() {
        return type;
    }

    /** Sets a password for User.
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /** Gets the User's Inbox.
     *
     * @return an Inbox object which is the User's Inbox.
     */
    public Inbox getInbox() {
        return inbox;
    }
}
