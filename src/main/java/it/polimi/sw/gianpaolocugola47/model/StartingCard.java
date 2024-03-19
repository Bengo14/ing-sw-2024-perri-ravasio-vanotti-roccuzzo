package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;
/**
 * This class represents all the starting cards.
 */
public class StartingCard extends PlaceableCard {
    private ArrayList<Resources> resourcesCentreBack;
    private Corner[] corners;
    private int line;
    private int column;

    /**
     * Corner constructor.
     * @param resourcesCentreBack all the resources in the back of a card.
     * @param corners all the 4 corners of the card.
     * @param backImgPath refer to the image of the back.
     * @param frontImgPath refer to the image of the front.
     * @param isFront boolean that represent the front or back of a card.
     */
    public StartingCard(boolean isFront, String backImgPath, String frontImgPath, ArrayList<Resources> resourcesCentreBack, Corner[] corners) {
        super(isFront, backImgPath, frontImgPath);
        this.resourcesCentreBack = resourcesCentreBack;
        this.corners = corners;
        this.line=31;
        this.column=31;
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
