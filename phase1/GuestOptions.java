package phase1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(menuOptions);
        uc.printDetails();

        String locationOptions = "If you want to filter traders by location, type 'locate'.";
        System.out.println(locationOptions);

        String mainOptions = "To go back, type 'exit'.";
        System.out.println(mainOptions);

        try{
            String input = br.readLine();
            while (!input.equals("exit")){
                if (input.equals("locate")){
                    chooseLocation(br);
                    System.out.println("To go back, type 'exit'.");
                }
                input = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }

    }

    private void chooseLocation(BufferedReader br){

        System.out.println("Enter a location: ");
        try{
            String location = br.readLine();
            if (!location.equals("exit")){
                uc.filterByLocation(location);
            }
        } catch (IOException e) {
            System.out.println("This location does not exist.");
        }
    }

}

