package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.network.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController implements Initializable {
    @FXML
    private TextField nickname_LBL;
    @FXML
    private VBox nickname_VB;

    private Client client;

    /**
     * @return The client associated with this controller
     */
    Client getClient() {
        return client;
    }

    /**
     * Link this controller to the client that is using it.
     * @param client The client that is using this controller
     */
    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    void sendNickname(ActionEvent event) {
        String nickname = nickname_LBL.getText();
        if (nickname.isEmpty() || !validateString(nickname) || nickname.length() >= 15) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Nickname not inserted");
            alert.showAndWait();
        }else {
            //this.client.setNickname(nickname);
        }
    }

    void nicknameAlreadyUsed() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Nickname already used");
        alert.showAndWait();
    }

    /**
     * Check if the string is valid, i.e. it contains only letters and numbers.
     * @param input The string to check
     * @return True if the string is valid, false otherwise
     */
    private boolean validateString(String input) {
        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourcesBundle) {
        nickname_VB.setManaged(true);
        nickname_VB.setVisible(true);
    }
}