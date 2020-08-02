package phase1;

public class GuestOptions {

    private phase1.UserCatalogue uc;
    private phase1.UserSerialization us;
    private String menuOptions;

    public GuestOptions(phase1.UserCatalogue uc, phase1.UserSerialization us){
        this.uc = uc;
        this.us = us;
        this.menuOptions = "\nWelcome to the Trade Catalogue! \nYou can only view all potential trades and traders'" +
                " information here.\n";
    }

    public void run(){

        System.out.println(menuOptions);
        uc.printDetails();

        String mainOptions = "To go back to the main menu, type 'exit'.";
        System.out.println(mainOptions);

    }

}

