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

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class ViewGui extends Application implements View {

    private static Client client;
    private Stage stage;
    private FXMLLoader fxmlLoader;
    private Scene scene;
    private final HashMap<String, String> scenes;
    //private Media media;
    //private MediaPlayer mediaPlayer;
    private LoginController loginController;
    private ChoiceController choiceController;
    private PreGameController preGameController;
    private GameController gameController;
    private EndGameController endGameController;
    private static PlayerTable localPlayerTable;
    private Objectives[] objectives;
    private ResourceCard[] cardsOnTable; //cards on table that can be picked up
    private GoldCard goldCardOnTop; //NOT on playerTable
    private ResourceCard resourceCardOnTop; //NOT on playerTable
    private int globalPoints = 0; //NOT on playerTable
    private int boardPoints = 0;  //NOT on playerTable
    private String[] nicknames;
    private String nickname;




    public ViewGui() {
        this.localPlayerTable = new PlayerTable(0);//id posto a zero di default
        scenes = new HashMap<>();
        scenes.put("PreGame", "/it/polimi/sw/gianpaolocugola47/fxml/PreGameFXML.fxml");
        scenes.put("Login", "/it/polimi/sw/gianpaolocugola47/fxml/LoginFXML.fxml");
        scenes.put("Choice", "/it/polimi/sw/gianpaolocugola47/fxml/ChoiceFXML.fxml");
        scenes.put("Game", "/it/polimi/sw/gianpaolocugola47/fxml/GameFXML.fxml");
        scenes.put("EndGame", "/it/polimi/sw/gianpaolocugola47/fxml/EndGameFXML.fxml");
    }


    public void setClient(Client client) {
        ViewGui.client = client;
        //update the player table with the correct id
        localPlayerTable.setId(client.getIdLocal());
    }

    public static Client getClient() {
        return ViewGui.client;
    }
    public static PlayerTable getPlayerTable() {
        return ViewGui.localPlayerTable;
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
        delay.setOnFinished(event -> setScene("Choice"));
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
            stage.setMaximized(true);
            stage.setTitle("Codex Naturalis");
            switch (sceneName) {
                case "PreGame":
                    preGameController = fxmlLoader.getController();
                    preGameController.setClient(client);
                    break;
                case "Choice":
                    choiceController = fxmlLoader.getController();
                    break;
                case "Login":
                    loginController = fxmlLoader.getController();
                    loginController.setClient(client);
                    break;
                case "Game":
                    gameController = fxmlLoader.getController();
                    this.nickname = this.client.getNicknameLocal();
                    gameController.setClient(client);
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
    public void logOut(Stage primaryStage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Press OK to logout, Cancel to stay logged in");

        if(alert.showAndWait().get() == ButtonType.OK){
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
            Platform.runLater(() -> {
                try {
                    start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

    }

    @Override
    public void setId(int id) {
        this.localPlayerTable.setId(id);
    }
    /**
     * Sets the nickname of the player that is using this GUI.
     * @param nickname The nickname of the player
     */
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

    public void nicknameAlreadyUsed() {
        loginController.nicknameAlreadyUsed();
    }



}
