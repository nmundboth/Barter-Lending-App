package phase1;

import java.io.Serializable;

public class NoMeetTrade extends Trade implements Serializable {


    /**
     * @param ogTrader    the trader that originally proposed this trade
     * @param otherTrader the trader that originally received this trade request
     */
    public NoMeetTrade(Trader ogTrader, Trader otherTrader) {
        super(ogTrader, otherTrader);
    }

}
