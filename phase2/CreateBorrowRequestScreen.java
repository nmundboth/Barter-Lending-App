package phase2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateBorrowRequestScreen implements Initializable {

    private User currUser;
    private UserCatalogue uc;
    private UserSerialization us;
    private Item borrowItem;
    private ArrayList<User> usersWithItem;
    private TradeManager tm = new TradeManager();

    @FXML
    private TableView<User> borrowUsersTable;
    @FXML
    private TableColumn<User, String> userColumn;
    @FXML
    private TableColumn <User, String> locationColumn;

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

        durationToggleGroup = new ToggleGroup();
        permanentRadioButton.setToggleGroup(durationToggleGroup);
        temporaryRadioButton.setToggleGroup(durationToggleGroup);
        durationToggleGroup.selectToggle(permanentRadioButton);

        digitalToggleGroup = new ToggleGroup();
        digitalRadioButton.setToggleGroup(digitalToggleGroup);
        notDigitalRadioButton.setToggleGroup(digitalToggleGroup);
        digitalToggleGroup.selectToggle(notDigitalRadioButton);

        noSelectedUserLabel.setText("");
    }

    public void initData(User currUser, UserCatalogue uc, UserSerialization us, Item borrowItem, ArrayList<User> usersWithItem) {
        this.currUser = currUser;
        this.uc = uc;
        this.us = us;
        this.borrowItem = borrowItem;
        this.usersWithItem = usersWithItem;
        borrowUsersTable.setItems(getUsers());
    }

    public void filterLocationBtnClicked() {
        borrowUsersTable.setItems(getCloseUsers());
    }

    public void resetListBtnClicked() {
        borrowUsersTable.setItems(getUsers());
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
        if (!borrowUsersTable.getSelectionModel().isEmpty()) {
            User selectedUser = borrowUsersTable.getSelectionModel().getSelectedItem();
            tm.sendBorrowRequest((Trader)currUser, (Trader)selectedUser, borrowItem, isPermanent, isDigital);

            // Save and close window
            us.toSerialize(uc.userBase);
            createBtn.getScene().getWindow().hide();
        }
        else { // No user selected to borrow from
            noSelectedUserLabel.setText("Please select a user to borrow from!");
        }
    }

    public ObservableList<User> getUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        users.addAll(usersWithItem);

        return users;
    }

    public ObservableList<User> getCloseUsers() {
        ObservableList<User> closeUsers = FXCollections.observableArrayList();
        ArrayList<User> filteredUsers = new ArrayList<>();
        String location = ((Trader)currUser).getLocation();
        for (User user : usersWithItem) {
            if (((Trader)user).getLocation().equals(location)) {
                filteredUsers.add(user);
            }
        }
        closeUsers.addAll(filteredUsers);

        return closeUsers;
    }
}
