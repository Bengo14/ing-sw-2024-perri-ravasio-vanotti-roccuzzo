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

    public String getOrientation() {
        return orientation;
    }

    public Resources getMainResource() {
        return mainResource;
    }

    public Resources getSecondaryResource() {
        return secondaryResource;
    }

    /**
     * This method checks if the player table contains the required pattern and computes the points.
     * It also flags the cards that are part of the pattern.
     * @param playerTable the player table to check.
     * @return the points obtained by the player.
     */
    @Override
    public int checkPatternAndComputePoints(PlayerTable playerTable) {
        int patterns;
        int shiftX=0;
        int corner=0;
        if(getOrientation().equals("topLeft")){
            shiftX=-2;
            corner=0;
        }
        if(getOrientation().equals("topRight")) {
            shiftX=-2;
            corner=1;
        }
        if(getOrientation().equals("bottomLeft")) {
            shiftX= 2;
            corner=2;
        }
        if(getOrientation().equals("bottomRight")){
            shiftX= 2;
            corner=3;
        }
        patterns=LShapePatternsCounter(playerTable, shiftX, corner);
        // unflagger
        for(int i=0; i<PlayerTable.getMatrixDimension(); i++){
            for(int j=0; j<PlayerTable.getMatrixDimension(); j++){
                if(playerTable.getPlacedCard(i,j)!=null)
                    playerTable.getPlacedCard(i,j).setFlaggedForObjective(false);
            }
        }
        return getPoints()* patterns;
    }

    /**
     * This method counts the number of patterns present on the player table.
     * @param playerTable the player table to check.
     * @param shift the shift of the pattern.
     * @param corner the corner of the pattern.
     * @return the number of patterns present on the player table.
     */
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
                if(isMainResourceMatchedAndNotFlagged(playerTable.getPlacedCard(i,j))){
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
        return card!=null && getMainResource().equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
    private boolean isSecondaryResourceMatchedAndNotFlagged(PlaceableCard card){
        return card!=null && getSecondaryResource().equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
    private boolean LShapePatternVerifier(int x, int y, int verticalCardsRequired, int diagonalCardsRequired, int shift, int corner, PlayerTable playerTable){
        if(verticalPatternVerifier(x, y, verticalCardsRequired,shift,playerTable)){
            if(shift>0){
                x=x+(shift*verticalCardsRequired-1);
            }
            if(shift<0){
                x=x+(shift*verticalCardsRequired+1);
            }
            y=playerTable.setYCoordinate(y,corner);
            // diagonalPattern failed
            return diagonalPatternVerifier(x, y, diagonalCardsRequired, corner, playerTable); // LShapePattern verified
        }else
            return false; // verticalPattern failed
    }

    /**
     * This method checks if the player table contains the required pattern and flags the cards that are part of the pattern.
     * @param x the x coordinate of the card.
     * @param y the y coordinate of the card.
     * @param verticalCardsRequired the number of vertical cards required.
     * @param shift the shift of the pattern.
     * @param playerTable the player table to check.
     * @return true if the pattern is present, false otherwise.
     */
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
    /**
     * This method checks if the player table contains the required pattern and flags the cards that are part of the pattern.
     * @param x the x coordinate of the card.
     * @param y the y coordinate of the card.
     * @param diagonalCardsRequired the number of diagonal cards required.
     * @param corner the corner of the pattern.
     * @param playerTable the player table to check.
     * @return true if the pattern is present, false otherwise.
     */
    private boolean diagonalPatternVerifier(int x, int y, int diagonalCardsRequired, int corner, PlayerTable playerTable){
        int diagonalCardsMatch =0;
        for (int i = 0; i < diagonalCardsRequired; i++) {
            if (!(playerTable.getPlacedCard(x, y) instanceof StartingCard) && isSecondaryResourceMatchedAndNotFlagged(playerTable.getPlacedCard(x, y))) {
                diagonalCardsMatch++;
//                --- only needed on diagonalCardsRequired>1 ---
//                if (diagonalCardsMatch <= diagonalCardsRequired - 1) {
//                    if (playerTable.getPlacedCard(x, y).getCorners()[corner].getLinkedCorner() == playerTable.getPlacedCard(playerTable.setXCoordinate(x, corner), playerTable.setYCoordinate(y, corner)).getCorners()[3 - corner]) {
//                        x = (playerTable.setXCoordinate(x, corner)); //set coordinates to next card
//                        y = (playerTable.setYCoordinate(y, corner));
//                    } else {
//                        return false; // card is not linked to next card
//                    }
//                }
            } else
                return false; // found StartingCard OR resource is not matched OR card is already flaggedForObjective OR card is null
        }
        return diagonalCardsMatch == diagonalCardsRequired;
    }
    /**
     * This method flags the cards that are part of the pattern.
     * @param x the x coordinate of the card.
     * @param y the y coordinate of the card.
     * @param verticalCardsRequired the number of vertical cards required.
     * @param diagonalCardsRequired the number of diagonal cards required.
     * @param shift the shift of the pattern.
     * @param corner the corner of the pattern.
     * @param playerTable the player table to check.
     */
    private void LShapePatternFlagger(int x, int y, int verticalCardsRequired, int diagonalCardsRequired, int shift, int corner, PlayerTable playerTable){
        verticalPatternFlagger(x,y,verticalCardsRequired,shift,playerTable);
        if(shift>0){
            x=x+(shift*verticalCardsRequired-1);
        }
        if(shift<0){
            x=x+(shift*verticalCardsRequired+1);
        }
        y=playerTable.setYCoordinate(y,corner);
        diagonalPatternFlagger(x,y,diagonalCardsRequired,corner,playerTable);
    }
    /**
     * This method flags the vertical cards that are part of the pattern.
     * @param x the x coordinate of the card.
     * @param y the y coordinate of the card.
     * @param verticalCardsRequired the number of vertical cards required.
     * @param shift the shift of the pattern.
     * @param playerTable the player table to check.
     */
    private void verticalPatternFlagger(int x, int y, int verticalCardsRequired, int shift, PlayerTable playerTable){
        for (int k = 0; k < verticalCardsRequired; k++) {
            playerTable.getPlacedCard(x,y).setFlaggedForObjective(true);
            x=x+shift; // set coordinates to next vertical card
        }
    }
    /**
     * This method flags the diagonal cards that are part of the pattern.
     * @param x the x coordinate of the card.
     * @param y the y coordinate of the card.
     * @param diagonalCardsRequired the number of diagonal cards required.
     * @param corner the corner of the pattern.
     * @param playerTable the player table to check.
     */
    private void diagonalPatternFlagger(int x, int y, int diagonalCardsRequired, int corner, PlayerTable playerTable) {
        for (int i = 0; i < diagonalCardsRequired; i++) {
            playerTable.getPlacedCard(x, y).setFlaggedForObjective(true);
            x = (playerTable.setXCoordinate(x, corner));
            y = (playerTable.setYCoordinate(y, corner));
        }
    }
}