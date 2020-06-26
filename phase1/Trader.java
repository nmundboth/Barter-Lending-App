package group_0073.phase1;

import java.util.List;

public class Trader extends User{

    //Might have to create an inventory class?
    private List<Item> inventory;
    private int notifs_count;
    private String name;

    public Trader(String username, String password, String type, Inbox inbox, List<Item> inventory, String name) {
        super(username, password, type, inbox);
        this.inventory = inventory;
        this.notifs_count = 0;
        this.name = name;
    }

    public List<Trade> readTrades(){
        return this.getInbox().getTrades();
    }

    public void readNotifs(){
        this.notifs_count = 0;

        //Reads notifications line by line
        int i = 0;
        while (i < this.getInbox().getNotifs().size()){
            System.out.println(this.getInbox().getNotifs().get(i));
            i++;
        }

    }

    //Keeps track of new notifications
    public int unreadNotifs(){
        return this.notifs_count;
    }

    //Not sure if trades for trader also includes requested trades
    public void sendRequest(Trader trader, Item item){
        trader.notifs_count += 1;
        trader.getInbox().getUnacceptedTrades().add(new Trade(item, this.getName(), trader.getName()));
        this.getInbox().getUnacceptedTrades().add(new Trade(item, this.getName(), trader.getName()));
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void refuseTrade(Trader trader, Trade trade){
        trader.notifs_count += 1;
        trader.getInbox().getUnacceptedTrades().remove(trade);
        trader.getInbox().getNotifs().add(trade.getPerson_1()+" refuses to trade "+trade.getObj()+" with "+
                trade.getPerson_2());
        this.getInbox().getUnacceptedTrades().remove(trade);
    }

    public void acceptTrade(Trader trader, Trade trade){
        trader.notifs_count += 1;
        trader.getInbox().getUnacceptedTrades().remove(trade);
        this.getInbox().getUnacceptedTrades().remove(trade);
        trader.getInbox().getTrades().add(trade);
        this.getInbox().getTrades().add(trade);
        trader.getInbox().getNotifs().add(trade.getPerson_1()+" accepts to trade "+trade.getObj()+" with "+
                trade.getPerson_2());
    }

    public void scheduleMeet(Trader trader, Meeting meet){
        trader.notifs_count += 1;
        trader.getInbox().getNotifs().add("Hey "+trader.name+"! Can you meet "+this.name+" on"+meet.getDate()+" at "+
                meet.getLocation()+" at "+meet.getTime()+"?");
    }

    public String getName() {
        return name;
    }

    public void confirmTrade(Trader trader, Trade trade, Meeting meet){
        this.notifs_count += 1;
        trader.notifs_count += 1;
        trader.getInbox().getNotifs().add(trade.getPerson_1()+" will meet "+trade.getPerson_2()+" on "+meet.getDate()+
                " at "+meet.getTime()+" at "+meet.getLocation()+" to trade "+trade.getObj());
        this.getInbox().getNotifs().add(trade.getPerson_1()+" will meet "+trade.getPerson_2()+" on "+meet.getDate()+
                " at "+meet.getTime()+" at "+meet.getLocation()+" to trade "+trade.getObj());
        trade.setOpen(true);
    }

    public void tradeComplete(Trader trader, Trade trade, Meeting meet){
        this.notifs_count += 1;
        trader.notifs_count += 1;
        trader.getInbox().getNotifs().add(trade.getPerson_1()+" met "+trade.getPerson_2()+" on "+meet.getDate()+
                " at "+meet.getTime()+" at "+meet.getLocation()+" to trade "+trade.getObj());
        this.getInbox().getNotifs().add(trade.getPerson_1()+" met "+trade.getPerson_2()+" on "+meet.getDate()+
                " at "+meet.getTime()+" at "+meet.getLocation()+" to trade "+trade.getObj());
        trade.setOpen(false);
    }

}
