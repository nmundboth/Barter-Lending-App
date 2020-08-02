package phase1;

public class GuestOptions {

    private UserCatalogue uc;
    private UserSerialization us;
    private String menuOptions;

    public GuestOptions(UserCatalogue uc, UserSerialization us){
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

