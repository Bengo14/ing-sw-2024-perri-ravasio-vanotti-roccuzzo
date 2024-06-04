package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.view.View;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
//import javafx.scene.*;
//import javafx.scene.media.Media;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

public class ViewGui extends Application implements View, Initializable {

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
    private EndGameController endGameController;

    private PlayerTable localPlayerTable;
    private Objectives[] objectives;
    private ResourceCard[] cardsOnTable; //cards on table that can be picked up
    private GoldCard goldCardOnTop; //NOT on playerTable
    private ResourceCard resourceCardOnTop; //NOT on playerTable
    private int globalPoints = 0; //NOT on playerTable
    private int boardPoints = 0;  //NOT on playerTable
    private String[] nicknames;

    @FXML
    private Label nickLabel;
    @FXML
    private Label turnLabel;
    private int id_gold_1,id_gold_2,id_res_1,id_res_2,id_deck_gold,id_deck_res,id_obj_1,id_obj_2;
    @FXML
    private ImageView deck_gold,gold_1,gold_2;
    @FXML
    private ImageView deck_res,res_1,res_2;
    @FXML
    private ImageView hand_0,hand_1,hand_2;
    @FXML
    private ImageView secret_obj,obj_1,obj_2;
    @FXML
    private Pane pos_0,pos_1,pos_2,pos_3,pos_4,pos_5,pos_6,pos_7,pos_8,pos_9,pos_10;
    @FXML
    private Pane pos_11,pos_12,pos_13,pos_14,pos_15,pos_16,pos_17,pos_18,pos_19,pos_20;
    @FXML
    private Pane pos_21,pos_22,pos_23,pos_24,pos_25,pos_26,pos_27,pos_28,pos_29;
    @FXML
    private ScrollPane boardScrollPane;
    @FXML
    private Pane board;
    @FXML
    private ImageView[][] cardMatrix = new ImageView[64][64];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Abilita il panning
        boardScrollPane.setPannable(true);

        // Gestisci lo zoom
        boardScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();
                if (deltaY < 0) {
                    zoomFactor = 0.95;
                }
                board.setScaleX(board.getScaleX() * zoomFactor);
                board.setScaleY(board.getScaleY() * zoomFactor);
                event.consume();
            }
        });
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                ImageView imageView = new ImageView();
                imageView.setFitWidth(70);
                imageView.setFitHeight(55);
                imageView.setLayoutX(110 * j);
                imageView.setLayoutY(29 * i);
                // imageView.setId("card_"+i+"_"+j); // Assegna un ID univoco basato sulla posizione nella matrice
                board.getChildren().add(imageView);
                cardMatrix[i][j] = imageView;
            }
        }
        nickLabel.setVisible(true);
        turnLabel.setVisible(true);

        secret_obj.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+getSecretObjective().getId()+".png")));
        // secret_obj.setImage(secret_obj.getImage());
        // Get the cards from the client or another data source
        //ResourceCard[] deckCards =.getDeckCards();
        //ResourceCard[] revealedCards = client.getRevealedCards();
        // cardMatrix[32][32].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+startingCard.getId()+".png")));
        // Set the images for the hand cards
//        playerTable.setCardsOnHand();
//        hand_0.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+cardsOnHand[0].getId()+".png")));
//        hand_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+cardsOnHand[1].getId()+".png")));
//        hand_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+cardsOnHand[2].getId()+".png")));


//        deck_res.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+id_deck_res+".png")));
//        deck_gold.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+id_deck_gold+".png")));

//        // Set the images for the revealed cards
//        res_1.setImage(new Image(getClass().getResourceAsStream("/path/to/card/images/" + revealedCards[0].getId() + ".png")));
//        res_2.setImage(new Image(getClass().getResourceAsStream("/path/to/card/images/" + revealedCards[1].getId() + ".png")));
//        // Set the images for the gold cards
//        gold_1.setImage(new Image(getClass().getResourceAsStream("/path/to/card/images/" + revealedCards[2].getId() + ".png")));
//        gold_2.setImage(new Image(getClass().getResourceAsStream("/path/to/card/images/" + revealedCards[3].getId() + ".png")));

        nickLabel.setText(localPlayerTable.getNickName());
        nickLabel.setStyle("-fx-font-weight: bold");
        nickLabel.setVisible(true);
    }


    public ViewGui() {
        scenes = new HashMap<>();
        scenes.put("PreGame", "/it/polimi/sw/gianpaolocugola47/fxml/PreGameFXML.fxml");
        scenes.put("Login", "/it/polimi/sw/gianpaolocugola47/fxml/LoginFXML.fxml");
        scenes.put("StartingCard", "/it/polimi/sw/gianpaolocugola47/fxml/StartingCardFXML.fxml");
        scenes.put("SecretObj", "/it/polimi/sw/gianpaolocugola47/fxml/SecretObjFXML.fxml");
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
    public void initView(String nickname, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) throws IOException {
        Platform.runLater(() -> {
            localPlayerTable.setNickname(nickname);
            this.objectives = globalObjectives;
            this.cardsOnTable = cardsOnTable;
            localPlayerTable.setCardsOnHand(cardsOnHand);
            System.out.println("init");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/sw/gianpaolocugola47/fxml/GameFXML.fxml"));
            loader.setController(this);
            Parent root;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(new Scene(root));
            stage.show();
        });
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
    public void showTurn() {
        if(client.isItMyTurn()) {
            turnLabel.setText(localPlayerTable.getNickName()+"'s turn");
            turnLabel.setStyle("-fx-font-weight: bold");
            turnLabel.setVisible(true);
        }else{
            turnLabel.setText("It's not "+localPlayerTable.getNickName()+"'s turn");
            turnLabel.setStyle("-fx-font-weight: bold");
            turnLabel.setVisible(true);
        }
    }

}
