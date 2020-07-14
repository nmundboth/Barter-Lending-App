***************
IMPORTANT NOTES
***************

* Before running TradingMain, run Initialize to create a 'UserData' serializable file, which contains an initial admin
  with username: 'admin', password 'admin'.

* After running Initialize, running TradingMain will start the program. Upon starting the program, all registered users'
  usernames will be printed at the top (should only see admin upon first startup). This is for testing purposes to keep
  track of the users, and can later be removed by removing lines 57-59 in UserSerialization.

* Saving of UserData is done upon normally exiting the program, that is, returning to the home menu (the one that allows
  you to login or register) and typing 'exit'. This was a design decision, as if the program unexpectedly crashes, we
  did not want to save in that case.

* The inbox will be undergoing a major overhaul in Phase 2, and as such the feature of 'displaying' how many
  notifications a user currently has' has been omitted from the UI, though it remains in the inbox classes.

* When entering the date when proposing/editing a meeting, it must be in the format YYYY-MM-DD, in order to be properly
  parsed by the java.time methods.


**************
UI WALKTHROUGH
**************

The menus should explain what to do in order to perform an action for the most part.

Typing 'exit' will almost always bring you back to the previous menu.

If the program is ever asking for a prompt without telling you what to enter, then typing 'exit' will likely send you
back to a menu, and fix the problem.

The first menu will allow you to log in or register a user. After registering a user, you will be directed back to the
first menu, where you can then log in.

Upon logging in as a trader, you will be presented with the actions you can perform. All functionality pertaining to
trades/meetings is contained within the inbox (under '1. View Inbox' in the Trader Options menu). Additionally, a
trader's inventory and wishlist are displayed on login.

Once you select '1. View Inbox', it presents you with a list of options pertaining to trades and notifications, as well
as an option to request to be unfrozen by an admin, which is only available to users that are currently frozen. All of
the functionality pertaining to meetings is contained within '2. View accepted trades', since traders should only be
able to organize meetings for trades that they have accepted.

After selecting '2. View accepted trades', you will be presented with actions pertaining to meetings, as well as the
option to request to cancel a trade.

Upon logging in as an admin (from home menu), you will be presented with a list of actions that are only available to
admins.


*******************
A NOTE ABOUT TRADES
*******************

Trades have multiple 'states'. They are created upon a user proposing a trade.
    1. 'Unaccepted' - Traders can not propose meetings for unaccepted trades, traders can either accept or reject these
        unaccepted trades.
    2. 'Accepted', 'Unscheduled' - Trade that has been accepted by the user that received the trade request, but for
        which no meeting has been scheduled yet.
    3. 'Accepted', 'Scheduled', 'Closed' - Trade that has had a meeting proposed at least once, but that has not yet
        had the meeting accepted by the person that received the meeting request.
    4. 'Accepted', 'Scheduled', 'Open', 'Unconfirmed' - Trade that has had a meeting agreed upon (accepted) by both
        users, but that meeting has not yet been confirmed to have happened by both users (if only one user confirms,
        the trade will remain open until both confirm)
    5. 'Accepted', 'Scheduled', 'Open', 'Confirmed' - Trade that has been confirmed to have happened in real life by
        both users involved in the trade. This will complete the trade if it is a permanent trade, or a temporary trade
        where one meeting has already been completed. This will schedule the second meeting if it is a temporary trade
        for which no meetings had yet been completed.

* A trade is marked as incomplete (and counts towards a trader's incomplete trade limit) when a meeting is agreed upon
  ('Accepted', 'Scheduled', 'Open')

* A trade is marked as counting towards a user's weekly transaction limit when it is 'Accepted'.