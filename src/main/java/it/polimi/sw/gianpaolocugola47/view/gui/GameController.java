package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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
    private Label labelAnimal, labelPlant, labelInsect, labelFungi, labelManuscript, labelInkwell, labelQuill;
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
    private ImageView place_0_0,place_0_2,place_0_4,place_0_6,place_0_8,place_0_10,place_0_12,place_0_14,place_0_16,place_0_18,place_0_20,
            place_1_1,place_1_3,place_1_5,place_1_7,place_1_9,place_1_11,place_1_13,place_1_15,place_1_17,place_1_19,place_1_21,
            place_2_0,place_2_2,place_2_4,place_2_6,place_2_8,place_2_10,place_2_12,place_2_14,place_2_16,place_2_18,place_2_20,
            place_3_1,place_3_3,place_3_5,place_3_7,place_3_9,place_3_11,place_3_13,place_3_15,place_3_17,place_3_19,place_3_21,
            place_4_0,place_4_2,place_4_4,place_4_6,place_4_8,place_4_10,place_4_12,place_4_14,place_4_16,place_4_18,place_4_20,
            place_5_1,place_5_3,place_5_5,place_5_7,place_5_9,place_5_11,place_5_13,place_5_15,place_5_17,place_5_19,place_5_21,
            place_6_0,place_6_2,place_6_4,place_6_6,place_6_8,place_6_10,place_6_12,place_6_14,place_6_16,place_6_18,place_6_20,
            place_7_1,place_7_3,place_7_5,place_7_7,place_7_9,place_7_11,place_7_13,place_7_15,place_7_17,place_7_19,place_7_21,
            place_8_0,place_8_2,place_8_4,place_8_6,place_8_8,place_8_10,place_8_12,place_8_14,place_8_16,place_8_18,place_8_20;
    @FXML
    private ImageView place_9_1,place_9_3,place_9_5,place_9_7,place_9_9,place_9_11,place_9_13,place_9_15,place_9_17,place_9_19,place_9_21,
            place_10_0,place_10_2,place_10_4,place_10_6,place_10_8,place_10_10,place_10_12,place_10_14,place_10_16,place_10_18,place_10_20,
            place_11_1,place_11_3,place_11_5,place_11_7,place_11_9,place_11_11,place_11_13,place_11_15,place_11_17,place_11_19,place_11_21,
            place_12_0,place_12_2,place_12_4,place_12_6,place_12_8,place_12_10,place_12_12,place_12_14,place_12_16,place_12_18,place_12_20,
            place_13_1,place_13_3,place_13_5,place_13_7,place_13_9,place_13_11,place_13_13,place_13_15,place_13_17,place_13_19,place_13_21,
            place_14_0,place_14_2,place_14_4,place_14_6,place_14_8,place_14_10,place_14_12,place_14_14,place_14_16,place_14_18,place_14_20,
            place_15_1,place_15_3,place_15_5,place_15_7,place_15_9,place_15_11,place_15_13,place_15_15,place_15_17,place_15_19,place_15_21;
    @FXML
    private ImageView place_16_0,place_16_2,place_16_4,place_16_6,place_16_8,place_16_10,place_16_12,place_16_14,place_16_16,place_16_18,place_16_20,
            place_17_1,place_17_3,place_17_5,place_17_7,place_17_9,place_17_11,place_17_13,place_17_15,place_17_17,place_17_19,place_17_21,
            place_18_0,place_18_2,place_18_4,place_18_6,place_18_8,place_18_10,place_18_12,place_18_14,place_18_16,place_18_18,place_18_20,
            place_19_1,place_19_3,place_19_5,place_19_7,place_19_9,place_19_11,place_19_13,place_19_15,place_19_17,place_19_19,place_19_21,
            place_20_0,place_20_2,place_20_4,place_20_6,place_20_8,place_20_10,place_20_12,place_20_14,place_20_16,place_20_18,place_20_20,
            place_21_1,place_21_3,place_21_5,place_21_7,place_21_9,place_21_11,place_21_13,place_21_15,place_21_17,place_21_19,place_21_21;
    @FXML
    private ScrollPane boardScrollPane;
    @FXML
    private Label globalPointsLabel,lastTurnLabel;
    @FXML
    private ImageView idPawn,isFirstPawn;
    @FXML
    private Pane boardPane;
    @FXML
    private Button switch_1,switch_2,switch_3;

    // Variables to track mouse position and board offset
    private double mouseX;
    private double mouseY;
    private double boardOffsetX;
    private double boardOffsetY;
    private int selectedCard = -1;
    private ViewGui gui;
    private ImageView[] boardPositions;
    private ImageView[][] matrix;
    private boolean[][] playablePos;
    private boolean cardPlayed = false;
    private Image gold;
    private boolean goldShowed = false;
    private ImageView selectedImage;
    private ImageView[] cardsOnHand;
    private Button[] buttons;
    private ImageView[] table;
    private boolean winPoints = false;
    private boolean lastTurn = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addZoomFunctionality();
        addDragFunctionality();

        cardsOnHand = new ImageView[] {hand_0, hand_1, hand_2};
        buttons = new Button[] {switch_1, switch_2, switch_3};
        table = new ImageView[] {res_1, res_2, gold_1, gold_2, deck_res, deck_gold};

        boardPositions = new ImageView[] {
                pos_0,pos_1,pos_2,pos_3,pos_4,pos_5,pos_6,pos_7,pos_8,pos_9,pos_10,
                pos_11,pos_12,pos_13,pos_14,pos_15,pos_16,pos_17,pos_18,pos_19,pos_20,
                pos_21,pos_22,pos_23,pos_24,pos_25,pos_26,pos_27,pos_28,pos_29
        };
        matrix = new ImageView[][] {
                {place_0_0,null,place_0_2,null,place_0_4,null,place_0_6,null,place_0_8,null,place_0_10,null,place_0_12,null,place_0_14,null,place_0_16,null,place_0_18,null,place_0_20,null},
                {null,place_1_1,null,place_1_3,null,place_1_5,null,place_1_7,null,place_1_9,null,place_1_11,null,place_1_13,null,place_1_15,null,place_1_17,null,place_1_19,null,place_1_21},
                {place_2_0,null,place_2_2,null,place_2_4,null,place_2_6,null,place_2_8,null,place_2_10,null,place_2_12,null,place_2_14,null,place_2_16,null,place_2_18,null,place_2_20,null},
                {null,place_3_1,null,place_3_3,null,place_3_5,null,place_3_7,null,place_3_9,null,place_3_11,null,place_3_13,null,place_3_15,null,place_3_17,null,place_3_19,null,place_3_21},
                {place_4_0,null,place_4_2,null,place_4_4,null,place_4_6,null,place_4_8,null,place_4_10,null,place_4_12,null,place_4_14,null,place_4_16,null,place_4_18,null,place_4_20,null},
                {null,place_5_1,null,place_5_3,null,place_5_5,null,place_5_7,null,place_5_9,null,place_5_11,null,place_5_13,null,place_5_15,null,place_5_17,null,place_5_19,null,place_5_21},
                {place_6_0,null,place_6_2,null,place_6_4,null,place_6_6,null,place_6_8,null,place_6_10,null,place_6_12,null,place_6_14,null,place_6_16,null,place_6_18,null,place_6_20,null},
                {null,place_7_1,null,place_7_3,null,place_7_5,null,place_7_7,null,place_7_9,null,place_7_11,null,place_7_13,null,place_7_15,null,place_7_17,null,place_7_19,null,place_7_21},
                {place_8_0,null,place_8_2,null,place_8_4,null,place_8_6,null,place_8_8,null,place_8_10,null,place_8_12,null,place_8_14,null,place_8_16,null,place_8_18,null,place_8_20,null},
                {null,place_9_1,null,place_9_3,null,place_9_5,null,place_9_7,null,place_9_9,null,place_9_11,null,place_9_13,null,place_9_15,null,place_9_17,null,place_9_19,null,place_9_21},
                {place_10_0,null,place_10_2,null,place_10_4,null,place_10_6,null,place_10_8,null,place_10_10,null,place_10_12,null,place_10_14,null,place_10_16,null,place_10_18,null,place_10_20,null},
                {null,place_11_1,null,place_11_3,null,place_11_5,null,place_11_7,null,place_11_9,null,place_11_11,null,place_11_13,null,place_11_15,null,place_11_17,null,place_11_19,null,place_11_21},
                {place_12_0,null,place_12_2,null,place_12_4,null,place_12_6,null,place_12_8,null,place_12_10,null,place_12_12,null,place_12_14,null,place_12_16,null,place_12_18,null,place_12_20,null},
                {null,place_13_1,null,place_13_3,null,place_13_5,null,place_13_7,null,place_13_9,null,place_13_11,null,place_13_13,null,place_13_15,null,place_13_17,null,place_13_19,null,place_13_21},
                {place_14_0,null,place_14_2,null,place_14_4,null,place_14_6,null,place_14_8,null,place_14_10,null,place_14_12,null,place_14_14,null,place_14_16,null,place_14_18,null,place_14_20,null},
                {null,place_15_1,null,place_15_3,null,place_15_5,null,place_15_7,null,place_15_9,null,place_15_11,null,place_15_13,null,place_15_15,null,place_15_17,null,place_15_19,null,place_15_21},
                {place_16_0,null,place_16_2,null,place_16_4,null,place_16_6,null,place_16_8,null,place_16_10,null,place_16_12,null,place_16_14,null,place_16_16,null,place_16_18,null,place_16_20,null},
                {null,place_17_1,null,place_17_3,null,place_17_5,null,place_17_7,null,place_17_9,null,place_17_11,null,place_17_13,null,place_17_15,null,place_17_17,null,place_17_19,null,place_17_21},
                {place_18_0,null,place_18_2,null,place_18_4,null,place_18_6,null,place_18_8,null,place_18_10,null,place_18_12,null,place_18_14,null,place_18_16,null,place_18_18,null,place_18_20,null},
                {null,place_19_1,null,place_19_3,null,place_19_5,null,place_19_7,null,place_19_9,null,place_19_11,null,place_19_13,null,place_19_15,null,place_19_17,null,place_19_19,null,place_19_21},
                {place_20_0,null,place_20_2,null,place_20_4,null,place_20_6,null,place_20_8,null,place_20_10,null,place_20_12,null,place_20_14,null,place_20_16,null,place_20_18,null,place_20_20,null},
                {null,place_21_1,null,place_21_3,null,place_21_5,null,place_21_7,null,place_21_9,null,place_21_11,null,place_21_13,null,place_21_15,null,place_21_17,null,place_21_19,null,place_21_21}
        };

        for(int i = 0; i < matrix.length/2; i++)
            for(int j = 0; j < matrix[i].length/2; j++) {
                matrix[2 * i][2 * j].setOnMouseClicked(this::handleBoardClick);
                matrix[2 * i][2 * j].setMouseTransparent(true);
            }
        for(int i = 0; i < matrix.length/2; i++)
            for(int j = 0; j < matrix[i].length/2; j++) {
                matrix[2 * i + 1][2 * j + 1].setOnMouseClicked(this::handleBoardClick);
                matrix[2 * i + 1][2 * j + 1].setMouseTransparent(true);
            }

        for(ImageView img : table) {
            img.setOnMouseClicked(this::handleTableClick);
            img.setMouseTransparent(true);
        }

        gold = new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/gold.png"));
    }


    public void start(ViewGui gui) {
        this.gui = gui;

        if (gui.getStartingCard().isFront()) {
            place_10_10.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+gui.getStartingCard().getId()+".png")));
        } else {
            place_10_10.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getStartingCard().getId()+".png")));
        }
        place_10_10.setMouseTransparent(false);

        secret_obj.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getSecretObjective().getId()+".png")));
        hand_0.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getCardsOnHand()[0].getId()+".png")));
        hand_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getCardsOnHand()[1].getId()+".png")));
        hand_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getCardsOnHand()[2].getId()+".png")));
        obj_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getObjectives()[0].getId()+".png")));
        obj_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getObjectives()[1].getId()+".png")));
        res_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getCardsOnTable()[0].getId()+".png")));
        res_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getCardsOnTable()[1].getId()+".png")));
        gold_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getCardsOnTable()[2].getId()+".png")));
        gold_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/front_"+gui.getCardsOnTable()[3].getId()+".png")));
        idPawn.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/pawns/pawn_"+gui.getLocalPlayerTable().getId()+".png")));
        labelAnimal.setText(":"+gui.getResourcesCounter()[0]);
        labelPlant.setText(":"+gui.getResourcesCounter()[1]);
        labelInsect.setText(":"+gui.getResourcesCounter()[2]);
        labelFungi.setText(":"+gui.getResourcesCounter()[3]);
        labelInkwell.setText(":"+gui.getResourcesCounter()[4]);
        labelManuscript.setText(":"+gui.getResourcesCounter()[5]);
        labelQuill.setText(":"+gui.getResourcesCounter()[6]);

        if(gui.getLocalPlayerTable().getId() == 0)
            isFirstPawn.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/pawns/pawn_first.png")));
        
        globalPointsLabel.setText("Global points: 0");
        int num = gui.getNicknames().length-1;
        pos_0.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/pawns/pawn_"+num+".png")));

        String text;
        for(int i = 0; i<gui.getNicknames().length; i++) {
            text = i + ") " + gui.getNicknames()[i] + " | " + "Board points: 0";
            rankingList.getItems().add(text);
        }

        rankingList.setStyle("-fx-font-weight: bold");
        rankingList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        turnLabel.setStyle("-fx-font-weight: bold");
        globalPointsLabel.setStyle("-fx-font-weight: bold");

        chat.getItems().add("Chat service is on!");
        chat.getItems().add("Type --listPlayers to see who your opponents are.");
        chat.getItems().add("Start a message with '@' to send a private message.");
    }

    private void addZoomFunctionality() {
        boardPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) {
                double delta = 1.2;
                double scaleX = boardPane.getScaleX();
                double scaleY = boardPane.getScaleY();
                if (event.getDeltaY() < 0) {
                    scaleX /= delta;
                    scaleY /= delta;
                } else {
                    scaleX *= delta;
                    scaleY *= delta;
                }
                boardPane.setScaleX(scaleX);
                boardPane.setScaleY(scaleY);
                event.consume();
            }
        });
    }

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

    public void receiveMessage(ChatMessage message) {
        if(message.getSenderId() != gui.getLocalPlayerTable().getId()) {
            if (!message.isPrivate())
                chat.getItems().add(message.getSender() + ": " + message.getMessage());
            else chat.getItems().add(message.getSender() + ": psst, " + message.getMessage());
            chat.scrollTo(chat.getItems().size() - 1);
            chat.refresh();
        }
    }

    public void setTurnLabelText(String text) {
        turnLabel.setText(text);
    }

    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        rankingList.getItems().clear();
        String text;

        for(int i = 0; i<boardPoints.length; i++) {
            text = i + ") " + gui.getNicknames()[i] + " | " + "Board points: " + boardPoints[i];
            rankingList.getItems().add(text);
        }
        rankingList.refresh();

        for(int j = 0; j <= 29; j++)
            boardPositions[j].setImage(null);

        for(int i = 0; i<boardPoints.length; i++) {
            if(boardPoints[i]<=29)
                boardPositions[boardPoints[i]].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/pawns/pawn_"+i+".png")));
        }
        if(winPoints && gui.getLocalPlayerTable().getId()==0){
            lastTurn = true;
            lastTurnLabel.setText("Last turn!");
            lastTurnLabel.getStyleClass().add("tooltip");
            lastTurnLabel.setStyle("-fx-font-weight: bold");
        }
        globalPointsLabel.setText("Global points: " + globalPoints[gui.getLocalPlayerTable().getId()]);
        if(boardPoints[gui.getLocalPlayerTable().getId()]>=20 && !winPoints){
            winPoints = true;
        }
        labelAnimal.setText(":"+gui.getResourcesCounter()[0]);
        labelPlant.setText(":"+gui.getResourcesCounter()[1]);
        labelInsect.setText(":"+gui.getResourcesCounter()[2]);
        labelFungi.setText(":"+gui.getResourcesCounter()[3]);
        labelInkwell.setText(":"+gui.getResourcesCounter()[4]);
        labelManuscript.setText(":"+gui.getResourcesCounter()[5]);
        labelQuill.setText(":"+gui.getResourcesCounter()[6]);
    }

    public void updateDecks(ResourceCard res, GoldCard gold, int drawPos) {
        deck_res.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+res.getId()+".png")));
        deck_gold.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+gold.getId()+".png")));

        if(drawPos>=0 && drawPos<=3) {
            String imagePath = "/it/polimi/sw/gianpaolocugola47/graphics/cards/";
            imagePath += gui.getCardsOnTable()[drawPos].isFront() ? "front_" : "back_";
            imagePath += gui.getCardsOnTable()[drawPos].getId() + ".png";
            table[drawPos].setImage(new Image(getClass().getResourceAsStream(imagePath)));
        }
        for(int i = 0; i<3; i++) {
            String imagePath = "/it/polimi/sw/gianpaolocugola47/graphics/cards/";
            imagePath += gui.getCardsOnHand()[i].isFront() ? "front_" : "back_";
            imagePath += gui.getCardsOnHand()[i].getId() + ".png";
            cardsOnHand[i].setImage(new Image(getClass().getResourceAsStream(imagePath)));
        }
        if(lastTurn && gui.getLocalPlayerTable().getId()==gui.getNicknames().length-1){
            gui.gameOver();
        }
    }

    @FXML
    public void handleRankingClick(MouseEvent event) {
        int id = rankingList.getFocusModel().getFocusedIndex();
        if(id != gui.getLocalPlayerTable().getId())
            gui.setOtherBoardScene(id);
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

    private void setImageViewBorder(ImageView card) {
        hand_0.getStyleClass().remove("selected-image");
        hand_1.getStyleClass().remove("selected-image");
        hand_2.getStyleClass().remove("selected-image");
        card.getStyleClass().add("selected-image");
    }

    @FXML
    public void handleBoardClick(MouseEvent event) {

        if (selectedCard == -1 || !gui.isItMyTurn() || cardPlayed)
            return;

        ResourceCard card = gui.getCardsOnHand()[selectedCard];
        ImageView image = (ImageView) event.getSource();

        if (image.getImage().equals(gold)) {

            int x = 0, y = 0, corner = 0;
            selectedImage.getStyleClass().remove("play");

            for (int i = 0; i < playablePos.length; i++)
                for (int j = 0; j < playablePos[i].length; j++)
                    if(matrix[i][j] != null && matrix[i][j].equals(selectedImage)) {
                        x = i;
                        y = j;
                        break;
                    }
            for (int i = 0; i < playablePos.length; i++)
                for (int j = 0; j < playablePos[i].length; j++)
                    if(playablePos[i][j] && matrix[i][j] != null) {
                        matrix[i][j].setImage(null);
                        matrix[i][j].setMouseTransparent(true);

                        if(matrix[i][j].equals(image)) {
                            if(i-x < 0 && j-y < 0) corner = 0;
                            if(i-x < 0 && j-y > 0) corner = 1;
                            if(i-x > 0 && j-y < 0) corner = 2;
                            if(i-x > 0 && j-y > 0) corner = 3;

                            if(gui.playCard(selectedCard, x, y, corner, gui.getCardsOnHand()[selectedCard].isFront())) {
                                String imagePath = "/it/polimi/sw/gianpaolocugola47/graphics/cards/";
                                imagePath += gui.getCardsOnHand()[selectedCard].isFront() ? "front_" : "back_";
                                imagePath += gui.getCardsOnHand()[selectedCard].getId() + ".png";
                                matrix[i][j].setImage(new Image(getClass().getResourceAsStream(imagePath)));
                                matrix[i][j].setMouseTransparent(false);
                                matrix[i][j].toFront();
                                cardsOnHand[selectedCard].getStyleClass().remove("selected-image");
                                cardsOnHand[selectedCard].setImage(null);
                                gui.getCardsOnHand()[selectedCard] = null;
                                disableHand(false);
                                cardsOnHand[selectedCard].setMouseTransparent(true);
                                buttons[selectedCard].setMouseTransparent(true);
                                selectedCard = -1;
                                cardPlayed = true;
                                disableTable(false);
                            } else disableHand(false);
                        }
                    }
            goldShowed = false;

        } else if (!goldShowed) {
            selectedImage = image;
            selectedImage.getStyleClass().add("play");

            this.playablePos = gui.getPlayablePositions();
            for (int i = 0; i < playablePos.length; i++)
                for (int j = 0; j < playablePos[i].length; j++)
                    if(playablePos[i][j] && matrix[i][j] != null) {
                        matrix[i][j].setImage(gold);
                        matrix[i][j].setMouseTransparent(false);
                    }
            disableHand(true);
            goldShowed = true;
        }
    }

    private void disableHand(boolean b) {
        hand_0.setMouseTransparent(b);
        hand_1.setMouseTransparent(b);
        hand_2.setMouseTransparent(b);
        switch_1.setMouseTransparent(b);
        switch_2.setMouseTransparent(b);
        switch_3.setMouseTransparent(b);
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        int pos = 0;
        ImageView source = (ImageView) event.getSource();
        Image image = source.getImage();

        for(int i = 0; i < table.length; i++)
            if(table[i].equals(source)) {
                pos = i;
                break;
            }
        gui.drawCard(pos);
        disableHand(false);
        disableTable(true);
        cardPlayed = false;
    }

    private void disableTable(boolean b) {
        for(ImageView img : table) {
            img.setMouseTransparent(b);
            if(img.getImage() == null)
                img.setMouseTransparent(true);
        }
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
                chat.getItems().add(line);
                chat.scrollTo(chat.getItems().size()-1);
                chat.refresh();
                gui.sendMessage(message);
            } catch (StringIndexOutOfBoundsException e) {
                chat.getItems().add("Invalid input, try again...");
                chat.scrollTo(chat.getItems().size()-1);
                chat.refresh();
            }
        } else if (line.equals("--listPlayers")) {

            chat.getItems().add("List of all the players: ");
            for(String nickname : gui.getNicknames()) {
                if(nickname.equals(gui.getLocalPlayerTable().getNickName()))
                    chat.getItems().add(nickname + " (you)");
                else
                    chat.getItems().add(nickname);
            }
            chat.scrollTo(chat.getItems().size()-1);
            chat.refresh();
        } else {
            message.setPrivate(false);
            message.setMessage(line);
            chat.getItems().add(line);
            chat.scrollTo(chat.getItems().size()-1);
            chat.refresh();
            gui.sendMessage(message);
        }
    }

    @FXML
    private void handleChatInputKey(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER))
            handleChatInput(new ActionEvent());
    }
}
