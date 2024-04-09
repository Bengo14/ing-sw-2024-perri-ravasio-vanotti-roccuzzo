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
     * LShapePatternObjective CONSTRUCTOR
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
        //creating a copy of the matrix
        PlaceableCard[][] matrix = new PlaceableCard[PlayerTable.getMatrixDimension()][PlayerTable.getMatrixDimension()];
        for (int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
            for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                matrix[i][j] = playerTable.getElement(i, j);
            }
        }
        int patternOccurrences=0;
        //looking for pattern occurrences
        if(this.orientation.equals("bottomRight")){
            for (int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
                for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                    if (playerTable.isStartingCard(i,j)){
                        j++;
                    }
                    if(isMainResourceMatched(matrix[i][j]) && i<(PlayerTable.getMatrixDimension()-3) && j<(PlayerTable.getMatrixDimension()-1)){
                        if(isMainResourceMatched(matrix[i+2][j]) && !playerTable.isStartingCard(i+2, j)){
                            if(isSecondaryResourceMatched(matrix[i+3][j+1]) && !playerTable.isStartingCard(i+3,j+1)){
                                matrix[i][j].setFlaggedForObjective(true);
                                matrix[i+2][j].setFlaggedForObjective(true);
                                matrix[i+3][j+1].setFlaggedForObjective(true);
                                patternOccurrences++;
                            }
                        }
                    }
                }
            }
        }
        if(this.orientation.equals("bottomLeft")) {
            for (int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
                for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                    if (playerTable.isStartingCard(i, j)) {
                        j++;
                    }
                    if (isMainResourceMatched(matrix[i][j]) && i < (PlayerTable.getMatrixDimension() - 3) && j >0) {
                        if (isMainResourceMatched(matrix[i + 2][j]) && !playerTable.isStartingCard(i + 2, j)) {
                            if (isSecondaryResourceMatched(matrix[i + 3][j -1]) && !playerTable.isStartingCard(i + 3, j - 1)) {
                                matrix[i][j].setFlaggedForObjective(true);
                                matrix[i + 2][j].setFlaggedForObjective(true);
                                matrix[i + 3][j - 1].setFlaggedForObjective(true);
                                patternOccurrences++;
                            }
                        }
                    }
                }
            }
        }
        if(this.orientation.equals("topRight")) {
            for (int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
                for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                    if (playerTable.isStartingCard(i, j)) {
                        j++;
                    }
                    if (isSecondaryResourceMatched(matrix[i][j]) && i < (PlayerTable.getMatrixDimension() - 3) && j >0) {
                        if (isMainResourceMatched(matrix[i + 2][j-1]) && !playerTable.isStartingCard(i + 2, j-1)) {
                            if (isMainResourceMatched(matrix[i + 3][j -1]) && !playerTable.isStartingCard(i + 3, j - 1)) {
                                matrix[i][j].setFlaggedForObjective(true);
                                matrix[i + 2][j-1].setFlaggedForObjective(true);
                                matrix[i + 3][j - 1].setFlaggedForObjective(true);
                                patternOccurrences++;
                            }
                        }
                    }
                }
            }
        }
        if(this.orientation.equals("topLeft")){
            for (int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
                for (int j = 0; j < PlayerTable.getMatrixDimension(); j++) {
                    if (playerTable.isStartingCard(i,j)){
                        j++;
                    }
                    if(isSecondaryResourceMatched(matrix[i][j]) && i<(PlayerTable.getMatrixDimension()-3) && j<(PlayerTable.getMatrixDimension()-1)){
                        if(isMainResourceMatched(matrix[i+2][j+1]) && !playerTable.isStartingCard(i+2, j+1)){
                            if(isMainResourceMatched(matrix[i+3][j+1]) && !playerTable.isStartingCard(i+3,j+1)){
                                matrix[i][j].setFlaggedForObjective(true);
                                matrix[i+2][j+1].setFlaggedForObjective(true);
                                matrix[i+3][j+1].setFlaggedForObjective(true);
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
     */
    private boolean isMainResourceMatched(PlaceableCard card){
        return this.mainResource.equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
    private boolean isSecondaryResourceMatched(PlaceableCard card){
        return this.secondaryResource.equals(((ResourceCard) card).getResourceCentreBack()) && !card.getIsFlaggedForObjective();
    }
}