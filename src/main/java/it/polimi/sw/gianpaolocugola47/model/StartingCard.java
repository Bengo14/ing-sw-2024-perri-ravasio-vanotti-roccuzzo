package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;
/**
 * This class represents all the starting cards.
 */
public class StartingCard extends PlaceableCard {
    private ArrayList<Resources> resourcesCentreBack;

    /**
     * Corner constructor.
     * @param backImgPath refer to the image of the back.
     * @param frontImgPath refer to the image of the front.
     */
    public StartingCard(String backImgPath, String frontImgPath) {
        super(backImgPath, frontImgPath);
        setCoordinates(31, 31);
    }

    public ArrayList<Resources> getResourcesCentreBack() {
        return resourcesCentreBack;
    }

    public void setResourcesCentreBack(ArrayList<Resources> resourcesCentreBack) {
        this.resourcesCentreBack = resourcesCentreBack;
    }

}
