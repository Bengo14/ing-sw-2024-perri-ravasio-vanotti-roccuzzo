package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.network.rmi.RMIClient;
import it.polimi.sw.gianpaolocugola47.network.socket.SocketClient;
import it.polimi.sw.gianpaolocugola47.view.View;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class ViewGui extends Application implements View {

    private Client client;
    private Stage stage;
    private FXMLLoader fxmlLoader;
    private Scene scene;
    private final HashMap<String, String> scenes = new HashMap<>();
    private LoginController loginController;
    private PreGameController preGameController;
    private GameController gameController;
    private EndGameController endGameController;
    private final PlayerTable localPlayerTable = new PlayerTable(0);
    private Objectives[] objectives;
    private ResourceCard[] cardsOnTable;
    private GoldCard goldCardOnTop;
    private ResourceCard resourceCardOnTop;
    private int globalPoints = 0;
    private int boardPoints = 0;
    private String[] nicknames;
    private String nickname;

    public static void main(String[] args) {
        launch(args);
    }

    public ViewGui() {
        scenes.put("PreGame", "/it/polimi/sw/gianpaolocugola47/fxml/PreGameFXML.fxml");
        scenes.put("Login", "/it/polimi/sw/gianpaolocugola47/fxml/LoginFXML.fxml");
        scenes.put("Game", "/it/polimi/sw/gianpaolocugola47/fxml/GameFXML.fxml");
        scenes.put("EndGame", "/it/polimi/sw/gianpaolocugola47/fxml/EndGameFXML.fxml");
        fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource(scenes.get("PreGame")));
//        try {
//            scene = new Scene(fxmlLoader.load());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/backGround/frontPage.jpeg")));
        stage.setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setMinWidth(1280);
        stage.setMinHeight(760);
        setScene("PreGame");
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> setScene("Game"));
        delay.play();
        stage.show();
    }

    public void setScene(String sceneName) {
        Platform.runLater(() -> {
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(scenes.get(sceneName)));
            try {
                Scene newScene = new Scene(fxmlLoader.load());
                stage.setScene(newScene);
                //scene.setRoot(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading scene " + sceneName);
                return;
            }

            switch (sceneName) {
                case "PreGame":
                    preGameController = fxmlLoader.getController();
                    if (preGameController != null) {
                        preGameController.setClient(client);
                    } else {
                        System.err.println("PreGameController is null");
                    }
                    break;
                case "Game":
                    gameController = fxmlLoader.getController();
                    if (gameController != null) {
                        gameController.setClient(client);
                        this.nickname = this.client.getNicknameLocal();
                    } else {
                        System.err.println("GameController is null");
                    }
                    break;
                case "EndGame":
                    endGameController = fxmlLoader.getController();
                    if (endGameController == null) {
                        System.err.println("EndGameController is null");
                    }
                    break;
            }

            stage.setScene(scene);
            stage.setResizable(true);

            stage.setTitle("Codex Naturalis");
            stage.setMinWidth(1280);
            stage.setMinHeight(720);
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
        }
    }

    protected static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(semaphore::release);
        semaphore.acquire();
    }

    @Override
    public void start() {
        launch();
    }

    @Override
    public void setId(int id) {
        this.localPlayerTable.setId(id);
    }

    @Override
    public void setNickname(String nickname) {
        this.localPlayerTable.setNickname(nickname);
    }

    @Override
    public void initView(String nickname, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {
        this.localPlayerTable.setNickname(nickname);
        this.objectives = globalObjectives;
        this.cardsOnTable = cardsOnTable;
        this.localPlayerTable.setCardsOnHand(cardsOnHand);
    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) {
        this.resourceCardOnTop = resourceCardOnTop;
        this.goldCardOnTop = goldCardOnTop;
    }

    @Override
    public void updatePoints(int boardPoints, int globalPoints) {
        this.boardPoints = boardPoints;
        this.globalPoints = globalPoints;
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
    public int getGlobalPoints() {
        return this.globalPoints;
    }

    @Override
    public int getBoardPoints() {
        return this.boardPoints;
    }

    @Override
    public void setClient(RMIClient client) {
        this.client = client;
        this.localPlayerTable.setId(client.getIdLocal());
    }

    @Override
    public void setClient(SocketClient client) {
        this.client = client;
        this.localPlayerTable.setId(client.getIdLocal());
    }

    public void nicknameAlreadyUsed() {
        loginController.nicknameAlreadyUsed();
    }
}