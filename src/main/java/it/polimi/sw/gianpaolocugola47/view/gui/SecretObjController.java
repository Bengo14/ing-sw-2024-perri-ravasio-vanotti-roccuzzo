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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.sw.gianpaolocugola47.view.gui.StartingCardController.getSelectedStartingCard;

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
    private static StartingCard selectedStartingCard;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void start(Client client, PlayerTable playerTable) {
        this.client = client;
        this.playerTable = playerTable;
        selectedStartingCard = playerTable.getStartingCard();
        Objectives[] objectives = client.setStartingCardAndDrawObjectives();
        secretObjective1 = objectives[0];
        secretObjective2 = objectives[1];
        String imagePath1 = "/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+secretObjective1.getId()+".png";
        secret_1.setImage(new Image(getClass().getResourceAsStream(imagePath1)));
        String imagePath2 = "/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+secretObjective2.getId()+".png";
        secret_2.setImage(new Image(getClass().getResourceAsStream(imagePath2)));
    }

    @FXML
    private void handleSecret1Clicked(MouseEvent event) {
        selectedObjective = secretObjective1;
        setImageViewBorder(secret_1);
    }

    @FXML
    private void handleSecret2Clicked(MouseEvent event) {
        selectedObjective = secretObjective2;
        setImageViewBorder(secret_2);
    }

    @FXML
    private void handleObjButtonClicked(ActionEvent event) {
        if (selectedObjective != null) {
            playerTable.setSecretObjective(selectedObjective);
            client.setSecretObjective();
            label.setText("Ok! Now wait for all the players to choose their objective...");
            obj_button.setDisable(true);
            secret_1.setDisable(true);
            secret_2.setDisable(true);
        } else {
            label.setText("Please select an objective");
        }
    }

    private void setImageViewBorder(ImageView selectedImageView) {
        secret_1.getStyleClass().remove("selected-image");
        secret_2.getStyleClass().remove("selected-image");
        selectedImageView.getStyleClass().add("selected-image");
    }
}
