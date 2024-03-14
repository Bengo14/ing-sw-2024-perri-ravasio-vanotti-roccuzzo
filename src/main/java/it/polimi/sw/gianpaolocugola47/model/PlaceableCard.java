package it.polimi.sw.gianpaolocugola47.model;

public abstract class PlaceableCard {
    private boolean isFront;
    private String backImgPath;
    private String frontImgPath;

    public PlaceableCard(boolean isFront, String backImgPath, String frontImgPath) {
        this.isFront = isFront;
        this.backImgPath = backImgPath;
        this.frontImgPath = frontImgPath;
    }

    public boolean isFront() {
        return isFront;
    }

    public void setFront(boolean front) {
        isFront = front;
    }

    public String getBackImgPath() {
        return backImgPath;
    }

    public void setBackImgPath(String backImgPath) {
        this.backImgPath = backImgPath;
    }

    public String getFrontImgPath() {
        return frontImgPath;
    }

    public void setFrontImgPath(String frontImgPath) {
        this.frontImgPath = frontImgPath;
    }

    public void switchFrontBack() {
        this.isFront = !this.isFront;
    }
}
