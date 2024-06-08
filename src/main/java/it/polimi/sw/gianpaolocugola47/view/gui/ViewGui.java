package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;
import it.polimi.sw.gianpaolocugola47.view.View;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;

public class ViewGui extends Application implements View {

    private Client client;
    private Stage stage;
    private FXMLLoader fxmlLoader;
    private Scene scene;
    private Scene oldScene;
    private final HashMap<String, String> scenes;
    //private Media media;

    private StartingCardController startingCardController;
    private EndGameController endGameController;
    private GameController gameController;
    private OtherBoardController otherBoardController;

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
        scenes.put("EndGame", "/it/polimi/sw/gianpaolocugola47/fxml/EndGameFXML.fxml");
        scenes.put("Game", "/it/polimi/sw/gianpaolocugola47/fxml/GameFXML.fxml");
        scenes.put("OtherBoard", "/it/polimi/sw/gianpaolocugola47/fxml/OtherBoardFXML.fxml");
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

        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(scenes.get("PreGame")));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Error loading scene PreGame");
            client.terminateLocal();
        }
        stage.setTitle("Codex Naturalis");
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth();
        double height = screenBounds.getHeight();
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setX(0);
        stage.setY(0);
        stage.setResizable(true);
        stage.setMaximized(true);

        stage.setOnCloseRequest(event -> {
            event.consume();
            logOut(stage);
        });

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

            switch (sceneName) {
                case "StartingCard":
                    startingCardController = fxmlLoader.getController();
                    startingCardController.start(client, localPlayerTable);
                    break;
                case "EndGame":
                    endGameController = fxmlLoader.getController();
                    endGameController.showResults(globalPoints, nicknames);
                    break;
                case "Game":
                    break;
            }
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

    public void setOtherBoardScene(int id) {
        Platform.runLater(() -> {
            this.oldScene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource(scenes.get("OtherBoard")));
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
                client.terminateLocal();
            }
            otherBoardController = fxmlLoader.getController();
            otherBoardController.start(this, id);

            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
    }

    public void resetScene() {
        Platform.runLater(() -> {
            this.scene = oldScene;
            stage.setScene(scene);
            stage.show();
        });
    }

    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {
        Platform.runLater(() -> {
            for(ResourceCard card : cardsOnTable)
                card.switchFrontBack();
            for(ResourceCard card : cardsOnHand)
                card.switchFrontBack();

            this.nicknames = nicknames;
            this.objectives = globalObjectives;
            this.cardsOnTable = cardsOnTable;
            localPlayerTable.setCardsOnHand(cardsOnHand);
            
            fxmlLoader = new FXMLLoader(getClass().getResource(scenes.get("Game")));
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
                client.terminateLocal();
            }
            this.gameController = fxmlLoader.getController();
            gameController.start(this);
            scene = new Scene(root);
            stage.setScene(scene);
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
    public void showTurn() { // called by client when turn status changes
        Platform.runLater(() -> {
            if(client.isItMyTurn())
                gameController.setTurnLabelText(localPlayerTable.getNickName() + ", it's your turn!");
            else gameController.setTurnLabelText(localPlayerTable.getNickName() + ", it's not your turn...");
        });
    }

    @Override
    public StartingCard getStartingCard() {
        return localPlayerTable.getStartingCard();
    }

    public boolean[][] getPlayablePositions() {
        return client.getPlayablePositions();
    }

    @Override
    public Objectives getSecretObjective() {
        return localPlayerTable.getSecretObjective();
    }

    @Override
    public void receiveMessage(ChatMessage message) {
        Platform.runLater(() -> {
            gameController.receiveMessage(message);
        });
    }

    @Override
    public void gameOver() {
        /*todo set game over scene*/
    }

    @Override
    public void showWinner() {
        /*todo set winner scene*/
    }

    protected void sendMessage(ChatMessage message) {
        if (message.isPrivate())
            client.sendPrivateMessage(message);
        else client.sendMessage(message);
    }

    protected PlayerTable getLocalPlayerTable() {
        return localPlayerTable;
    }
    protected ResourceCard[] getCardsOnTable() {
        return cardsOnTable;
    }
    protected Objectives[] getObjectives() {
        return objectives;
    }
    protected ResourceCard[] getCardsOnHand() {
        return localPlayerTable.getCardsOnHand();
    }
    protected ResourceCard[][] getAllCardsOnHand() {
        return client.getCardsOnHand();
    }
    protected PlaceableCard[][] getPlacedCards(int id) {
        return client.getPlacedCards(id);
    }
    protected ResourceCard getResourceCardOnTop() {
        return resourceCardOnTop;
    }
    protected GoldCard getGoldCardOnTop() {
        return goldCardOnTop;
    }
    protected String[] getNicknames() {
        return nicknames;
    }
}
