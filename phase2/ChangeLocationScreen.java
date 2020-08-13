package phase2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ChangeLocationScreen {

    private User currUser;
    private UserCatalogue uc;
    private UserSerialization us;

    @FXML
    private TextField locationField;

    @FXML
    private Label emptyLocationLabel;

    public void initData(User currUser, UserCatalogue uc, UserSerialization us) {
        this.currUser = currUser;
        this.uc = uc;
        this.us = us;

        emptyLocationLabel.setText("");
    }

    public void okBtnClicked() throws Exception {
        String location = locationField.getText();
        if (!location.isEmpty()) {
            ((Trader)currUser).setLocation(location);
            us.toSerialize(uc.userBase); // Save
            locationField.getScene().getWindow().hide();
        }
        else {
            emptyLocationLabel.setText("Please enter a location.");
        }
    }
}
