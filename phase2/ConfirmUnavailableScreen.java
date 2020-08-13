package phase2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ConfirmUnavailableScreen {

    private User currUser;
    private UserCatalogue uc;
    private UserSerialization us;
    private boolean confirmed = false;

    @FXML
    private Button yesBtn;

    public void initData(User currUser, UserCatalogue uc, UserSerialization us) {
        this.currUser = currUser;
        this.uc = uc;
        this.us = us;
    }

    public void yesBtnClicked() throws Exception {
        ((Trader)currUser).getTraderStatus().setUnavailable();
        us.toSerialize(uc.userBase); // Save
        this.confirmed = true;
        yesBtn.getScene().getWindow().hide();
    }

    public void noBtnClicked() {
        yesBtn.getScene().getWindow().hide();
    }

    public boolean getConfirmed() {
        return confirmed;
    }
}
