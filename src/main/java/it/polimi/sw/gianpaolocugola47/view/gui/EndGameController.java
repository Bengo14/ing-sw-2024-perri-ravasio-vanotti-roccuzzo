package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.network.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller of the end game scene.
 * It contains the methods to show the results of the game and to logout.
 */
public class EndGameController implements Initializable {
    private Client client;
    private Stage stage;
    @FXML
    private Button ExitGame_BTN;
    @FXML
    private Label nameWinnerLable;
    @FXML
    private Label winner_LBL;

    /**
     * This method shows the results of the game.
     * @param globalPoints the points of the players
     * @param nicknames the nicknames of the players
     */
    public void showResults(int[] globalPoints, String[] nicknames) {
        //print the player with the highest score
        int max = 0;
        int winner = 0;
        for (int i = 0; i < globalPoints.length; i++) {
            if (globalPoints[i] > max) {
                max = globalPoints[i];
                winner = i;
            }
        }
        nameWinnerLable.setText(nicknames[winner]+" is the winner with "+max+" points!");
    }
    void setStage(Stage stage) {
        this.stage = stage;
    }
    void setClient(Client client) {
        this.client = client;
    }

    /**
     * this method create the logout button
     * @param event the event of the button
     */
    public void logOut(ActionEvent event){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Logout from Codex Naturalis");
            alert.setContentText("Are you sure you want to logout?");
            Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/backGround/frontPage.jpeg")));
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/it/polimi/sw/gianpaolocugola47/css/Style.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("tooltip");
            alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black;");
            if (alert.showAndWait().get() == ButtonType.OK) {
                System.out.println("Logged out successfully");
                stage.close();
                client.terminateLocal();
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resources) {

    }
}