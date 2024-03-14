package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;

public class StartingCard extends PlaceableCard {
    private ArrayList<Resources> resourcesCentreBack;
    private Corner[] corners;

    public StartingCard(boolean isFront, String backImgPath, String frontImgPath, ArrayList<Resources> resourcesCentreBack, Corner[] corners) {
        super(isFront, backImgPath, frontImgPath);
        this.resourcesCentreBack = resourcesCentreBack;
        this.corners = corners;
    }

    public ArrayList<Resources> getResourcesCentreBack() {
        return resourcesCentreBack;
    }

    public void setResourcesCentreBack(ArrayList<Resources> resourcesCentreBack) {
        this.resourcesCentreBack = resourcesCentreBack;
    }

    public Corner[] getCorners() {
        return corners;
    }

    public void setCorners(Corner[] corners) {
        this.corners = corners;
    }
}
