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

// SELECT AN ITEM FROM YOUR WISHLIST THAT YOU WOULD LIKE TO BORROW
public class BorrowItemSelectScreen implements Initializable {

    private User currUser;
    private UserCatalogue uc;
    private UserSerialization us;

    @FXML
    private TableView<Item> borrowItemsTable;
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
        borrowItemsTable.setItems(getAvailableItems());
    }

    public void continueBtnClicked() throws IOException {
        if (!borrowItemsTable.getSelectionModel().isEmpty()) {
            Item selectedItem = borrowItemsTable.getSelectionModel().getSelectedItem();
            ArrayList<User> usersWithItem = uc.findUserWithItem(selectedItem);
            ArrayList<User> availableUsersWithItem = new ArrayList<>();
            for (User user : usersWithItem) {
                if (((Trader)user).getTraderStatus().isAvailable() && !((Trader)user).getTraderStatus().isFrozen()) {
                    availableUsersWithItem.add(user);
                }
            }
            if (!availableUsersWithItem.isEmpty()) {
                createBorrowRequestScene(selectedItem, availableUsersWithItem);
            }
            else { // No users with the item are available/unfrozen
                continueBtnLabel.setText("The user(s) with that item are currently unavailable/frozen. Please select a" +
                        "different item.");
            }
        }
        else { // No item selected
            continueBtnLabel.setText("Please select an item!");
        }
    }

    private void createBorrowRequestScene(Item selectedItem, ArrayList<User> availableUsersWithItem) throws IOException {
        Stage currStage = (Stage) continueBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateBorrowRequest.fxml"));
        Scene borrowScene = new Scene(loader.load());
        CreateBorrowRequestScreen controller = loader.getController();
        controller.initData(currUser, uc, us, selectedItem, availableUsersWithItem);
        currStage.setScene(borrowScene);
        currStage.show();
    }

    public ObservableList<Item> getAvailableItems() {
        ObservableList<Item> wantedItems = FXCollections.observableArrayList();
        ArrayList<Item> wishlist = ((Trader)currUser).getWishList().getInv();
        wantedItems.addAll(wishlist);

        return wantedItems;
    }
}
