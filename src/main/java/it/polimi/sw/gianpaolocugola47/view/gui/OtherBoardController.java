package it.polimi.sw.gianpaolocugola47.view.gui;

import it.polimi.sw.gianpaolocugola47.model.PlaceableCard;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class is the controller of the other board scene.
 * It contains the methods to show the board of the other player.
 */
public class OtherBoardController implements Initializable {

    @FXML
    private ImageView hand_0,hand_1,hand_2;
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
    private ImageView idPawn,isFirstPawn;
    @FXML
    private Pane boardPane;
    @FXML
    Label label;

    private ViewGui gui;

    private ImageView [][] matrix;

    /**
     * Initializes the controller.
     * This method is automatically called after the fxml file has been loaded.
     * It creates a matrix of ImageViews that represent the board of the player
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addZoomFunctionality();

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
    }
    /**
     * Adds the zoom functionality to the board
     */
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

    /**
     * Starts the controller and shows the board of the player
     * It links the controller to the gui that is using it
     * @param gui The gui that is using this controller
     * @param id The id of the player
     */
    public void start(ViewGui gui, int id) {
        this.gui = gui;

        label.setText("Placed cards of player "+id+": "+gui.getNicknames()[id]);
        label.setStyle("-fx-font-weight: bold");

        ResourceCard[] cardsOnHand = gui.getAllCardsOnHand()[id];

        if(cardsOnHand[0]!=null)
            hand_0.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+cardsOnHand[0].getId()+".png")));
        if(cardsOnHand[1]!=null)
            hand_1.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+cardsOnHand[1].getId()+".png")));
        if(cardsOnHand[2]!=null)
            hand_2.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/back_"+cardsOnHand[2].getId()+".png")));

        if(id == 0)
            isFirstPawn.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/pawns/pawn_first.png")));
        idPawn.setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/pawns/pawn_"+id+".png")));

        PlaceableCard[][] placedCards = this.gui.getPlacedCards(id);
        String front = placedCards[10][10].getIsFront() ? "back_" : "front_";
        matrix[10][10].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/"+ front + placedCards[10][10].getId() + ".png")));
        matrix[10][10].setMouseTransparent(false);
        ArrayList<int[]> array = gui.getPlacingOrder(id);
        for(int i=0;i<array.size();i++){
            int x = array.get(i)[0];
            int y = array.get(i)[1];
            String frnt = placedCards[x][y].getIsFront() ? "front_" : "back_";
            matrix[x][y].setImage(new Image(getClass().getResourceAsStream("/it/polimi/sw/gianpaolocugola47/graphics/cards/"+ frnt + placedCards[x][y].getId() + ".png")));
            matrix[x][y].setMouseTransparent(false);
            matrix[x][y].toFront();
        }
    }

    /**
     * Handles the go back button
     */
    @FXML
    private void goBackHandler() {
        gui.resetScene();
    }
}