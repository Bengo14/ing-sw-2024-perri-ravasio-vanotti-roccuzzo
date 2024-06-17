package it.polimi.sw.gianpaolocugola47.view.gui;

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
import javafx.scene.input.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * This class is the controller of the starting card scene.
 * It contains the methods to set the starting card of the player and to move to the next scene.
 */
public class StartingCardController implements Initializable {

    @FXML
    ImageView starting_front;
    @FXML
    ImageView starting_back;
    @FXML
    Button choice_button;
    private static StartingCard selectedStartingCard;
    private Client client;
    private PlayerTable playerTable;
    private boolean front = true;
    private boolean isStartingCardSelected = false;
    @FXML
    Label label;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    /**
     * This method is called when the controller is created.
     * It sets the client and the player table that are using this controller.
     * It also sets the starting card of the player.
     * @param client The client that is using this controller
     * @param playerTable The player table that is using this controller
     */
    public void start(Client client, PlayerTable playerTable) {
        this.client = client;
        this.playerTable = playerTable;

        if (client != null) {
            selectedStartingCard = client.drawStartingCard();
            int id_start = selectedStartingCard.getId();
            String frontImagePath = "/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+id_start+".png";
            Image frontImage = new Image(getClass().getResourceAsStream(frontImagePath));
            starting_front.setImage(frontImage);
            String backImagePath = "/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+id_start+".png";
            Image backImage = new Image(getClass().getResourceAsStream(backImagePath));
            starting_back.setImage(backImage);
        }
    }
    /**
     * @return The selected starting card
     */
    public static StartingCard getSelectedStartingCard() {
        return selectedStartingCard;
    }
    /**
     * This method is called when the confirm button is clicked.
     * It sets the front of the starting card and moves to the next scene.
     * @param event The event that triggered the method
     */
    @FXML
    private void handleConfirmButtonClicked(ActionEvent event) {

        if (!isStartingCardSelected) {
            label.setText("Please, select a starting card");
            return;
        }
        selectedStartingCard.setFront(front);
        playerTable.setStartingCard(selectedStartingCard);
        choice_button.setDisable(true);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/sw/gianpaolocugola47/fxml/SecretObjFXML.fxml"));
            Parent root = loader.load();
            choice_button.getScene().setRoot(root);
            SecretObjController controller = loader.getController();
            controller.start(client, playerTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * This method is called when the front of the starting card is clicked.
     * It sets the front of the starting card and sets the border of the image view.
     * @param event The event that triggered the method
     */
    @FXML
    private void handleStartingFrontClicked(MouseEvent event) {
        front = false;
        isStartingCardSelected = true;
        setImageViewBorder(starting_front);
    }
    /**
     * This method is called when the back of the starting card is clicked.
     * It sets the front of the starting card and sets the border of the image view.
     * @param event The event that triggered the method
     */
    @FXML
    private void handleStartingBackClicked(MouseEvent event) {
        front = true;
        isStartingCardSelected = true;
        setImageViewBorder(starting_back);
    }
    /**
     * This method sets the border of the selected image view.
     * @param selectedImageView The image view that has to be selected
     */
    private void setImageViewBorder(ImageView selectedImageView) {
        starting_front.getStyleClass().remove("selected-image");
        starting_back.getStyleClass().remove("selected-image");
        selectedImageView.getStyleClass().add("selected-image");
    }
}
