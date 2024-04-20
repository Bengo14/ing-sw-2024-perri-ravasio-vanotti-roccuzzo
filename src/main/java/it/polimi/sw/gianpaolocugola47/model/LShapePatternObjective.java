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
        int shiftX =0;
        int corner=0;
        if(this.orientation.equals("bottomRight")){
            shiftX++;
            corner=3;
        }
        if(this.orientation.equals("bottomLeft")) {
            shiftX++;
            corner=2;
        }
        if(this.orientation.equals("topRight")) {
            shiftX--;
            corner=1;
        }
        if(this.orientation.equals("topLeft")){
            shiftX--;
            corner=0;
        }
        return this.getPoints()*patternsCounter(playerTable, shiftX, corner);
    }
    private int patternsCounter(PlayerTable playerTable, int shift , int corner){
        int patternsCounter=0;
        int verticalCardsRequired=2;
        int verticalCardsMatch=0;
        int diagonalCardsRequired=1;
        int diagonalCardsMatch=0;
        // scroll placedCards[i][j]
        for(int i = 0; i< PlayerTable.getMatrixDimension(); i++){
            for(int j = 0; j< PlayerTable.getMatrixDimension(); j++){
                if (playerTable.getPlacedCard(i,j) instanceof StartingCard){
                    j++; // skip StartingCard
                }
                //looking for vertical cards
                if(isMainResourceMatchedAndNotFlagged(playerTable.getPlacedCard(i,j))){
                    // found vertical card BUT not yet counted
                    int x=i;
                    int y=j;
                    for (int k = 0; k < verticalCardsRequired; k++) {
                        if(isMainResourceMatchedAndNotFlagged(playerTable.getPlacedCard(x,y)) && !(playerTable.getPlacedCard(x,y) instanceof StartingCard)){
                            verticalCardsMatch++;
                            x=x+shift; // set coordinates to next vertical card
                        }else
                            break; // found StartingCard OR resource is not matched OR card is already flaggedForObjective
                    }
                    x=x-shift; //set coordinates to last vertical card
                    //looking for diagonal cards
                    if(isSecondaryResourceMatchedAndNotFlagged(playerTable.getPlacedCard(playerTable.setXCoordinate(x,corner),playerTable.setYCoordinate(y,corner))) && verticalCardsMatch==verticalCardsRequired && playerTable.getPlacedCard(x,y).getCorners()[corner].getLinkedCorner()==playerTable.getPlacedCard(playerTable.setXCoordinate(x,corner),playerTable.setYCoordinate(y,corner)).getCorners()[3-corner]){
                        //found first diagonal card BUT not yet counted
                        x=playerTable.setXCoordinate(x,corner); // set coordinates to first diagonal card
                        y=playerTable.setYCoordinate(y,corner);

                        for (int k = 0; k < diagonalCardsRequired; k++) {
                            if(isSecondaryResourceMatchedAndNotFlagged(playerTable.getPlacedCard(x,y)) && !(playerTable.getPlacedCard(x,y) instanceof StartingCard)){
                                diagonalCardsMatch++;
                                if(diagonalCardsMatch<=diagonalCardsMatch-1) {
                                    if(playerTable.getPlacedCard(x,y).getCorners()[corner].getLinkedCorner()==playerTable.getPlacedCard(playerTable.setXCoordinate(x,corner),playerTable.setYCoordinate(y,corner)).getCorners()[3-corner]){
                                        x=(playerTable.setXCoordinate(x,corner)); //set coordinates to next diagonal card
                                        y=(playerTable.setYCoordinate(y,corner));
                                    }else {
                                        break; // card is not linked to next card
                                    }
                                }
                            }else
                                break; // found StartingCard OR resource is not matched OR card is already flaggedForObjective
                        }
                    }
                    if(verticalCardsMatch==verticalCardsRequired && diagonalCardsMatch==diagonalCardsRequired){
                        patternsCounter++;
                        x=i;
                        y=j;
                        for (int k = 0; k < verticalCardsMatch; k++) {
                                playerTable.getPlacedCard(x,y).setFlaggedForObjective(true);
                                x=x+shift; // set coordinates to next vertical card
                        }

                        x=x-shift; // set coordinates to last vertical card

                        for (int k = 0; k < diagonalCardsMatch; k++) {
                            x=(playerTable.setXCoordinate(x,corner)); //set coordinates to next diagonal card
                            y=(playerTable.setYCoordinate(y,corner));
                            playerTable.getPlacedCard(x,y).setFlaggedForObjective(true);
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
    private boolean isMainResourceMatchedAndNotFlagged(PlaceableCard card){
        return this.mainResource.equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
    private boolean isSecondaryResourceMatchedAndNotFlagged(PlaceableCard card){
        return this.secondaryResource.equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
}