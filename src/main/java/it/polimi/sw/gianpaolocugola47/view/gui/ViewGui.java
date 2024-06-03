package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.Client;
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
//import javafx.scene.*;
//import javafx.scene.media.Media;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class ViewGui extends Application implements View {

    private Client client;
    private Stage stage;
    private FXMLLoader fxmlLoader;
    private Scene scene;
    private final HashMap<String, String> scenes;
    private Media media;
    //private MediaPlayer mediaPlayer;
    private LoginController loginController;
    private StartingCardController startingCardController;
    private SecretObjController secretObjController;
    private PreGameController preGameController;
    private GameController gameController;
    private EndGameController endGameController;
    private PlayerTable localPlayerTable;
    private Objectives[] objectives;
    private ResourceCard[] cardsOnTable; //cards on table that can be picked up
    private GoldCard goldCardOnTop; //NOT on playerTable
    private ResourceCard resourceCardOnTop; //NOT on playerTable
    private int globalPoints = 0; //NOT on playerTable
    private int boardPoints = 0;  //NOT on playerTable
    private String[] nicknames;
    private String nickname;


    public ViewGui() {
        scenes = new HashMap<>();
        scenes.put("PreGame", "/it/polimi/sw/gianpaolocugola47/fxml/PreGameFXML.fxml");
        scenes.put("Login", "/it/polimi/sw/gianpaolocugola47/fxml/LoginFXML.fxml");
        scenes.put("StartingCard", "/it/polimi/sw/gianpaolocugola47/fxml/StartingCardFXML.fxml");
        scenes.put("SecretObj", "/it/polimi/sw/gianpaolocugola47/fxml/SecretObjFXML.fxml");
        scenes.put("Game", "/it/polimi/sw/gianpaolocugola47/fxml/GameFXML.fxml");
        scenes.put("EndGame", "/it/polimi/sw/gianpaolocugola47/fxml/EndGameFXML.fxml");
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.nicknames = client.getNicknames();
        localPlayerTable = new PlayerTable(client.getIdLocal());
        localPlayerTable.setNickname(client.getNicknameLocal());

        this.stage = stage;
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/backGround/frontPage.jpeg")));
        stage.setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setMinWidth(1280);
        stage.setMinHeight(760);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(scenes.get("PreGame")));
        try{
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Error loading scene PreGame");
            return;
        }
        setScene("PreGame");
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> setScene("StartingCard"));
        delay.play();
        stage.show();
    }

    public void setScene(String sceneName) {
        Platform.runLater(()->{
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(scenes.get(sceneName)));
            try {
                scene.setRoot(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(" Error loading scene " + sceneName);
                return;
            }
            //add css
            scene.getStylesheets().add(getClass().getResource("/it/polimi/sw/gianpaolocugola47/css/Style.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setTitle("Codex Naturalis");

            switch (sceneName) {
                case "PreGame":
                    preGameController = fxmlLoader.getController();
                    preGameController.setClient(client);
                    break;
                case "StartingCard":
                    startingCardController = fxmlLoader.getController();
                    startingCardController.start(client, localPlayerTable);
                    break;
                case "SecretObj":
                    secretObjController = fxmlLoader.getController();
                    secretObjController.start(client, localPlayerTable);
                    break;
                case "Login":
                    loginController = fxmlLoader.getController();
                    loginController.setClient(client);
                    break;
                case "Game":
                    gameController = fxmlLoader.getController();
                    gameController.start(client, localPlayerTable);
                    break;
                case "EndGame":
                    endGameController = fxmlLoader.getController();
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
    protected static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(semaphore::release);
        semaphore.acquire();
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
    public void setId(int id) {
        localPlayerTable.setId(id);
    }
    /**
     * Sets the nickname of the player that is using this GUI.
     * @param nickname The nickname of the player
     */
    @Override
    public void setNickname(String nickname) {
        localPlayerTable.setNickname(nickname);
    }

    @Override
    public void initView(String nickname, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {
        localPlayerTable.setNickname(nickname);
        this.objectives = globalObjectives;
        this.cardsOnTable = cardsOnTable;
        localPlayerTable.setCardsOnHand(cardsOnHand);
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

}
