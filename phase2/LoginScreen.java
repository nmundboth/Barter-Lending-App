package phase2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class LoginScreen {

    private UserSerialization us;
    private UserCatalogue uc;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginBtn;


    public LoginScreen() throws Exception {
        us = new UserSerialization();
        uc = new UserCatalogue(us.deserialize());
    }

    public void initData(UserCatalogue uc, UserSerialization us) {
        this.uc = uc;
        this.us = us;
    }


    public void loginButtonClicked() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (!uc.inUserBase(username)) {
            showIncorrectLoginBox();

            // Remove later
            System.out.println("Incorrect username");
        }

        else { // User is in userbase
            User currUser = uc.getUserByName(username);
            String currPassword = currUser.getPassword();

            if (!password.equals(currPassword)) {
                showIncorrectLoginBox();

                // Remove later
                System.out.println("Incorrect password");
            }
            else { // Correctly entered username and password
                login(currUser);
            }
        }
    }

    public void registerButtonClicked() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Navnee: added case where username and password fields cannot be left empty
        if (username.equals("") || password.equals("")){
            showInvalidInfoBox();
        }
        else {
            if (!uc.inUserBase(username)) {

                //Prepare the registration window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("RegistrationScreen.fxml"));

                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("TradeApp Registration");

                window.setScene(new Scene(loader.load()));
                RegistrationScreen controller = loader.getController();
                controller.initData(username, password);

                window.showAndWait();

                usernameField.clear();
                passwordField.clear();
                System.out.println("User created!");


                //Add created user to user catalogue userbase and serialize (save)
                Optional<User> createdUser = controller.getUser();
                createdUser.ifPresent(user -> uc.userBase.add((user)));
                us.toSerialize(uc.userBase);

            }

            else { // duplicate username
                showDuplicateUserBox();

                //remove later
                System.out.println("Duplicate user");
            }
        }
    }

    public void guestButtonClicked() throws IOException {
        Stage currStage = (Stage) loginBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GuestMode.fxml"));
        Scene guestScene = new Scene(loader.load());
        GuestModeScreen controller = loader.getController();
        controller.initData(uc, us);
        currStage.setScene(guestScene);
        currStage.show();
    }


    /*
    HELPER METHODS
     */
    public void login(User user) throws Exception {
        if (user.getType().equals("trader")) {
            if (((Trader)user).getTraderStatus().isAvailable()) {
                Stage currStage = (Stage) loginBtn.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("TraderScreen.fxml"));
                Scene traderScene = new Scene(loader.load());
                TraderScreen controller = loader.getController();
                controller.initData(user, uc, us);
                currStage.setScene(traderScene);
                currStage.show();
            }
            else { // Trader is marked as unavailable
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmAvailable.fxml"));

                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Confirmation");

                window.setScene(new Scene(loader.load()));
                ConfirmAvailableScreen controller = loader.getController();
                controller.initData(user, uc, us);

                window.showAndWait();

                boolean confirmed = controller.getConfirmed();

                // Log in if user confirmed
                if (confirmed) {
                    us.toSerialize(uc.userBase); // Save before login
                    Stage currStage = (Stage) loginBtn.getScene().getWindow();
                    FXMLLoader loader1 = new FXMLLoader(getClass().getResource("TraderScreen.fxml"));
                    Scene traderScene = new Scene(loader1.load());
                    TraderScreen controller1 = loader1.getController();
                    controller1.initData(user, uc, us);
                    currStage.setScene(traderScene);
                    currStage.show();
                }
            }

            //remove later
            System.out.println("Trader logged in");
        }
        else { // admin
            loginBtn.getScene().getWindow().hide();
            AdminOptions ao = new AdminOptions(user, uc, us);
            ao.run();
        }
    }

    public void showIncorrectLoginBox() throws IOException {
        Parent alertBoxParent = FXMLLoader.load(getClass().getResource("IncorrectLogin.fxml"));
        Scene alertBoxScene = new Scene(alertBoxParent);

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("TradeApp");


        window.setScene(alertBoxScene);
        window.showAndWait();
    }

    public void showDuplicateUserBox() throws IOException {
        Parent alertBoxParent = FXMLLoader.load(getClass().getResource("DuplicateUser.fxml"));
        Scene alertBoxScene = new Scene(alertBoxParent);

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("TradeApp");


        window.setScene(alertBoxScene);
        window.showAndWait();
    }

    public void showInvalidInfoBox() throws IOException {
        Parent alertBoxParent = FXMLLoader.load(getClass().getResource("InvalidInfo.fxml"));
        Scene alertBoxScene = new Scene(alertBoxParent);

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("TradeApp");


        window.setScene(alertBoxScene);
        window.showAndWait();
    }


}
