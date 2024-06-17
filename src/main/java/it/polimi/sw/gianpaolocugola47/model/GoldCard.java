package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;
/**
 * This class represents all the gold cards.
 * Have the required of a card and the points that can do.
 */
public class GoldCard extends ResourceCard {
    private ArrayList<Resources> resourcesRequired;
    private final boolean pointsForCorners;
    private final boolean pointsForItems;
    private final Items itemRequired;

    /**
     * GoldCard constructor.
     *
     * @param backImgPath      refer to the image of the back.
     * @param frontImgPath     refer to the image of the front.
     * @param points           the points the card scores when played.
     * @param pointsForCorners the points the card scores covering angles when played.
     */
    public GoldCard(String backImgPath, String frontImgPath, int points, boolean pointsForCorners, Resources resourceCentreBack,
                    Items itemRequired, boolean pointsForItems, Items itemThatGivesPoints) {
        super(backImgPath, frontImgPath, points, resourceCentreBack);
        this.pointsForCorners = pointsForCorners;
        this.pointsForItems = pointsForItems;
        this.itemRequired = itemThatGivesPoints;
    }

    /**
     * This method return the resources required for the card.
     * @return the resources required for the card.
     */
    public ArrayList<Resources> getResourcesRequired() {
        return resourcesRequired;
    }

    public String resourcesRequiredToString() {
        StringBuilder retString = new StringBuilder();
        for (Resources resource : resourcesRequired) {
            retString.append("%s%s\u001B[0m".formatted(resource.getAsciiEscape(), resource.getSymbol()));
        }
        return retString.toString();
    }

    /**
     * This method return the points condition of the card.
     * @return the points condition of the card.
     */
    public String pointConditionToString() {
        if (pointsForCorners) {
            return "C";
        }
        if (pointsForItems) {
            return Character.toString(itemRequired.getSymbol());
        } else
            return "N/A";
    }

    /**
     * This method return if the card give points for corners.
     * @return true if the card give points for corners, false otherwise.
     */
    public boolean isPointsForCorners() {
        return pointsForCorners;
    }

    /**
     * This method return the item required for the card.
     * @return the item required for the card.
     */
    public Items getItemRequired() {
        return itemRequired;
    }
    /**
     * This method return if the card give points for items.
     * @return true if the card give points for items, false otherwise.
     */
    public boolean isPointsForItems() {
        return pointsForItems;
    }

    /**
     * This method update the resource counter after a card is placed.
     * @param counter the counter to update.
     */
    @Override
    public void updateResourceCounter(int[] counter) {
        if (isFront()) {
            Corner[] visibleCorners = getVisibleCorners();
            for (int i = 0; i < 4; i++) {
                Corner corner = visibleCorners[i];
                if (corner.isItem()) {
                    counter[corner.getItem().ordinal() + 4]++;
                    break;
                }
            }
        } else {
            counter[getResourceCentreBack().ordinal()]++;
        }
    }
    /**
     * This method return the points of the playerTable.
     * @param playerTable the player table to check.
     * @return the points of the playerTable.
     */
    @Override
    public int getPoints(PlayerTable playerTable) {
        if(this.isFront()) {
            int points = this.getThisPoints();
            if (!this.isPointsForCorners() && !this.isPointsForItems()) {
                return points; // default points
            }
            if (this.isPointsForCorners()) {
                int coveredCorners = 0;
                int x = this.getLine();
                int y = this.getColumn();
                for (int corner = 0; corner < 4; corner++) {
                    if (checkIfCovers(x, y, corner, playerTable))
                        coveredCorners++;
                }
                return points * coveredCorners; // 2*coveredCorners
            }
            if (this.isPointsForItems()) {
                return points * playerTable.getResourceCounter(this.itemRequired.ordinal() + 4); // points=ResourceCounter[item]
            }
        } else
            return 0; // card is not front
        return 0;
    }

    private boolean checkIfCovers(int x, int y, int corner, PlayerTable playerTable) {
        return playerTable.getPlacedCard(playerTable.setXCoordinate(x, corner), playerTable.setYCoordinate(y, corner)) != null && playerTable.getPlacedCard(x, y).getVisibleCorners()[corner].getLinkedCorner().equals(playerTable.getPlacedCard(playerTable.setXCoordinate(x, corner), playerTable.setYCoordinate(y, corner)).getVisibleCorners()[3 - corner]);
    }

    @Override
    public String toString() {
        return "GoldCard: resource: " + getResourceCentreBack().toString();
    }
}
