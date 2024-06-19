package it.polimi.sw.gianpaolocugola47.model;

/**
 * This class represents pattern-oriented objectives, i.e. objectives that give points only if a certain card
 * pattern is present on a given player table. It extends the Objectives class.
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

    /**
     * This method returns the resource of the objective.
     * @return the resource of the objective.
     */
    public Resources getResource() {
        return resource;
    }

    /**
     * This method returns the type of the objective.
     * @return true if it is ascending, false otherwise.
     */
    public boolean isAscending() {
        return isAscending;
    }

    /**
     * This method checks if the pattern is present on a given board and, if that is the case, computes the points.
     * @param playerTable the player table to check.
     * @return the points computed * patterns.
     */
    @Override
    public int checkPatternAndComputePoints(PlayerTable playerTable) {
        int patterns;
        int corner;
        int cardMatches = 0;
        // checks from top to bottom
        if (isAscending()) {
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
    /**
     * This method counts the number of diagonal patterns.
     * @param playerTable the player table to check.
     * @param corner the corner to start from.
     * @return the number of diagonal patterns.
     */
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
    /**
     * This method checks if the resource is matched and not flagged.
     * @param card the card to check.
     * @return true if the resource is matched and not flagged, false otherwise.
     */
    protected boolean isResourceMatchedAndNotFlagged(PlaceableCard card) {
        return card!=null && getResource().equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
    /**
     * This method verifies the diagonal pattern.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param cardsRequired the number of cards required.
     * @param corner the corner to start from.
     * @param playerTable the player table to check.
     * @return true if the diagonal pattern is verified, false otherwise.
     */
    private boolean diagonalPatternVerifier(int x, int y, int cardsRequired, int corner, PlayerTable playerTable){
        int cardsMatch=0;
        for (int i = 0; i < cardsRequired; i++) {
            if (isResourceMatchedAndNotFlagged(playerTable.getPlacedCard(x, y))) {
                cardsMatch++;
                if (cardsMatch <= cardsRequired - 1) {
                    if (!(playerTable.getPlacedCard(playerTable.setXCoordinate(x, corner), playerTable.setYCoordinate(y, corner)) instanceof StartingCard) && playerTable.getPlacedCard(playerTable.setXCoordinate(x, corner),playerTable.setYCoordinate(y,corner))!=null) {
                        x = (playerTable.setXCoordinate(x, corner)); //set coordinates to next card
                        y = (playerTable.setYCoordinate(y, corner));
                    } else {
                        return false; // found starting card OR NEXT card is null OR card is not linked to next card
                    }
                }
            } else
                return false; // resource is not matched OR card is already flaggedForObjective OR card is null
        }
        return true; // diagonal pattern verified
    }
    /**
     * This method flags the diagonal pattern.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param cardsRequired the number of cards required.
     * @param corner the corner to start from.
     * @param playerTable the player table to check.
     */
    private void diagonalPatternFlagger(int x, int y, int cardsRequired, int corner, PlayerTable playerTable) {
        for (int i = 0; i < cardsRequired; i++) {
            playerTable.getPlacedCard(x, y).setFlaggedForObjective(true);
            x = (playerTable.setXCoordinate(x, corner));
            y = (playerTable.setYCoordinate(y, corner));
        }
    }
}