package it.polimi.sw.gianpaolocugola47.model;

/**
 * This class represents pattern-oriented objectives, i.e. objectives that give points only if a certain card
 * pattern is present on a given player table.
 */
public class DiagonalPatternObjective extends Objectives {

    private final boolean isAscending;
    private final Resources resource;

    /**
     * DiagonalPatternObjective constructor.
     *
     * @param points 2
     * @param imgPathFront the reference to the back's image.
     * @param imgPathBack the reference to the back image's path.
     * @param isAscending boolean
     * @param mainResource the resource that appears 3 times
     */
    public DiagonalPatternObjective(int points, String imgPathFront, String imgPathBack, boolean isAscending, Resources mainResource) {
        super(points, imgPathFront, imgPathBack);
        this.isAscending = isAscending;
        this.resource = mainResource;
    }

    public Resources getResource() {
        return resource;
    }

    public boolean isAscending() {
        return isAscending;
    }

    @Override
    public int checkPatternAndComputePoints(PlayerTable playerTable) {
        int corner;
        int cardMatches=0;
        // checks from top to bottom
        if(this.isAscending) {
            corner=2;
        }else{
            corner=3;
        }
        return this.getPoints()*patternsCounter(playerTable, corner);
    }
    private int patternsCounter(PlayerTable playerTable, int corner) {
        int patternsCounter=0;
        int cardsRequired=3;
        int cardsMatch=0;
        // scroll placedCards[i][j]
        for (int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
            for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                if (playerTable.getPlacedCard(i,j) instanceof StartingCard){
                    j++; // skip StartingCard
                }
                if(isResourceMatchedAndNotFlagged(playerTable.getPlacedCard(i,j))){
                    // found first card BUT not yet counted
                    int x=i;
                    int y=j;
                    for (int k = 0; k < cardsRequired; k++) {
                        if(isResourceMatchedAndNotFlagged(playerTable.getPlacedCard(x,y)) && !(playerTable.getPlacedCard(x,y) instanceof StartingCard)){
                            cardsMatch++;
                            if(cardsMatch<=cardsRequired-1) {
                                if(playerTable.getPlacedCard(x,y).getCorners()[corner].getLinkedCorner()==playerTable.getPlacedCard(playerTable.setXCoordinate(x,corner),playerTable.setYCoordinate(y,corner)).getCorners()[3-corner]){
                                    x=(playerTable.setXCoordinate(x,corner)); //set coordinates to next card
                                    y=(playerTable.setYCoordinate(y,corner));
                                }else {
                                    break; // card is not linked to next card
                                }
                            }
                        }else
                            break; // found StartingCard OR resource is not matched OR card is already flaggedForObjective
                    }
                    if(cardsMatch==cardsRequired){
                        patternsCounter++;
                        x=i;
                        y=j;
                        for(int k = 0; k < cardsRequired; k++){
                            playerTable.getPlacedCard(x,y).setFlaggedForObjective(true);
                            x=(playerTable.setXCoordinate(x,corner));
                            y=(playerTable.setYCoordinate(y,corner));
                        }
                    }
                }
            }
        }
        return patternsCounter;
    }
    protected boolean isResourceMatchedAndNotFlagged(PlaceableCard card){
        return this.resource.equals(((ResourceCard)card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
}