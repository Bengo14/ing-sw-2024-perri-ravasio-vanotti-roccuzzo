package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;
/**
 * This class represents all the gold cards.
 * Have the required of a card and the points that can do.
 */
public class GoldCard extends ResourceCard {
    private ArrayList<Resources> resourcesRequired;
    private final Items itemRequired;
    private final boolean pointsForCorners;
    private final boolean pointsForItems;
    private final Items itemThatGivesPoints;
    /**
     * Corner constructor.
     *
     * @param backImgPath refer to the image of the back.
     * @param frontImgPath refer to the image of the front.
     * @param points the points the card scores when played.
     * @param pointsForCorners the points the card scores covering angles when played.
     */
    public GoldCard(String backImgPath, String frontImgPath, int points, boolean pointsForCorners, Resources resourceCentreBack,
                    Items itemRequired, boolean pointsForItems, Items itemThatGivesPoints) {
        super(backImgPath, frontImgPath, points, resourceCentreBack);
        this.itemRequired = itemRequired;
        this.pointsForCorners = pointsForCorners;
        this.pointsForItems = pointsForItems;
        this.itemThatGivesPoints = itemThatGivesPoints;
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
    public boolean isPointsForCorners() {
        return pointsForCorners;
    }
    public Items getItemThatGivesPoints() {
        return itemThatGivesPoints;
    }
    public boolean isPointsForItems() {
        return pointsForItems;
    }
    @Override
    public int getPoints(PlaceableCard[][] board) {
        int points = 0;
        /*todo*/
        return points;
    }
}
