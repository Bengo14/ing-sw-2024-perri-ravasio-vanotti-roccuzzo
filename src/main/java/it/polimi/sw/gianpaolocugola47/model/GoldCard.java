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
    private final Items itemThatGivesPoints;
    /**
     * GoldCard constructor.
     *
     * @param backImgPath refer to the image of the back.
     * @param frontImgPath refer to the image of the front.
     * @param points the points the card scores when played.
     * @param pointsForCorners the points the card scores covering angles when played.
     */
    public GoldCard(String backImgPath, String frontImgPath, int points, boolean pointsForCorners, Resources resourceCentreBack,
                    Items itemRequired, boolean pointsForItems, Items itemThatGivesPoints) {
        super(backImgPath, frontImgPath, points, resourceCentreBack);
        this.pointsForCorners = pointsForCorners;
        this.pointsForItems = pointsForItems;
        this.itemThatGivesPoints = itemThatGivesPoints;
    }

    public ArrayList<Resources> getResourcesRequired() {
        return resourcesRequired;
    }

    protected boolean isPointsForCorners() {
        return pointsForCorners;
    }
    protected Items getItemThatGivesPoints() {
        return itemThatGivesPoints;
    }
    protected boolean isPointsForItems() {
        return pointsForItems;
    }

    @Override
    public void updateResourceCounter(int[] counter){
        if(isFront()){
            Corner[] visibleCorners = getVisibleCorners();
            for(int i=0; i<4; i++){
                Corner corner = visibleCorners[i];
                if(corner.isItem()){
                    counter[corner.getItem().ordinal() + 4]++;
                    break;
                }
            }
        }
        else{
            counter[getResourceCentreBack().ordinal()]++;
        }
    }
    @Override
    public int getPoints(PlayerTable playerTable) {
        if(this.isFront()){
            int points=this.getThisPoints();
            if(!this.isPointsForCorners() && !this.isPointsForItems()){
                return points; // default points
            }
            if(this.isPointsForCorners()){
                int coveredCorners = 0;
                int x=this.getLine();
                int y=this.getColumn();
                for(int corner=0; corner<4; corner++){
                    if(checkIfCovers(x, y, corner, playerTable))
                        coveredCorners++;
                }
                return points*coveredCorners; // 2*coveredCorners
            }
            if(this.isPointsForItems()){
                return points*playerTable.getResourceCounter(this.itemThatGivesPoints.ordinal() + 4); // points=ResourceCounter[item]
            }
        }else
            return 0;
        return -1; // incorrect GoldCard initialization
    }
    private boolean checkIfCovers(int x, int y, int corner, PlayerTable playerTable){
        return playerTable.getPlacedCard(playerTable.setXCoordinate(x, corner), playerTable.setYCoordinate(y, corner)) != null && playerTable.getPlacedCard(x,y).getVisibleCorners()[corner].getLinkedCorner() == playerTable.getPlacedCard(playerTable.setXCoordinate(x,corner), playerTable.setYCoordinate(y, corner)).getVisibleCorners()[3-corner];
    }
}
