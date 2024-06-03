package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.PlayerTable;
import it.polimi.sw.gianpaolocugola47.network.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private Client client;
    private String nickname = null;
    private PlayerTable playerTable;

    @FXML
    private Label nickLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void start(Client client, PlayerTable playerTable) {
        this.client = client;
        this.playerTable = playerTable;
        nickLabel.setVisible(true);
        this.nickname = client.getNicknameLocal();
        nickLabel.setText(nickname);
        nickLabel.setStyle("-fx-font-weight: bold");
        nickLabel.setVisible(true);
    }
}
