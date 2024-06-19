package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.PlayerTable;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.network.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller of the secret objective scene.
 * It contains the methods to set the secret objective of the player and to send it to the server.
 */
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    /**
     * This method is called when the controller is created.
     * It sets the client and the player table that are using this controller.
     * It also sets the starting card and draws the objectives of the player.
     * @param client The client that is using this controller
     * @param playerTable The player table that is using this controller
     */
    public void start(Client client, PlayerTable playerTable) {
        this.client = client;
        this.playerTable = playerTable;
        Objectives[] objectives = client.setStartingCardAndDrawObjectives();
        secretObjective1 = objectives[0];
        secretObjective2 = objectives[1];
        String imagePath1 = "/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+secretObjective1.getId()+".png";
        secret_1.setImage(new Image(getClass().getResourceAsStream(imagePath1)));
        String imagePath2 = "/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+secretObjective2.getId()+".png";
        secret_2.setImage(new Image(getClass().getResourceAsStream(imagePath2)));
    }

    /**
     * This method is called when the first secret objective is clicked.
     * It sets the selected objective to the first secret objective and sets the border of the image view.
     * @param event The event that triggered the method
     */
    @FXML
    private void handleSecret1Clicked(MouseEvent event) {
        selectedObjective = secretObjective1;
        setImageViewBorder(secret_1);
    }

    /**
     * This method is called when the second secret objective is clicked.
     * It sets the selected objective to the second secret objective and sets the border of the image view.
     * @param event The event that triggered the method
     */
    @FXML
    private void handleSecret2Clicked(MouseEvent event) {
        selectedObjective = secretObjective2;
        setImageViewBorder(secret_2);
    }

    /**
     * This method is called when the send button is clicked.
     * It sets the secret objective of the player and sends it to the server.
     * @param event The event that triggered the method
     */
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

    /**
     * This method sets the border of the selected image view.
     * @param selectedImageView The image view that has to be selected
     */
    private void setImageViewBorder(ImageView selectedImageView) {
        secret_1.getStyleClass().remove("selected-image");
        secret_2.getStyleClass().remove("selected-image");
        selectedImageView.getStyleClass().add("selected-image");
    }
}