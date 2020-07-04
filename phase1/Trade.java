package phase1;

import java.util.ArrayList;

public class Trade {

    // og is Original Trader
    private Trader ogTrader;
    private Trader otherTrader;
    private int ogEdits;
    private int otherEdits;
    private boolean open;
    private boolean permanent;
    private ArrayList<Trader> confirmations;

    public Trade(Trader ogTrader, Trader otherTrader){
        this.ogTrader = ogTrader;
        this.otherTrader = otherTrader;
        this.ogEdits = 3;
        this.otherEdits = 3;
        this.open = false;
        this.permanent = false;
        this.confirmations = new ArrayList<Trader>();
    }

    public Trader getOgTrader(){
        return ogTrader;
    }

    public Trader getOtherTrader(){
        return otherTrader;
    }

    public int getOgEdits() {
        return ogEdits;
    }

    public int getOtherEdits() {
        return otherEdits;
    }

    public ArrayList<Trader> getConfirmations(){
        return confirmations;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setOgEdits(int ogEdits) {
        this.ogEdits = ogEdits;
    }

    public void setOtherEdits(int otherEdits) {
        this.otherEdits = otherEdits;
    }

    public void addConfirmation(Trader trader){
        confirmations.add(trader);
    }

    public void clearConfirmations(){
        confirmations.clear();
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }
}