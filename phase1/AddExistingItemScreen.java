package phase1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddExistingItemScreen implements Initializable {

    private User currUser;
    private UserCatalogue uc;

    private boolean added = false;

    @FXML
    private TableView<Item> allItemsTable;
    @FXML
    private TableColumn<Item, String> itemColumn;
    @FXML
    private TableColumn <Item, String> descripColumn;
    @FXML
    private Button addBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        descripColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("descrip"));
    }

    public void initData(User user, UserCatalogue uc) {
        this.currUser = user;
        this.uc = uc;

        allItemsTable.setItems(getItems());
    }

    public ObservableList<Item> getItems() {
        ObservableList<Item> items = FXCollections.observableArrayList();
        ArrayList<Item> allItems = uc.findConfirmed();
        for (Item item : allItems) {
            if (!(((Trader)currUser).getInventory().getInv().contains(item))) {
                items.add(item);
            }
        }

        return items;
    }

    public void addBtnClicked() {
        if (!allItemsTable.getSelectionModel().isEmpty()) {
            added = true;
            addBtn.getScene().getWindow().hide();
        }
    }

    public Optional<Item> getAddedItem() {
        if (added) {
            Item selectedItem = allItemsTable.getSelectionModel().getSelectedItem();
            return Optional.of(selectedItem);
        }
        else {
            return Optional.empty();
        }
    }
}
