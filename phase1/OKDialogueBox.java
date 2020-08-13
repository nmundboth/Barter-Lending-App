package phase1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class OKDialogueBox {

    @FXML
    Button okButton;

    public void okButtonClicked() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
