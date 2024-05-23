package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.network.Client;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setClient(Client client) {
        this.client = client;
    }
}
