package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.PlayerTable;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.network.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.sw.gianpaolocugola47.view.gui.StartingCardController.getSelectedStartingCard;
import static it.polimi.sw.gianpaolocugola47.view.gui.ViewGui.getClient;
import static it.polimi.sw.gianpaolocugola47.view.gui.ViewGui.getPlayerTable;

public class SecretObjController implements Initializable {

    @FXML
    ImageView secret_1;
    Objectives secretObjective1;
    @FXML
    ImageView secret_2;
    Objectives secretObjective2;
    @FXML
    Button obj_button;
    @FXML
    Label label;
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
            playerTable.setStartingCard(selectedStartingCard);
            Objectives[] objectives = client.setStartingCardAndDrawObjectives();
            if (objectives.length >= 2) {
                int id_obj1 = objectives[0].getId();
                secretObjective1 = objectives[0];
                int id_obj2 = objectives[1].getId();
                secretObjective2 = objectives[1];
                String obj1ImagePath = "/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+id_obj1+".png";
                Image obj1Image = new Image(getClass().getResourceAsStream(obj1ImagePath));
                secret_1.setImage(obj1Image);
                String obj2ImagePath = "/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+id_obj2+".png";
                Image obj2Image = new Image(getClass().getResourceAsStream(obj2ImagePath));
                secret_2.setImage(obj2Image);
            }

        }
    }
    @FXML
    private void handleSecret1Clicked(MouseEvent event) {
        // Azioni da eseguire quando l'utente clicca sul primo obiettivo segreto
        selectedObjective = secretObjective1;
        setImageViewBorder(secret_1);
    }

    @FXML
    private void handleSecret2Clicked(MouseEvent event) {
        // Azioni da eseguire quando l'utente clicca sul secondo obiettivo segreto
        selectedObjective = secretObjective2;
        setImageViewBorder(secret_2);
    }
    @FXML
    private void handleObjButtonClicked(ActionEvent event) {
        // Azioni da eseguire quando l'utente conferma la scelta degli obiettivi segreti
        if (selectedObjective != null) {
            playerTable.setSecretObjective(selectedObjective);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/sw/gianpaolocugola47/fxml/GameFXML.fxml"));
                Parent root = loader.load();
                obj_button.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            label.setText("Please select an objective");
        }
    }
    private void setImageViewBorder(ImageView selectedImageView) {
        secret_1.getStyleClass().remove("selected-image");
        secret_2.getStyleClass().remove("selected-image");
        selectedImageView.getStyleClass().add("selected-image");
    }
}
