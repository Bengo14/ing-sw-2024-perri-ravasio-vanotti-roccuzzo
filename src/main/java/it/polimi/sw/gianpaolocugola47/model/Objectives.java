package it.polimi.sw.gianpaolocugola47.model;

import java.io.Serializable;

/**
 * This class represents all the objectives in a game with their points,the reference at the front and back image
 * and the items,the resource and the pattern use for make the points.
 */
public class Objectives implements Serializable {

    private final int points;
    private final String imgPathFront;
    private final String imgPathBack;
    private int id;
    private String description;

    /**
     * Objectives constructor.
     *
     * @param points the points made by the card.
     * @param imgPathBack the reference to the back's image.
     * @param imgPathFront the reference to the front's image.
     */
    public Objectives(int points, String imgPathFront, String imgPathBack) {
        this.points = points;
        this.imgPathFront = imgPathFront;
        this.imgPathBack = imgPathBack;
    }

    /**
     * Returns the objectives id.
     * @return : the id of the objective.
     */
    public int getId(){
        return id;
    }

    /**
     * Returns the points of the objective.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Returns the image path of the objective, front side.
     * @return : the image path of the objective.
     */
    public String getImgPathFront() {
        return imgPathFront;
    }

    /**
     * Returns the image path of the objective, back side.
     * @return : the image path of the objective.
     */
    public String getImgPathBack() {
        return imgPathBack;
    }

    /**
     * This method checks if the pattern of the player is the same of the objective and if that is true it computes the points.
     * Implemented in the subclasses.
     * @param playerTable : the player table of the player.
     * @return : the points made by the player.
     */
    public int checkPatternAndComputePoints(PlayerTable playerTable){
        return 0;
    }

    /**
     * Returns the description of the objective. Used for printing objectives card in the CLI.
     * @return : the description of the objective.
     */
    public String getDescription(){
        return description;
    }
}