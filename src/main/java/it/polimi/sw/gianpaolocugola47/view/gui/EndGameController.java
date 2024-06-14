package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.PlayerTable;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EndGameController implements Initializable {
    private Stage stage;
    @FXML
    private Button ExitGame_BTN;
    @FXML
    private Label nameWinnerLable;
    @FXML
    private VBox leaderboardPane;

    @FXML
    private Label winner_LBL;


    public void showResults(int[] globalPoints, String[] nicknames) {
     // todo to be updated using new parameters (num of players by Array.length + points + nicknames)
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

    public void logOut(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Press OK to logout, Cancel to stay logged in");

        if(alert.showAndWait().get() == ButtonType.OK){
            stage = (Stage) leaderboardPane.getScene().getWindow();
            System.out.println("Logged out successfully");
            stage.close();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resources) {

    }
}
