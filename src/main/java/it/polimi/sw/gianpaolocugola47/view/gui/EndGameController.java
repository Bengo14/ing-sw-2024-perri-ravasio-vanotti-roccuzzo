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
    private final TableView<PlayerTable> resultsTable = new TableView<>();

    @FXML
    private VBox leaderboardPane;

    @FXML
    private Label winner_LBL;

    public void showResults(int[] globalPoints, String[] nicknames) {
     /* todo to be updated using new parameters (num of players by Array.length + points + nicknames)

        // Create columns for the TableView
        TableColumn<PlayerTable, String> nicknameColumn = new TableColumn<>("Nickname");
        nicknameColumn.setCellValueFactory(new PropertyValueFactory<>("nickname")); // Assuming PlayerTable has a "nickname" property

        TableColumn<PlayerTable, Integer> pointsColumn = new TableColumn<>("Points");
        pointsColumn.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(mainTable.getGlobalPoints(cellData.getValue().getId())).asObject());
        // Add the columns to the TableView
        resultsTable.getColumns().add(nicknameColumn);
        resultsTable.getColumns().add(pointsColumn);

        // Create an ObservableList to hold the PlayerTables
        ObservableList<PlayerTable> resultsData = FXCollections.observableArrayList();

        // Add the PlayerTables to the ObservableList
        for (int i = 0; i < mainTable.getNumOfPlayers(); i++) {
            resultsData.add(mainTable.getPlayerTable(i));
        }

        // Sort the ObservableList based on the points
        resultsData.sort((player1, player2) -> Integer.compare(mainTable.getGlobalPoints(player2.getId()), mainTable.getGlobalPoints(player1.getId())));

        // Set the ObservableList as the items for the TableView
        resultsTable.setItems(resultsData);

        // Add the TableView to the leaderboardPane
        leaderboardPane.getChildren().add(resultsTable);

        // Display the winner
        winner_LBL.setText("The winner is: " + resultsData.get(0).getNickName());*/
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

//    public void handleExitGame() {
//        javafx.application.Platform.exit();
//        System.exit(0);
//    }
    @Override
    public void initialize(URL url, ResourceBundle resources) {

    }
}
