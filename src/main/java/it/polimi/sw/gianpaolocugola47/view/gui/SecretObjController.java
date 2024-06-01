package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.PlayerTable;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.network.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.sw.gianpaolocugola47.view.gui.StartingCardController.getSelectedStartingCard;
import static it.polimi.sw.gianpaolocugola47.view.gui.ViewGui.getClient;
import static it.polimi.sw.gianpaolocugola47.view.gui.ViewGui.getPlayerTable;

public class SecretObjController implements Initializable {

    @FXML
    ImageView secret_1;
    @FXML
    ImageView secret_2;
    @FXML
    Button obj_button;
    private Objectives selectedObjective;
    private Client client;
    private PlayerTable playerTable;
    private StartingCard selectedStartingCard;

    public void setClient(Client client) {
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.selectedStartingCard = getSelectedStartingCard();
        this.client = getClient();
        this.playerTable = getPlayerTable();
        if (client != null) {
            Objectives[] objectives = client.setStartingCardAndDrawObjectives();
            if (objectives.length >= 2) {
                int id_obj1 = objectives[0].getId();
                int id_obj2 = objectives[1].getId();
                String obj1ImagePath = "/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+id_obj1+".png";
                Image obj1Image = new Image(getClass().getResourceAsStream(obj1ImagePath));
                secret_1.setImage(obj1Image);
                String obj2ImagePath = "/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+id_obj1+".png";
                Image obj2Image = new Image(getClass().getResourceAsStream(obj2ImagePath));
                secret_2.setImage(obj2Image);
            }

        }
    }
    @FXML
    private void handleSecret1Clicked() {
        // Azioni da eseguire quando l'utente clicca sul primo obiettivo segreto
    }

    @FXML
    private void handleSecret2Clicked() {
        // Azioni da eseguire quando l'utente clicca sul secondo obiettivo segreto
    }
    @FXML
    private void handleObjButtonClicked() {
        // Azioni da eseguire quando l'utente conferma la scelta degli obiettivi segreti
    }
}
