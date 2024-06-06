package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.PlaceableCard;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;
import it.polimi.sw.gianpaolocugola47.view.View;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

import java.net.URL;
import java.util.ResourceBundle;


public class GameController implements Initializable {

    @FXML
    private TextField chatInput;
    @FXML
    private ListView<String> chat;
    @FXML
    private Button sendButton;
    @FXML
    private ListView<String> rankingList;
    @FXML
    private Label turnLabel;
    @FXML
    private ImageView deck_gold,gold_1,gold_2;
    @FXML
    private ImageView deck_res,res_1,res_2;
    @FXML
    private ImageView hand_0,hand_1,hand_2;
    @FXML
    private ImageView secret_obj,obj_1,obj_2;
    @FXML
    private ImageView pos_0,pos_1,pos_2,pos_3,pos_4,pos_5,pos_6,pos_7,pos_8,pos_9,pos_10;
    @FXML
    private ImageView pos_11,pos_12,pos_13,pos_14,pos_15,pos_16,pos_17,pos_18,pos_19,pos_20;
    @FXML
    private ImageView pos_21,pos_22,pos_23,pos_24,pos_25,pos_26,pos_27,pos_28,pos_29;
    @FXML
    private ScrollPane boardScrollPane;
    @FXML
    private Label globalPointsLabel;
    @FXML
    private ImageView idPawn,isFirstPawn;
    @FXML
    private Pane boardPane;
    private Group boardGroup = new Group();

    private ImageView[][] cardMatrix = new ImageView[64][64];
    // Variables to track mouse position and board offset
    private double mouseX;
    private double mouseY;
    private double boardOffsetX;
    private double boardOffsetY;
    @FXML
    private Button switch_1,switch_2,switch_3;

    private ViewGui gui;
    int selectedCard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeBoard();
        addZoomFunctionality();
        boardPane.setScaleX(2.0);
        boardPane.setScaleY(2.0);
        boardScrollPane.setHvalue(0.5);
        boardScrollPane.setVvalue(0.5);

    }
    private void addZoomFunctionality() {
        boardPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) {
                double delta = 1.2;
                double scale = boardPane.getScaleX(); // Usa solo la scala X, ma potresti usare la Y se necessario
                if (event.getDeltaY() < 0) {
                    scale /= delta;
                } else {
                    scale *= delta;
                }
                boardPane.setScaleX(scale);
                boardPane.setScaleY(scale); // Aggiorna anche la scala Y
                event.consume();
            }
        });
    }
    private void setImageViewBorder(ImageView card) {
        hand_0.getStyleClass().remove("selected-image");
        hand_1.getStyleClass().remove("selected-image");
        hand_2.getStyleClass().remove("selected-image");
        card.getStyleClass().add("selected-image");
    }
    @FXML
    private void handleHand0Click(MouseEvent event) {
        setImageViewBorder(hand_0);
        selectedCard = 0;
    }
    @FXML
    private void handleHand1Click(MouseEvent event) {
        setImageViewBorder(hand_1);
        selectedCard = 1;
    }
    @FXML
    private void handleHand2Click(MouseEvent event) {
        setImageViewBorder(hand_2);
        selectedCard = 2;
    }
//    @FXML
//    public void handleBoardClick(MouseEvent event) {
//        PlaceableCard card = gui.getLocalPlayerTable().getCardOnHand(selectedCard);
//        if (card != null) {
//            int x = (int) event.getX() / 110;
//            int y = (int) event.getY() / 29;
//            if(gui.getLocalPlayerTable().isPlaceable(x,y)){
//                if (x >= 0 && x < 64 && y >= 0 && y < 64) {
//                    if (card.isFront()) {
//                       cardMatrix[y][x].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+card.getId()+".png")));
//                    } else {
//                       cardMatrix[y][x].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+card.getId()+".png")));
//                    }
//                    gui.getLocalPlayerTable().checkAndPlaceCard(selectedCard, x, y, 0 );
//                }
//            }
//        }
//    }
    private void addDragFunctionality() {
        boardPane.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
            boardOffsetX = boardPane.getLayoutX();
            boardOffsetY = boardPane.getLayoutY();
        });
        boardPane.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;
            boardPane.setLayoutX(boardOffsetX + deltaX);
            boardPane.setLayoutY(boardOffsetY + deltaY);
        });
    }
    private void centerBoardPane() {
        boardPane.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            boardPane.setLayoutX((boardScrollPane.getViewportBounds().getWidth() - newBounds.getWidth()) / 2);
            boardPane.setLayoutY((boardScrollPane.getViewportBounds().getHeight() - newBounds.getHeight()) / 2);
        });
    }

    private void initializeBoard(){

        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                ImageView imageView = new ImageView();
                imageView.setFitWidth(70);
                imageView.setFitHeight(55);
                imageView.setLayoutX(110 * j);
                imageView.setLayoutY(29 * i);
                //imageView.setOnMouseClicked(event -> handleBoardClick(event));
                boardPane.getChildren().add(imageView);
                cardMatrix[i][j] = imageView;

            }
        }
    }
    public void start(ViewGui gui) {
        this.gui = gui;

        if (gui.getStartingCard().isFront()) {
            cardMatrix[32][32].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getStartingCard().getId()+".png")));
        } else {
            cardMatrix[32][32].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+gui.getStartingCard().getId()+".png")));
        }
        cardMatrix[0][0].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_0.png")));
        cardMatrix[63][63].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_1.png")));


        secret_obj.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getSecretObjective().getId()+".png")));
        hand_0.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getLocalPlayerTable().getCardOnHand(0).getId()+".png")));
        hand_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getLocalPlayerTable().getCardOnHand(1).getId()+".png")));
        hand_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getLocalPlayerTable().getCardOnHand(2).getId()+".png")));
        obj_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getObjectives()[0].getId()+".png")));
        obj_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getObjectives()[1].getId()+".png")));
        res_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getCardsOnTable()[0].getId()+".png")));
        res_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getCardsOnTable()[1].getId()+".png")));
        gold_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getCardsOnTable()[2].getId()+".png")));
        gold_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getCardsOnTable()[3].getId()+".png")));
        idPawn.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/pawns/pawn_"+gui.getLocalPlayerTable().getId()+".png")));
        if(gui.getLocalPlayerTable().getId()==0){
            isFirstPawn.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/pawns/pawn_first.png")));
        }
        globalPointsLabel.setText("Global points: 0");
        for(int num = 0; num < gui.getNicknames().length; num++){
            pos_0.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/pawns/pawn_"+num+".png")));
        }
        String text;
        for(int i = 0; i<gui.getNicknames().length; i++) {
            text = i + ": " + gui.getNicknames()[i] + " | " + "global points: 0";
            rankingList.getItems().add(text);
        }
        rankingList.setStyle("-fx-font-weight: bold");
        rankingList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        rankingList.setVisible(true);

        turnLabel.setStyle("-fx-font-weight: bold");
        turnLabel.setVisible(true);

        chat.getItems().add("Chat service is on!");
        chat.getItems().add("Type --listPlayers to see who your opponents are.");
        chat.getItems().add("Start a message with '@' to send a private message.");
    }

    public void receiveMessage(ChatMessage message) {
        if(!message.isPrivate())
            chat.getItems().add(message.getSender() + ": " + message.getMessage());
        else chat.getItems().add(message.getSender() + ": psst, " + message.getMessage());
        chat.scrollTo(chat.getItems().size()-1);
        chat.refresh();
    }

    public void setTurnLabelText(String text) {
        turnLabel.setText(text);
    }

    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        rankingList.getItems().clear();
        String text;
        for(int i = 0; i<globalPoints.length; i++) {
            text = i + ": " + gui.getNicknames()[i] + " | " + "global points: "+globalPoints[i];
            rankingList.getItems().add(text);
        }
        rankingList.refresh();

        for(int i = 0; i<boardPoints.length; i++) {
            /*todo*/
        }
    }

    public void updateDecks(ResourceCard res, GoldCard gold) {
        deck_res.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+res.getId()+".png")));
        deck_gold.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+gold.getId()+".png")));
    }

    @FXML
    public void handleRankingClick(MouseEvent event) {
        System.err.println(rankingList.getSelectionModel().getSelectedItem());
        rankingList.getFocusModel().focus(rankingList.getFocusModel().getFocusedIndex());
        rankingList.refresh();
    }

    @FXML
    private void handleSwitch_1(ActionEvent event) {
        switchCardImage(gui.getCardsOnHand()[0], hand_0);
    }
    @FXML
    private void handleSwitch_2(ActionEvent event) {
        switchCardImage(gui.getCardsOnHand()[1], hand_1);
    }
    @FXML
    private void handleSwitch_3(ActionEvent event) {
        switchCardImage(gui.getCardsOnHand()[2], hand_2);
    }
    private void switchCardImage(ResourceCard card, ImageView imageView) {
        card.switchFrontBack();
        String imagePath = "/it/polimi/sw/gianpaolocugola47/graphics/cards/";
        imagePath += card.isFront() ? "front_" : "back_";
        imagePath += card.getId() + ".png";
        imageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
    }

    @FXML
    private void handleChatInput(ActionEvent event) {

        ChatMessage message = new ChatMessage(gui.getLocalPlayerTable().getNickName(), gui.getLocalPlayerTable().getId());
        String line = chatInput.getText();
        chatInput.clear();

        if (line.startsWith("@")) {
            try {
                message.setPrivate(true);
                message.setReceiver(line.substring(1, line.indexOf(" ")));
                message.setMessage(line.substring(line.indexOf(" ") + 1));
                gui.sendMessage(message);
            } catch (StringIndexOutOfBoundsException e) {
                chat.getItems().add("Invalid input, try again...");
            }
        } else if (line.equals("--listPlayers")) {

            chat.getItems().add("list of all players in the lobby: ");
            for(String nickname : gui.getNicknames()) {
                if(nickname.equals(gui.getLocalPlayerTable().getNickName()))
                    chat.getItems().add(nickname + " (you)");
                else
                    chat.getItems().add(nickname);
            }
        } else {
            message.setPrivate(false);
            message.setMessage(line);
            gui.sendMessage(message);
        }
    }
}
