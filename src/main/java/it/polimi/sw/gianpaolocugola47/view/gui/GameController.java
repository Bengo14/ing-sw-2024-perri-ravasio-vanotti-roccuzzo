package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;
import static it.polimi.sw.gianpaolocugola47.view.gui.ViewGui.*;

public class GameController implements Initializable {
    public ResourceCard[][] handCards;
    Objectives secretObj;
    private Client client;
    private String nickname = null;
    private PlayerTable playerTable;

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
    }

    public void start(Client client, PlayerTable playerTable) {
        this.client = client;
        this.playerTable = playerTable;
        nickLabel.setVisible(true);
        turnLabel.setVisible(true);

        secret_obj.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+secretObj.getId()+".png")));
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
    }
    /**
     * Set the nickname of the player that is using this GUI.
     * @param nickname The nickname of the player
     */
    public void setMyNickname(String nickname) {
        this.nickname = nickname;
        this.nickname = client.getNicknameLocal();
        nickLabel.setText(nickname);
        nickLabel.setStyle("-fx-font-weight: bold");
        nickLabel.setVisible(true);
    }
    public void showTurn(){
        if(client.isItMyTurn()) {
            turnLabel.setText(nickname+"'s turn");
            turnLabel.setStyle("-fx-font-weight: bold");
            turnLabel.setVisible(true);
        }else{
            turnLabel.setText("It's not "+nickname+"'s turn");
            turnLabel.setStyle("-fx-font-weight: bold");
            turnLabel.setVisible(true);
        }
    }

}
