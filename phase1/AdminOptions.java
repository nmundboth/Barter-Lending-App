package phase1;

public class AdminOptions {

    private User curr;
    private UserCatalogue uc;


    public AdminOptions(User curr, UserCatalogue uc){
        this.curr = curr;
        this.uc = uc;
    }

    public void run(){
        System.out.println("Admin options reached");
    }
}
