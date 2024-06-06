package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

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
    private Button switch_1,switch_2,switch_3;

    private ImageView[][] cardMatrix = new ImageView[64][64];
    private ViewGui gui;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void start(ViewGui gui) {
        this.gui = gui;

//        boardScrollPane.setPannable(true);
//        boardScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
//            if (event.isControlDown()) {
//                double zoomFactor = 1.05;
//                double deltaY = event.getDeltaY();
//                if (deltaY < 0) {
//                    zoomFactor = 0.95;
//                }
//                board.setScaleX(board.getScaleX() * zoomFactor);
//                board.setScaleY(board.getScaleY() * zoomFactor);
//                event.consume();
//            }
//        });
//        for (int i = 0; i < 64; i++) {
//            for (int j = 0; j < 64; j++) {
//                ImageView imageView = new ImageView();
//                imageView.setFitWidth(70);
//                imageView.setFitHeight(55);
//                imageView.setLayoutX(110 * j);
//                imageView.setLayoutY(29 * i);
//                // imageView.setId("card_"+i+"_"+j); // Assegna un ID univoco basato sulla posizione nella matrice
//                board.getChildren().add(imageView);
//                cardMatrix[i][j] = imageView;
//            }
//        }

//        if (gui.getStartingCard().isFront()) {
//            cardMatrix[32][32].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getStartingCard().getId()+".png")));
//        } else {
//            cardMatrix[32][32].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+gui.getStartingCard().getId()+".png")));
//        }

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
        /*todo show other boards*/
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
