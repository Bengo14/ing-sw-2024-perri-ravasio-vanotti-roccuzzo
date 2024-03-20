package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;
/**
 * This class represents all the objectives in a game with their points,the reference at the front and back image
 * and the items,the resource and the pattern use for make the points.
 */
public class Objectives {
    private int points;
    private String imgPathFront;
    private String imgPathBack;
    private ArrayList<Items> items;
    private ArrayList<Resources> resources;
    private Pattern pattern;
    /**
     * Corner constructor.
     *
     * @param points the points made by the card.
     * @param imgPathBack the reference to the back's image.
     * @param imgPathFront the reference to the front's image.
     */
    public Objectives(int points, String imgPathFront, String imgPathBack) {
        this.points = points;
        this.imgPathFront = imgPathFront;
        this.imgPathBack = imgPathBack;
        this.items = items;
        this.resources = resources;
        this.pattern = pattern;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getImgPathFront() {
        return imgPathFront;
    }

    public void setImgPathFront(String imgPathFront) {
        this.imgPathFront = imgPathFront;
    }

    public String getImgPathBack() {
        return imgPathBack;
    }

    public void setImgPathBack(String imgPathBack) {
        this.imgPathBack = imgPathBack;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }

    public ArrayList<Resources> getResources() {
        return resources;
    }

    public void setResources(ArrayList<Resources> resources) {
        this.resources = resources;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public int checkPatternAndComputePoints(PlaceableCard[][] board){
        int totalPoints = 0;
        /*todo*/
        return  totalPoints;
    }
}
