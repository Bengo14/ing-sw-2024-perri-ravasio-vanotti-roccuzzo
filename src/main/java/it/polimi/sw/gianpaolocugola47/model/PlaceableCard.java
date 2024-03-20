package it.polimi.sw.gianpaolocugola47.model;

public abstract class PlaceableCard {
    private boolean isFront;
    private final String backImgPath;
    private final String frontImgPath;
    private int line;
    private int column;
    private Corner[] corners;

    public PlaceableCard(String backImgPath, String frontImgPath) {
        this.isFront = true;
        this.backImgPath = backImgPath;
        this.frontImgPath = frontImgPath;
    }

    public void setCorners(Corner[] corners){
        this.corners = corners;
    }
    public Corner getCorner(int num){
        return this.corners[num];
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

    public void switchFrontBack() {
        isFront = !isFront;
    }
}
