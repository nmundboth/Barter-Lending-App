package group_0073.phase1;

public class Trade {

    private String obj;
    private String person_1;
    private String person_2;
    private Item item;
    private Trader trader;

    // Store objects Item and User or only details about Item and User e.g. item's and persons' names?
    public Trade(String obj, String person_1, String person_2){
        this.obj = obj;
        this.person_1 = person_1;
        this.person_2 = person_2;
    }

}
