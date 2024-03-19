package it.polimi.sw.gianpaolocugola47.model;
/**
 * This class represents all the resource cards.
 *
 */
public class ResourceCard extends PlaceableCard {
    private Resources resourceCentreBack;
    private int points;

    /**
     * Corner constructor.
     *
     * @param backImgPath refer to the image of the back.
     * @param frontImgPath refer to the image of the front.
     * @param points the points the card scores when played.
     */
    public ResourceCard(String backImgPath, String frontImgPath, int points) {
        super(backImgPath, frontImgPath);
        this.points = points;
    }

    public Resources getResourceCentreBack() {
        return resourceCentreBack;
    }

    public void setResourceCentreBack(Resources resourceCentreBack) {
        this.resourceCentreBack = resourceCentreBack;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
