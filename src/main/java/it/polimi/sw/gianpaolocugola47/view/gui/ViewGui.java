package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;
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
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * This class is the GUI view of the game.
 * It extends the Application class and implements the View interface.
 * It contains the methods to start the view, set the client of the view, set the scene of the view, reset the scene of the view.
 */
public class ViewGui extends Application implements View {

    private Client client;
    private Stage stage;
    private FXMLLoader fxmlLoader;
    private Scene scene;
    private Scene oldScene;
    private final HashMap<String, String> scenes;
    private int idWinner;
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
    private boolean isLoaded;

    /**
     * Constructor of the class
     * It creates a new HashMap with the scenes of the game
     * @param isLoaded : true if the game is loaded from a previous save, false otherwise.
     */
    public ViewGui(boolean isLoaded) {
        scenes = new HashMap<>();
        this.isLoaded = isLoaded;
        scenes.put("PreGame", "/it/polimi/sw/gianpaolocugola47/fxml/PreGameFXML.fxml");
        scenes.put("StartingCard", "/it/polimi/sw/gianpaolocugola47/fxml/StartingCardFXML.fxml");
        scenes.put("EndGame", "/it/polimi/sw/gianpaolocugola47/fxml/EndGameFXML.fxml");
        scenes.put("Game", "/it/polimi/sw/gianpaolocugola47/fxml/GameFXML.fxml");
        scenes.put("OtherBoard", "/it/polimi/sw/gianpaolocugola47/fxml/OtherBoardFXML.fxml");
    }

    /**
     * This method sets the client of the view
     * @param client the client that is using the view
     */
    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * This method starts the view
     */
    @Override
    public void start() {
        try {
            start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method starts the view
     * @param stage the stage of the view
     */
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
        if (!isLoaded) {
            delay.setOnFinished(event -> setScene("StartingCard"));
            delay.play();
        } else {
            client.startGameFromFile();
            localPlayerTable.setStartingCard((StartingCard) client.getPlacedCards(client.getIdLocal())[PlayerTable.STARTING_CARD_POS][PlayerTable.STARTING_CARD_POS]);
            localPlayerTable.setSecretObjective(client.getSecretObjective());
        }
    }

    /**
     * This method sets the scene of the view, given the name of the scene.
     * It loads the fxml file of the scene and sets the controller of the scene
     * @param sceneName the name of the scene
     */
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
                    endGameController.setClient(client);
                    endGameController.setStage(stage);
                    break;
            }
            stage.show();
        });
    }

    /**
     * This method creates the logout button and the allert that asks if the user wants to logout.
     * @param primaryStage the stage of the view
     */
    public void logOut(Stage primaryStage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Logout from Codex Naturalis");
            alert.setContentText("Are you sure you want to logout?");
            Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/backGround/frontPage.jpeg")));
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/it/polimi/sw/gianpaolocugola47/css/Style.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("tooltip");
            alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black;");
            if (alert.showAndWait().get() == ButtonType.OK) {
                System.out.println("Logged out successfully");
                primaryStage.close();
                client.terminateLocal();
            }
        });
    }
    /**
     * This method sets the scene of the view to the other board scene
     * @param id the id of the player
     */
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

    /**
     * This method resets the scene of the view to the previous scene
     */
    public void resetScene() {
        Platform.runLater(() -> {
            this.scene = oldScene;
            stage.setScene(scene);
            stage.show();
        });
    }

    /**
     * This method sets the scene of the view to the game scene
     * @param nicknames the nicknames of the players
     * @param globalObjectives the global objectives of the game
     * @param cardsOnHand the cards on the hand of the player
     * @param cardsOnTable the cards on the table
     */
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
            gameController.updatePoints(boardPoints, globalPoints);
            scene = new Scene(root);
            stage.setScene(scene);
            showTurn();
            stage.show();
        });
    }

    /**
     * This method get the resources counter of the player from the client.
     * @return the resources counter of the player.
     */
    public int[] getResourcesCounter(){
        return client.getResourceCounter(getLocalPlayerTable().getId());
    }

    /**
     * This method updates the deck.
     * @param resourceCardOnTop the resource card on top of the deck.
     * @param goldCardOnTop the gold card on top of the deck.
     * @param drawPos the position of the card drawn.
     */
    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) {
        Platform.runLater(() -> {
            if(drawPos == 0 || drawPos == 1) {
                cardsOnTable[drawPos] = this.resourceCardOnTop;
                cardsOnTable[drawPos].setFront(true);
            }
            if(drawPos == 2 || drawPos == 3) {
                cardsOnTable[drawPos] = this.goldCardOnTop;
                cardsOnTable[drawPos].setFront(true);
            }
            this.resourceCardOnTop = resourceCardOnTop;
            this.goldCardOnTop = goldCardOnTop;
            gameController.updateDecks(resourceCardOnTop, goldCardOnTop, drawPos);
        });
    }

    /**
     * This method updates the points of the player.
     * @param boardPoints the points of the player.
     * @param globalPoints the global points of the player.
     */
    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        Platform.runLater(() -> {
            this.boardPoints = boardPoints;
            this.globalPoints = globalPoints;
            gameController.updatePoints(boardPoints, globalPoints);
        });
    }

    /**
     * This method shows the turn of the player.
     */
    @Override
    public void showTurn() { // called by client when turn status changes
        Platform.runLater(() -> {
            if(client.isItMyTurn())
                gameController.setTurnLabelText(localPlayerTable.getNickName() + ", it's your turn!");
            else gameController.setTurnLabelText(localPlayerTable.getNickName() + ", it's not your turn...");
        });
    }

    /**
     * This method receives a message from the client.
     * @param message the message received.
     */
    @Override
    public void receiveMessage(ChatMessage message) {
        Platform.runLater(() -> gameController.receiveMessage(message));
    }

    /**
     * This method shows the winner of the game.
     */
    @Override
    public void gameOver() {
        int max = 0;
        for (int i = 0; i < globalPoints.length; i++) {
            if (globalPoints[i] > max) {
                max = globalPoints[i];
                idWinner = i;
            }
        }
    }

    /**
     * This method shows the winner of the game.
     * It sets the scene to the end game scene.
     * @param id the id of the winner.
     */
    @Override
    public void showWinner(int id) {
        setScene("EndGame");
    }

    /**
     * This method get the secret objective of the player from the client.
     * @return the secret objective of the player.
     */
    @Override
    public Objectives getSecretObjective() {
        return localPlayerTable.getSecretObjective();
    }

    /**
     * This method get the starting card of the player from the client.
     * @return the starting card of the player.
     */
    @Override
    public StartingCard getStartingCard() {
        return localPlayerTable.getStartingCard();
    }

    /**
     * This method show the turn of the player.
     * @return true if it's the turn of the player, false otherwise.
     */
    protected boolean isItMyTurn() {
        return client.isItMyTurn();
    }

    /**
     * This method gets the playable positions from the client.
     * @return the playable positions.
     */
    public boolean[][] getPlayablePositions() {
        return client.getPlayablePositions();
    }

    /**
     * This method plays a card.
     * @param hand the hand of the player.
     * @param x the x position of the card.
     * @param y the y position of the card.
     * @param corner the corner of the card.
     * @param isFront true if the card is front, false otherwise.
     * @return true if the card can be played, false otherwise.
     */
    protected boolean playCard(int hand, int x, int y, int corner, boolean isFront) {
        return client.playCard(hand, x, y, corner, isFront);
    }

    /**
     * This method gets the loaded condition.
     * @return true if the game is loaded, false otherwise.
     */
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * This method draw a card from the deck.
     * @param position the position of the card drawn.
     */
    protected void drawCard(int position) {

        ResourceCard choice = null;
        if(position==0||position==1||position==2||position==3) {
            choice = cardsOnTable[position];
            cardsOnTable[position] = null;
        }
        if(position == 4)
            choice = resourceCardOnTop;
        if(position == 5)
            choice = goldCardOnTop;

        if(choice != null)
            for(int i = 0; i<3; i++)
                if(getCardsOnHand()[i] == null) {
                    choice.setFront(true);
                    getCardsOnHand()[i] = choice;
                }
        client.drawCard(position);
    }

    /**
     * This method sends a message to other client.
     * @param message the message to send.
     */
    protected void sendMessage(ChatMessage message) {
        if (message.isPrivate())
            client.sendPrivateMessage(message);
        else client.sendMessage(message);
    }

    /**
     * This method gets the local player table
     * @return the local player table
     */
    protected PlayerTable getLocalPlayerTable() {
        return localPlayerTable;
    }

    /**
     * This method gets the cards on the table that can be picked up.
     * @return the cards on the table that can be picked up.
     */
    protected ResourceCard[] getCardsOnTable() {
        return cardsOnTable;
    }

    /**
     * This method gets the objectives of the match.
     * @return the objectives of the match.
     */
    protected Objectives[] getObjectives() {
        return objectives;
    }

    /**
     * This method gets the cards on the hand of the player.
     * @return the cards on the hand of the player.
     */
    protected ResourceCard[] getCardsOnHand() {
        return localPlayerTable.getCardsOnHand();
    }

    /**
     * This method gets all the cards on the hand of the players.
     * @return all the cards on the hand of the players.
     */
    protected ResourceCard[][] getAllCardsOnHand() {
        return client.getCardsOnHand();
    }

    /**
     * This method gets all the placed cards of the players.
     * @param id the id of the player.
     * @return all the placed cards of the players.
     */
    protected PlaceableCard[][] getPlacedCards(int id) {
        return client.getPlacedCards(id);
    }

    /**
     * This method gets the resource card on top of the deck.
     * @return the resource card on top of the deck.
     */
    protected ResourceCard getResourceCardOnTop() {
        return resourceCardOnTop;
    }

    /**
     * This method gets the gold card on top of the deck.
     * @return the gold card on top of the deck.
     */
    protected GoldCard getGoldCardOnTop() {
        return goldCardOnTop;
    }
    /**
     * This method gets the nicknames of the players.
     * @return the nicknames of the players.
     */
    protected String[] getNicknames() {
        return nicknames;
    }
    /**
     * This method gets the Board Points of the players.
     * @return the Board Points of the players.
     */
    protected int[] getBoardPoints() {
        if(boardPoints == null){
            boardPoints = new int[getNicknames().length];
        }
        return boardPoints;
    }
    /**
     * This method gets the Global Points of the players.
     * @return the Global Points of the players.
     */
    protected int[] getGlobalPoints() {
        if(globalPoints == null){
            globalPoints = new int[getNicknames().length];
        }
        return globalPoints;
    }
}