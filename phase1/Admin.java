package phase1;

import java.io.Serializable;

/**
 * <h1>Administrative User</h1>
 * <p>The Admin class represents an administrative user. Each admin has an inbox, where they can view which traders
 * have requested to be unfrozen. Admin also contains methods to confirm items proposed by users, and freeze a user
 * that has been flagged.</p>
 */

public class Admin extends User implements Serializable {
    private AdminInbox inbox;

    /**
     * @param username The username of the admin
     * @param password The password of the admin
     * @param inbox The admin's inbox
     */
    public Admin(String username, String password, AdminInbox inbox) {
        super(username, password, "admin", inbox);
        this.inbox = inbox;
    }

    /**
     * Allows the admin to freeze a specified trader.
     *
     * @param trader The trader that the admin wishes to freeze
     */
    protected void freezeTrader(Trader trader){trader.getTraderStatus().setFrozen();}

    /**
     * Allows an admin to confirm an item.
     *
     * @param item The item that the admin wishes to confirm
     */
    protected void confirmItem(Item item){item.setConfirm();}

    /**
     * Gets this admin's inbox.
     *
     * @return an AdminInbox object representing the admin's inbox.
     */
    protected AdminInbox getAdminInbox() {return this.inbox;}

    /**
     * Returns a string representation of an Admin object, which will be their username.
     *
     * @return a string of the admin's username.
     */
    public String toString(){
        return getUsername();
    }

}
