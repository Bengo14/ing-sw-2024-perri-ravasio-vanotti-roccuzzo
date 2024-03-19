package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;
/**
 * This class represents all the gold cards.
 * Have the required of a card and the points that can do.
 */
public class GoldCard extends ResourceCard {
    private ArrayList<Resources> resourcesRequired;
    private Items itemRequired;
    private boolean pointsForCorners;
    /**
     * Corner constructor.
     *
     * @param backImgPath refer to the image of the back.
     * @param frontImgPath refer to the image of the front.
     * @param points the points the card scores when played.
     * @param pointsForCorners the points the card scores covering angles when played.
     */
    public GoldCard(String backImgPath, String frontImgPath, int points, boolean pointsForCorners) {
        super(backImgPath, frontImgPath, points);
        this.pointsForCorners = pointsForCorners;
    }

    public ArrayList<Resources> getResourcesRequired() {
        return resourcesRequired;
    }

    public void setResourcesRequired(ArrayList<Resources> resourcesRequired) {
        this.resourcesRequired = resourcesRequired;
    }

    public Items getItemRequired() {
        return itemRequired;
    }

    public void setItemRequired(Items itemRequired) {
        this.itemRequired = itemRequired;
    }

    public boolean isPointsForCorners() {
        return pointsForCorners;
    }
}
