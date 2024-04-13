package it.polimi.sw.gianpaolocugola47.model;

import java.util.Arrays;

public abstract class PlaceableCard {
    private boolean isFront;
    private final String backImgPath;
    private final String frontImgPath;
    private int line;
    private int column;
    private boolean isFlaggedForObjective;
    private Corner[] corners = new Corner[8];

    public PlaceableCard(String backImgPath, String frontImgPath) {
        this.isFront = true;
        this.backImgPath = backImgPath;
        this.frontImgPath = frontImgPath;
        this.isFlaggedForObjective=false;
    }

    public Corner[] getVisibleCorners(){
        if(isFront)
            return Arrays.copyOfRange(corners, 0, 3);
        else return Arrays.copyOfRange(corners, 4, 7);
    }

    public void setCoordinates(int line, int column){
        this.line = line;
        this.column = column;
    }
    public boolean isFront() {
        return isFront;
    }

    public String getBackImgPath() {
        return backImgPath;
    }

    public String getFrontImgPath() {
        return frontImgPath;
    }
    public boolean getIsFlaggedForObjective(){
        return isFlaggedForObjective;
    }
    public void setFlaggedForObjective(boolean flag){
        this.isFlaggedForObjective=flag;
    }
    public Corner[] getCorners() {return this.corners;}
    public void switchFrontBack() {
        isFront = !isFront;
    }
    public abstract void updateResourceCounter(int[] counter);
}
