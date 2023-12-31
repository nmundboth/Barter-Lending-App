package phase2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class TraderScreen implements Initializable {

    private User currUser;
    private UserCatalogue uc;
    private UserSerialization us;
    private TradeManager tm = new TradeManager();

    @FXML
    private TableView<Item> lendTable;
    @FXML
    private TableColumn<Item, String> lendItemColumn;
    @FXML
    private TableColumn<Item, String> lendDescripColumn;

    @FXML
    private Label lendlistNotifLabel;

    @FXML
    private TableView<Item> wishTable;
    @FXML
    private TableColumn<Item, String> wishItemColumn;
    @FXML
    private TableColumn<Item, String> wishDescripColumn;

    @FXML
    private Label wishlistNotifLabel;

    @FXML
    private TableView<TradeMessage> requestTable;
    @FXML
    private TableColumn<TradeMessage, Trade> tradeInfoColumn;

    @FXML
    private Label noTradeSelectedLabel;

    @FXML
    private TableView<Message> tradeNotifTable;
    @FXML
    private TableColumn<Message, String> tradeMessageColumn;

    @FXML
    private TableView<Message> adminNotifTable;
    @FXML
    private TableColumn<Message, String> adminMessageColumn;

    @FXML
    private Label messageNotSelectedLabel;

    @FXML
    private TableView<Trader> recentTraderTable;
    @FXML
    private TableColumn<Trader, String> recentTraderColumn;

    @FXML
    private TableView<Item> recentItemTable;
    @FXML
    private TableColumn<Item, String> recentItemColumn;
    @FXML
    private TableColumn<Item, String> recentDescripColumn;

    @FXML
    private Label accountStatusLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lendItemColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        lendDescripColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("descrip"));
        wishItemColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        wishDescripColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("descrip"));
        tradeInfoColumn.setCellValueFactory(new PropertyValueFactory<TradeMessage, Trade>("trade"));
        tradeMessageColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("content"));
        adminMessageColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("content"));
        recentTraderColumn.setCellValueFactory(new PropertyValueFactory<Trader, String>("username"));
        recentItemColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        recentDescripColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("descrip"));

        lendlistNotifLabel.setText("");
        wishlistNotifLabel.setText("");
        noTradeSelectedLabel.setText("");
        messageNotSelectedLabel.setText("");
        accountStatusLabel.setText("");
    }

    public void initData(User user, UserCatalogue uc, UserSerialization us) {
        this.currUser = user;
        this.uc = uc;
        this.us = us;

        wishTable.setItems(getWishList());
        lendTable.setItems(getLendList());
        requestTable.setItems(getTradeRequests());
        tradeNotifTable.setItems(getTraderMessages());
        adminNotifTable.setItems(getAdminMessages());
        recentTraderTable.setItems(getRecentTraders());
        recentItemTable.setItems(getRecentItems());
    }

    public void logoutBtnClicked() throws Exception {
        us.toSerialize(uc.userBase); // Save before logout
        Stage currStage = (Stage) lendTable.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
        Scene loginScene = new Scene(loader.load());
        LoginScreen controller = loader.getController();
        controller.initData(uc, us);
        currStage.setScene(loginScene);
        currStage.show();
    }

    public void lendlistAddExistingButtonClicked() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddExistingItem.fxml"));

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Item");

        window.setScene(new Scene(loader.load()));
        AddExistingItemScreen controller = loader.getController();
        controller.initData(currUser, uc);

        window.showAndWait();

        Optional<Item> createdItem = controller.getAddedItem();
        if (createdItem.isPresent()) {
            Item item = createdItem.get();
            ((Trader)currUser).getInventory().addItem(item);
            lendTable.getItems().add(item);
            us.toSerialize(uc.userBase);
        }
    }

    public void lendlistAddNewItemButtonClicked() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddNewItem.fxml"));

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Item");

        window.setScene(new Scene(loader.load()));
        AddNewItemScreen controller = loader.getController();

        window.showAndWait();

        Optional<Item> createdItem = controller.getAddedItem();
        if (createdItem.isPresent()) {
            Item item = createdItem.get();
            ((Trader)currUser).getInventory().addItem(item);
            lendTable.getItems().add(item);
            us.toSerialize(uc.userBase);
        }
    }

    public void lendlistRemoveButtonClicked() throws Exception {
        if (!lendTable.getSelectionModel().isEmpty()) {
            AdminInbox adminInbox = getAdminInbox();
            Item itemToRemove = lendTable.getSelectionModel().getSelectedItem();
            ItemMessage message = new ItemMessage("Request for this item to be removed", currUser,
                    currUser, itemToRemove);
            adminInbox.undoInventory.add(message);
            lendlistNotifLabel.setText("Lendlist removal request sent.");

            lendTable.setItems(getLendList());
            us.toSerialize(uc.userBase); // Save
        }
    }

    public void wishlistRemoveButtonClicked() throws Exception {
        if (!wishTable.getSelectionModel().isEmpty()) {
            AdminInbox adminInbox = getAdminInbox();
            Item itemToRemove = wishTable.getSelectionModel().getSelectedItem();
            ItemMessage message = new ItemMessage("Request for this item to be removed", currUser,
                    currUser, itemToRemove);
            adminInbox.undoWishList.add(message);
            wishlistNotifLabel.setText("Wishlist removal request sent.");

            wishTable.setItems(getWishList());
            us.toSerialize(uc.userBase); // Save
        }
    }

    public void addWishlistButtonClicked() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddWishlist.fxml"));

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Item");

        window.setScene(new Scene(loader.load()));
        AddWishlistScreen controller = loader.getController();
        controller.initData(currUser, uc);

        window.showAndWait();

        Optional<Item> addedItem = controller.getAddedItem();
        if (addedItem.isPresent()) {
            Item item = addedItem.get();
            ((Trader)currUser).getWishList().addItem(item);
            wishTable.getItems().add(item);
            us.toSerialize(uc.userBase);
        }
    }

    public void acceptBtnClicked() throws Exception {
        if (!requestTable.getSelectionModel().isEmpty()) {
            TradeMessage selectedTradeMsg = requestTable.getSelectionModel().getSelectedItem();
            Trade selectedTrade = selectedTradeMsg.getTrade();
            if (selectedTrade instanceof OneWayTrade) {
                //Accepts the trade
                tm.acceptTrade((Trader)currUser, selectedTrade.getOgTrader(), (OneWayTrade)selectedTrade);

                //Removes the trade from the user's unaccepted trades
                ((TraderInbox)(currUser.getInbox())).getUnacceptedTrades().remove(selectedTradeMsg);
            }
            else if (selectedTrade instanceof TwoWayTrade) {
                //Accepts the trade
                tm.acceptTrade((Trader)currUser, selectedTrade.getOgTrader(), (TwoWayTrade)selectedTrade);

                //Removes the trade from the user's unaccepted trades
                ((TraderInbox)(currUser.getInbox())).getUnacceptedTrades().remove(selectedTradeMsg);
            }

            us.toSerialize(uc.userBase); // Save
            requestTable.setItems(getTradeRequests());
        }
        else { // No trade selected
            noTradeSelectedLabel.setText("Please select a trade.");
        }

    }

    public void rejectBtnClicked() throws Exception {
        if (!requestTable.getSelectionModel().isEmpty()) {
            TradeMessage selectedTradeMsg = requestTable.getSelectionModel().getSelectedItem();
            Trade selectedTrade = selectedTradeMsg.getTrade();
            if (selectedTrade instanceof OneWayTrade) {
                //Rejects the trade
                tm.rejectUnaccepted((Trader)currUser, selectedTrade.getOgTrader(), (OneWayTrade)selectedTrade);

                //Removes the trade from the user's unaccepted trades
                ((TraderInbox)(currUser.getInbox())).getUnacceptedTrades().remove(selectedTradeMsg);
            }
            else if (selectedTrade instanceof TwoWayTrade) {
                //Rccepts the trade
                tm.rejectUnaccepted((Trader)currUser, selectedTrade.getOgTrader(), (TwoWayTrade)selectedTrade);

                //Removes the trade from the user's unaccepted trades
                ((TraderInbox)(currUser.getInbox())).getUnacceptedTrades().remove(selectedTradeMsg);
            }

            us.toSerialize(uc.userBase); // Save
            requestTable.setItems(getTradeRequests());
        }
        else { // No trade selected
            noTradeSelectedLabel.setText("Please select a trade.");
        }
    }

    public void sendTransactionRequestBtnClicked() throws IOException {
        boolean frozen = ((Trader)currUser).getTraderStatus().isFrozen();
        if (frozen) {
            showFrozenUserBox();
        }
        else { // User is not frozen, so can send transaction requests
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SelectTradeType.fxml"));

            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Trade Type");

            window.setScene(new Scene(loader.load()));
            SelectTradeTypeScreen controller = loader.getController();
            controller.initData(currUser, uc, us);

            window.showAndWait();
        }
    }

    public void acceptedTradesBtnClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AcceptedTrades.fxml"));

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Accepted Trades");

        window.setScene(new Scene(loader.load()));
        AcceptedTradesScreen controller = loader.getController();
        controller.initData(currUser, uc, us);

        window.showAndWait();
    }

    public void deleteTradeNotifBtnClicked() throws Exception {
        if (!tradeNotifTable.getSelectionModel().isEmpty()) {
            Message selectedMessage = tradeNotifTable.getSelectionModel().getSelectedItem();
            currUser.getInbox().getTraderNoti().remove(selectedMessage);
            tradeNotifTable.setItems(getTraderMessages());
            us.toSerialize(uc.userBase);
        }
        else {
            messageNotSelectedLabel.setText("No message selected.");
        }

    }

    public void deleteAdminNotifBtnClicked() throws Exception {
        if (!adminNotifTable.getSelectionModel().isEmpty()) {
            Message selectedMessage = tradeNotifTable.getSelectionModel().getSelectedItem();
            currUser.getInbox().getAdmiNoti().remove(selectedMessage);
            adminNotifTable.setItems(getAdminMessages());
            us.toSerialize(uc.userBase);
        }
        else {
            messageNotSelectedLabel.setText("No message selected.");
        }
    }

    public void unavailableBtnClicked() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmUnavailable.fxml"));

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Confirmation");

        window.setScene(new Scene(loader.load()));
        ConfirmUnavailableScreen controller = loader.getController();
        controller.initData(currUser, uc, us);

        window.showAndWait();

        boolean confirmed = controller.getConfirmed();

        // Log out if user confirmed
        if (confirmed) {
            us.toSerialize(uc.userBase); // Save before logout
            Stage currStage = (Stage) lendTable.getScene().getWindow();
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
            Scene loginScene = new Scene(loader1.load());
            LoginScreen controller1 = loader1.getController();
            controller1.initData(uc, us);
            currStage.setScene(loginScene);
            currStage.show();
        }
    }

    public void unfreezeBtnClicked() throws Exception {
        if (((Trader)currUser).getTraderStatus().isFrozen()) {
            AdminInbox adminInbox = getAdminInbox();
            if (!adminInbox.undoFrozen.contains((Trader)currUser)) {
                adminInbox.undoFrozen.add((Trader)currUser);
                us.toSerialize(uc.userBase); // Save
                accountStatusLabel.setText("You have requested to be unfrozen.");
            }
            else { // Trader has already requested to be unfrozen
                accountStatusLabel.setText("You have already requested to be unfrozen.");
            }
        }
        else { // Trader is not frozen
            accountStatusLabel.setText("You are not frozen!");
        }
    }

    public void changeLocationBtnClicked() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangeLocation.fxml"));

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Confirmation");

        window.setScene(new Scene(loader.load()));
        ChangeLocationScreen controller = loader.getController();
        controller.initData(currUser, uc, us);

        window.showAndWait();
        us.toSerialize(uc.userBase);
    }

    public void restartTradeBtnClicked() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RequestRestart.fxml"));

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Restart Trade");

        window.setScene(new Scene(loader.load()));
        RequestRestartScreen controller = loader.getController();
        controller.initData(currUser, uc, us);

        window.showAndWait();
        us.toSerialize(uc.userBase); // Save
    }

    /*
    HELPER METHODS
     */

    private void showFrozenUserBox() throws IOException {
        Parent alertBoxParent = FXMLLoader.load(getClass().getResource("AccountFrozen.fxml"));
        Scene alertBoxScene = new Scene(alertBoxParent);

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("TradeApp");


        window.setScene(alertBoxScene);
        window.showAndWait();
    }

    private ObservableList<Item> getWishList() {
        ObservableList<Item> items = FXCollections.observableArrayList();
        ArrayList<Item> wishlist = ((Trader)currUser).getWishList().getInv();
        items.addAll(wishlist);

        return items;
    }

    private ObservableList<Item> getLendList() {
        ObservableList<Item> items = FXCollections.observableArrayList();
        ArrayList<Item> lendlist = ((Trader)currUser).getInventory().getInv();
        items.addAll(lendlist);

        return items;
    }

    private ObservableList<TradeMessage> getTradeRequests() {
        ObservableList<TradeMessage> trades = FXCollections.observableArrayList();
        TraderInbox tInbox = ((TraderInbox) currUser.getInbox());
        trades.addAll(tInbox.getUnacceptedTrades());

        return trades;
    }

    private ObservableList<Message> getTraderMessages() {
        ObservableList<Message> messages = FXCollections.observableArrayList();
        Inbox inbox = currUser.getInbox();
        messages.addAll(inbox.getTraderNoti());

        return messages;
    }

    private ObservableList<Message> getAdminMessages() {
        ObservableList<Message> messages = FXCollections.observableArrayList();
        Inbox inbox = currUser.getInbox();
        messages.addAll(inbox.getAdmiNoti());

        return messages;
    }

    private ObservableList<Trader> getRecentTraders() {
        ObservableList<Trader> traders = FXCollections.observableArrayList();
        ArrayList<Trader> recentTraders = ((Trader)currUser).frequentPartners();
        traders.addAll(recentTraders);

        return traders;
    }

    private ObservableList<Item> getRecentItems() {
        ObservableList<Item> items = FXCollections.observableArrayList();
        ArrayList<Item> recentItems = ((Trader)currUser).getRecentItems();
        items.addAll(recentItems);

        return items;
    }

    private AdminInbox getAdminInbox() {
        ArrayList<User> list = uc.userBase;
        Admin admin = null;
        for (User user : list) {
            if (user instanceof Admin) {
                admin = (Admin)user;
            }
        }
        assert admin != null;

        return (AdminInbox)admin.getInbox();
    }

}
