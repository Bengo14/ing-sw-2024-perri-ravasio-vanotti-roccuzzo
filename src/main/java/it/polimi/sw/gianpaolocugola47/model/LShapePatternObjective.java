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
        int patterns;
        int shiftX =0;
        int corner=0;
        if(this.orientation.equals("topLeft")){
            shiftX--;
            corner=0;
        }
        if(this.orientation.equals("topRight")) {
            shiftX--;
            corner=1;
        }
        if(this.orientation.equals("bottomLeft")) {
            shiftX++;
            corner=2;
        }
        if(this.orientation.equals("bottomRight")){
            shiftX++;
            corner=3;
        }
        patterns=LShapePatternsCounter(playerTable, shiftX, corner);
        for(int i=0; i<PlayerTable.getMatrixDimension(); i++){
            for(int j=i+1; j<PlayerTable.getMatrixDimension(); j++){
                if(playerTable.getPlacedCard(i,j)!=null)
                    playerTable.getPlacedCard(i,j).setFlaggedForObjective(false);
            }
        }
        return this.getPoints()* patterns;
    }
    private int LShapePatternsCounter(PlayerTable playerTable, int shift , int corner){
        int patternsCounter=0;
        int verticalCardsRequired=2;
        int diagonalCardsRequired=1;
        // scroll placedCards[i][j]
        for(int i = 0; i< PlayerTable.getMatrixDimension(); i++){
            for(int j = 0; j< PlayerTable.getMatrixDimension(); j++){
                if (playerTable.getPlacedCard(i,j) instanceof StartingCard){
                    j++; // skip StartingCard
                }
                //looking for vertical cards
                if(isMainResourceMatchedAndNotFlagged(playerTable.getPlacedCard(i,j)) && verticalCardsRequired>0 && diagonalCardsRequired>0){
                    // found vertical card BUT not yet counted
                    if(shift<0 && corner==0 && i>=verticalCardsRequired+diagonalCardsRequired-1 && j>=diagonalCardsRequired){
                        if(LShapePatternVerifier(i, j, verticalCardsRequired, diagonalCardsRequired, shift, corner, playerTable)){
                            patternsCounter++;
                            LShapePatternFlagger(i,j, verticalCardsRequired,diagonalCardsRequired, shift, corner, playerTable);
                        }
                    }
                    if(shift<0 && corner==1 && i>=verticalCardsRequired+diagonalCardsRequired-1 && j<=PlayerTable.getMatrixDimension()-diagonalCardsRequired-1){
                        if(LShapePatternVerifier(i, j, verticalCardsRequired, diagonalCardsRequired, shift, corner, playerTable)){
                            patternsCounter++;
                            LShapePatternFlagger(i,j, verticalCardsRequired,diagonalCardsRequired, shift, corner, playerTable);
                        }
                    }
                    if(shift>0 && corner==2 && i<=PlayerTable.getMatrixDimension()-verticalCardsRequired-diagonalCardsRequired && j>=diagonalCardsRequired){
                        if(LShapePatternVerifier(i, j, verticalCardsRequired, diagonalCardsRequired, shift, corner, playerTable)){
                            patternsCounter++;
                            LShapePatternFlagger(i,j, verticalCardsRequired,diagonalCardsRequired, shift, corner, playerTable);
                        }
                    }
                    if(shift>0 && corner==3 && i<=PlayerTable.getMatrixDimension()-verticalCardsRequired-diagonalCardsRequired && j<=PlayerTable.getMatrixDimension()-diagonalCardsRequired-1){
                        if(LShapePatternVerifier(i, j, verticalCardsRequired, diagonalCardsRequired, shift, corner, playerTable)){
                            patternsCounter++;
                            LShapePatternFlagger(i,j, verticalCardsRequired,diagonalCardsRequired, shift, corner, playerTable);
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
        return card!=null && this.mainResource.equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
    private boolean isSecondaryResourceMatchedAndNotFlagged(PlaceableCard card){
        return card!=null && this.secondaryResource.equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
    private boolean LShapePatternVerifier(int x, int y, int verticalCardsRequired, int diagonalCardsRequired, int shift, int corner, PlayerTable playerTable){
        if(verticalPatternVerifier(x, y, verticalCardsRequired,shift,playerTable) && diagonalCardsRequired>0 && playerTable.getPlacedCard(x+verticalCardsRequired-1,y).getCorners()[corner].getLinkedCorner()==playerTable.getPlacedCard(x, playerTable.setYCoordinate(y, corner)).getCorners()[3-corner]){
            x=x+verticalCardsRequired;
            y=playerTable.setYCoordinate(y,corner);
            if(diagonalPatternVerifier(x,y,diagonalCardsRequired,corner,playerTable)){
                return true; // LShapePattern verified
            }else
                return false; // diagonalPattern failed
        }else
            return false; // verticalPattern failed
    }
    private boolean verticalPatternVerifier(int x, int y, int verticalCardsRequired, int shift, PlayerTable playerTable){
        int verticalCardsMatch=0;
        for (int i = 0; i < verticalCardsRequired; i++) {
            if(!(playerTable.getPlacedCard(x, y) instanceof StartingCard) && isMainResourceMatchedAndNotFlagged(playerTable.getPlacedCard(x,y))){
                verticalCardsMatch++;
                x=x+shift; // set coordinates to next vertical card
            }else
                return false; // found StartingCard OR resource is not matched OR card is already flaggedForObjective OR card is null
        }
        return verticalCardsMatch == verticalCardsRequired;
    }
    private boolean diagonalPatternVerifier(int x, int y, int diagonalCardsRequired, int corner, PlayerTable playerTable){
        int diagonalCardsMatch =0;
        for (int i = 0; i < diagonalCardsRequired; i++) {
            if (isSecondaryResourceMatchedAndNotFlagged(playerTable.getPlacedCard(x, y)) && !(playerTable.getPlacedCard(x, y) instanceof StartingCard)) {
                diagonalCardsMatch++;
                if (diagonalCardsMatch <= diagonalCardsRequired - 1) {
                    if (playerTable.getPlacedCard(x, y).getCorners()[corner].getLinkedCorner() == playerTable.getPlacedCard(playerTable.setXCoordinate(x, corner), playerTable.setYCoordinate(y, corner)).getCorners()[3 - corner]) {
                        x = (playerTable.setXCoordinate(x, corner)); //set coordinates to next card
                        y = (playerTable.setYCoordinate(y, corner));
                    } else {
                        return false; // card is not linked to next card
                    }
                }
            } else
                return false; // found StartingCard OR resource is not matched OR card is already flaggedForObjective OR card is null
        }
        return diagonalCardsMatch == diagonalCardsRequired;
    }
    private void LShapePatternFlagger(int x, int y, int verticalCardsRequired, int diagonalCardsRequired, int shift, int corner, PlayerTable playerTable){
        verticalPatternFlagger(x,y,verticalCardsRequired,shift,playerTable);
        x=x+verticalCardsRequired;
        y=playerTable.setYCoordinate(y,corner);
        diagonalPatternFlagger(x,y,diagonalCardsRequired,corner,playerTable);
    }
    private void verticalPatternFlagger(int x, int y, int verticalCardsRequired, int shift, PlayerTable playerTable){
        for (int k = 0; k < verticalCardsRequired; k++) {
            playerTable.getPlacedCard(x,y).setFlaggedForObjective(true);
            x=x+shift; // set coordinates to next vertical card
        }
    }
    private void diagonalPatternFlagger(int x, int y, int diagonalCardsRequired, int corner, PlayerTable playerTable) {
        for (int i = 0; i < diagonalCardsRequired; i++) {
            playerTable.getPlacedCard(x, y).setFlaggedForObjective(true);
            x = (playerTable.setXCoordinate(x, corner));
            y = (playerTable.setYCoordinate(y, corner));
        }
    }
}