package phase2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SelectTradeTypeScreen implements Initializable {

    private User currUser;
    private UserCatalogue uc;
    private UserSerialization us;

    @FXML
    private RadioButton borrowButton;
    @FXML
    private RadioButton lendButton;
    @FXML
    private RadioButton tradeButton;
    private ToggleGroup tradeTypeToggleGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tradeTypeToggleGroup = new ToggleGroup();
        borrowButton.setToggleGroup(tradeTypeToggleGroup);
        lendButton.setToggleGroup(tradeTypeToggleGroup);
        tradeButton.setToggleGroup(tradeTypeToggleGroup);
        tradeTypeToggleGroup.selectToggle(borrowButton);
    }

    public void initData(User currUser, UserCatalogue uc, UserSerialization us) {
        this.currUser = currUser;
        this.uc = uc;
        this.us = us;
    }

    public void okButtonClicked () throws IOException {
        String tradeType = selectedTradeType();
        boolean greedy = ((Trader)currUser).getTraderStatus().isGreedy();
        switch (tradeType) {
            case "borrow":
                if (greedy) {
                    showGreedyUserWindow();
                }
                else {
                    selectBorrowItemScene();
                }
                break;
            case "lend":
                selectLendItemScene();
                break;
            case "trade":
                selectTradeItemScene();
                break;
        }
    }

    public String selectedTradeType() {
        RadioButton selectedTradeType = (RadioButton) tradeTypeToggleGroup.getSelectedToggle();
        if (selectedTradeType == borrowButton) {
            System.out.println("borrow selected");
            return "borrow";
        }
        else if (selectedTradeType == lendButton) {
            System.out.println("lend selected");
            return "lend";
        }
        else { // trade button selected
            System.out.println("trade selected");
            return "trade";
        }
    }

    private void selectBorrowItemScene() throws IOException {
        Stage currStage = (Stage) borrowButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BorrowItemSelect.fxml"));
        Scene borrowScene = new Scene(loader.load());
        BorrowItemSelectScreen controller = loader.getController();
        controller.initData(currUser, uc, us);
        currStage.setScene(borrowScene);
        currStage.show();
    }

    private void selectLendItemScene() throws IOException {
        Stage currStage = (Stage) lendButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LendItemSelect.fxml"));
        Scene lendScene = new Scene(loader.load());
        LendItemSelectScreen controller = loader.getController();
        controller.initData(currUser, uc, us);
        currStage.setScene(lendScene);
        currStage.show();
    }

    private void selectTradeItemScene() throws IOException {
        Stage currStage = (Stage) tradeButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TradeItemSelect.fxml"));
        Scene tradeScene = new Scene(loader.load());
        TradeItemSelectScreen controller = loader.getController();
        controller.initData(currUser, uc, us);
        currStage.setScene(tradeScene);
        currStage.show();
    }

    private void showGreedyUserWindow() throws IOException {
        Parent alertBoxParent = FXMLLoader.load(getClass().getResource("GreedyUser.fxml"));
        Scene alertBoxScene = new Scene(alertBoxParent);

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("TradeApp");


        window.setScene(alertBoxScene);
        window.showAndWait();
    }
}
