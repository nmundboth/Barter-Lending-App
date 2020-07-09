package phase1;

public interface Tradable {

    // Reference: https://www.geeksforgeeks.org/interfaces-in-java/

    void sendBorrowRequest(Trader ogTrader, Trader otherTrader, Item item, boolean isPermanent);
    void sendLendRequest(Trader ogTrader, Trader otherTrader, Item item, boolean isPermanent);
    void sendTWTradeRequest(Trader ogTrader, Trader otherTrader, Item ogItem, Item otherItem,
                            boolean isPermanent);
    void rejectUnaccepted(Trader rejecting, Trader rejected, OneWayTrade trade);
    void rejectUnaccepted(Trader rejecting, Trader rejected, TwoWayTrade trade);
    void acceptTrade(Trader accepting, Trader accepted, OneWayTrade trade);
    void acceptTrade(Trader accepting, Trader accepted, TwoWayTrade trade);
    void completeTrade(Trade trade);

}
