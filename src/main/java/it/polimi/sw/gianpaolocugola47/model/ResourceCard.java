package it.polimi.sw.gianpaolocugola47.model;
/**
 * This class represents all the resource cards.
 *
 */
public class ResourceCard extends PlaceableCard {
    private Resources resourceCentreBack;
    private Corner[] corners;
    private int points;
    private int line;
    private int column;
    /**
     * Corner constructor.
     *
     * @param column the column the card is in.
     * @param corners all the 4 corners of the card.
     * @param backImgPath refer to the image of the back.
     * @param frontImgPath refer to the image of the front.
     * @param isFront boolean that represent the front or back of a card.
     * @param line the line the card is in.
     * @param points the points the card scores when played.
     * @param resourceCentreBack  the resource on the back of the card.
     */
    public ResourceCard(boolean isFront, String backImgPath, String frontImgPath, Resources resourceCentreBack, Corner[] corners, int points, int line, int column) {
        super(isFront, backImgPath, frontImgPath);
        this.resourceCentreBack = resourceCentreBack;
        this.corners = corners;
        this.points = points;
        this.line = line;
        this.column = column;
    }

    public Resources getResourceCentreBack() {
        return resourceCentreBack;
    }

    public Corner[] getCorners() {
        return corners;
    }

    public void setCorners(Corner[] corners) {
        this.corners = corners;
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

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
