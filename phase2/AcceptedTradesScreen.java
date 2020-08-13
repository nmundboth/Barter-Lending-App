package phase2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AcceptedTradesScreen implements Initializable {

    private User currUser;
    private UserCatalogue uc;
    private UserSerialization us;
    private TradeManager tm = new TradeManager();
    private MeetingManager mm = new MeetingManager();

    @FXML
    private TableView<TradeMessage> acceptedTradeTable;
    @FXML
    private TableColumn<TradeMessage, Trade> tradeInfoColumn;

    @FXML
    private Label incorrectSelectionLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tradeInfoColumn.setCellValueFactory(new PropertyValueFactory<TradeMessage, Trade>("trade"));

        incorrectSelectionLabel.setText("");
    }

    public void initData(User currUser, UserCatalogue uc, UserSerialization us) {
        this.currUser = currUser;
        this.uc = uc;
        this.us = us;

        acceptedTradeTable.setItems(getAcceptedTrades());
    }

    public void proposeBtnClicked() throws Exception {
        if (!acceptedTradeTable.getSelectionModel().isEmpty()) {
            Trade selectedTrade = acceptedTradeTable.getSelectionModel().getSelectedItem().getTrade();
            if (selectedTrade.getMeeting().isEmpty() && !(selectedTrade.isOpen())) {
                showProposeMeetingScreen(selectedTrade);
            }
            else { // Trade has scheduled meeting or is open
                incorrectSelectionLabel.setText("Please select a valid trade.");
            }
        }
        else { // No trade selected
            incorrectSelectionLabel.setText("Please select a trade.");
        }
    }


    public void editBtnClicked() throws Exception {
        if (!acceptedTradeTable.getSelectionModel().isEmpty()) {
            Trade selectedTrade = acceptedTradeTable.getSelectionModel().getSelectedItem().getTrade();
            if (!selectedTrade.getMeeting().isEmpty() && !selectedTrade.isOpen()) {
                showProposeMeetingScreen(selectedTrade);
            }
            else { // Trade has no scheduled meeting or is open
                incorrectSelectionLabel.setText("Please select a valid trade.");
            }
        }
        else { // No trade selected
            incorrectSelectionLabel.setText("Please select a trade.");
        }
    }

    public void acceptBtnClicked() throws Exception {
        if (!acceptedTradeTable.getSelectionModel().isEmpty()) {
            Trade selectedTrade = acceptedTradeTable.getSelectionModel().getSelectedItem().getTrade();
            if (!selectedTrade.getMeeting().isEmpty() && !selectedTrade.isOpen() &&
            !(((Trader)currUser).equals(selectedTrade.getMeeting().getProposedBy()))) {
                if (selectedTrade instanceof TwoWayTrade) {
                    mm.confirmMeet((Trader)currUser, (TwoWayTrade)selectedTrade);
                    incorrectSelectionLabel.setText("Meeting accepted!");
                    us.toSerialize(uc.userBase); // Save
                    acceptedTradeTable.setItems(getAcceptedTrades());
                }
                else if (selectedTrade instanceof OneWayTrade) {
                    mm.confirmMeet((Trader)currUser, (OneWayTrade) selectedTrade);
                    incorrectSelectionLabel.setText("Meeting accepted!");
                    us.toSerialize(uc.userBase); // Save
                    acceptedTradeTable.setItems(getAcceptedTrades());
                }

            }
            else { // Trade has no scheduled meeting or is open
                incorrectSelectionLabel.setText("Please select a valid trade, where you didn't propose the meeting.");
            }
        }
        else { // No trade selected
            incorrectSelectionLabel.setText("Please select a trade.");
        }
    }

    public void confirmBtnClicked() throws Exception {
        if (!acceptedTradeTable.getSelectionModel().isEmpty()) {
            Trade selectedTrade = acceptedTradeTable.getSelectionModel().getSelectedItem().getTrade();
            if (selectedTrade.isOpen()) {
                tm.confirmTrade((Trader)currUser, selectedTrade);
                us.toSerialize(uc.userBase);// save
                acceptedTradeTable.setItems(getAcceptedTrades());

                //Check if the trade has been completed
                if (!selectedTrade.isOpen()) {

                    //Remove the completed trade from both users' TradeMessages lists
                    Trader ogTrader = selectedTrade.getOgTrader();
                    Trader otherTrader = selectedTrade.getOtherTrader();
                    TradeMessage completedTrade = acceptedTradeTable.getSelectionModel().getSelectedItem();
                    ((TraderInbox)ogTrader.getInbox()).getTrades().remove(completedTrade);
                    ((TraderInbox)otherTrader.getInbox()).getTrades().remove(completedTrade);

                    us.toSerialize(uc.userBase);
                    acceptedTradeTable.setItems(getAcceptedTrades());// Save
                    incorrectSelectionLabel.setText("Trade completed!");
                }
                else { // Trade is still open
                    incorrectSelectionLabel.setText("Trade marked as confirmed.");
                }
            }
            else { // Trade is not open (can't be confirmed)
                incorrectSelectionLabel.setText("Please select a valid trade.");
            }
        }
        else { // No trade selected
            incorrectSelectionLabel.setText("Please select a trade.");
        }
    }

    public void cancelBtnClicked() throws Exception {
        if (!acceptedTradeTable.getSelectionModel().isEmpty()) {
            Trade selectedTrade = acceptedTradeTable.getSelectionModel().getSelectedItem().getTrade();
            if (!selectedTrade.isOpen()) {

                tm.cancelTrade((Trader)currUser, selectedTrade);

                if (selectedTrade.getCancellations().size() == 2) {
                    Trader ogTrader = selectedTrade.getOgTrader();
                    Trader otherTrader = selectedTrade.getOtherTrader();
                    TradeMessage cancelledTrade = acceptedTradeTable.getSelectionModel().getSelectedItem();
                    ((TraderInbox)ogTrader.getInbox()).getTrades().remove(cancelledTrade);
                    ((TraderInbox)otherTrader.getInbox()).getTrades().remove(cancelledTrade);
                    acceptedTradeTable.setItems(getAcceptedTrades());
                }

                acceptedTradeTable.setItems(getAcceptedTrades());
                us.toSerialize(uc.userBase); // Save
                incorrectSelectionLabel.setText("Trade marked as cancelled.");

            }
            else { // Trade is already open, so can't cancel it
                incorrectSelectionLabel.setText("Please select a valid trade.");
            }
        }
        else { // No trade selected
            incorrectSelectionLabel.setText("Please select a trade.");
        }
    }

    private void showProposeMeetingScreen(Trade selectedTrade) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProposeMeeting.fxml"));

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Propose Meeting");

        window.setScene(new Scene(loader.load()));
        ProposeMeetingScreen controller = loader.getController();
        controller.initData(currUser, uc, us, selectedTrade);

        window.showAndWait();
        acceptedTradeTable.setItems(getAcceptedTrades());
    }

    private ObservableList<TradeMessage> getAcceptedTrades() {
        ObservableList<TradeMessage> trades = FXCollections.observableArrayList();
        TraderInbox tInbox = ((TraderInbox) currUser.getInbox());
        trades.addAll(tInbox.getTrades());

        return trades;
    }
}

