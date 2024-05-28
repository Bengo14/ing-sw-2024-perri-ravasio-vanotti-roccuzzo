package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.network.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private Client client;
    private String nickname = null;
    @FXML
    private Label nickLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nickLabel.setVisible(true);
    }
    /**
     * Set the nickname of the player that is using this GUI.
     * @param nickname The nickname of the player
     */
    public void setMyNickname(String nickname) {
        this.nickname = nickname;
        nickLabel.setText(nickname);
        nickLabel.setStyle("-fx-font-weight: bold");
        nickLabel.setVisible(true);
    }








    /**
     * Link this controller to the client that is using it.
     * @param client The client that is using this controller
     */
    public void setClient(Client client) {
        this.client = client;
    }
}
