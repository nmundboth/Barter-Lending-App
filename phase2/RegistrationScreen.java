package phase2;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistrationScreen {

    private String username;
    private String password;
    @FXML
    private TextField name;
    @FXML
    private TextField city;


    private boolean created = false;

    public void initData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void createButtonClicked() {
        created = true;
        name.getScene().getWindow().hide();
    }

    public void cancel() {
        name.getScene().getWindow().hide();
    }

    public Optional<User> getUser() {
        if (created) {
            String name = this.name.getText();
            String location = this.city.getText();
            List<TradeMessage> trades = new ArrayList<TradeMessage>();
            List<Message> traderNotifs = new ArrayList<Message>();
            List<Message> adminNotifs = new ArrayList<Message>();
            TraderInbox inbox = new TraderInbox(trades, traderNotifs, adminNotifs);
            Inventory inventory = new Inventory(new ArrayList<Item>(), "inventory");
            Inventory wishlist = new Inventory(new ArrayList<Item>(), "wishlist");
            TraderStatus traderStatus = new TraderStatus();
            User user = new Trader(username, password, inbox, inventory, wishlist, name, traderStatus, location);
            inbox.setOwner(user);
            return Optional.of(user);
        }
        else {
            return Optional.empty();
        }
    }
}
