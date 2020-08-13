package phase1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

// SELECT AN ITEM FROM YOUR LENDLIST THAT YOU WOULD LIKE TO LEND
public class LendItemSelectScreen implements Initializable {

    private User currUser;
    private UserCatalogue uc;
    private UserSerialization us;

    @FXML
    private TableView<Item> lendItemsTable;
    @FXML
    private TableColumn<Item, String> itemColumn;
    @FXML
    private TableColumn <Item, String> descripColumn;
    @FXML
    private Button continueBtn;
    @FXML
    private Label continueBtnLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        continueBtnLabel.setText("");
        itemColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        descripColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("descrip"));
    }

    public void initData(User currUser, UserCatalogue uc, UserSerialization us) {
        this.currUser = currUser;
        this.uc = uc;
        this.us = us;
        lendItemsTable.setItems(getAvailableItems());
    }

    public void continueBtnClicked() throws IOException {
        if (!lendItemsTable.getSelectionModel().isEmpty()) {
            Item selectedItem = lendItemsTable.getSelectionModel().getSelectedItem();
            ArrayList<User> usersWantItem = uc.findUserWantsItem(selectedItem);
            ArrayList<User> availableUsersWantItem = new ArrayList<>();
            for (User user : usersWantItem) {
                if (((Trader)user).getTraderStatus().isAvailable() && !((Trader)user).getTraderStatus().isFrozen()) {
                    availableUsersWantItem.add(user);
                }
            }
            if (!availableUsersWantItem.isEmpty()) {
                //TODO: Change scene to user select/trade creation screen
                createLendRequestScene(selectedItem, availableUsersWantItem);
            }
            else { // No users with the item are available/unfrozen
                continueBtnLabel.setText("The user(s) who want that item are currently unavailable/frozen. Please select a" +
                        "different item.");
            }
        }
        else { // No item selected
            continueBtnLabel.setText("Please select an item!");
        }
    }

    private void createLendRequestScene(Item selectedItem, ArrayList<User> availableUsersWantItem) throws IOException {
        Stage currStage = (Stage) continueBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateLendRequest.fxml"));
        Scene lendScene = new Scene(loader.load());
        CreateLendRequestScreen controller = loader.getController();
        controller.initData(currUser, uc, us, selectedItem, availableUsersWantItem);
        currStage.setScene(lendScene);
        currStage.show();
    }

    public ObservableList<Item> getAvailableItems() {
        ObservableList<Item> ownedItems = FXCollections.observableArrayList();
        ArrayList<Item> lendlist = ((Trader)currUser).getInventory().getInv();
        ownedItems.addAll(lendlist);

        return ownedItems;
    }
}
