package group_0073.phase1;

abstract class User {

    private String username;
    private String password;
    private String type;

    public User(String username, String password, String type, Inbox inbox){
        this.username = username;
        this.password = password;
        this.type = type;
    }

}
