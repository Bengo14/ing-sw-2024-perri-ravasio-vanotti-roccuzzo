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
     * @param points       2
     * @param imgPathFront the reference to the back's image.
     * @param imgPathBack  the reference to the back image's path.
     * @param isAscending  boolean
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
        int patterns;
        int corner;
        int cardMatches = 0;
        // checks from top to bottom
        if (this.isAscending) {
            corner = 2;
        } else {
            corner = 3;
        }
        patterns=diagonalPatternsCounter(playerTable, corner);
        // unflagger
        for(int i=0; i<PlayerTable.getMatrixDimension(); i++){
            for(int j=0; j<PlayerTable.getMatrixDimension(); j++){
                if(playerTable.getPlacedCard(i,j)!=null)
                    playerTable.getPlacedCard(i,j).setFlaggedForObjective(false);
            }
        }
        return this.getPoints()* patterns;
    }
    private int diagonalPatternsCounter(PlayerTable playerTable, int corner) {
        int patternsCounter = 0;
        int cardsRequired = 3;
        // scroll placedCards[i][j]
        for (int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
            for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                if (playerTable.getPlacedCard(i, j) instanceof StartingCard) {
                    j++; // skip StartingCard
                }
                if (isResourceMatchedAndNotFlagged(playerTable.getPlacedCard(i, j))) {
                    // found first card BUT not yet counted
                    if(corner==2 && i<=PlayerTable.getMatrixDimension()-cardsRequired && j>=cardsRequired-1){
                        if(diagonalPatternVerifier(i,j,cardsRequired, corner,playerTable)){
                            patternsCounter++;
                            diagonalPatternFlagger(i,j,cardsRequired,corner,playerTable);
                        }
                    }
                    if(corner==3 && i<=PlayerTable.getMatrixDimension()-cardsRequired && j<=PlayerTable.getMatrixDimension()-cardsRequired){
                        if(diagonalPatternVerifier(i,j,cardsRequired,corner,playerTable)){
                            patternsCounter++;
                            diagonalPatternFlagger(i,j,cardsRequired,corner,playerTable);
                        }
                    }
                }
            }
        }
        return patternsCounter;
    }
    protected boolean isResourceMatchedAndNotFlagged(PlaceableCard card) {
        return card!=null && this.resource.equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
    private boolean diagonalPatternVerifier(int x, int y, int cardsRequired, int corner, PlayerTable playerTable){
        int cardsMatch=0;
        for (int i = 0; i < cardsRequired; i++) {
            if (!(playerTable.getPlacedCard(x, y) instanceof StartingCard) && isResourceMatchedAndNotFlagged(playerTable.getPlacedCard(x, y))) {
                cardsMatch++;
                if (cardsMatch <= cardsRequired - 1) {
                    if (!(playerTable.getPlacedCard(playerTable.setXCoordinate(x, corner), playerTable.setYCoordinate(y, corner)) instanceof StartingCard) && playerTable.getPlacedCard(playerTable.setXCoordinate(x, corner),playerTable.setYCoordinate(y,corner))!=null && playerTable.getPlacedCard(x, y).getVisibleCorners()[corner].getLinkedCorner() == playerTable.getPlacedCard(playerTable.setXCoordinate(x, corner), playerTable.setYCoordinate(y, corner)).getVisibleCorners()[3 - corner]) {
                        x = (playerTable.setXCoordinate(x, corner)); //set coordinates to next card
                        y = (playerTable.setYCoordinate(y, corner));
                    } else {
                        return false; // found starting card OR NEXT card is null OR card is not linked to next card
                    }
                }
            } else
                return false; // found StartingCard OR resource is not matched OR card is already flaggedForObjective OR card is null
        }
        return cardsMatch == cardsRequired;
    }
    private void diagonalPatternFlagger(int x, int y, int cardsRequired, int corner, PlayerTable playerTable) {
        for (int i = 0; i < cardsRequired; i++) {
            playerTable.getPlacedCard(x, y).setFlaggedForObjective(true);
            x = (playerTable.setXCoordinate(x, corner));
            y = (playerTable.setYCoordinate(y, corner));
        }
    }
}