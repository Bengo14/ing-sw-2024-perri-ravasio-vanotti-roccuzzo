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
     * @param column the column the card is in.
     * @param corners all the 4 corners of the card.
     * @param backImgPath refer to the image of the back.
     * @param frontImgPath refer to the image of the front.
     * @param isFront boolean that represent the front or back of a card.
     * @param line the line the card is in.
     * @param itemRequired the items that if on the board give the points.
     * @param points the points the card scores when played.
     * @param pointsForCorners the points the card scores covering angles when played.
     * @param resourceCentreBack  the resource on the back of the card.
     * @param resourcesRequired the resources which is required to play the card.
     */
    public GoldCard(boolean isFront, String backImgPath, String frontImgPath, Resources resourceCentreBack, Corner[] corners, int points, int line, int column, ArrayList<Resources> resourcesRequired, Items itemRequired, boolean pointsForCorners) {
        super(isFront, backImgPath, frontImgPath, resourceCentreBack, corners, points, line, column);
        this.resourcesRequired = resourcesRequired;
        this.itemRequired = itemRequired;
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

    public void setPointsForCorners(boolean pointsForCorners) {
        this.pointsForCorners = pointsForCorners;
    }
}
