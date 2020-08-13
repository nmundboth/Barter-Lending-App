package phase2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RequestRestartScreen implements Initializable {

    private User currUser;
    private UserCatalogue uc;
    private UserSerialization us;

    @FXML
    private TableView<TradeMessage> cancelledTradesTable;
    @FXML
    private TableColumn<TradeMessage, Trade> tradeColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tradeColumn.setCellValueFactory(new PropertyValueFactory<TradeMessage, Trade>("trade"));
    }

    public void initData(User currUser, UserCatalogue uc, UserSerialization us) {
        this.currUser = currUser;
        this.uc = uc;
        this.us = us;

        cancelledTradesTable.setItems(getCancelledTrades());
    }

    public void requestRestartBtnClicked() throws Exception {
        if (!cancelledTradesTable.getSelectionModel().isEmpty()) {
            AdminInbox adminInbox = getAdminInbox();
            TradeMessage selectedTrade = cancelledTradesTable.getSelectionModel().getSelectedItem();
            adminInbox.restartedTrades.add(selectedTrade);

            us.toSerialize(uc.userBase); //Save
            cancelledTradesTable.getScene().getWindow().hide();
        }
    }

    private ObservableList<TradeMessage> getCancelledTrades() {
        ObservableList<TradeMessage> cancelledTrades = FXCollections.observableArrayList();
        TraderInbox tInbox = ((TraderInbox) currUser.getInbox());
        cancelledTrades.addAll(tInbox.getCancelledTrades());

        return cancelledTrades;
    }

    private AdminInbox getAdminInbox() {
        ArrayList<User> list = uc.userBase;
        Admin admin = null;
        for (User user : list) {
            if (user instanceof Admin) {
                admin = (Admin) user;
            }
        }
        assert admin != null;

        return (AdminInbox) admin.getInbox();
    }
}
