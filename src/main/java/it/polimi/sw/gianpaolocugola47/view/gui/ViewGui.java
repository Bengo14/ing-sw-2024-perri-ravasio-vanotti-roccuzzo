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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
//import javafx.scene.media.Media;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ViewGui extends Application implements View, Initializable {

    private final Client client;
    private Stage stage;
    private FXMLLoader fxmlLoader;
    private Scene scene;
    private final HashMap<String, String> scenes;
    //private Media media;

    private StartingCardController startingCardController;
    private EndGameController endGameController;

    private PlayerTable localPlayerTable;
    private Objectives[] objectives;
    private ResourceCard[] cardsOnTable; //cards on table that can be picked up
    private GoldCard goldCardOnTop; //NOT on playerTable
    private ResourceCard resourceCardOnTop; //NOT on playerTable
    private int[] globalPoints; //NOT on playerTable
    private int[] boardPoints;  //NOT on playerTable
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
    @FXML
    private Button switch_1,switch_2,switch_3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        boardScrollPane.setPannable(true);
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
        secret_obj.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+getSecretObjective().getId()+".png")));
//        if (localPlayerTable.getStartingCard().isFront()){
//            cardMatrix[32][32].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+localPlayerTable.getStartingCard().getId()+".png")));
//        }else{
//            cardMatrix[32][32].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+localPlayerTable.getStartingCard().getId()+".png")));
//        }

        hand_0.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+localPlayerTable.getCardOnHand(0).getId()+".png")));
        hand_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+localPlayerTable.getCardOnHand(1).getId()+".png")));
        hand_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+localPlayerTable.getCardOnHand(2).getId()+".png")));
        obj_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+objectives[0].getId()+".png")));
        obj_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+objectives[1].getId()+".png")));
        res_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+cardsOnTable[0].getId()+".png")));
        res_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+cardsOnTable[1].getId()+".png")));
        gold_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+cardsOnTable[2].getId()+".png")));
        gold_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+cardsOnTable[3].getId()+".png")));

        String text = "";
        for(int i = 0; i<nicknames.length; i++)
            text += i+": "+nicknames[i]+" | "+"global points: 0\n";
        nickLabel.setText(text);
        nickLabel.setStyle("-fx-font-weight: bold");
        nickLabel.setVisible(true);
        turnLabel.setStyle("-fx-font-weight: bold");
        turnLabel.setVisible(true);
        showTurn();
    }


    public ViewGui(Client client) {
        scenes = new HashMap<>();
        scenes.put("PreGame", "/it/polimi/sw/gianpaolocugola47/fxml/PreGameFXML.fxml");
        scenes.put("StartingCard", "/it/polimi/sw/gianpaolocugola47/fxml/StartingCardFXML.fxml");
        scenes.put("SecretObj", "/it/polimi/sw/gianpaolocugola47/fxml/SecretObjFXML.fxml");
        scenes.put("EndGame", "/it/polimi/sw/gianpaolocugola47/fxml/EndGameFXML.fxml");
        scenes.put("Game", "/it/polimi/sw/gianpaolocugola47/fxml/GameFXML.fxml");
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
    @FXML
    public void handelSwitch_1(ActionEvent event){
        System.out.println("Switch 1 button clicked");
        switchCardImage(localPlayerTable.getCardOnHand(0), hand_0);
    }
    @FXML
    public void handelSwitch_2(ActionEvent event){
        System.out.println("Switch 2 button clicked");
        switchCardImage(localPlayerTable.getCardOnHand(1), hand_1);
    }
    @FXML
    public void handelSwitch_3(ActionEvent event){
        System.out.println("Switch 3 button clicked");
        switchCardImage(localPlayerTable.getCardOnHand(2), hand_2);
    }

    private void switchCardImage(PlaceableCard card, ImageView imageView) {
        String imagePath = "/it/polimi/sw/gianpaolocugola47/graphics/cards/";
        imagePath+=card.isFront()?"back_":"front_";
        imagePath+=card.getId()+".png";
        imageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
    }
    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) throws IOException {
        Platform.runLater(() -> {
            this.nicknames = nicknames;
            this.objectives = globalObjectives;
            this.cardsOnTable = cardsOnTable;
            localPlayerTable.setCardsOnHand(cardsOnHand);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(scenes.get("Game")));
            loader.setController(this);
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                client.terminateLocal();
            }
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/it/polimi/sw/gianpaolocugola47/css/Style.css").toExternalForm());
            stage.setScene(scene);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double width = screenBounds.getWidth();
            double height = screenBounds.getHeight();
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setX(0);
            stage.setY(0);
            stage.show();
        });
    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) {
        Platform.runLater(() -> {
            this.resourceCardOnTop = resourceCardOnTop;
            this.goldCardOnTop = goldCardOnTop;
            deck_res.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+resourceCardOnTop.getId()+".png")));
            deck_gold.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+goldCardOnTop.getId()+".png")));
        });
    }

    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        Platform.runLater(() -> {
            this.boardPoints = boardPoints;
            this.globalPoints = globalPoints;

            String text = "";
            for(int i = 0; i<nicknames.length; i++)
                text += i+": "+nicknames[i]+" | "+"global points: "+globalPoints[i]+"\n";
            nickLabel.setText(text);
            /*todo update board */
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

    @Override
    public void showTurn() { // called by client when turn status changes
        Platform.runLater(() -> {
            if(client.isItMyTurn())
                turnLabel.setText(localPlayerTable.getNickName() + ", it's your turn!");
            else turnLabel.setText(localPlayerTable.getNickName() + ", it's not your turn...");
        });
    }
}
