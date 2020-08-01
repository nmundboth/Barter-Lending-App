package phase1;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;

public class TraderStatus {

    static int greedLimit = -1; // The lower this limit, the more a trader must lend before they can borrow.

    private ArrayList<Boolean> statusList;
    private ArrayList<Integer> limitList;
    private int incompleteLimit;
    private int weeklyTransxnLimit;
    private LocalDateTime weeklyEnd;

    public TraderStatus(){
        this.statusList = new ArrayList<Boolean>();
        this.statusList.add(false); //not frozen
        this.statusList.add(false); //not flagged

        this.limitList = new ArrayList<Integer>();
        this.setGreedyInt(0); //greedyInt: Higher = greedier, so in order to borrow,
                            // must be <= -1 (or whatever the threshold is set to by the admin(s))
        this.limitList.add(0); // # of outstanding incomplete trades currently associated with the trader
        this.limitList.add(0); //weeklyTransxn is at 0

        this.incompleteLimit = 3; // Change this to change the limit on incomplete transxns a trader can have
        this.weeklyTransxnLimit = 10; // Change this to change the weekly transxn limit
        this.weeklyEnd = LocalDateTime.now().plus(Period.ofWeeks(1));// Initial period is one week after creation
                                                                    // of account
    }

    public ArrayList<Boolean> getStatusList(){
        return this.statusList;
    }

    public ArrayList<Integer> getLimitList(){
        return this.limitList;
    }

    /**
     * Checks whether this user has borrowed more than they have lent.
     * @return a boolean indicating whether the trader has borrowed more than they lent.
     */
    public boolean isGreedy(){
        return this.getGreedyInt() > greedLimit;
    }

    /** Checks if the Trader is within the limits of borrowing from other Traders.
     *
     * @return an integer which checks if Trader has exceeded limit in borrowing.
     */
    public int getGreedyInt(){
        return limitList.get(0);
    }

    /**
     * Used to track the lend/borrow ratio of users (higher = more borrows than lends = greedier)
     * @param greedyInt The integer tracking how greedy a user is (how much more they have borrowed than lent)
     */
    public void setGreedyInt(int greedyInt){
        if (this.limitList.size() != 0) {
            this.limitList.remove(0);
        }
        this.limitList.add(0, greedyInt);
    }

    /** Gets the number of incomplete Trades for a Trader.
     *
     * @return an integer telling the number of incomplete Trades.
     */
    public int getIncomplete(){
        return this.limitList.get(1);
    }

    /**
     * Gets the number of transactions that a trader has conducted this week.
     *
     * @return an integer representing number of weekly transactions.
     */
    public int getWeeklyTransxns(){
        return this.limitList.get(2);
    }

    /** Checks if Trader's account is frozen. true means the account is frozen.
     *
     * @return state of Trader's account.
     */
    public boolean isFrozen(){return statusList.get(0);}

    /**
     * Checks whether the trader has been automatically flagged for having too many incomplete transactions, or too
     * many weekly transactions. Flagged traders are sent to admins, who can then decide whether or not to freeze a
     * trader.
     *
     * @return a boolean representing whether the user has been flagged.
     */
    public boolean isFlagged(){
        return statusList.get(1);
    }

    /**
     * Flags a trader - indicates too many outstanding incomplete transactions, or too many weekly transactions.
     */
    public void flag(){
        this.statusList.remove(1);
        this.statusList.add(1, true);
    }

    /**
     * Keeps count of incomplete Trades. Records number of incomplete Trades.
     */
    public void addIncomplete(){
        int incomplete = this.getIncomplete() + 1;
        this.limitList.remove(1);
        this.limitList.add(1, incomplete);
    }

    /**
     * Once a Trade is completed, number of incomplete Trades decreases.
     */
    public void removeIncomplete(){
        int incomplete = this.getIncomplete() - 1;
        this.limitList.remove(1);
        this.limitList.add(1, incomplete);
    }

    /** Checks if limit of Trades for a Trader has been exceeded in a week.
     *
     * @return true if limit of Trades has been exceeded.
     */
    public boolean overWeeklyLimit(){
        return this.getWeeklyTransxns() > weeklyTransxnLimit;
    }

    /** Checks if Trader has too many incomplete Trades.
     *
     * @return true if limit of incomplete Trades has been exceeded.
     */
    public boolean overIncompleteLimit(){
        return this.getIncomplete() > incompleteLimit;
    }

    /**
     * Adds a weekly transaction to the trader's number of weekly transactions conducted.
     * Every time a transaction is added, checks to see if the current week has ended. If it has, then the
     * weekly transaction period resets, and so the added trade would be the trader's first transaction of the week.
     */
    public void addWeeklyTransxn(){
        if (LocalDateTime.now().isAfter(weeklyEnd) || LocalDateTime.now().isEqual(weeklyEnd)){
            this.limitList.remove(2);
            this.limitList.add(1, 1);
            weeklyEnd = LocalDateTime.now().plus(Period.ofWeeks(1));
        }
        else{ // Weekly period hasn't ended
            int weekly = this.getWeeklyTransxns() + 1;
            this.limitList.remove(2);
            this.limitList.add(2, weekly);
        }
    }

}

