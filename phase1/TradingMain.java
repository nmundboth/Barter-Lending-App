package phase1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class containing the main method through which the program starts.
 */
public class TradingMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        primaryStage.setTitle("TradeApp");
        primaryStage.setScene(new Scene(root, 800, 350));
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        //TradingSystem ts = new TradingSystem();
        //ts.run();
        launch(args);

    }
}