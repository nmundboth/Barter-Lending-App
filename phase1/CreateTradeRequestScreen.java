package phase1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateTradeRequestScreen implements Initializable {

    private User currUser;
    private UserCatalogue uc;
    private UserSerialization us;
    private Item otherItem;
    private ArrayList<User> usersWithItem;
    private ArrayList<User> compatibleUsers;
    private TradeManager tm = new TradeManager();

    @FXML
    private TableView<User> compatibleUsersTable;
    @FXML
    private TableColumn<User, String> userColumn;
    @FXML
    private TableColumn <User, String> locationColumn;

    @FXML
    private TableView<Item> suggestedTable;
    @FXML
    private TableColumn<Item, String> itemColumn;
    @FXML
    private TableColumn<Item, String> descripColumn;

    @FXML
    private Label addedSuggestedLabel;

    @FXML
    private TableView<Item> offerableItemsTable;
    @FXML
    private TableColumn<Item, String> offerableItemColumn;
    @FXML
    private TableColumn<Item, String> offerableDescripColumn;

    @FXML
    private RadioButton permanentRadioButton;
    @FXML
    private RadioButton temporaryRadioButton;
    @FXML
    private RadioButton digitalRadioButton;
    @FXML
    private RadioButton notDigitalRadioButton;

    private ToggleGroup durationToggleGroup;
    private ToggleGroup digitalToggleGroup;

    @FXML
    private Label noSelectedUserLabel;
    @FXML
    private Button createBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<User, String>("location"));

        itemColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        descripColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("descrip"));

        offerableItemColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        offerableDescripColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("descrip"));

        durationToggleGroup = new ToggleGroup();
        permanentRadioButton.setToggleGroup(durationToggleGroup);
        temporaryRadioButton.setToggleGroup(durationToggleGroup);
        durationToggleGroup.selectToggle(permanentRadioButton);

        digitalToggleGroup = new ToggleGroup();
        digitalRadioButton.setToggleGroup(digitalToggleGroup);
        notDigitalRadioButton.setToggleGroup(digitalToggleGroup);
        digitalToggleGroup.selectToggle(notDigitalRadioButton);

        addedSuggestedLabel.setText("");
        noSelectedUserLabel.setText("");
    }

    // Structure for setting action on mouse click taken from StackOverFlow user Kachna
    // https://stackoverflow.com/questions/38492341/how-to-detect-a-selection-on-javafx-tableview-when-clicking-a-highlighted-item
    public void initData(User currUser, UserCatalogue uc, UserSerialization us, Item otherItem,
                         ArrayList<User> usersWithItem, ArrayList<User> compatibleUsers) {

        this.currUser = currUser;
        this.uc = uc;
        this.us = us;
        this.otherItem = otherItem;
        this.usersWithItem = usersWithItem;
        this.compatibleUsers = compatibleUsers;

        compatibleUsersTable.setItems(getUsers());
        compatibleUsersTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                offerableItemsTable.setItems(getOfferableItems());
            }
        });

        suggestedTable.setItems(getItems());
    }

    public void filterLocationBtnClicked() {
        compatibleUsersTable.setItems(getCloseUsers());
    }

    public void resetListBtnClicked() {
        compatibleUsersTable.setItems(getUsers());
    }

    public void addSuggestedBtnClicked() throws Exception {
        if (!suggestedTable.getSelectionModel().isEmpty()) {
            Item item = suggestedTable.getSelectionModel().getSelectedItem();
            ((Trader)currUser).getInventory().getInv().add(item);
            us.toSerialize(uc.userBase); // Save
            ArrayList<User> availableUsersWantItem = new ArrayList<>();
            for (User user : uc.findUserWantsItem(item)) {
                if ((!((Trader)user).getTraderStatus().isFrozen()) && ((Trader)user).getTraderStatus().isAvailable()) {
                    availableUsersWantItem.add(user);
                }
            }

            compatibleUsers.addAll(availableUsersWantItem);
            compatibleUsersTable.setItems(getUsers());
        }

        else {
            addedSuggestedLabel.setText("Please select an item!");
        }
    }

    public String selectedDuration() {
        RadioButton selectedDuration = (RadioButton) durationToggleGroup.getSelectedToggle();
        if (selectedDuration == permanentRadioButton) {
            System.out.println("permanent selected");
            return "permanent";
        }
        else { // temporary selected
            System.out.println("temporary selected");
            return "temporary";
        }
    }

    public String selectedDigital() {
        RadioButton selectedDigital = (RadioButton) digitalToggleGroup.getSelectedToggle();
        if (selectedDigital == digitalRadioButton) {
            System.out.println("digital selected");
            return "digital";
        }
        else { // temporary selected
            System.out.println("not digital selected");
            return "not digital";
        }
    }

    public void createBtnClicked() throws Exception {
        boolean isPermanent = selectedDuration().equals("permanent");
        boolean isDigital = selectedDigital().equals("digital");
        if ((!compatibleUsersTable.getSelectionModel().isEmpty()) &&
                (!offerableItemsTable.getSelectionModel().isEmpty())) {

            User selectedUser = compatibleUsersTable.getSelectionModel().getSelectedItem();
            Item selectedItem = offerableItemsTable.getSelectionModel().getSelectedItem();
            tm.sendTWTradeRequest((Trader)currUser, (Trader)selectedUser, selectedItem, otherItem, isPermanent, isDigital, isDigital);

            // Save and close window
            us.toSerialize(uc.userBase);
            createBtn.getScene().getWindow().hide();
        }
        else { // No user selected to borrow from
            noSelectedUserLabel.setText("Please select a user to trade with, and an item to offer them!");
        }
    }

    public ObservableList<User> getUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        users.addAll(compatibleUsers);

        return users;
    }

    public ObservableList<Item> getItems() {
        ObservableList<Item> items = FXCollections.observableArrayList();
        ArrayList<Item> suggestedItems = new ArrayList<>();
        ArrayList<Item> currInventory = ((Trader)currUser).getInventory().getInv();
        for (User user : usersWithItem) {
            ArrayList<Item> userWishlist = ((Trader)user).getWishList().getInv();
            for (Item item : userWishlist) {
                if (!suggestedItems.contains(item) && (!currInventory.contains(item))) {
                    suggestedItems.add(item);
                }
            }
        }
        items.addAll(suggestedItems);

        return items;
    }

    public ObservableList<Item> getOfferableItems() {
        ObservableList<Item> items = FXCollections.observableArrayList();
        ArrayList<Item> offerableItems = new ArrayList<>();
        if (!compatibleUsersTable.getSelectionModel().isEmpty()) {
            User otherTrader = compatibleUsersTable.getSelectionModel().getSelectedItem();
            ArrayList<Item> otherWishlist = ((Trader)otherTrader).getWishList().getInv();
            ArrayList<Item> currInv = ((Trader)currUser).getInventory().getInv();
            for (Item item : otherWishlist) {
                if (currInv.contains(item) && (!offerableItems.contains(item))) {
                    offerableItems.add(item);
                }
            }
            items.addAll(offerableItems);
        }

        return items;
    }

    public ObservableList<User> getCloseUsers() {
        ObservableList<User> closeUsers = FXCollections.observableArrayList();
        ArrayList<User> filteredUsers = new ArrayList<>();
        String location = ((Trader)currUser).getLocation();
        for (User user : compatibleUsers) {
            if (((Trader)user).getLocation().equals(location)) {
                filteredUsers.add(user);
            }
        }
        closeUsers.addAll(filteredUsers);

        return closeUsers;
    }
}
