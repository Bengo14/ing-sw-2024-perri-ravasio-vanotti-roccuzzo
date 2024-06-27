package it.polimi.sw.gianpaolocugola47.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This abstract class represents all the placeable cards in the game.
 * It includes the front and back image path, the coordinates, the flag for the objective, the corners and the id of the card.
 * The flag for the objective is used to check whether a card is used for calculating a given L-shape or diagonal pattern objective.
 * It is inherited by the StartingCard, ResourceCard and GoldCard classes.
 */
public abstract class PlaceableCard implements Serializable {

    private boolean isFront;
    private final String backImgPath;
    private final String frontImgPath;
    private int line;
    private int column;
    private boolean isFlaggedForObjective;
    private final Corner[] corners = new Corner[8];
    private int id;

    /**
     * PlaceableCard constructor.
     * @param backImgPath : the path of the back image.
     * @param frontImgPath : the path of the front image.
     */
    public PlaceableCard(String backImgPath, String frontImgPath) {
        this.isFront = true;
        this.backImgPath = backImgPath;
        this.frontImgPath = frontImgPath;
        this.isFlaggedForObjective = false;
    }

    /**
     * Returns the visible corners, meaning the corners of the card that are visible to the player once
     * the card is placed onto the board; 0-3 are the front corners, 4-7 are the back corners.
     * @return : the visible corners of the card.
     */
    public Corner[] getVisibleCorners() {
        if(isFront)
            return Arrays.copyOfRange(corners, 0, 4);
        else return Arrays.copyOfRange(corners, 4, 8);
    }

    /**
     * Returns the id of the card.
     * @return : the id of the card.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the coordinates of the card.
     * @param line : the row of the card.
     * @param column : the column of the card.
     */
    public void setCoordinates(int line, int column) {
        this.line = line;
        this.column = column;
    }

    /**
     * Sets the row of the card.
     * @return : the row of the card.
     */
    public int getLine() {return this.line;}

    /**
     * Returns the column of the card.
     * @return : the column of the card.
     */
    public int getColumn() {return this.column;}

    /**
     * Returns the side of the card.
     * @return : true if the card is front, false otherwise.
     */
    public boolean getIsFront() {
        return isFront;
    }

    /**
     * Returns the path of the back image.
     * @return : the path of the back image.
     */
    public String getBackImgPath() {
        return backImgPath;
    }

    /**
     * Returns the path of the front image.
     * @return : the path of the front image.
     */
    public String getFrontImgPath() {
        return frontImgPath;
    }

    /**
     * Returns whether the card is flagged for the objective.
     * @return : true if the card is flagged, false otherwise.
     */
    public boolean getIsFlaggedForObjective(){
        return isFlaggedForObjective;
    }

    /**
     * Sets the flag for the objective.
     * @param flag : true if the card is flagged, false otherwise.
     */
    public void setFlaggedForObjective(boolean flag){
        this.isFlaggedForObjective=flag;
    }

    /**
     * Switches the side of the card. If the card is front, it becomes back and vice versa.
     */
    public void switchFrontBack() {
        isFront = !isFront;
    }

    /**
     * Sets the side of the card.
     * @param isFront : true if the card is front, false otherwise.
     */
    public void setFront(boolean isFront) {
        this.isFront = isFront;
    }

    /**
     * Returns all the corners of the card.
     * @return : the corners of the card.
     */
    public Corner[] getCorners() {return corners;}

    /**
     * Updated the resource counter. Implemented in subclasses.
     * @param counter : the resource counter.
     */
    public abstract void updateResourceCounter(int[] counter);

    /**
     * Returns the points of the card. Implemented in subclasses.
     * @param playerTable : the player table of the player.
     * @return : the points of the card.
     */
    public int getPoints(PlayerTable playerTable) {
        return 0; // default value
    }
}