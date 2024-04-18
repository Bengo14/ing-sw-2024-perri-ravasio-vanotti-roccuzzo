package it.polimi.sw.gianpaolocugola47.model;

/**
 * This class represents pattern-oriented objectives, i.e. objectives that give points only if a certain card
 * pattern is present on a given player table.
 */
public class LShapePatternObjective extends Objectives{

    private final String orientation;
    private final Resources mainResource;
    private final Resources secondaryResource;

    /**
     * LShapePatternObjective constructor.
     *
     * @param points 3
     * @param imgPathFront the reference to the back's image.
     * @param imgPathBack the reference to the back image's path.
     * @param orientation "bottomRight" "bottomLeft" "topRight" "topLeft"
     * @param mainResource is the resource that appears 2 times
     * @param secondaryResource is the resource that appears 1 time
     */
    public LShapePatternObjective(int points, String imgPathFront, String imgPathBack, String orientation, Resources mainResource, Resources secondaryResource){
        super(points, imgPathFront, imgPathBack);
        this.orientation = orientation;
        this.mainResource = mainResource;
        this.secondaryResource = secondaryResource;
    }

    @Override
    public int checkPatternAndComputePoints(PlayerTable playerTable) {
        int x=0;
        int corner=0;
        if(this.orientation.equals("bottomRight")){
            x++;
            corner=3;
        }
        if(this.orientation.equals("bottomLeft")) {
            x++;
            corner=2;
        }
        if(this.orientation.equals("topRight")) {
            x--;
            corner=1;
        }
        if(this.orientation.equals("topLeft")){
            x--;
            corner=0;
        }
        return this.getPoints()*patternsCounter(playerTable, x, corner);
    }
    private int patternsCounter(PlayerTable playerTable, int shift , int corner){
        int patternsCounter=0;
        int verticalCardsRequired=2;
        int verticalCardsMatch=0;
        int diagonalCardsRequired=1;
        int diagonalCardsMatch=0;
        for(int i = 0; i< PlayerTable.getMatrixDimension(); i++){
            for(int j = 0; j< PlayerTable.getMatrixDimension(); j++){
                if (playerTable.getPlacedCard(i,j) instanceof StartingCard){
                    j++;
                }
                if(isMainResourceMatched(playerTable.getPlacedCard(i,j))){
                    int x=i;
                    int y=j;
                    for (int k = 0; k < verticalCardsRequired; k++) {
                        if(isMainResourceMatched(playerTable.getPlacedCard(x,y)) && !(playerTable.getPlacedCard(x,y) instanceof StartingCard)){
                            verticalCardsMatch++;
                            x=x+shift;
                        }
                    }
                    x=x-shift;
                    if(verticalCardsMatch==verticalCardsRequired && playerTable.getPlacedCard(x,y).getCorners()[corner].getLinkedCorner()==playerTable.getPlacedCard(playerTable.setXCoordinate(x,corner),playerTable.setYCoordinate(y,corner)).getCorners()[3-corner]){
                        x=playerTable.setXCoordinate(x,corner);
                        y=playerTable.setYCoordinate(y,corner);
                        for (int k = 0; k < diagonalCardsRequired; k++) {
                            if(isSecondaryResourceMatched(playerTable.getPlacedCard(x,y)) && !(playerTable.getPlacedCard(x,y) instanceof StartingCard)){
                                if(diagonalCardsMatch<diagonalCardsRequired-1){
                                   if(playerTable.getPlacedCard(x,y).getCorners()[corner].getLinkedCorner()==playerTable.getPlacedCard(playerTable.setXCoordinate(x,corner),playerTable.setYCoordinate(y,corner)).getCorners()[3-corner]){
                                       diagonalCardsMatch++;
                                       x=(playerTable.setXCoordinate(x,corner));
                                       y=(playerTable.setYCoordinate(j,corner));
                                   }
                                }else{
                                    diagonalCardsMatch++;
                                    x=(playerTable.setXCoordinate(x,corner));
                                    y=(playerTable.setYCoordinate(j,corner));
                                }
                            }
                        }
                    }
                    if(verticalCardsMatch==verticalCardsRequired && diagonalCardsMatch==diagonalCardsRequired){
                        patternsCounter++;
                        x=i;
                        y=j;
                        for (int k = 0; k < verticalCardsRequired; k++) {
                                playerTable.getPlacedCard(x,y).setFlaggedForObjective(true);
                                x=x+shift;
                        }
                        x=x-shift;
                        x=playerTable.setXCoordinate(x,corner);
                        y=playerTable.setYCoordinate(y,corner);
                        for (int k = 0; k < diagonalCardsRequired; k++) {
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
    /**
     * checks if the ResourceOfTheCard==ResourceOfThePattern, also checks if card is flagged for objective
     */
    private boolean isMainResourceMatched(PlaceableCard card){
        return this.mainResource.equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
    private boolean isSecondaryResourceMatched(PlaceableCard card){
        return this.secondaryResource.equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
}