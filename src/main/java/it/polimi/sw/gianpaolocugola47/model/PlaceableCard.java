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
    private int id;

    public PlaceableCard(String backImgPath, String frontImgPath) {
        this.isFront = true;
        this.backImgPath = backImgPath;
        this.frontImgPath = frontImgPath;
        this.isFlaggedForObjective = false;
    }

    public Corner[] getVisibleCorners() {
        if(isFront)
            return Arrays.copyOfRange(corners, 0, 4);
        else return Arrays.copyOfRange(corners, 4, 8);
    }

    public int getId() {
        return id;
    }

    public void setCoordinates(int line, int column) {
        this.line = line;
        this.column = column;
    }
    public int getLine() {return this.line;}
    public int getColumn() {return this.column;}

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

    public void switchFrontBack() {
        isFront = !isFront;
    }

    public Corner[] getCorners() {return corners;}

    public abstract void updateResourceCounter(int[] counter);
}
