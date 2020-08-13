package phase2;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.Optional;

public class AddNewItemScreen {

    private boolean added = false;
    
    @FXML
    private TextField itemField;
    @FXML
    private TextField descripField;

    public void addBtnClicked() {
        added = true;
        itemField.getScene().getWindow().hide();
    }

    public Optional<Item> getAddedItem() {
        if (added) {
            String itemName = itemField.getText();
            String itemDescrip = descripField.getText();
            if (!itemName.isEmpty()) {
                Item addedItem = new Item(itemName, itemDescrip);
                return Optional.of(addedItem);
            }
            else { // Invalid (empty) name for item
                return Optional.empty();
            }
        }
        else { // Item not added
            return Optional.empty();
        }
    }
}
