package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;

public class Objectives {
    private int points;
    private String imgPathFront;
    private String imgPathBack;
    private ArrayList<Items> items;
    private ArrayList<Resources> resources;
    private Pattern pattern;

    public Objectives(int points, String imgPathFront, String imgPathBack, ArrayList<Items> items, ArrayList<Resources> resources, Pattern pattern) {
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

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
