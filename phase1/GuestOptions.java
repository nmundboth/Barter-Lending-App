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

        String options = "To perform an action, type the corresponding number.\n"+
                "1. Filter traders by city\n"+
                "2. Filter traders by trader name\n"+
                "3. Filter by item\n";
        System.out.println(options);

        uc.printDetails();

        String mainOptions = "To go back, type 'exit'.";
        System.out.println(mainOptions);

        try{
            String input = br.readLine();
            while (!input.equals("exit")){
                if (input.equals("1")){
                    chooseLocation(br);
                    System.out.println(menuOptions);
                    System.out.println(options);
                    uc.printDetails();
                    System.out.println(mainOptions);
                }
                else if (input.equals("2")){
                    searchTrader(br);
                    System.out.println(menuOptions);
                    System.out.println(options);
                    uc.printDetails();
                    System.out.println(mainOptions);
                }
                else if (input.equals("3")){
                    searchItem(br);
                    System.out.println(menuOptions);
                    System.out.println(options);
                    uc.printDetails();
                    System.out.println(mainOptions);
                }
                input = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }

    }

    private void chooseLocation(BufferedReader br){
        try{
            System.out.println("\nTo go back, type 'exit'.\n");
            System.out.println("Enter a city: ");
            String location = br.readLine();
            while (!location.equals("exit")){
                if (!uc.validLocation(location)){
                    System.out.println("There is no trader living in " +
                            "this city\n.");
                    System.out.println("Please enter another city again.");
                    System.out.println("\nTo go back, type 'exit'.\n");
                    System.out.println("Enter a city: ");
                }
                else{
                    uc.filterByLocation(location);
                    System.out.println("To go back, type 'exit'.");
                }
                location = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }
    }

    private void searchTrader(BufferedReader br){
        try{
            System.out.println("\nTo go back, type 'exit'.\n");
            System.out.println("Enter a trader's name: ");
            String name = br.readLine();
            while (!name.equals("exit")){
                if (!uc.validName(name)){
                    System.out.println("There is no trader by that name.\n");
                    System.out.println("Please enter a name again.");
                    System.out.println("\nTo go back, type 'exit'.\n");
                    System.out.println("Enter a trader's name: ");
                }
                else{
                    uc.filterByName(name);
                    System.out.println("To go back, type 'exit'.");
                }
                name = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }
    }

    private void searchItem(BufferedReader br){
        String subOptions = "To perform an action, type the corresponding number.\n"+
                "1. Buy an item\n"+
                "2. Sell an item\n";
        System.out.println(subOptions);
        try {
            System.out.println("\nTo go back, type 'exit'.\n");
            String op = br.readLine();
            while (!op.equals("exit")){
                if (op.equals("1")){
                    searchInventory(br);
                    System.out.println(subOptions);
                    System.out.println("exit");
                }
                else if (op.equals("2")){
                    searchWishlist(br);
                    System.out.println(subOptions);
                    System.out.println("exit");
                }
                op = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    private void searchInventory(BufferedReader br){
        try{
            System.out.println("\nTo go back, type 'exit'.\n");
            System.out.println("Enter an item: ");
            String item = br.readLine();
            while(!item.equals("exit")){
                if (!uc.validInventoryItem(item)){
                    System.out.println("There is no trader selling that item.\n"+"Please enter another item.");
                    System.out.println("\nTo go back, type 'exit'.\n");
                    System.out.println("Enter an item: ");
                }
                else{
                    uc.filterInventory(item);
                    System.out.println("To go back, type 'exit'.\n");
                }
                item = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

    private void searchWishlist(BufferedReader br){
        try{
            System.out.println("\nTo go back, type 'exit'.\n");
            System.out.println("Enter an item: ");
            String item = br.readLine();
            while(!item.equals("exit")){
                if (!uc.validWishlistItem(item)){
                    System.out.println("There is no trader interested in that item.\n"+"Please enter another item.");
                    System.out.println("\nTo go back, type 'exit'.\n");
                    System.out.println("Enter an item: ");
                }
                else{
                    uc.filterWishlist(item);
                    System.out.println("To go back, type 'exit'.\n");
                }
                item = br.readLine();
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong.");
        }
    }

}

