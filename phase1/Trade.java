package group_0073.phase1;

public class Trade {

    //Only works for one-way Trade so far

    private String obj;
    private String og_trader;
    private String other_trader;
    // og is Original Trader
    private int og_edits;
    private int other_edits;
    private boolean open;

    // Store objects Item and User or only details about Item and User e.g. item's and persons' names?
    public Trade(Item item, String og_trader, String other_trader){
        this.obj = item.getName();
        this.og_trader = og_trader;
        this.other_trader = other_trader;
        this.og_edits = 3;
        this.other_edits = 3;
        this.open = false;
    }

    public String getObj() {
        return obj;
    }

    public int getOther_edits() {
        return other_edits;
    }

    public int getOg_edits() {
        return og_edits;
    }

    public String getOg_trader() {
        return og_trader;
    }

    public String getOther_trader() {
        return other_trader;
    }

    public void setOg_edits(int og_edits) {
        this.og_edits = og_edits;
    }

    public void setOther_edits(int other_edits) {
        this.other_edits = other_edits;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
