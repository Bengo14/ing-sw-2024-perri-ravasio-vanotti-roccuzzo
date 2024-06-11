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

    public static StartingCard getSelectedStartingCard() {
        return selectedStartingCard;
    }

    @FXML
    private void handleConfirmButtonClicked(ActionEvent event) {

        if (!isStartingCardSelected) {
            label.setText("Please, select a starting card");
            return;
        }
        selectedStartingCard.setFront(!front);
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
    @FXML
    private void handleStartingFrontClicked(MouseEvent event) {

        front = true;
        isStartingCardSelected = true;
        setImageViewBorder(starting_front);

    }
    private void setImageViewBorder(ImageView selectedImageView) {
        starting_front.getStyleClass().remove("selected-image");
        starting_back.getStyleClass().remove("selected-image");
        selectedImageView.getStyleClass().add("selected-image");
    }

    @FXML
    private void handleStartingBackClicked(MouseEvent event) {

        front = false;
        isStartingCardSelected = true;
        setImageViewBorder(starting_back);
    }

}
