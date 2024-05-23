package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.MainTable;
import it.polimi.sw.gianpaolocugola47.model.PlayerTable;
import it.polimi.sw.gianpaolocugola47.network.Client;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class EndGameController implements Initializable {

    @FXML
    private Button ExitGame_BTN;
    private final TableView<PlayerTable> resultsTable = new TableView<>();

    @FXML
    private VBox leaderboardPane;

    @FXML
    private Label winner_LBL;

    public void showResults(MainTable mainTable) {
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
        winner_LBL.setText("The winner is: " + resultsData.get(0).getNickName()); // Assuming PlayerTable has a "getNickname" method
    }












    public void handleExitGame() {
        javafx.application.Platform.exit();
        System.exit(0);
    }


    @Override
    public void initialize(URL url, ResourceBundle resources) {

    }
}
