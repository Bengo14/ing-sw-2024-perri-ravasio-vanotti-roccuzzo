package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.network.rmi.RMIServer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import static it.polimi.sw.gianpaolocugola47.view.gui.ViewGui.getClient;


public class ChoiceController implements Initializable {
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
    private StartingCard selectedStartingCard;
    private Objectives selectedObjective;
    private Client client;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.client = getClient();
        if (client != null) {
            StartingCard startingCard = client.drawStartingCard();//non pesca la carta, il metodo successivo da nullpointexception
            int id_start = startingCard.getId();
            String frontImagePath = "/graphics/cards/front_"+id_start+".png";
            Image frontImage = new Image(getClass().getResourceAsStream(frontImagePath));
            starting_front.setImage(frontImage);
            String backImagePath = "/graphics/cards/back_"+id_start+".png";
            Image backImage = new Image(getClass().getResourceAsStream(backImagePath));
            starting_back.setImage(backImage);

            Objectives[] objectives = client.setStartingCardAndDrawObjectives();
            if (objectives.length >= 2) {
                int id_obj1 = objectives[0].getId();
                int id_obj2 = objectives[1].getId();
                String obj1ImagePath = "/graphics/cards/front_"+id_obj1+".png";
                Image obj1Image = new Image(getClass().getResourceAsStream(obj1ImagePath));
                secret_1.setImage(obj1Image);
                String obj2ImagePath = "/graphics/cards/front_"+id_obj2+".png";
                Image obj2Image = new Image(getClass().getResourceAsStream(obj2ImagePath));
                secret_2.setImage(obj2Image);
            }
        } else {
            System.err.println("Client is not initialized!");
        }
    }


    @FXML
    private void handleConfirmButtonClicked() {
        // Verifica se sono state fatte tutte le scelte necessarie
        if (selectedStartingCard != null && selectedObjective != null) {


        } else {
            // Mostra un messaggio di errore o gestisci il caso in cui le scelte non sono state fatte
        }

    }
    @FXML
    private void handleStartingFrontClicked() {
        // Azioni da eseguire quando l'utente clicca sul lato frontale della carta iniziale
    }

    @FXML
    private void handleStartingBackClicked() {
        // Azioni da eseguire quando l'utente clicca sul lato posteriore della carta iniziale
    }

    @FXML
    private void handleSecret1Clicked() {
        // Azioni da eseguire quando l'utente clicca sul primo obiettivo segreto
    }

    @FXML
    private void handleSecret2Clicked() {
        // Azioni da eseguire quando l'utente clicca sul secondo obiettivo segreto
    }



}
