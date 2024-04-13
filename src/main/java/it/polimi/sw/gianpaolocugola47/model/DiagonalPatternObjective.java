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
        //creating a copy of the matrix
        PlaceableCard[][] matrix = new PlaceableCard[PlayerTable.getMatrixDimension()][PlayerTable.getMatrixDimension()];
        for (int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
            for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                matrix[i][j] = playerTable.getElement(i, j);
            }
        }
        int patternOccurrences=0;
        //looking for pattern occurrences
        if(this.isAscending) {
            for (int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
                for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                    if (playerTable.isStartingCard(i,j)){
                        j++;
                    }
                    if(isResourceMatched(matrix[i][j]) && i<(PlayerTable.getMatrixDimension()-2) && j>1){
                        if(isResourceMatched(matrix[i+1][j-1]) && !playerTable.isStartingCard(i+1, j-1)){
                            if(isResourceMatched(matrix[i+2][j-2]) && !playerTable.isStartingCard(i+2,j-2)){
                                matrix[i][j].setFlaggedForObjective(true);
                                matrix[i+1][j-1].setFlaggedForObjective(true);
                                matrix[i+2][j-2].setFlaggedForObjective(true);
                                patternOccurrences++;
                            }
                        }
                    }
                }
            }
        }else{
            for (int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
                for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                    if (playerTable.isStartingCard(i,j)){
                        j++;
                    }
                    if(isResourceMatched(matrix[i][j]) && i<(PlayerTable.getMatrixDimension()-2) && j<(PlayerTable.getMatrixDimension()-2)){
                        if(isResourceMatched(matrix[i+1][j+1]) && !playerTable.isStartingCard(i+1, j+1)){
                            if(isResourceMatched(matrix[i+2][j+2]) && !playerTable.isStartingCard(i+2,j+2)){
                                matrix[i][j].setFlaggedForObjective(true);
                                matrix[i+1][j+1].setFlaggedForObjective(true);
                                matrix[i+2][j+2].setFlaggedForObjective(true);
                                patternOccurrences++;
                            }
                        }
                    }
                }
            }
        }
        return this.getPoints()*patternOccurrences;
    }
    /**
     * checks if the ResourceOfTheCard==ResourceOfThePattern, also checks if card is flagged for objective
     * @param card card[i][j]
     */
    private boolean isResourceMatched(PlaceableCard card){
        return this.resource.equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
}