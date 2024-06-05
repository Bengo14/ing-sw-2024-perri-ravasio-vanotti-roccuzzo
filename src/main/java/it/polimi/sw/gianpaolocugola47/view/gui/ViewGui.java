package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.view.View;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;

public class ViewGui extends Application implements View {

    private Client client;
    private Stage stage;
    private FXMLLoader fxmlLoader;
    private Scene scene;
    private final HashMap<String, String> scenes;
    //private Media media;

    private StartingCardController startingCardController;
    private EndGameController endGameController;
    private GameController gameController;

    private PlayerTable localPlayerTable;
    private Objectives[] objectives;
    private ResourceCard[] cardsOnTable; //cards on table that can be picked up
    private GoldCard goldCardOnTop; //NOT on playerTable
    private ResourceCard resourceCardOnTop; //NOT on playerTable
    private int[] globalPoints; //NOT on playerTable
    private int[] boardPoints;  //NOT on playerTable
    private String[] nicknames;


    public ViewGui() {
        scenes = new HashMap<>();
        scenes.put("PreGame", "/it/polimi/sw/gianpaolocugola47/fxml/PreGameFXML.fxml");
        scenes.put("StartingCard", "/it/polimi/sw/gianpaolocugola47/fxml/StartingCardFXML.fxml");
        scenes.put("SecretObj", "/it/polimi/sw/gianpaolocugola47/fxml/SecretObjFXML.fxml");
        scenes.put("EndGame", "/it/polimi/sw/gianpaolocugola47/fxml/EndGameFXML.fxml");
        scenes.put("Game", "/it/polimi/sw/gianpaolocugola47/fxml/GameFXML.fxml");
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void start() {
        try {
            start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        localPlayerTable = new PlayerTable(client.getIdLocal());
        localPlayerTable.setNickname(client.getNicknameLocal());

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/backGround/frontPage.jpeg")));
        stage.setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
        });
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(scenes.get("PreGame")));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Error loading scene PreGame");
            client.terminateLocal();
        }
        this.stage = stage;
        setScene("PreGame");
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> setScene("StartingCard"));
        delay.play();
    }

    public void setScene(String sceneName) {
        
        Platform.runLater(()-> {
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(scenes.get(sceneName)));
            try {
                scene.setRoot(fxmlLoader.load());
            } catch (IOException e) {
                System.out.println("Error loading scene " + sceneName);
                client.terminateLocal();
            }
            scene.getStylesheets().add(getClass().getResource("/it/polimi/sw/gianpaolocugola47/css/Style.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.setTitle("Codex Naturalis");

            switch (sceneName) {
                case "StartingCard":
                    startingCardController = fxmlLoader.getController();
                    startingCardController.start(client, localPlayerTable);
                    break;
                case "EndGame":
                    endGameController = fxmlLoader.getController();
                    endGameController.showResults(globalPoints, nicknames);
                    break;
            }

            stage.setOnCloseRequest(event -> {
                event.consume();
                logOut(stage);
            });
            stage.show();
        });
    }

    public void logOut(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Logout from Codex Naturalis");
        alert.setContentText("Are you sure you want to logout?");
        Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/backGround/frontPage.jpeg")));
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/it/polimi/sw/gianpaolocugola47/css/style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("tooltip");
        alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black;");
        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Logged out successfully");
            primaryStage.close();
            client.terminateLocal();
        }
    }

    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) throws IOException {
        Platform.runLater(() -> {
            for(ResourceCard card : cardsOnTable)
                card.switchFrontBack();
            for(ResourceCard card : cardsOnHand)
                card.switchFrontBack();

            this.nicknames = nicknames;
            this.objectives = globalObjectives;
            this.cardsOnTable = cardsOnTable;
            localPlayerTable.setCardsOnHand(cardsOnHand);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(scenes.get("Game")));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                client.terminateLocal();
            }
            this.gameController = loader.getController();
            gameController.start(this);

            scene = new Scene(root);
            stage.setScene(scene);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double width = screenBounds.getWidth();
            double height = screenBounds.getHeight();
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setX(0);
            stage.setY(0);
            showTurn();
            stage.show();
        });
    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) {
        Platform.runLater(() -> {
            this.resourceCardOnTop = resourceCardOnTop;
            this.goldCardOnTop = goldCardOnTop;
            gameController.updateDecks(resourceCardOnTop, goldCardOnTop);
        });
    }

    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        Platform.runLater(() -> {
            this.boardPoints = boardPoints;
            this.globalPoints = globalPoints;
            gameController.updatePoints(boardPoints, globalPoints);
        });
    }

    @Override
    public StartingCard getStartingCard() {
        return localPlayerTable.getStartingCard();
    }

    @Override
    public Objectives getSecretObjective() {
        return localPlayerTable.getSecretObjective();
    }

    @Override
    public int[] getGlobalPoints() {
        return this.globalPoints;
    }

    @Override
    public int[] getBoardPoints() {
        return this.boardPoints;
    }

    public PlayerTable getLocalPlayerTable() {
        return localPlayerTable;
    }
    public ResourceCard[] getCardsOnTable() {
        return cardsOnTable;
    }
    public Objectives[] getObjectives() {
        return objectives;
    }
    public ResourceCard[] getCardsOnHand() {
        return localPlayerTable.getCardsOnHand();
    }
    public ResourceCard getResourceCardOnTop() {
        return resourceCardOnTop;
    }
    public GoldCard getGoldCardOnTop() {
        return goldCardOnTop;
    }
    public String[] getNicknames() {
        return nicknames;
    }

    @Override
    public void showTurn() { // called by client when turn status changes
        Platform.runLater(() -> {
            if(client.isItMyTurn())
                gameController.setTurnLabelText(localPlayerTable.getNickName() + ", it's your turn!");
            else gameController.setTurnLabelText(localPlayerTable.getNickName() + ", it's not your turn...");
        });
    }
}
