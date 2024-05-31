package it.polimi.sw.gianpaolocugola47.model;

import java.io.Serializable;

/**
 * This class represents all the objectives in a game with their points,the reference at the front and back image
 * and the items,the resource and the pattern use for make the points.
 */
public abstract class Objectives implements Serializable {

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
    public int getId(){
        return id;
    }

    public int getPoints() {
        return points;
    }

    public String getImgPathFront() {
        return imgPathFront;
    }

    public String getImgPathBack() {
        return imgPathBack;
    }

    public abstract int checkPatternAndComputePoints(PlayerTable playerTable);
    public String getDescription(){
        return description;
    }
}
