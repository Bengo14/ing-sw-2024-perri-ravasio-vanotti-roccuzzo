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

    @Override
    public int checkPatternAndComputePoints(PlayerTable playerTable) {
        int corner;
        int cardMatches=0;
        if(this.isAscending) {
            corner=2;
        }else{
            corner=1;
        }
        return this.getPoints()*patternsCounter(playerTable, corner);
    }
    private int patternsCounter(PlayerTable playerTable, int corner) {
        int patternsCounter=0;
        int cardsRequired=3;
        int cardsMatch=0;
        for (int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
            for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                if (playerTable.getPlacedCard(i,j) instanceof StartingCard){
                    j++;
                }
                int x=i;
                int y=j;
                for (int k = 0; k < cardsRequired; k++) {
                    if(isResourceMatched(playerTable.getPlacedCard(x,y)) && !(playerTable.getPlacedCard(x,y) instanceof StartingCard)){
                        cardsMatch++;
                        x=(playerTable.setXCoordinate(x,corner));
                        y=(playerTable.setYCoordinate(y,corner));
                    }
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
        return patternsCounter;
    }
    private boolean isResourceMatched(PlaceableCard card){
        return this.resource.equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
}