package group_0073.phase1;

public class Trade {

    private String obj;
    private String person_1;
    private String person_2;
    private boolean open;

    // Store objects Item and User or only details about Item and User e.g. item's and persons' names?
    public Trade(Item item, String person_1, String person_2){
        this.obj = item.getName();
        this.person_1 = person_1;
        this.person_2 = person_2;
        this.open = false;
    }

    public String getObj() {
        return obj;
    }

    public String getPerson_1() {
        return person_1;
    }

    public String getPerson_2() {
        return person_2;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
