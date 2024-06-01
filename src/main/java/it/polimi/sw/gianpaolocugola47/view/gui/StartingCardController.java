package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.PlayerTable;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.network.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.sw.gianpaolocugola47.view.gui.ViewGui.getClient;
import static it.polimi.sw.gianpaolocugola47.view.gui.ViewGui.getPlayerTable;


public class StartingCardController implements Initializable {
    @FXML
    ImageView secret_1;
    @FXML
    ImageView secret_2;
    @FXML
    ImageView starting_front;
    @FXML
    ImageView starting_back;
    @FXML
    Button confirm;
    private static StartingCard selectedStartingCard;
    private Objectives selectedObjective;
    private Client client;
    private PlayerTable playerTable;
    private boolean front = true;
    @FXML
    Label label;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.client = getClient();
        this.playerTable = getPlayerTable();
        if (client != null) {
            StartingCard startingCard = client.drawStartingCard();
            int id_start = startingCard.getId();
            System.out.println("the id is:"+id_start);
            String frontImagePath = "/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+id_start+".png";
            System.out.println(getClass().getResource(frontImagePath));
            Image frontImage = new Image(getClass().getResourceAsStream(frontImagePath));
            starting_front.setImage(frontImage);
            String backImagePath = "/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+id_start+".png";
            Image backImage = new Image(getClass().getResourceAsStream(backImagePath));
            starting_back.setImage(backImage);
        } else {
            System.err.println("Client is not initialized!");
        }
    }

    public static StartingCard getSelectedStartingCard() {
        return selectedStartingCard;
    }
    @FXML
    private void handleConfirmButtonClicked() {
        // Verifica se sono state fatte tutte le scelte necessarie
        if (selectedStartingCard != null) {
            // Azioni da eseguire quando l'utente conferma la scelta
            if(front){
                selectedStartingCard.setFront(true);
            } else {
                selectedStartingCard.setFront(false);
            }
            //passa la starting card al client e passa alla scena successiva
            // Cambia la scena
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/sw/gianpaolocugola47/fxml/SecretObjFXML.fxml"));
                Parent root = loader.load();
                confirm.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            label.setText("Please select a starting card");
        }

    }
    @FXML
    private void handleStartingFrontClicked() {
        // Azioni da eseguire quando l'utente clicca sul lato frontale della carta iniziale
        front = true;
    }

    @FXML
    private void handleStartingBackClicked() {
        // Azioni da eseguire quando l'utente clicca sul lato posteriore della carta iniziale
        front = false;
    }

}
