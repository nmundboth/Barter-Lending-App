package phase1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class GuestModeScreen implements Initializable {

    private UserCatalogue uc;
    private UserSerialization us;

    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> userColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> locationColumn;

    @FXML
    private TextField locationField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField itemField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<User, String>("location"));
    }

    public void initData(UserCatalogue uc, UserSerialization us) {
        this.uc = uc;
        this.us = us;

        usersTable.setItems(getUsers());
    }

    public void locationBtnClicked() {
        String location = locationField.getText();
        if (!location.isEmpty()) {
            usersTable.setItems(getUsersByLocation(location));
        }
    }

    public void nameBtnClicked() {
        String name = nameField.getText();
        if (!name.isEmpty()) {
            usersTable.setItems(getUsersByName(name));
        }
    }

    public void itemBtnClicked() {
        String item = itemField.getText();
        if (!item.isEmpty()) {
            usersTable.setItems(getUsersByItem(item));
        }
    }

    public void resetBtnClicked() {
        usersTable.setItems(getUsers());
    }

    public void returnBtnClicked() throws IOException {
        Stage currStage = (Stage) usersTable.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
        Scene loginScene = new Scene(loader.load());
        LoginScreen controller = loader.getController();
        controller.initData(uc, us);
        currStage.setScene(loginScene);
        currStage.show();
    }

    private ObservableList<User> getUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        ArrayList<User> traders = getTraders();
        users.addAll(traders);

        return users;
    }

    private ObservableList<User> getUsersByLocation(String location) {
        ObservableList<User> users = FXCollections.observableArrayList();
        ArrayList<User> traders = getTraders();
        ArrayList<User> tradersInLocation = new ArrayList<>();
        for (User user : traders) {
            if (((Trader)user).getLocation().equals(location)) {
                tradersInLocation.add(user);
            }
        }
        uc.filterByLocation(location);
        users.addAll(tradersInLocation);

        return users;
    }

    private ObservableList<User> getUsersByName(String name) {
        ObservableList<User> users = FXCollections.observableArrayList();
        ArrayList<User> traders = getTraders();
        ArrayList<User> tradersWithName = new ArrayList<>();
        for (User user : traders) {
            if (((Trader)user).getName().equals(name)) {
                tradersWithName.add(user);
            }
        }
        uc.filterByName(name);
        users.addAll(tradersWithName);

        return users;
    }

    private ObservableList<User> getUsersByItem(String item) {
        ObservableList<User> users = FXCollections.observableArrayList();
        ArrayList<User> traders = getTraders();
        ArrayList<User> tradersWithItem = new ArrayList<>();
        for (User trader : traders) {
            ArrayList<Item> traderItems = ((Trader)trader).getInventory().getInv();
            for (Item traderItem : traderItems) {
                if (traderItem.getName().equals(item)) {
                    tradersWithItem.add(trader);
                }
            }
        }

        users.addAll(tradersWithItem);

        return users;
    }

    private ArrayList<User> getTraders() {
        ArrayList<User> traders = new ArrayList<>();
        for (User user : uc.userBase) {
            if (user.getType().equals("trader")) {
                traders.add(user);
            }
        }
        return traders;
    }
}
