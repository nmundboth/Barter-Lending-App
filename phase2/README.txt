Java 8 (SDK 1.8) was used to create this program, and should be set as the project structure in order to run this
project.

JavaFX is required to run the program.
https://gluonhq.com/products/scene-builder/thanks/?dl=/download/scene-builder-11-windows-x64/

Settings -> Languages and frameworks -> javafx, in the “path to scenebuilder”, find SceneBuilder.exe and set it.

To run the program, first run Initialize, and then run TradingMain.

Certain actions on the GUI result in lines being printed to the console -> these are to provide information about
what is happening as a result of user input.






************************************************************************************************************************
                                                    GUI WALKTHROUGH
************************************************************************************************************************
The first screen (Login Screen) allows you to log in, create users, or enter Guest Mode.

Guest Mode allows users to view all traders in the system, and filter them based on their location, name or item in
their inventory.

Initially, one admin account exists, UN: admin
                                     PW: admin

When you log in as an admin, the program switches from GUI to text UI.

To register a new user, enter the desired username and password and click register. After entering name and location
in the pop-up window, the user account will be available to log in to.

After logging in, there are 6 tabs to choose from: Home, Inventory, Trade, Inbox, Misc, and Account.

Home provides a short summary of what is available in each tab. I will go into more depth in this file.

Clicking log out at any point will return to the Login Screen.


INVENTORY TAB
==============
* Allows users to add new or existing items to their lendlist (inventory). New items must be confirmed by an admin
before they appear in the system to other traders.

* Allows users to add existing items to their wishlist.

* Allows users to request removal of an item from their lendlist or wishlist by an admin.


TRADE TAB
==========
* Allows users to view transaction requests, and decide to either accept or reject the offer.

* Allows users to send transaction requests to other users (Borrow, Lend, Trade) if they are able to.
    -> Clicking 'Send Transaction Request' opens up a new window that guides User through creating a transaction.

* Allows users to view their Accepted Trade details, and perform actions on their accepted trades.
    -> These actions are: Proposing a meeting, Accepting a meeting, Editing a meeting, Confirming a trade has happened,
    and requesting to cancel a trade.


INBOX TAB
==========
* Allows users to view and delete messages, either from admins or other traders, which are automatically generated when
certain actions happen (i.e. another user accepts their trade offer).


MISC TAB
=========
* Allows users to see their three most recent trading partners, and their three most recently traded items.


ACCOUNT TAB
============
* Allows users to mark themselves as unavailable to trades, which will log them out and not allow them to have trades
proposed to them until they log back in.

* Allows frozen users to request that they are unfrozen (sends a request to an admin).

* Allows users to change their location.

* Allows users to request that a cancelled trade be restarted (sends request to admin).

ADMIN UI
=========
* The admin UI is not part of the GUI.

* Admins can freeze flagged users, review and confirm unconfirmed items, change how many times a user must lend before
they can borrow, undo actions that have been requested by traders, and create new admins.


A NOTE ABOUT TRADES
====================

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




************************************************************************************************************************
                                               PHASE 2 IMPLEMENTED FEATURES
************************************************************************************************************************

All of the mandatory extensions have been implemented:
    * Undo functionality is in the form of requests by users, and can be carried out by admins.
    * Automatic suggestion functionality happens when a user tries to propose a trade for an item, but does not have
    any items to exchange for their desired item.
    * Guest mode is our implementation of the 3rd mandatory extension.
    * Adjusting the thresholds is done by Admin Users.
    * The 'Unavailable' status is the other status (besides frozen/unfrozen) that we chose to implement.


Of the optional extensions:
    * Allowing users to enter their city upon registering was added, but it does not limit the users to trading with
    only other users from the same city. Instead, it allows them to filter users based on city, and select who they
    would like to trade with that way (so inter-city trades are still possible, if both users want to do that).
    * Adding another type of trade (digital trade) was added, where users can propose transactions to each other that
    are digital, and do not require a meeting to occur for the transaction to take place.
    * Implemented a GUI



