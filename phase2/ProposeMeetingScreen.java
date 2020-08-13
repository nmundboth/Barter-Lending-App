package phase2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;


public class ProposeMeetingScreen implements Initializable {

    private User currUser;
    private UserCatalogue uc;
    private UserSerialization us;
    private Trade currTrade;
    private MeetingManager mm = new MeetingManager();

    @FXML
    private TextField yearTextField;
    @FXML
    private TextField monthTextField;
    @FXML
    private TextField dayTextField;
    @FXML
    private TextField timeTextField;
    @FXML
    private TextField locationTextField;

    @FXML
    private Label invalidDateLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invalidDateLabel.setText("");
    }

    public void initData(User currUser, UserCatalogue uc, UserSerialization us, Trade currTrade) {
        this.currUser = currUser;
        this.uc = uc;
        this.us = us;
        this.currTrade = currTrade;
    }

    public void okBtnClicked() throws Exception {
        if (checkDateFormat() && !locationTextField.getText().equals("") && !timeTextField.getText().equals("")) {
            String year = yearTextField.getText();
            String month = monthTextField.getText();
            String day = dayTextField.getText();
            String date = year + "-" + month + "-" + day;
            String location = locationTextField.getText();
            String time = timeTextField.getText();

            mm.proposeMeeting((Trader)currUser, currTrade, location, date, time);
            us.toSerialize(uc.userBase);

            invalidDateLabel.getScene().getWindow().hide();

        }
        else {
            invalidDateLabel.setText("Please enter a valid date/time/location (no same-day meetings).");
        }
    }

    private boolean checkDateFormat() {
        String year = yearTextField.getText();
        String month = monthTextField.getText();
        String day = dayTextField.getText();

        if (isYear(year) && isMonth(month) && isDay(day)) {
            String dateString = year + "-" + month + "-" + day;
            try {
                LocalDate.parse(dateString);
            } catch (DateTimeParseException dtpe) {
                return false;
            }

            LocalDate date = LocalDate.parse(dateString);
            return date.isAfter(LocalDate.now());
        }
        else {
            return false;
        }
    }

    // isYear, isMonth, isDay methods adapted from https://www.baeldung.com/java-check-string-number
    // (Section 3. Using Plain Java)
    private boolean isYear(String s) {
        if (s == null || s.equals("") || s.length() != 4) {
            return false;
        }

        try {
            int i = Integer.parseInt(s);

        } catch (NumberFormatException nfe) {
            return false;
        }

        int i = Integer.parseInt(s);

        return i >= 0;
    }

    private boolean isMonth(String s) {
        if (s == null || s.equals("") || s.length() != 2) {
            return false;
        }

        try {
            int i = Integer.parseInt(s);

        } catch (NumberFormatException nfe) {
            return false;
        }

        int i = Integer.parseInt(s);

        return i >= 1 && i <= 12;
    }

    private boolean isDay(String s) {
        if (s == null || s.equals("") || s.length() != 2) {
            return false;
        }

        try {
            int i = Integer.parseInt(s);

        } catch (NumberFormatException nfe) {
            return false;
        }

        int i = Integer.parseInt(s);

        return i >= 1 && i <= 31;
    }



}
